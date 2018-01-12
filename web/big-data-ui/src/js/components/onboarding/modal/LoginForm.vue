<template>
  <div id="form-login" class="onboarding-form form-login" :class="{active: active}">
    <input id="form-username" type="text" name="user" placeholder="Username" v-model="username" v-on:input="hideLoginError" v-on:keyup.enter.stop="submit()" v-on:change="hideLoginError">
    <input id="form-password" type="password" name="password" placeholder="Password" v-model="password" v-on:keyup.enter.stop="submit()" v-on:input="hideLoginError" v-on:change="hideLoginError">
    <div v-if="hasError" class="alert alert-danger" role="alert">
      {{errorText}}
    </div>
    <input id="form-login-submit" type="submit" :class="{disabled: reqStatus.pending}" v-on:click.stop="submit()" value="Login">
    <div class="links">
      <a href="" v-on:click.prevent.stop="openRegistration()">Don't have an account yet?</a>
    </div>
  </div>
</template>

<script>
  import {
    mapState
  } from "vuex";
  import {
    isPasswordValid,
    isNameValid
  } from "../../../utils/validation";
  import RequestStatus from 'src/js/models/RequestStatus';
  import {
    ServerErrors
  } from 'src/js/utils/constants';
  
  export default {
    name: "login-form",
    props: {
      active: Boolean
    },
    data: () => ({
      username: "",
      password: "",
      errorText: "",
      hasError: false,
      reqStatus: new RequestStatus()
    }),
    computed: mapState({
      // arrow functions can make the code very succinct!
      user: state => state.user.user,
    }),
    mounted() {

      // fill in username if user just registered
      if (this.user != undefined && this.user.username != undefined && this.user.username.length > 2) {
        this.username = this.user.username
      }
    },
    methods: {
      submit() {
        var data = {
          form: this.type
        };
  
        let validLoginData = this.validateLogin(this.username, this.password);
        if (validLoginData) {
          data.password = this.password;
          data.user = this.username;
          this.doLogin(this.username, this.password);
        }
      },
      doLogin(name, password) {
        this.reqStatus.pending = true;
        this.$store.dispatch('loginUser', {
            username: name,
            password
          })
          .then(requestStatus => {
            this.reqStatus = requestStatus;
            console.log(this.user)
            this.$router.push({
              path: "engagement"
            });
          }).catch(requestStatus => {
            this.reqStatus = requestStatus;
            console.log(requestStatus)
            if (this.reqStatus !== ServerErrors.ERROR_SERVER) {
              this.showLoginError("Login failure - please try again!");
            } else {
              this.showLoginError("Server error - please try again later!");
            }
          })
        /*api.auth
          .postLogin(name, password)
          .then(response => {
            this.$router.push({
              path: "engagement"
            });
          })
          .catch(error => {
            let isServerError =
              !error ||
              !error.response ||
              !error.response.status ||
              error.response.status >= 500;
            if (!isServerError) {
              this.showLoginError("Login failure - please try again!");
            } else {
              this.showLoginError("Server error - please try again later!");
            }
          });*/
      },
      validateLogin(name, password) {
        let isValid = false;
        if (!isNameValid(name)) {
          this.showLoginError("Please enter a valid username!");
        } else if (!isPasswordValid(password)) {
          this.showLoginError("Please enter a valid password!");
        } else {
          isValid = true;
          this.hideLoginError();
        }
  
        return isValid;
      },
      showLoginError(message) {
        if (typeof message === "string" || message instanceof String) {
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
        this.$emit("openRegistration");
      }
    }
  };
</script>

<style lang='scss'>
  @import "../../../../scss/forms";
</style>
