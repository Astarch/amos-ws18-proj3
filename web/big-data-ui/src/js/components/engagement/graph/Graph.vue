<template>
  <div id="graph"></div>
</template>

<style>

  .line {
  fill: none;
  stroke: #e96772;
  stroke-width: 2px;
}

  .smoothedline {
  fill: none;
  stroke: #127281;
  stroke-width: 2px;
}

#graph {
  padding: 10px 10px 10px 10px;
}


</style>
<script>
  import * as d3 from 'd3'

  export default {
    name: 'graph',
    data() {
      return {
        dataset: this.data
      };
    },
        props: ['data'],
    watch: {
      data: function (newData, oldData) {
        this.prepareData(newData);
      }
    },

   mounted: function () {
      window.addEventListener("resize", this.onResize);
    },

    beforeDestroy() {
      window.removeEventListener("resize", this.onResize);
    },

    methods: {

      prepareData : function(data) {
      this.formatted_data = new Array(); this.smoothed_data = new Array();
      var parseTime = d3.timeParse("%Y%m%d");
      var self = this;

      $.each(data, function (key, value) {
        value += value;
        self.formatted_data.push({'date' : parseTime(key), 'count' : value});
      });

        var count = 0; var self = this;
        $.each(data, function (key, value) {
          count++;
          if(count%7 != 0 && count%8 != 0) {
            self.smoothed_data.push({'date' : parseTime(key), 'count' : value});
          } 
        });
        this.buildGraph(this.formatted_data);
      },

      buildGraph: function (data) {

      var parentW = document.getElementById("graph").clientWidth;
      var parentH = 400;
      this.margin = {top: 20, right: 50, bottom: 50, left: 50};


      this.x = d3.scaleTime().range([0, parentW - this.margin.left - this.margin.right]);
      this.y = d3.scaleLinear().range([parentH - this.margin.top - this.margin.bottom, 0]);

      this.xAxis = d3.axisBottom().scale(this.x);
      var yAxis = d3.axisLeft().scale(this.y);

      this.x.domain(d3.extent(data, function(d) { return d.date; }));
      this.y.domain([0, d3.max(data, function(d) { return d.count; })]);


      this.line = d3.line();
      this.smoothed_line = d3.line();


      this.svg = d3.select("#graph").append("svg").attr("height", parentH);
      this.canvas = this.svg.append("g").attr("transform", "translate(" + this.margin.left + "," + this.margin.top + ")");

      this.path = this.canvas.append("path").data([data]).attr("class", "line");
      this.smoothed_path = this.canvas.append("path").data([this.smoothed_data]).attr("class", "smoothedline");

      this.xEl = this.canvas.append("g").attr("transform", "translate(0 " + (parentH - this.margin.top - this.margin.bottom) + ")");
      this.yEl = this.canvas.append("g").call(yAxis);


      this.updateValues(this.x, this.y);
      },


      updateValues: function (x, y) {

      var parentW = document.getElementById("graph").clientWidth;

        this.svg.attr("width", parentW);
        x.range([0, parentW - this.margin.left - this.margin.right]);
        this.xAxis.scale(x);
        this.xEl.call(this.xAxis);

        this.line.x(function(d) { return x(d.date); }).y(function(d) { return y(d.count); });
        this.smoothed_line.x(function(d) { return x(d.date); }).y(function(d) { return y(d.count); });

        this.path.attr("d", this.line);
        this.smoothed_path.attr("d", this.smoothed_line);
      },

      onResize(event) {      
      if($("#graph").is(':parent')) {
        this.updateValues(this.x, this.y);
      }
      }
    },
  }
</script>


