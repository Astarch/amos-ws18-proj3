<template>
  <div class="row" style="margin-top: 70px">
    <!-- include child component and pass data -->
    <chart class="col s12" :data="dataset"></chart>
  </div>
</template>
<script>

  import chart from "./Chart"
  import * as d3 from 'd3'

  export default {
    name: 'graphContainer',
    components: {
      chart
    },
    data: function () {
      return {
        dataset: new Array(),
        options: {
        host: 'localhost:8081',
        path: '/api/dummy',
        method: 'GET',
        user: '',
        password: '',
        apiKey: '',
        sessionID: ''
        }
      };
    },
    mounted: function () {
      var self = this;

      // read external csv | !!!keep for testing!!!

      /**
      d3.csv('/static/data/test.csv', function (err, csvdata) {
        self.dataset = csvdata.slice(0);
        csvdata.forEach(function (d) {
          // here, each value which should be numeric must be converted from string
          d.count = +d.count;
        });
      }); **/

      // http GET
      this.$http.get('http://localhost:8081/api/dummy/graphData').then(response => {
        // status: ok
        if(response.status ==200){
          console.log("status: " + response.status);
          self.dataset = response.body;
        }
      }, response => {
        console.log("error: " + response.status);
      });
    }
  };
</script>
