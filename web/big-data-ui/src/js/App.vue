<template>
  <div id="app">
    <git-info></git-info>
    <router-view></router-view>
  </div>
</template>

<script>
  import GitInfo from './components/GitInfo';

  export default {
    components: { GitInfo },
    name: 'app',
  };
</script>

<style lang="scss">
  @import '~bootstrap/dist/css/bootstrap.css';
  @import '../scss/base';

  body {
    background: map-get($theme-colors, 'dark');

  }

  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
