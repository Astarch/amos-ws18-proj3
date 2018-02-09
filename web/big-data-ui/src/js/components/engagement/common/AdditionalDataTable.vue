<template>
  <div class="table">
	<table class="table table-hover">
    <thead>
      <tr>
        <th>Query Name</th>
        <th>min (rel)</th>
        <th>max (rel)</th>
        <th>avg (rel)</th>
        <th>median (rel)</th>
      </tr>
    </thead>
    <tbody>
      <data-row v-for="row in computedData" :rowData="row" :key="row.query"></data-row>
    </tbody>
	</table>
</div>
</template>

<script>
import DataRow from "src/js/components/engagement/common/AdditionalDataTableRow";
import { aggregateDataArray } from "js/utils/datahelper";

export default {
  name: "additional-graph-data",
  props: {
    data: {
      type: Array,
      required: true
    }
  },
  components: {
    DataRow
  },
  data: function() {
    return {
      tableData: [
        {
          query: "Test",
          color: "#e6194b",
          min: 0,
          max: 543,
          avg: 65,
          median: 76
        },
        {
          query: "Trump",
          color: "#3cb44b",
          min: 68,
          max: 6785,
          avg: 4575,
          median: 4690
        },
        {
          query: "Bitcoin",
          color: "#f58231",
          min: 5,
          max: 4678,
          avg: 2545,
          median: 3245
        }
      ],
      dataset: this.data
    };
  },
  watch: {
    data: function(newDataArray, oldData) {
      console.log(newDataArray)
      this.dataset = aggregateDataArray(newDataArray);
      console.log(this.dataset)
    },
  },
  computed: {
    computedData() {

      let globalMaxMin = 1;
      let globalMax = 1;
      let globalMaxAvg = 1;
      let globalMaxMedian = 1;
      console.log(this.dataset)

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
            query: this.formattedTerm(val),
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
  methods: {
     formattedTerm(query) {
      if (query.method == "ALL") {
        return query.query.trim().replace(/\s/g, "&");
      } else if (query.method == "EXACT") {
        return `"${query.query.trim()}"`;
      } else if (query.method == "ANY") {
        return query.query.trim();
      }
      return query.query.trim();
    },
  }
};
</script>

<style lang="scss" scoped>
table thead {
  background-color: map_get($theme-colors, primary);
  color: white;
  th {
    font-weight: bold;
  }
}
</style>
