<template>
<div class="row">
    <div class="col s12" style="margin-top: 70px">
        <!-- include child component and pass data -->
        <graph class="col s12" :data="dataset"></graph>
    </div>
</div>
</template>
<script>

import graph from "./Graph"
import * as d3 from 'd3'

        export default {
            components: {
                graph
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