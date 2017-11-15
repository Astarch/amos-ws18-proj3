<template>
    <div class="row" style="margin-top: 70px">
        <!-- include child component and pass data -->
        <chart class="col s6" :data="dataset"></chart>
    </div>
</template>
<script>

import chart from "./Chart"
import * as d3 from 'd3'

        export default {
            components: {
                chart
        },
            data: function () {
                return {
                   dataset: new Array(),
                };
            },
            mounted: function () {
                // read external csv
                var self = this;
                d3.csv('/static/data/test.csv', function(err,csvdata) {
                    self.dataset = csvdata.slice(0); 
                    csvdata.forEach(function(d) {
                    // here, each value which should be numeric must be converted from string
                    d.count = +d.count;
                    });
                });
            }
        };
</script>