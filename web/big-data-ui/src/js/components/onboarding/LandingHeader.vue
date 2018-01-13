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
          <img class="icon" src="~assets/logo/icon_primary.svg">
          <b>PRETRENDR</b>
        </a>
      </div>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="#register" id="header-link-register" v-on:click.prevent.stop="openRegister()">Register</a>
          </li>
          <li>
            <a href="#login" id="header-link-login" v-on:click.prevent.stop="openLogin()">Login</a>
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
    name: 'landing-header',
    methods: {
      openRegister() {
        this.$emit('openRegistration')
      },
      openLogin() {
        this.$emit('openLogin')
      }
    }
  }


</script>

<style lang="scss" scoped>

  .topnav {
    font-size: 14px;
    background-color: white;
    color: map_get($theme-colors, primary);
    .icon {
      height: 30px;
      display: inline-block;
    }
    a.topnav{
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      b {
        margin-left:5px;
      }
    }

    div.collapse  a,
    div.collapsing a{
      padding: 5px 15px;
    }
    .navbar-collapse{
      height: auto!important;
    }
  }


</style>
