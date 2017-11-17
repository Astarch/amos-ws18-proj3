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
        <input type="text"
               name="name"
               placeholder="Name"
               v-model="registerName"
               v-on:keyup.enter.stop="submit(modal_type_register)"
               v-on:input="toggleRegistrationError">
        <input type="email"
               name="email"
               placeholder="Email"
               v-model="registerEmail"
               v-on:keyup.enter.stop="submit(modal_type_register)"
               v-on:input="checkEmail"
               ref="emailInput"
               v-bind:style="regEmailStyle">
        <input type="password"
               name="password"
               placeholder="Password"
               v-model="registerPassword"
               v-on:keyup.enter.stop="submit(modal_type_register)"
               v-on:input="checkPassword"
               ref="pwinput"
               v-bind:style="regPwStyle">
        <div v-if="showRegistrationError" class="alert alert-danger" role="alert">
          {{registrationErrorText}}
        </div>
        <input type="submit"
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
        <input type="text"
               name="user"
               placeholder="Email or Username"
               v-model="loginUser"
               v-on:input="toggleLoginError"
               v-on:keyup.enter.stop="submit(modal_type_login)"
               v-on:change="toggleLoginError">
        <input type="password"
               name="password"
               placeholder="Password"
               v-model="loginPassword"
               v-on:keyup.enter.stop="submit(modal_type_login)"
               v-on:input="checkPassword"
               v-on:change="toggleLoginError">
        <div v-if="showLoginError" class="alert alert-danger" role="alert">
          {{loginErrorText}}
        </div>
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

  const emailRegex = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
  const smallCharRegex = /[a-z]+/;
  const capitalCharRegex = /[A-Z]+/;
  const numberCharRegex = /[0-9]+/;
  const specialCharRegex = /[^A-Za-z0-9]+/;
  const colors = new Array("", "#F09090", "#EBA096", "#E5B09C", "#E0C0A2", "#DBCFA7", "#D6DFAD", "#D0EFB3", "#CBFFB9")


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

      showLoginError: false,
      showRegistrationError: false,

      loginErrorText: '',
      RegistrationErrorText: '',

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

      // Style Password Input Field
      regPwStyle: {
        backgroundColor: ''
      },

      // Style Email Input Field
      regEmailStyle: {
        backgroundColor: ''
      }
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
      submit(which) {
        //this.isSubmitting = true;
        var data = {form: this.type};

        switch (this.type) {
          case 'register':
            this.validateRegistration(this.registerName, this.registerEmail, this.registerPassword);
            console.log(this.registerName)
            data.name = this.registerName;
            data.email = this.registerEmail;
            data.password = this.registerPassword;
            break;
          case 'login':
            this.validateLogin(this.loginUser, this.loginPassword);
            data.user = this.loginUser;
            data.password = this.loginPassword;
            break;
        }
      },
      validateRegistration(name, mail, password) {
        if (!this.isNameValid(name)) {
          this.toggleRegistrationError('Please enter a valid name!');
        } else if (!this.isMailValid(mail)) {
          this.toggleRegistrationError('Please enter a valid mail!');
        } else if (!this.isPasswordValid(password)) {
          this.toggleRegistrationError('Please enter a valid password!');
        } else {
          this.toggleRegistrationError();
        }
      },
      validateLogin(name, password) {
        if (!this.isNameValid(name)) {
          this.toggleLoginError('Please enter a valid username!');
        } else if (!this.isPasswordValid(password)) {
          this.toggleLoginError('Please enter a valid password!');
        } else {
          this.toggleLoginError();
        }
      },
      toggleLoginError(message) {
        console.log('toggleLoginError(): ', message)
        if (typeof message === 'string' || message instanceof String) {
          this.showLoginError = true;
          this.loginErrorText = message;
        } else {
          this.showLoginError = false;
        }
      },
      toggleRegistrationError(message) {
        console.log('toggleRegistrationError(): ', message)
        if (typeof message === 'string' || message instanceof String) {
          this.showRegistrationError = true;
          this.registrationErrorText = message;
        } else {
          this.showRegistrationError = false;
        }
      },
      isMailValid(mail) {
        return mail.length > 0 && emailRegex.test(mail);
      },
      isNameValid(name) {
        return name.length > 2;
      },
      isPasswordValid(password) {
        return password.length > 4;
      },
      calculatePasswordStrength(password) {
        let strength = 0;

        if (password.length > 0) {
          strength += smallCharRegex.test(password); // contains a-z
          strength += capitalCharRegex.test(password); // contains A-Z
          strength += numberCharRegex.test(password); // contains 0-9
          strength += specialCharRegex.test(password); // contains special character
          strength += Math.floor(Math.min(4, password.length / 3)); // 0-12 characters result in 0-4 points
        }
        return strength;
      },
      checkEmail: function () {
        this.toggleRegistrationError();
        this.toggleLoginError();
        if (this.registerEmail.length > 0) {
          if (emailRegex.test(this.registerEmail)) {
            this.regEmailStyle.backgroundColor = colors[colors.length - 1];
          } else {
            this.regEmailStyle.backgroundColor = colors[1];
          }
        } else {
          this.regEmailStyle.backgroundColor = colors[0];
        }
      },
      checkPassword: function () {
        this.toggleRegistrationError();
        this.toggleLoginError();
        var strength = this.calculatePasswordStrength(this.registerPassword);

        if (strength > 0 && strength < colors.length) {
          this.regPwStyle.backgroundColor = colors[strength];
        } else {
          this.regPwStyle.backgroundColor = colors[0];
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
  .user-modal-container .form-register.active {
    display: block;
  }

  .user-modal-container .form-login .alert,
  .user-modal-container .form-register .alert {
    border-radius: 0;
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
