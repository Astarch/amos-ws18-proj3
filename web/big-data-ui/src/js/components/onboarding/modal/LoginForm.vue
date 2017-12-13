<template>
  <div id="form-login" class="onboarding-form form-login" :class="{active: active}">
    <input type="text"
           name="user"
           placeholder="Username"
           v-model="username"
           v-on:input="hideLoginError"
           v-on:keyup.enter.stop="submit()"
           v-on:change="hideLoginError">
    <input type="password"
           name="password"
           placeholder="Password"
           v-model="password"
           v-on:keyup.enter.stop="submit()"
           v-on:input="hideLoginError"
           v-on:change="hideLoginError">
    <div v-if="hasError" class="alert alert-danger" role="alert">
      {{errorText}}
    </div>
    <input id="loginSubmit"
           type="submit"
           :class="{disabled: isSubmitting}"
           v-on:click.stop="submit()"
           value="Login"
    >
    <div class="links">
      <a href="" v-on:click.prevent.stop="openRegistration()">Don't have an account yet?</a>
    </div>
  </div>
</template>

<script>
  import {isPasswordValid, isNameValid} from '../../../utils/validation'
  import http, {api} from '../../../utils/api';
  import qs from 'qs';

  export default {
    name: 'login-form',
    props: {
      active: Boolean
    },
    data: () => ({
      username: '',
      password: '',
      isSubmitting: false,
      errorText: '',
      hasError: false,

    }),
    methods: {
      submit() {
        //this.isSubmitting = true;
        var data = {form: this.type};

        let validLoginData = this.validateLogin(this.username, this.password);
        if (validLoginData) {
          data.password = this.password;
          data.user = this.username;
          this.doLogin(this.username, this.password);
        }
      },
      doLogin(name, password) {
        this.isSubmitting = true;
        api.auth.postLogin(name, password)
           .then(response => {
             this.isSubmitting = false;
             this.$router.push({
               path: 'engagement'
             }),
               console.log(response);
           })
           .catch(error => {
             this.isSubmitting = false;
             let isServerError = (!error || !error.response || !error.response.status || error.response.status >= 500);
             if (!isServerError) {
               this.showLoginError('Login failure - please try again!')
             } else {
               this.showLoginError('Server error - please try again later!')
             }
             console.log(error.response);

           });
      },
      validateLogin(name, password) {
        let isValid = false;
        if (!isNameValid(name)) {
          this.showLoginError('Please enter a valid username!');

        } else if (!isPasswordValid(password)) {
          this.showLoginError('Please enter a valid password!');
        } else {
          isValid = true;
          this.hideLoginError();
        }

        return isValid;
      },
      showLoginError(message) {
        console.log('toggleLoginError(): ', message)
        if (typeof message === 'string' || message instanceof String) {
          this.hasError = true;
          this.errorText = message;
        } else {
          this.hasError = false;
        }
      },
      hideLoginError() {
        this.hasError = false;
      },
      openRegistration() {
        this.$emit('openRegistration')
      }
    }
  }
</script>

<style lang='scss'>
  @import '../../../../scss/forms';
</style>
