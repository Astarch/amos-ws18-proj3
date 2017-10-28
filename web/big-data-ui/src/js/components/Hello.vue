<template>
  <div class="hello">
    <nav class="navbar navbar-expand-md bg-primary navbar-dark">
      <div class="container">
        <a class="navbar-brand" href="#"><i class="fa d-inline fa-lg fa-cloud"></i><b> Brand</b></a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
                data-target="#navbar2SupportedContent" aria-controls="navbar2SupportedContent" aria-expanded="false"
                aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse text-center justify-content-end" id="navbar2SupportedContent">
          <ul class="navbar-nav"></ul>
          <a class="btn navbar-btn ml-2 text-white btn-secondary"><i class="fa d-inline fa-lg fa-user-circle-o"></i>
            Sign in</a><a class="btn btn-primary" href="">Button </a>
        </div>
      </div>
    </nav>
    <div class="py-5 gradient-overlay"
         style="background-image: url(&quot;https://pingendo.github.io/templates/sections/assets/cover_event.jpg&quot;);">
      <div class="container py-5">
        <div class="row">
          <div class="col-md-3 text-white">
            <img class="img-fluid d-block mx-auto mb-5"
                 src="https://pingendo.github.io/templates/sections/assets/footer_logo2.png"></div>
          <div class="col-md-9 text-white align-self-center">
            <h1 class="display-3 mb-4">AMOS-Supercrunch</h1>
            <p class="lead mb-5">Get a fluid web page working on all devices with the Bootstrap 4 grid system.&nbsp;
              <br></p>
            <a href="#" class="btn btn-lg mx-1 btn-secondary">A claim here</a>
            <a href="#" class="btn btn-lg btn-primary mx-1">Your call to action</a>
          </div>
        </div>
      </div>
    </div>
    <div class="py-5 bg-dark text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-9">
            <p class="lead">Sign up to our newsletter for the latest news</p>
            <form class="form-inline">
              <div class="form-group">
                <input type="email" class="form-control" placeholder="Your e-mail here"></div>
              <button type="submit" class="btn btn-primary ml-3">Subscribe</button>
            </form>
          </div>
          <div class="col-4 col-md-1 align-self-center">
            <a href="https://www.facebook.com" target="_blank"><i class="fa fa-fw fa-facebook fa-3x text-white"></i></a>
          </div>
          <div class="col-4 col-md-1 align-self-center">
            <a href="https://twitter.com" target="_blank"><i class="fa fa-fw fa-twitter fa-3x text-white"></i></a>
          </div>
          <div class="col-4 col-md-1 align-self-center">
            <a href="https://www.instagram.com" target="_blank"><i
              class="fa fa-fw fa-instagram text-white fa-3x"></i></a>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12 mt-3 text-center">
            <p>© Copyright 2017 Pingendo - All rights reserved.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'hello',
    data() {
      return {
        msg: 'Welcome to Your Vue.js App',
      };
    },
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
  /*h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }*/

  // Semantic color scheme
  $theme-colors: (
    primary: #007bff,
    secondary: #004EA3,
    success: #28a745,
    info: #00fbff,
    warning: #ffc107,
    danger: #dc3545,
    light: #f8f9fa,
    dark: #343a40
  );

  // Options
  //
  // Quickly modify global styling by enabling or disabling optional features.

  $enable-rounded: true !default;
  $enable-shadows: false;
  $enable-gradients: false;
  $enable-transitions: true !default;
  $enable-hover-media-query: false !default;
  $enable-grid-classes: true !default;
  $enable-print-styles: true !default;

  // Variables
  //
  // Theme settings.

  $body-bg: white;
  $body-color: #292b2c;
  $body-color-inverse: invert($body-color) !default;
  $link-color: #007bff;

  $font-family-base: -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  $headings-font-family: -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif !default;

  $font-size-base: 1rem; // Assumes the browser default, typically `16px`
  $font-size-h1: 2.5 * $font-size-base;
  $font-size-h2: 2 * $font-size-base;
  $font-size-h3: 1.75 * $font-size-base;
  $font-size-h4: 1.5 * $font-size-base;
  $font-size-h5: 1.25 * $font-size-base;
  $font-size-h6: $font-size-base;

  $display1-size: 6 * $font-size-base;
  $display2-size: 5.5 * $font-size-base;
  $display3-size: 4.5 * $font-size-base;
  $display4-size: 3.5 * $font-size-base;

  $border-radius: .25rem;

  body > * {
    background-size: cover;
  }

  .opaque-overlay {
    overflow: hidden;
    position: relative;
    > *:first-child:before {
      content: '';
      width: 100%;
      height: 100%;
      display: block;
      position: absolute;
      left: 0px;
      top: 0px;
      pointer-events: none;
      background: rgba(map-get($theme-colors, 'dark'), 0.25);
    }
  }

  .gradient-overlay {
    overflow: hidden;
    position: relative;
    > *:first-child:before {
      content: '';
      width: 100%;
      height: 100%;
      display: block;
      position: absolute;
      left: 0px;
      top: 0px;
      pointer-events: none;
      background: linear-gradient(to top, map-get($theme-colors, 'dark') 0%, transparent 100%);

    }
  }


</style>
