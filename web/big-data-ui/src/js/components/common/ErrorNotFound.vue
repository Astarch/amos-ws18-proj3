<template>

<div class="wrap">

  <div class="banner">
    <router-link :to="{path:'/'}">
    <img src="~assets/error-page/banner.png" alt="" />
    </router-link>
  </div>
  <div class="page">
    <h2>Seems like 404 is trending right now.</h2>
    <h2>This trend predicts that this page is not found!</h2>
  </div>
  <div class="footer">

  </div>
</div>

</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
.wrap {
  margin: 0 auto;
  width: 96%;
}

h1 {
  margin-top: 20px;
  color: map_get($theme-colors, secondary);
  font-size: 3em;
  text-transform: uppercase;
  font-weight: bold;
}

.banner {
  text-align: center;
  margin-top: 30px;
  img {
    margin-top: 0px;
  }
}

.page {
  text-align: center;
  font-family: "Century Gothic";
  h2 {
    font-size: 3em;
    color: map_get($theme-colors, secondary);
    font-weight: bold;
  }
}
</style>