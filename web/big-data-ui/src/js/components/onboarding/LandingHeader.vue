<template>
  <!-- Navigation -->
  <nav class="navbar navbar-default navbar-fixed-top topnav" role="navigation">
    <slot></slot>
    <div class="container topnav">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target="#bs-example-navbar-collapse-1">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand topnav" href="#">
          <img class="icon" src="~assets/logo/icon_white.svg">
          <b>PRETRENDR</b>
        </a>
      </div>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="#register" ref="register">Register</a>
          </li>
          <li>
            <a href="#login">Login</a>
          </li>
        </ul>
      </div>
      <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
  </nav>
</template>

<script>
  export default {
    name: 'landing-header'
  }


</script>

<style lang="scss" scoped>

  .topnav {
    font-size: 14px;
    background-color: #053948;
    .icon {
      height: 30px;
      display: inline-block;
    }
  }


</style>
