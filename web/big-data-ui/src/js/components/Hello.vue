<template>
  <div class="hello">
    <nav class="navbar navbar-expand-md bg-primary navbar-dark">
      <div class="container">
        <a class="navbar-brand" href="#"><i class="fa d-inline fa-lg fa-cloud"></i><b> Brand</b></a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse"
                data-target="#navbar2SupportedContent" aria-controls="navbar2SupportedContent" aria-expanded="false"
                aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
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
            <p class="lead mb-5">To analyse and visualise your Data and find trends in it.&nbsp;
              <br></p>
              <div id = loginButtons>
                <a href="#register" class="btn btn-lg btn-primary mx-1" v-on:click="open('register', $event)">Register</a>
                <a href="#login" class="btn btn-lg btn-primary mx-1" v-on:click="open('login', $event)">Log In</a>
              </div>
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
        </div>
        <div class="row">
          <div class="col-md-12 mt-3 text-center">
            <p>© Copyright 2017 Pingendo - All rights reserved.</p>
          </div>
        </div>
      </div>
    </div>
    <div class="user-modal-container" id="login-modal" v-on:click=" close"> 
       <div class="user-modal"> 
          <ul class="form-switcher">
            <li v-on:click=" flip('register', $event)"><a href="" id="register-form">Register</a>

            </li>
            <li v-on:click= "flip('login', $event)"><a href="" id="login-form">Login</a>
            </li>
        </ul>
        <div class="form-register" id="form-register">
            <div class="error-message" v-text="registerError"></div>  
                <input type="text" name="name" placeholder="Name" v-model="registerName" v-on:keyup.enter="submit('register', $event)">  
                <input type="email" name="email" placeholder="Email" v-model="registerEmail" v-on:keyup.enter="submit('register', $event)">  
                <input type="password" name="password" placeholder="Password" v-model="registerPassword" v-on:keyup.enter="submit('register', $event)">  
                <input type="submit" v-on:click= "submit('register', $event)" v-model="registerSubmit" id="registerSubmit">  
             <div class="links">  
        <a href="" v-on:click="flip('login', $event)">Already have an account?</a>
</div>  
        </div>  
        <div class="form-login" id="form-login">
          <div class="error-message" v-text="loginError"></div>  
<input type="text" name="user" placeholder="Email or Username" v-model="loginUser" v-on:keyup.enter="submit('login', $event)">  
<input type="password" name="password" placeholder="Password" v-model="loginPassword" v-on:keyup.enter="submit('login', $event)">  
<input type="submit" v-on:click= "submit('login', $event)" v-model="loginSubmit"  id="loginSubmit">  
<div class="links">  
  <a href="" v-on:click="flip('password', $event)">Forgot your password?</a>
</div>  
        </div>  
        <div class="form-password" id="form-password">
          <div class="error-message" v-text="passwordError"></div>  
            <input type="text" name="email" placeholder="Email" v-model="passwordEmail" v-on:keyup.enter="submit('password', $event)">  
            <input type="submit" v-on:click="submit('password', $event)" v-model="passwordSubmit" id="passwordSubmit">  
        </div> 

        </div> 
  </div> 

  </div>


</template>



<script>
  var modal_submit_register = 'Register';  
  var modal_submit_password = 'Reset Password';  
  var modal_submit_login    = 'Login';
  export default {
    name: 'hello',
    data: function() {
      return{
            active: null,

               // Submit button text
            registerSubmit: modal_submit_register,
            passwordSubmit: modal_submit_password,
            loginSubmit: modal_submit_login,

            // Modal text fields
            registerName:     '',
            registerEmail:    '',
            registerPassword: '',
            loginUser:        '',
            loginPassword:    '',
            passwordEmail:    '',

            // Modal error messages
            registerError: '',
            loginError:    '',
            passwordError: '',
              }
    },
    methods:{
      open: function(which, event){
        event.preventDefault();
        if (this.active !== null) {
        $('#form-'+this.active).removeClass('active');
        $('#'+this.active+'-form').removeClass('active');
  }

        $('#login-modal').addClass('active');
        $('#form-'+which).addClass('active');
        $('#'+which+'-form').addClass('active');
        this.active = which;
      },

      close: function(event) {
      event.preventDefault();
      if (event.target.id =="login-modal"){
         $('#login-modal').removeClass('active');
        }
      },
      flip: function(which, e) {  
        e.preventDefault();
        if (which !== this.active) {
          $('#form-' + this.active).removeClass('active');
          $('#form-' + which).addClass('active');
          $('#'+which+'-form').addClass('active');
          $('#'+this.active+'-form').removeClass('active');

          this.active = which;
       }
    },

      submit: function(which, e) {  
        e.preventDefault();
        $('#'+which+'Submit').addClass('disabled');
        var data = { form: which };

        switch(which) {
          case 'register':
              console.log(this.registerName)
              data.name = this.registerName;
              data.email = this.registerEmail;
              data.password = this.registerPassword;
              break;
          case 'login':
              data.user = this.loginUser;
              data.password = this.loginPassword;
              break;
          case 'password':
              data.email = this.passwordEmail;
              break;
        }
      }


  }}
 
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

  .user-modal-container * {
  box-sizing: border-box;
}

.user-modal-container {
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  opacity: 0;
  visibility: hidden;
  cursor: pointer;
  overflow-y: auto;
  z-index: 3;
  font-family: 'Lato', 'Helvetica Neue', 'Helvetica', 'Arial', 'sans-serif';
  font-size: 14px;
  background-color: rgba(17,17,17,.9);
  -webkit-transition: all 0.25s linear;
  -moz-transition: all 0.25s linear;
  -o-transition: all 0.25s linear;
  -ms-transition: all 0.25s linear;
  transition: all 0.25s linear;
}

.user-modal-container.active {
  opacity: 1;
  visibility: visible;
}

.user-modal-container .user-modal {
  position: relative;
  margin: 50px auto;
  width: 90%;
  max-width: 500px;
  background-color: #f6f6f6;
  cursor: initial;
}

user-modal-container ul.form-switcher {
  margin: 0;
  padding: 0;
}

.user-modal-container ul.form-switcher li {
  list-style: none;
  display: inline-block;
  width: 50%;
  float: left;
  margin: 0;
}

.user-modal-container ul.form-switcher li a {
  width: 100%;
  display: block;
  height: 50px;
  line-height: 50px;
  color: #666666;
  background-color: #dddddd;
  text-align: center;
}

.user-modal-container ul.form-switcher li a.active {
  color: #000000;
  background-color: #f6f6f6;
}

.user-modal-container .form-login,
.user-modal-container .form-register,
.user-modal-container .form-password {
  padding: 75px 25px 25px;
  display: none;
}

.user-modal-container .form-login.active,
.user-modal-container .form-register.active,
.user-modal-container .form-password.active {
  display: block;
}


.user-modal-container input {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #eeeeee;
}

.user-modal-container input[type="submit"] {
  color: #f6f6f6;
  border: 0;
  margin-bottom: 0;
  background-color: #1842db;
  cursor: pointer;
}

.user-modal-container input[type="submit"]:hover {
  background-color: #4667e2;
}

.user-modal-container input[type="submit"]:active {
  background-color: #1842db;
}

.user-modal-container .links {
  text-align: center;
  padding-top: 25px;
}

.user-modal-container .links a {
  color: #1842db;
}

.user-modal-container input[type="submit"].disabled {
  background-color: #a2b3f0;
}

</style>
