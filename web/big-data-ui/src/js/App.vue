<template>
  <div id="app" :class="{'nav-open': $sidebar.showSidebar}">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "app"
};
</script>

<style lang="scss">
@import "~bootstrap/dist/css/bootstrap.css";
@import "../scss/base";
@import "../scss/paper-dashboard";

html {
  // background: map-get($theme-colors, 'dark');
  position: relative;
  min-height: 100%;
}

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
