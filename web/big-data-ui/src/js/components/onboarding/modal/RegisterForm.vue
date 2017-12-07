<template>
  <div id="form-register" class="onboarding-form form-register" :class="{active: active}">
    <input type="text"
           name="name"
           placeholder="Name"
           v-model="username"
           v-on:keyup.enter.stop="submit()"
           v-on:input="hideError">
    <input type="email"
           name="email"
           placeholder="Email"
           v-model="email"
           v-on:keyup.enter.stop="submit()"
           v-on:input="checkEmail"
           ref="emailInput"
           v-bind:style="regEmailStyle">
    <input type="password"
           name="password"
           placeholder="Password"
           v-model="password"
           v-on:keyup.enter.stop="submit()"
           v-on:input="checkPassword"
           ref="pwinput"
           v-bind:style="regPwStyle">
    <div v-if="hasError" class="alert alert-danger" role="alert">
      {{errorText}}
    </div>
    <input id="registerSubmit"
           type="submit"
           :class="{disabled: isSubmitting}"
           v-on:click.stop="submit()"
           value="Register">
    <div class="links">
      <a href="" v-on:click.prevent.stop="openLogin()">Already have an account?</a>
    </div>
  </div>
</template>

<script>
  import {isPasswordValid, isNameValid, isMailValid, getPasswordStrength} from '../../../utils/validation'
  import http, {api} from '../../../utils/api';
  import qs from 'qs';

  const colors = new Array("", "#F09090", "#EBA096", "#E5B09C", "#E0C0A2", "#DBCFA7", "#D6DFAD", "#D0EFB3", "#CBFFB9")



  export default {
    name: 'register-form',
    props: {
      active: Boolean
    },
    data: () => ({
      username: '',
      email: '',
      password: '',
      isSubmitting: false,
      errorText: '',
      hasError: false,

      // Style Password Input Field
      regPwStyle: {
        backgroundColor: ''
      },

      // Style Email Input Field
      regEmailStyle: {
        backgroundColor: ''
      }

    }),
    methods: {
      submit() {
        //this.isSubmitting = true;

        let validData = this.validateRegistration(this.username, this.email, this.password);

        if (validData) {
          this.doRegistration(this.username, this.email, this.password);
        }
      },
      doRegistration(name, email, password) {
        this.isSubmitting = true;
        api.auth.postRegistration(name, email, password)
           .then(response => {
             this.isSubmitting = false;
             console.log(response);
           })
           .catch(error => {
             this.isSubmitting = false;
             let errorCode = error.response.status;
             if (errorCode < 500) {
               this.showError('Register failure - please try again!')
             } else {
               this.showError('Server error - please try again later!')
             }
             console.log(error.response);

           });
      },
      validateRegistration(name, mail, password) {
        let validRegistration = false;
        if (!isNameValid(name)) {
          this.showError('Please enter a valid name!');
        } else if (!isMailValid(mail)) {
          this.showError('Please enter a valid mail!');
        } else if (!isPasswordValid(password)) {
          this.showError('Please enter a valid password!');
        } else {
          validRegistration = true;
          this.hideError();
        }
        return validRegistration;
      },
      checkEmail: function () {
        this.hideError();
        if (this.email.length > 0) {
          if (isMailValid(this.email)) {
            this.regEmailStyle.backgroundColor = colors[colors.length - 1];
          } else {
            this.regEmailStyle.backgroundColor = colors[1];
          }
        } else {
          this.regEmailStyle.backgroundColor = colors[0];
        }
      },
      checkPassword: function () {
        this.hideError();
        var strength = getPasswordStrength(this.password);

        if (strength > 0 && strength < colors.length) {
          this.regPwStyle.backgroundColor = colors[strength];
        } else {
          this.regPwStyle.backgroundColor = colors[0];
        }
      },
      showError(message) {
        if (typeof message === 'string' || message instanceof String) {
          this.hasError = true;
          this.errorText = message;
        } else {
          this.hasError = false;
        }
      },
      hideError() {
        this.hasError = false;
      },
      openLogin() {
        this.$emit('openLogin')
      }
    }
  }
</script>

<style lang='scss'>
  @import '../../../../scss/forms';
</style>
