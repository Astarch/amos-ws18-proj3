<template>
  <nav id="pretrendr-header" class="navbar navbar-expand-md bg-primary navbar-dark">
    <div class="container">
      <a class="navbar-brand" href="#">
        <img class="icon" src="../../../assets/logo/icon_white.svg">
        <b>PRETRENDR</b>
      </a>
      <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
              data-target="#navbar2SupportedContent" aria-controls="navbar2SupportedContent" aria-expanded="false"
              aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
    </div>
  </nav>
</template>

<script>
  export default {
    name: 'pretrendr-header'
  }


</script>

<style lang="scss" scoped>

  #pretrendr-header a.navbar-brand .icon {
    height: 30px;
  }

</style>
