<template>
            <div id="barCharts"></div>

</template>

<script>
import * as d3 from "d3";

export default {
  name: "bars",
  props: {
    rowData: {
      type: Object,
      default: function() {
        return {
          query: "Test",
          color: "#e6194b",
          min: [25, 5],
          max: [67, 100],
          avg: [50, 50],
          median: [35, 45]
        };
      }
    }
  },
  watch: {
     rowData: function(data, oldData) {
     this.color = this.rowData.color;
     this.rowData = data;
      this.updateDataArray(data);
    }
  },
  computed: {
    backgroundColor() {
      return this.rowData.color;
    },
    normalizedMin() {
      return parseFloat(
        this.rowData.min[0] / this.rowData.min[1] * 100
      ).toFixed(0);
    },
    normalizedMax() {
      return parseFloat(
        this.rowData.max[0] / this.rowData.max[1] * 100
      ).toFixed(2);
    },
    normalizedAvg() {
      return parseFloat(
        this.rowData.avg[0] / this.rowData.avg[1] * 100
      ).toFixed(2);
    },
    normalizedMedian() {
      return parseFloat(
        this.rowData.median[0] / this.rowData.median[1] * 100
      ).toFixed(2);
    }
  },
  mounted: function () {
  this.color = this.rowData.color;
    this.updateDataArray(this.rowData);
  },
  methods: {
    updateDataArray: function(data) {
      //$("svg").remove();
      console.log(data.max[0]);
      this.doBars([{"what":"min", "count":data.min[0]}, {"what": "max", "count":data.max[0]}, {"what":"avg","count":data.avg[0]}, {"what":"median","count":data.median[0]}]);

    },
    doBars: function(data) {
      var parentH = 5000;
      var parentW = (document.getElementById("graph").clientWidth);
      var margin = { top: 20, right: 50, bottom: 50, left: 50 };

      var svg = d3.select("#barCharts").append("svg").attr("width", parentW-margin.left-margin.right).attr("height", parentH+margin.top+margin.bottom).append("g").attr("transform", 
          "translate(" + margin.left + "," + margin.top + ")");

      var x = d3.scaleBand()
          .range([0, parentW- 2*margin.left], .1);

var y = d3.scaleLinear()
          .range([parentH-margin.top-margin.bottom*2-margin.top*2, 0]);

      y.domain([0, d3.max(data, function(d){ return d.count; })])
      x.domain(data.map(function(d) { return d.what; })).paddingInner(0.1)
        .paddingOuter(0.5);

      var xAxis = svg.append("g").attr("class", "axis").attr("transform", "translate(0," + parentH + ")")
.call(d3.axisBottom(x));

     var bar = svg.append("g").selectAll("rect").data(data).enter();
      bar.append("rect").attr("class", "bar")
      .attr("fill", this.color.toString())
      .attr("x", function(d) {return x(d.what);})
      .attr("width", x.bandwidth())
      .attr("y", function(d,i) { return y(d.count);})
      .attr("height", function(d) { 
      return parentH - y(d.count); });

      svg.append("g").selectAll("text")
        .data(data)
        .enter()
        .append("text")//.attr('transform', 'rotate(-90)')
        .text(function (d) { return d.count; })
        .attr("x", function (d) { return x(d.what) + x.bandwidth() / 2; })
        .attr("y", function (d) { return (y(d.count)-5) })
        .style("fill", this.color.toString());

      }
      }
};
</script>

<style>

#barCharts {
  margin-bottom: 5px;
}

.axis path,
.axis line {
  fill: none;
  stroke: #D4D8DA;
  stroke-width: 1px;
  shape-rendering: crispEdges;
}
</style>
