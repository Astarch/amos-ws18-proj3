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
import * as d3 from "d3";
import { normalizeDataArray } from "js/utils/datahelper";

export default {
  name: "graph",
  data() {
    return {
      dataset: this.data,
      useNormalizedData: this.normalize
    };
  },
  props: {
    data: {
      type: Array,
      required: true
    },
    normalize: {
      type: Boolean,
      default: function() {
        return false;
      }
    }
  },
  watch: {
    data: function(newDataArray, oldData) {
      this.dataset = newDataArray
      this.updateDataArray(newDataArray);
    },
    normalize: function(normalizeVal, oldData) {
      this.useNormalizedData = normalizeVal
      this.updateDataArray(this.dataset);
    }
  },
  mounted: function() {
    window.addEventListener("resize", this.onResize);
  },

  beforeDestroy() {
    window.removeEventListener("resize", this.onResize);
  },

  methods: {
    updateDataArray: function(newDataArray) {
      $("svg").remove();

      if (newDataArray.length > 0) {
        let data = this.useNormalizedData
          ? normalizeDataArray(newDataArray)
          : newDataArray;
        this.prepareData(data);
      }
    },
    prepareData: function(data) {
      this.smoothed_data = new Array();
      this.colors = new Array();
      let parseTime = d3.timeParse("%Y%m%d");
      let count = 0;
      let self = this;
      $.each(data, function(index, item) {
        self.colors.push(item.color);
        $.each(item.data, function(key, value) {
          count++;
          if (
            !(parseTime(key).getDay() == 0) &&
            !(parseTime(key).getDay() == 6)
          ) {
            self.smoothed_data.push({
              query: index,
              date: parseTime(key),
              count: value
            });
          }
        });
      });
      this.group = d3
        .nest()
        .key(function(d) {
          return d.query;
        })
        .entries(this.smoothed_data);
      this.buildGraph(this.smoothed_data);
    },

    buildGraph: function(data) {
      var parentW = document.getElementById("graph").clientWidth;
      this.parentH = 400;
      this.margin = { top: 20, right: 50, bottom: 50, left: 50 };

      this.x = d3
        .scaleTime()
        .range([0, this.parentW - this.margin.left - this.margin.right]);
      this.y = d3
        .scaleLinear()
        .range([this.parentH - this.margin.top - this.margin.bottom, 0]);

      this.xAxis = d3.axisBottom().scale(this.x);
      this.yAxis = d3.axisLeft().scale(this.y);

      this.x.domain(
        d3.extent(data, function(d) {
          return d.date;
        })
      );
      this.y.domain([
        0,
        d3.max(data, function(d) {
          return d.count;
        })
      ]);

      this.buildLines(this.group);
    },

    buildLines: function(data) {
      let self = this;
      this.patharray = new Array();

      this.svg = d3
        .select("#graph")
        .append("svg")
        .attr("height", this.parentH);
      this.canvas = this.svg
        .append("g")
        .attr(
          "transform",
          "translate(" + this.margin.left + "," + this.margin.top + ")"
        );

      $.each(data, function(index, item) {
        self.patharray[index] = self.canvas
          .append("path")
          .attr("class", "line")
          .style("stroke", self.colors[index]);
      });

      this.xEl = this.canvas
        .append("g")
        .attr(
          "transform",
          "translate(0 " +
            (this.parentH - this.margin.top - this.margin.bottom) +
            ")"
        );
      this.yEl = this.canvas.append("g").call(this.yAxis);

      this.updateValues(this.x, this.y, data);
    },

    updateValues: function(x, y, data) {
      var parentW = document.getElementById("graph").clientWidth;

      this.svg.attr("width", parentW);
      x.range([0, parentW - this.margin.left - this.margin.right]);
      this.xAxis.scale(x);
      this.xEl.call(this.xAxis);

      let line = d3
        .line()
        .x(function(d) {
          return x(d.date);
        })
        .y(function(d) {
          return y(d.count);
        });

      $.each(this.patharray, function(index, item) {
        item.attr("d", line(data[index].values));
      });
    },

    onResize(event) {
      if ($("#graph").is(":parent")) {
        this.updateValues(this.x, this.y, this.group);
      }
    }
  }
};
</script>


