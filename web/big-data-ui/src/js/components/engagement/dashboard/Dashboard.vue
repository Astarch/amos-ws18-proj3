<template>
  <div>
  
    <!--Stats cards-->
    <div class="row">
      <div class="col-lg-12">
        <search-bar v-on:querySubmitted="onSearchQuerySubmitted" />
      </div>
  
      <div class="col-lg-6 col-sm-6" v-show="hasTrends">
        <card>
  
          <div slot="title">
            Trend Analysis
          </div>
          <div slot="subTitle" id="subtitle">
            Topic: {{query}}
          </div>
          <div slot="content" class="contentframe">
            <div id="spinner" class="overlay" :class="{hide: !isLoading}">
              <vue-loading spinner="circles"></vue-loading>
            </div>
            <graph :data="graphdata"></graph>
          </div>
          <div slot="footer">
            {{prettyTimerange}}
          </div>
  
        </card>
      </div>
  
      <div class="col-lg-6 col-sm-6" v-if="hasTrends">
        <card>
          <div slot="title">
            Google Trends
          </div>
          <div slot="subTitle">
            for comparison
          </div>
          <div slot="content" class="contentframe">
          </div>
          <div slot="footer">
            {{prettyTimerange}}
          </div>
        </card>
      </div>
    </div>
  
  </div>
</template>

<script>
import Card from "./../common/Card";
import SearchBar from "./../common/Searchbar";
import Graph from "./../graph/Graph";
import http, { api } from "js/utils/api";
import VueLoading from "vue-simple-loading";

import moment from "moment"

export default {
  components: {
    Card,
    Graph,
    SearchBar,
    VueLoading
  },
  /**
   * Chart data used to render stats, charts. Should be replaced with server data
   */
  data() {
    return {
      graphdata: undefined,
      dummy_path: "/api/dummy/wordcountByDay/bitcoin?from=20170101&to=20170331",
      path: "/api/gdelt/wordcountByDay/",
      timerange: "?from=20170101&to=20170331",
      query: "",
      isLoading: false
    };
  },
  computed: {
      hasTrends(){
        return this.graphdata != undefined
      },
      prettyTimerange(){
        let prettyDateFormat = "ll"
        let test = this.timerange.split("&")
        let start = moment(test[0].split("=")[1], "YYYYMMDD")
        let end = moment(test[1].split("=")[1], "YYYYMMDD")
        return "From "+ start.format(prettyDateFormat) + " to " + end.format(prettyDateFormat) 
      }
  },
  methods: {
    onSearchQuerySubmitted(newQuery) {
      console.log("onSearchQuerySubmitted() ", newQuery);
      $("svg").empty();
      $("svg").remove();
      this.isLoading = true;
      this.query = newQuery;

      var newpath = String(this.path + newQuery + this.timerange);
      api.graph
        .getData(newpath)
        .then(response => {
          console.log(response.data);
          this.isLoading = false;
          this.graphdata = response.data;
        })
        .catch(error => {
          console.log(error);
          this.isLoading = false;
          graphdata: undefined;

          let errorCode = error.response.status;
          if (errorCode < 500) {
            console.log("Failure - please try again!");
          } else {
            console.log("Error - please try again later!");
          }
          console.log(error.response);
        });
    }
  }
};
</script>

<style lang='scss'>
#spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  &.overlay {
    width: 100%;
    height: 400px;
  }

  .hide {
    display: none;
  }

  // change loading spinner color
  .sk-circle .sk-child:before {
    background-color: map_get($theme-colors, secondary);
  }
}

.contentframe {
  height: 400px;
}
</style>
