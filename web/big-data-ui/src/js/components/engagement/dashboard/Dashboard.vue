<template>
  <div>
  
    <!--Stats cards-->
    <div class="row">
      <div class="col-lg-12">
        <search-bar 
        :initialSearchType="queryMethod"
        :initialSearchTerm="query"
        v-on:querySubmitted="onSearchQuerySubmitted" />
      </div>
  
      <div class="col-lg-12 col-sm-12" v-show="hasTrends">
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
  
      <div class="col-lg-12 col-sm-12" v-if="hasTrends">
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
import { mapActions } from "vuex";
import RequestStatus from "src/js/models/RequestStatus";

import Card from "./../common/Card";
import SearchBar from "./../common/Searchbar";
import Graph from "./../graph/Graph";
import http, { api } from "js/utils/api";
import VueLoading from "vue-simple-loading";

import moment from "moment";
import { isLong } from "long";

export default {
  name: "dashboard",
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
      timerange: "from=20170101&to=20171231",
      query: "",
      queryMethod: "ANY",
      isLoading: false,
      reqStatus: new RequestStatus()
    };
  },
  computed: {
    hasTrends() {
      return this.graphdata != undefined || this.isLoading;
    },
    prettyTimerange() {
      let prettyDateFormat = "ll";
      let test = this.timerange.split("&");
      let start = moment(test[0].split("=")[1], "YYYYMMDD");
      let end = moment(test[1].split("=")[1], "YYYYMMDD");
      return (
        "From " +
        start.format(prettyDateFormat) +
        " to " +
        end.format(prettyDateFormat)
      );
    }
  },
  methods: {
    ...mapActions(["getWordcountByDay", "updateCurrentSearchQuery"]),
    setData(data) {
      console.log("setData:", data);
      this.isLoading = false;
      this.graphdata = data;
    },
    fetchStateWordCountData(query, method, timerange) {
      return this.$store.getters.getTrendByDay(query, method, timerange);
    },
    fetchWordcountData(queryObj) {
      $("svg").empty();
      $("svg").remove();
      this.isLoading = true;
      this.query = queryObj.query;

      let stateData = this.fetchStateWordCountData(
        queryObj.query,
        queryObj.method,
        queryObj.timerange
      );

      if (stateData != undefined && stateData.data != undefined) {
        console.log("stateData:", stateData);
        this.setData(stateData.data);
      } else {
        this.getWordcountByDay(queryObj)
          .then(resp => {
            this.reqStatus = resp.req;
            console.log(resp.trend);
            this.setData(resp.trend.data);
          })
          .catch(requestStatus => {
            this.reqStatus = requestStatus;
            console.log(requestStatus);
            this.isLoading = false;
            graphdata: undefined;
          });
      }
    },
    onSearchQuerySubmitted(queryObj) {
      console.log("onSearchQuerySubmitted() ", queryObj);
      if (
        queryObj.query === this.query &&
        queryObj.method === this.queryMethod
      ) {
        return;
      }

      let query = {
        query: queryObj.query,
        method: queryObj.method,
        timerange: this.timerange
      };
      this.updateCurrentSearchQuery(query)
        .then(resp => {
          console.log(this.$store.user.currentSearchQuery);
        })
        .catch(error => {});

      this.fetchWordcountData(query);
    }
  },
  mounted() {
    let lastQuery = this.$store.state.user.currentSearchQuery;
    if (lastQuery && lastQuery.query && lastQuery.query.length > 1) {
      //this.timerange = lastQuery.timerange;
      this.queryMethod = lastQuery.method;

      this.fetchWordcountData(lastQuery);
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
