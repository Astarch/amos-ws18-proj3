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
    <p>&copy; 2013 Poses-404. All Rights Reserved | Design by <a href="http://w3layouts.com">w3layouts</a></p>
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
.footer {
  font-family: "Century Gothic";
  text-align: right;
  margin-top: 2em;
  p {
    font-size: 1em;
    color: #603813;
  }
  a {
    color: #f9614d;
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>