<template>
  <div>
  
    <!--Stats cards-->
    <div class="row">
      <div class="col-lg-12">
        <search-bar 
        :initialSearchType="queryMethod"
        :initialSearchTerm="queryTerm"
        :cachedSearchTerms="cachedTerms"
        v-on:querySubmitted="onSearchQuerySubmitted" />
      </div>
      <div class="col-lg-12 col-sm-12 alert alert-danger" role="alert" v-show="hasError">
        {{errorMessage}}      <i class="ti-close" v-on:click.stop="clearError" />
      </div>

      <div class="col-lg-12 col-sm-12" v-show="hasTrends">
        <card>  
          <div slot="title" class="topic-chips clearfix">
            <h4>Terms:</h4>
              <span  v-for="query in queries"
              class="label label-primary"
              v-bind:style="{backgroundColor: query.color}">
              <span>{{formattedTerm(query)}}</span>
               <i class="ti-close" v-on:click.stop="removeQuery(query)" />
              </span>            
          </div>
  
        </card>
      </div>
  
      <div class="col-lg-12 col-sm-12" v-show="hasTrends">
        <card>  
          <div slot="title">
            Trend Graphs 
          </div>
          <div slot="subTitle">
             <div class="pretty p-default">
                <input type="checkbox" v-model="showNormalizedData"/>
                <div class="state p-primary">
                    <label>Normalize Data</label>
                </div>
            </div>          
          </div>
          <div slot="content" class="contentframe">
            <div id="spinner" class="overlay" :class="{hide: !isLoading}">
              <bounce-loader :loading="isLoading" color="#127281"></bounce-loader>
            </div>
            <graph :data="queries" :normalize="showNormalizedData"></graph>
          </div>
          <div slot="footer">
            <p><b>{{trendIndicatorMessage}}</b></p>
            <p>{{prettyTimerange}}</p>
          </div>  
        </card>
      </div>

      <div class="col-lg-12 col-sm-12" v-show="hasTrends">
        <card>  
          <div slot="title">
            Data Analysis
          </div>
          <div slot="content" class="contentframe datatable">
           <additional-data :data="queries"></additional-data>
          </div>
        </card>
      </div>
    </div>
  
  </div>
</template>

<script>
import moment from "moment";
import _ from "lodash";
import { mapActions } from "vuex";
import RequestStatus from "src/js/models/RequestStatus";
import http, { api } from "js/utils/api";
import { normalizeData } from "js/utils/datahelper";

import Card from "./../common/Card";
import SearchBar from "./../common/Searchbar";
import Graph from "./../graph/Graph";
import AdditionalData from "src/js/components/engagement/common/AdditionalDataTable";
import BounceLoader from "vue-spinner/src/BounceLoader.vue";

export default {
  name: "dashboard",
  components: {
    Card,
    Graph,
    SearchBar,
    BounceLoader,
    AdditionalData
  },
  /**
   * Chart data used to render stats, charts. Should be replaced with server data
   */
  data() {
    return {
      timerange: "from=20170101&to=20171231",
      trendIndicatorMessage: "",
      queries: [],
      queryTerm: "",
      queryMethod: "ANY",
      isLoading: false,
      reqStatus: new RequestStatus(),
      graphdata: undefined,
      errorMessage: "",
      showNormalizedData: false,
      colors: [
        "#e6194b",
        "#3cb44b",
        "#f58231",
        "#0082c8",
        "#911eb4",
        "#d2f53c",
        "#fabebe",
        "#aaffc3",
        "#008080",
        "#46f0f0"
      ],
      availableColors: this.colors
    };
  },
  computed: {
    hasTrends() {
      return this.queries.length > 0 || this.isLoading;
    },
    hasError() {
      return this.errorMessage.length > 0;
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
    },
    cachedTerms(){
      return this.$store.state.trends.cachedTrendsList
    }
  },
  methods: {
    ...mapActions(["getWordcountByDay", "updateCurrentSearchQueries", "getCachedTrends"]),
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
    clearError() {
      this.errorMessage = "";
    },
    giveTrendIndication(newQuery, graphdata) {
      var trendData = [];
      $.each(graphdata, function(key, value) {
        trendData.push({ value });
      });
      var countTrend = 0;
      var falling = undefined;
      console.log(trendData.length);
      var tmp = trendData.length - 1;
      for (var i = 0; i < tmp; i--) {
        if (
          trendData[tmp].value - trendData[tmp - 1].value > 0 &&
          falling == undefined
        ) {
          falling = false;
          countTrend += 1;
          tmp--;
          continue;
        }
        if (
          trendData[tmp].value - trendData[tmp - 1].value < 0 &&
          falling == undefined
        ) {
          falling = true;
          countTrend += 1;
          tmp--;
          continue;
        }
        if (
          trendData[tmp].value - trendData[tmp - 1].value < 0 &&
          falling == true
        ) {
          countTrend += 1;
          tmp--;
          continue;
        }
        if (
          trendData[tmp].value - trendData[tmp - 1].value > 0 &&
          falling == false
        ) {
          countTrend += 1;
          tmp--;
          continue;
        }
        break;
      }
      if (countTrend > 2) {
        if (falling == true) {
          this.trendIndicatorMessage =
            "The trend for '" +
            newQuery.query +
            "' seems to have been fallen for a while and will probably fall more!";
        } else {
          this.trendIndicatorMessage =
            "The trend for '" +
            newQuery.query +
            "' seems to have been rising for a while and will probably rise more!";
        }
      }
      if (countTrend < 3 && countTrend > 0) {
        if (falling == true) {
          this.trendIndicatorMessage =
            "The trend for '" + newQuery.query + "'  is falling at the moment!";
        } else {
          this.trendIndicatorMessage =
            "The trend for '" + newQuery.query + "'  is rising at the moment!";
        }
      }
      if (countTrend == 0) {
        this.trendIndicatorMessage = "The trend has not been falling or rising at the moment for " + newQuery.query;
      }
      console.log(
        "This is the message: " +
          this.trendIndicatorMessage +
          " ,The count: " +
          countTrend +
          " ,Falling?: " +
          falling
      );
    },
    setData(data) {
      console.log("setData:", data);
      this.isLoading = false;
      this.graphdata = data;
    },
    areQueriesEqual(oldQuery, newQuery) {
      return (
        oldQuery.query === newQuery.query && oldQuery.method === newQuery.method
      );
    },
    hasQuery(newQuery) {
      let oldQueryIndex = _.findIndex(this.queries, q => {
        let isSingleTerm = q.query.trim().split(" ").length == 1;
        if (isSingleTerm) {
          return q.query === newQuery.query;
        } else {
          return this.areQueriesEqual(q, newQuery);
        }
      });
      return oldQueryIndex >= 0;
    },
    removeQuery(newQuery) {
      let oldQueryIndex = _.findIndex(this.queries, q =>
        this.areQueriesEqual(q, newQuery)
      );
      if (oldQueryIndex >= 0) {
        this.queries.splice(oldQueryIndex, 1);
        this.availableColors.push(newQuery.color)
      }
      this.updateCurrentSearchQueries(this.queries)
        .then(resp => {
          console.log(this.$store.user.currentSearchQueries);
        })
        .catch(error => {});
    },
    addQueryData(newQuery, data) {
      let oldQueryIndex = _.findIndex(this.queries, q =>
        this.areQueriesEqual(q, newQuery)
      );
      let color = this.colors[this.queries.length % this.colors.length];
      color = this.availableColors.pop()
      let _query = Object.assign({}, newQuery, { data, color });
      if (oldQueryIndex >= 0) {
        this.queries = Object.assign([...this.queries], {
          [oldQueryIndex]: _query
        });
      } else {
        this.queries = [...this.queries, _query];
      }
      this.isLoading = false;
      this.updateCurrentSearchQueries(this.queries)
        .then(resp => {
          console.log(this.$store.user.currentSearchQueries);
        })
        .catch(error => {});
    },
    fetchStateWordCountData(query, method, timerange) {
      return this.$store.getters.getTrendByDay(query, method, timerange);
    },
    fetchWordcountData(queryObj) {
      this.isLoading = true;

      let stateData = this.fetchStateWordCountData(
        queryObj.query,
        queryObj.method,
        queryObj.timerange
      );

      let reqObj = Object.assign({}, queryObj, { normalize: false });

      if (stateData != undefined && stateData.data != undefined) {
        console.log("stateData:", stateData);
        this.addQueryData(stateData, stateData.data);
        //this.setData(stateData.data);
      } else {
        this.getWordcountByDay(reqObj)
          .then(resp => {
            this.reqStatus = resp.req;
            console.log(resp.trend);
            if(!resp.trend.data || Object.keys(resp.trend.data).length === 0){
              return undefined
            }
            //this.setData(resp.trend.data);
            // upload server result to firestore!
    
            this.addQueryData(queryObj, resp.trend.data);
            this.giveTrendIndication(queryObj, resp.trend.data);
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
      let query = {
        query: queryObj.query,
        method: queryObj.method,
        timerange: this.timerange
      };
      if (this.hasQuery(query)) {
        return;
      }
      if (this.queries.length >= 10) {
        this.errorMessage = "Only up to 10 terms allowed!";
        return;
      }
      this.queryTerm = "";
      this.fetchWordcountData(query);
    }
  },
  mounted() {
    this.availableColors = this.colors
    this.getCachedTrends()
    .then(response => console.log(response))
    .catch(error => console.error(error))
    let lastQueries = this.$store.state.user.currentSearchQueries;
    if (lastQueries && lastQueries.length > 0) {
      //this.timerange = lastQuery.timerange;
      lastQueries.forEach(query => this.fetchWordcountData(query));
    }
  }
};
</script>

<style lang='scss' scoped>
 @import '~pretty-checkbox/src/pretty-checkbox.scss';
div.datatable {
  overflow: auto;
  max-height: 400px;
  height: auto;
}
#spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  &.overlay {
    width: 100%;
    height: 400px;
  }
  .hide {
    display: none;
  }
  // change loading spinner color
  .sk-child:before {
    background-color: map_get($theme-colors, secondary);
  }
}
.sk-child {
  background-color: map_get($theme-colors, secondary);
}

div.alert-danger {
  background-color: map_get($theme-colors, warning);
  color: map_get($theme-colors, danger);
  position: relative;
  min-height: 1px;
  margin-right: 15px;
  margin-left: 15px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  i {
    cursor: pointer;
  }
}
.topic-chips {
  h4 {
    margin-top: 0;
    font-size: 1em;
  }
  span {
    font-size: 1.05em;
    margin: 5px;
    float: left;
    display: flex;
    align-items: center;
    span {
      max-width: 10em;
      text-overflow: ellipsis;
      white-space: nowrap;
      display: inline-block;
      overflow: hidden;
    }
  }
  i {
    vertical-align: middle;
    line-height: 1;
    font-size: 0.7em;
    margin: 0 2px;
    cursor: pointer;
  }
}
.contentframe {
  height: 400px;
}
</style>
