<template>

  <div class="user-modal-container" :class="{active: isActive}" id="login-modal" v-on:click.self="close">
    <div class="user-modal">
      <ul class="form-switcher">
        <li v-on:click.prevent.stop="flip('register')">
          <a href="" :class="{active: type === modal_type_register}" id="register-form">Register</a>
        </li>
        <li v-on:click.prevent.stop="flip('login')">
          <a href="" :class="{active: type === modal_type_login}" id="login-form">Login</a>
        </li>
      </ul>
      <div class="form-register" :class="{active: type == modal_type_register}" id="form-register">
        <div class="error-message" v-text="registerError"></div>
        <input type="text" name="name" placeholder="Name" v-model="registerName"
               v-on:keyup.enter.stop="submit(modal_type_register)">
        <input type="email" name="email" placeholder="Email" v-model="registerEmail"
               v-on:keyup.enter.stop="submit(modal_type_register)">
        <input type="password" name="password" placeholder="Password" v-model="registerPassword"
               v-on:keyup.enter.stop="submit(modal_type_register)">
        <input type="submit"
               class="lol2"
               :class="{disabled: isSubmitting}"
               v-on:click.stop="submit(modal_type_register)"
               v-model="registerSubmit"
               id="registerSubmit">
        <div class="links">
          <a href="" v-on:click.prevent.stop="flip(modal_type_login)">Already have an account?</a>
        </div>
      </div>
      <div class="form-login" :class="{active: type == modal_type_login}" id="form-login">
        <div class="error-message" v-text="loginError"></div>
        <input type="text" name="user" placeholder="Email or Username" v-model="loginUser"
               v-on:keyup.enter.stop="submit(modal_type_login)">
        <input type="password" name="password" placeholder="Password" v-model="loginPassword"
               v-on:keyup.enter.stop="submit(modal_type_login)">
        <input type="submit"
               :class="{disabled: isSubmitting}"
               v-on:click.stop="submit(modal_type_login)"
               v-model="loginSubmit"
               id="loginSubmit">
        <div class="links">
          <a href="" v-on:click.prevent.stop="flip(modal_type_register)">Don't have an account yet?</a>
        </div>
      </div>

    </div>
  </div>


</template>


<script>
  const modal_submit_register = 'Register';
  const modal_submit_login = 'Login';


  const modalTypeLogin = 'login';
  const modal_type_register = 'register';


  export default {
    name: 'login-modal',
    props: {
      isActive: Boolean,
      type: {
        validator: value => value === modalTypeLogin || value === modal_type_register,
        default: modalTypeLogin,
      },
    },
    data: () => ({
      isSubmitting: false,

      // Submit button text
      registerSubmit: modal_submit_register,
      loginSubmit: modal_submit_login,

      // Modal text fields
      registerName: '',
      registerEmail: '',
      registerPassword: '',
      loginUser: '',
      loginPassword: '',

      // Modal error messages
      registerError: '',
      loginError: '',
    }),
    created() {
      this.modal_type_login = modalTypeLogin;
      this.modal_type_register = modal_type_register;
    },
    methods: {
      close: function () {
        console.log("close");
        this.isSubmitting = false;
        this.$emit('close');
      },
      flip: function (which) {
        console.log("flip");
        this.isSubmitting = false;
        if (this.type === this.modal_type_register) {
          this.$emit('changeType', this.modal_type_login);
        } else {
          this.$emit('changeType', this.modal_type_register);
        }

      },

      submit: function (which) {
        this.isSubmitting = true;
        var data = {form: this.type};

        switch (this.type) {
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
        }
      }


    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>

  .user-modal-container * {
    box-sizing: border-box;
    padding-left: 0
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
    background-color: rgba(17, 17, 17, .9);
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
    background-color: map-get($theme-colors, 'light');
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
    background-color: map-get($theme-colors, 'light');
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
    background-color: map-get($theme-colors, 'primary');
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
