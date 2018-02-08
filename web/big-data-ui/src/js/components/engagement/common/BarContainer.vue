<template>
<div>
 <!-- <bars v-for="row in computedData" :rowData="row" :key="row.query"></bars> -->
    <verticalBars v-for="row in computedData" :rowData="row" :key="row.query"></verticalBars>

  </div>
</template>

<script>
import DataRow from "src/js/components/engagement/common/AdditionalDataTableRow";
import { aggregateDataArray } from "js/utils/datahelper";
import bars from "src/js/components/engagement/common/Bars";
import verticalBars from "src/js/components/engagement/common/VerticalBars";


export default {
  name: "additional-graph-data",
  props: {
    data: {
      type: Array,
      required: true
    }
  },
  components: {
    DataRow,
    bars,
    verticalBars
  },
  data: function() {
    return {
      dataset: this.data
    };
  },
  watch: {
    data: function(newDataArray, oldData) {
      this.dataset = aggregateDataArray(newDataArray);
    },
  },
  computed: {
    computedData() {

      let globalMaxMin = 1;
      let globalMax = 1;
      let globalMaxAvg = 1;
      let globalMaxMedian = 1;

      this.dataset.forEach(value => {
        if (value.min > globalMaxMin) {
          globalMaxMin = value.min;
        }
        if (value.max > globalMax) {
          globalMax = value.max;
        }
        if (value.avg > globalMaxAvg) {
          globalMaxAvg = value.avg;
        }
        if (value.median > globalMaxMedian) {
          globalMaxMedian = value.median;
        }
      });
      let transformedData = this.dataset.map(val => {
        return Object.assign(
          {},
          {
            query: val.query,
            color: val.color,
            min: [val.min, globalMaxMin],
            max: [val.max, globalMax],
            avg: [val.avg, globalMaxAvg],
            median: [val.median, globalMaxMedian]
          }
        );
      });

      return transformedData;
    }
  },
  methods: {}
};
</script>

<style lang="scss" scoped>

</style>
