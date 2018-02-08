<template>
  <div id="barCharts" :class="queryName"  >
   
  </div>
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
      this.updateDataArray(data);
    }
  },
  computed: {
    queryName(){
      return this.rowData.query
    },
    svgSelector(){
      return "#barCharts."+this.rowData.query
    },
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
  mounted: function() {
    this.color = this.rowData.color;
    this.updateDataArray(this.rowData);
  },
  methods: {
    updateDataArray: function(data) {
      $(this.svgSelector+" svg").remove();
      console.log(this.svgSelector)
      this.doBars([
        { what: "min", count: data.min[0] },
        { what: "max", count: data.max[0] },
        { what: "avg", count: data.avg[0] },
        { what: "median", count: data.median[0] }
      ]);
    },
    doBars: function(data) {
      var parentW = document.getElementById("graph").clientWidth;
      var parentH = 100;
      var margin = { top: 10, right: 50, bottom: 0, left: 45 };

      var svg = d3
        .select(this.svgSelector)
        .append("svg")
        .attr("width", parentW - margin.left - margin.right)
        .attr("height", parentH + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

      var y = d3
        .scaleBand()
        .range([parentH, 0])
        .padding(0.1);
      var x = d3
        .scaleLinear()
        .range([0, parentW - margin.left * 2 - margin.right * 2]);

      x.domain([
        0,
        d3.max(data, function(d) {
          return d.count;
        })
      ]);
      y.domain(
        data.map(function(d) {
          return d.what;
        })
      );

      var yAxis = svg
        .append("g")
        .attr("class", "axis")
        .call(d3.axisLeft(y));

      var bar = svg
        .append("g")
        .selectAll("rect")
        .data(data)
        .enter();
      bar
        .append("rect")
        .attr("class", "bar")
        .attr("fill", this.color.toString())
        .attr("y", function(d, i) {
          return y(d.what);
        })
        .attr("width", function(d) {
          return x(d.count);
        })
        .attr("height", y.bandwidth());

      svg
        .append("g")
        .selectAll("text")
        .data(data)
        .enter()
        .append("text")
        .text(function(d) {
          return d.count;
        })
        .attr("x", function(d) {
          return x(d.count) + 5;
        })
        .attr("y", function(d) {
          return y(d.what) + y.bandwidth() / 2;
        })
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
    stroke: #d4d8da;
    stroke-width: 1px;
    shape-rendering: crispEdges;
  }
</style>
