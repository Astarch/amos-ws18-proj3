<template>
  <div id="form-register" class="onboarding-form form-register" :class="{active: active}">
    <input id="register-username" type="text" name="name" placeholder="Username" v-model="username" v-on:keyup.enter.stop="submit()" v-on:input="hideError">
    <input id="register-firstname" type="text" name="firstname" placeholder="Firstname" v-model="firstname" v-on:keyup.enter.stop="submit()" v-on:input="hideError">
    <input id="register-lastname" type="text" name="lastname" placeholder="Lastname" v-model="lastname" v-on:keyup.enter.stop="submit()" v-on:input="hideError">
    <input id="register-email" type="email" name="email" placeholder="Email" v-model="email" v-on:keyup.enter.stop="submit()" v-on:input="checkEmail" ref="emailInput" v-bind:style="regEmailStyle">
    <input id="register-password" type="password" name="password" placeholder="Password" v-model="password" v-on:keyup.enter.stop="submit()" v-on:input="checkPassword" ref="pwinput" v-bind:style="regPwStyle">
    <div v-if="hasError" class="alert alert-danger" role="alert">
      {{errorText}}
    </div>
    <input id="registerSubmit" type="submit" :class="{disabled: requestStatus.pending}" v-on:click.stop="submit()" value="Register">
    <div v-if="requestStatus.success && !requestStatus.pending" class="alert alert-success" role="alert">
      Congratulations {{user.username}}! You can now start to use pretrendr.
    </div>
    <div class="links">
      <a href="" v-on:click.prevent.stop="openLogin()">Already have an account?</a>
    </div>
  </div>
</template>

<script>
  import {
    mapState
  } from "vuex";
  import {
    isPasswordValid,
    isNameValid,
    isMailValid,
    getPasswordStrength
  } from "../../../utils/validation";
  import RequestStatus from 'src/js/models/RequestStatus';
  import {
    ServerErrors
  } from 'src/js/utils/constants';
  
  const colors = [
    "",
    "#F09090",
    "#EBA096",
    "#E5B09C",
    "#E0C0A2",
    "#DBCFA7",
    "#D6DFAD",
    "#D0EFB3",
    "#CBFFB9"
  ];
  
  export default {
    name: "register-form",
    props: {
      active: Boolean
    },
    data: () => ({
      username: "",
      firstname: "",
      lastname: "",
      email: "",
      password: "",
      errorText: "",
      requestStatus: new RequestStatus(),
      hasError: false,
  
      // Style Password Input Field
      regPwStyle: {
        backgroundColor: ""
      },
  
      // Style Email Input Field
      regEmailStyle: {
        backgroundColor: ""
      }
    }),
    computed: mapState({
      // arrow functions can make the code very succinct!
      user: state => state.user.user
    }),
    mounted() {
      this.clearRegistration()
    },
    methods: {
      submit() {
        //this.isSubmitting = true;
  
        let validData = this.validateRegistration(
          this.username,
          this.firstname,
          this.lastname,
          this.email,
          this.password
        );
  
        if (validData) {
          this.doRegistration(this.username, this.firstname, this.lastname, this.email, this.password);
        }
      },
      doRegistration(name, firstname ,lastname,  email, password) {
        this.requestStatus.pending = true;
        this.$store
          .dispatch("registerUser", {
            username: name,
            firstname: firstname,
            lastname: lastname,
            email,
            password
          })
          .then(requestStatus => {
            this.requestStatus = requestStatus;
            this.openLogin();
          })
          .catch(error => {

            this.requestStatus = error;
            if (this.requestStatus !== ServerErrors.ERROR_SERVER) {
              this.showError("User already registered!");
            } else {
              this.showError("Server error - please try again later!");
            }
          });
      },
      clearRegistration() {
        this.username = "";
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.password = "";
        this.requestStatus = new RequestStatus();
      },
      validateRegistration(name, firstname, lastname, mail, password) {
        let validRegistration = false;
        if (!isNameValid(name)) {
          this.showError("Please enter a valid name!");
        } else if (!isNameValid(firstname)) {
          this.showError("Please enter a valid firstname!");
        } else if (!isNameValid(lastname)) {
          this.showError("Please enter a valid lastname!");
        } else if (!isMailValid(mail)) {
          this.showError("Please enter a valid mail!");
        } else if (!isPasswordValid(password)) {
          this.showError("Please enter a valid password!");
        } else {
          validRegistration = true;
          this.hideError();
        }
        return validRegistration;
      },
      checkEmail: function() {
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
      checkPassword: function() {
        this.hideError();
        var strength = getPasswordStrength(this.password);
  
        if (strength > 0 && strength < colors.length) {
          this.regPwStyle.backgroundColor = colors[strength];
        } else {
          this.regPwStyle.backgroundColor = colors[0];
        }
      },
      showError(message) {
        if (typeof message === "string" || message instanceof String) {
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
        this.$emit("openLogin");
      }
    }
  };
</script>

<style lang='scss' scoped>
  @import "../../../../scss/forms";
  div.alert-success {
    margin-top: 10px;
  }
</style>
