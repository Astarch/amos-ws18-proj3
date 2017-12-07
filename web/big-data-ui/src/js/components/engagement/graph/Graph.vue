<template>
  <svg id="graph"></svg>
</template>
<style>
  .lgnd {
    fill: #bbb;
    font-size: 12pt;
  }
</style>
<script>
  import * as d3 from 'd3'

  export default {
    name: 'graph',
    data() {
      return {
        dataset: this.data,
        width: null,
        height: null,
        datetime: null,
        svg: null
      };
    },
    props: ['data'],
    watch: {
      data: function (newData, oldData) {
        var self = this;
        newData.forEach(function (d) {
          d.date = self.datetime(d.date);
          d.num = +d.num;
        })
        this.buildGraph(newData);
      }
    },
    mounted: function () {
      this.datetime = d3.time.format("%d-%b-%y").parse;
    },
    methods: {
      // idea: https://leanpub.com/D3-Tips-and-Tricks
      buildGraph: function (newdata) {
        var self = this;
        this.width = document.getElementById('graph').getBoundingClientRect().width - 80;
        this.height = document.getElementById('graph').getBoundingClientRect().height - 60;
        this.svg = d3.select('#graph').append('g')
                     .attr('transform', 'translate(' + 30 + ',' + 30 + ')');

        var x = d3.time.scale().range[(0, this.width)];
        var xAxis = d3.svg.axis().scale(x);
        var y = d3.scale.linear().range([this.height, 0]);
        var yAxis = d3.svg.axis().scale(y).orient('left');//.ticks(5)

        var line = d3.svg.line().x(function (d) {
          return x(d.date);
        }).y(function (d) {
          return y(d.num)
        });
        x.domain(d3.extent(newdata, function (d) {
          return d.date;
        }));
        y.domain([0, d3.max(data, function (d) {
          return d.num;
        })]);

        this.svg.append('path').attr('class', 'line').attr('d', line(newdata));
        this.svg.append('g').attr('class', 'xaxis').attr('transform', 'translate(0,' + this.height + ')').call(xAxis);
        this.svg.append('g').attr('class', 'yaxis').call(yAxis);
        //.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
      },
      drawLegend: function (newdata) {
        var self = this;
        var num = newdata.length;
        var size = ((this.rad * 3) / (num + 5)) / 2;
        var space = (1 / 2) * size;
        var lgnd = this.svg.selectAll('.lgnd').data(self.colorScale.domain()).enter().append('g').attr('class', 'lgnd').attr('transform', function (d, i) {
          var lgndheight = size * 2;
          var offset = lgndheight * (self.colorScale).domain().length / 2;
          var h = self.rad + size * 2;
          var v = i * lgndheight - offset;
          return 'translate(' + h + ',' + v + ')';
        });
        lgnd.append('rect').attr('width', size).attr('height', size).style('fill', self.colorScale).style('stroke', self.colorScale);
        lgnd.append('text').attr('x', size + space).attr('y', size).text(function (d) {
          return d;
        });
      },
    },
  }
</script>
