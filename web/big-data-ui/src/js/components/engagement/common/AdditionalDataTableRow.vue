<template>  
      <tr>
        <td>
          <div class="value">{{rowData.query}}</div>
        </td>
        <td>
          <div class="background min" :style="{backgroundColor: backgroundColor, width: minBackgroundWidth}"></div>
          <div class="value">{{rowData.min[0]}}</div>
        </td>
        <td>
          <div class="background max" :style="{backgroundColor: backgroundColor, width: maxBackgroundWidth}"></div>
          <div class="value">{{rowData.max[0]}}</div>
        </td>
        <td>
          <div class="background avg" :style="{backgroundColor: backgroundColor, width: avgBackgroundWidth}"></div>
          <div class="value">{{rowData.avg[0]}}</div>
          </td>
        <td>
          <div class="background median" :style="{backgroundColor: backgroundColor, width: medianBackgroundWidth}"></div>
          <div class="value">{{rowData.median[0]}}</div>
        </td>
      </tr>
</template>
<script>
export default {
  name: "additional-graph-data-row",
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
  data: function() {
    return {};
  },
  watch: {},
  computed: {
    backgroundColor() {
      return this.rowData.color;
    },
    normalizedMin() {
      return parseFloat(
        this.rowData.min[0] / this.rowData.min[1] * 100
      ).toFixed(0);
    },
    minBackgroundWidth() {
      return Math.max(0, this.normalizedMin - 2) + "%";
    },
    normalizedMax() {
      return parseFloat(
        this.rowData.max[0] / this.rowData.max[1] * 100
      ).toFixed(2);
    },
    maxBackgroundWidth() {
      return Math.max(0, this.normalizedMax - 2) + "%";
    },
    normalizedAvg() {
      return parseFloat(
        this.rowData.avg[0] / this.rowData.avg[1] * 100
      ).toFixed(2);
    },
    avgBackgroundWidth() {
      return Math.max(0, this.normalizedAvg - 2) + "%";
    },
    normalizedMedian() {
      return parseFloat(
        this.rowData.median[0] / this.rowData.median[1] * 100
      ).toFixed(2);
    },
    medianBackgroundWidth() {
      return Math.max(0, this.normalizedMedian - 2) + "%";
    }
  },
  methods: {}
};
</script>

<style lang="scss" scoped>
.table > tbody > tr {
  & > td {
    padding: 0px;
    margin: 0px 5px;
    position: relative;
    div.value {
      padding: 12px;
      position: relative;
    }
    div.background {
      position: absolute;
      margin: 0px 1%;
      width: 80%;
      height: 100%;
      visibility: visible;
    }
  }
}
</style>
