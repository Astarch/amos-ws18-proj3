<template>
    <div id="cached-terms">
    <ul>
      <li v-for="(term, index) in cachedTerms" @click="suggestionClick(term)"
            ><p>{{ term.query.toLowerCase() }} <small>{{ term.method }}</small></p>
            </li>
    </ul>
    </div>
</template>
<script>
import { mapActions } from "vuex";
import router from "src/js/router";
export default {
  computed: {
    cachedTerms() {
      return this.$store.state.trends.cachedTrendsList;
    }
  },
  methods: {
    ...mapActions(["getCachedTrends", "updateCurrentSearchQueries"]),
    suggestionClick(termObj) {
      if (termObj.query.length <= 0 || termObj.method.length <= 0) {
        return;
      }
      this.updateCurrentSearchQueries([termObj])
        .then(resp => {
          console.log(resp);
          router.push({
            path: "/engagement/dashboard"
          });
        })
        .catch(error => {});
      console.dir("suggestionClick()" + termObj);
    },
    updateCachedTrends() {
      this.getCachedTrends()
        .then(response => console.log(response))
        .catch(error => console.error(error));
    }
  },
  mounted() {
    this.updateCachedTrends();
  }
};
</script>
<style lang='scss' scoped>
ul {
  box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5);
  padding: 0px;
  list-style: none;
  background: white;
  li {
    padding: 5px 19px;
    &.active,
    &:hover {
      background: map_get($theme-colors, light);
      color: map_get($theme-colors, secondary);
    }
  }
  p {
    cursor: pointer;
    margin: 0px;
  }
  small {
    font-size: 0.6em;
    color: map_get($theme-colors, secondary);
  }
}
</style>
