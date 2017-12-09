<template>
  <svg id="chart"></svg>
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
    name: 'chart',
    data() {
      return {
        width: null,
        height: null,
        rad: null,
        colorScale: null,
        svg: null
      };
    },
    props: ['data'],
    watch: {
      data: function (newData, oldData) {
        this.prepareData(newData);
      }
    },
    methods: {
      prepareData: function (newdata) {
        var key; var data = []; var self = this;

        for (key in newdata) {
          if (newdata.hasOwnProperty(key)){
            newdata[key] = Number(newdata[key]);
            data.push({'label':key, 'count':newdata[key]});
          }
        }
        this.buildGraph(data);
      },
      // idea: http://zeroviscosity.com/d3-js-step-by-step/step-1-a-basic-pie-chart
      buildGraph: function (newdata) {
        var self = this;
        this.width = document.getElementById('chart').getBoundingClientRect().width;
        this.height = document.getElementById('chart').getBoundingClientRect().height;
        this.rad = Math.min(this.width, this.height) / 2;
        this.colorScale = d3.scaleOrdinal().range(['#A468D5', '#3F046F', '#582781', '#640CAB', '#200039', '#9D74BF']);
        this.svg = d3.select('#chart').append('g').attr('transform', 'translate(' + (this.width / 2) + ',' + (this.height / 2) + ')');
        var radius = d3.arc().innerRadius(this.rad - (0.45 * this.rad)).outerRadius(this.rad);
        var pie = d3.pie().value(function (d) {
          return d.count;
        });
        var path = this.svg.selectAll('path').data(pie(newdata)).enter().append('path').attr('d', radius).attr('fill', function (d, i) {
          return self.colorScale(d.data.label);
        });
        this.drawLegend(newdata);
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
