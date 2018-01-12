<template>

  <div class="user-modal-container" :class="{active: isActive}" id="login-modal" v-on:click.self="close">
    <div class="user-modal">
      <ul class="form-switcher">
        <li v-on:click.prevent.stop="flip('register')">
          <a href="" :class="{active: type === FormTypeEnum.register}" id="register-form">Register</a>
        </li>
        <li v-on:click.prevent.stop="flip('login')">
          <a href="" :class="{active: type === FormTypeEnum.login}" id="login-form">Login</a>
        </li>
      </ul>
      <register-form
        v-if="type == FormTypeEnum.register && isActive"
        :active="type == FormTypeEnum.register"
        v-on:openLogin="flip(FormTypeEnum.login)"></register-form>
      <login-form
        v-if="type == FormTypeEnum.login && isActive"
        :active="type == FormTypeEnum.login"
        v-on:openRegistration="flip(FormTypeEnum.register)"></login-form>
    </div>
  </div>


</template>


<script>
  import LoginForm from './LoginForm';
  import RegisterForm from './RegisterForm';
  import {FormTypeEnum} from '../../../utils/constants';


  export default {
    name: 'login-modal',
    components: {LoginForm, RegisterForm},
    props: {
      isActive: Boolean,
      type: {
        validator: value => value === FormTypeEnum.login || value === FormTypeEnum.register,
        default: FormTypeEnum.login,
      },
    },

    data: () => ({
      FormTypeEnum
    }),
    methods: {
      close: function () {
        this.isSubmitting = false;
        this.$emit('close');
      },
      flip: function (which) {
        this.isSubmitting = false;
        if (this.type === this.FormTypeEnum.register) {
          this.$emit('changeType', this.FormTypeEnum.login);
        } else {
          this.$emit('changeType', this.FormTypeEnum.register);
        }
      },

    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
  @import '../../../../scss/forms';

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
    z-index: 99999;
    font-family: 'Lato', 'Helvetica Neue', 'Helvetica', 'Arial', 'sans-serif';
    font-size: 14px;
    background-color: rgba(17, 17, 17, .9);
    -webkit-transition: all 0.25s linear;
    -moz-transition: all 0.25s linear;
    -o-transition: all 0.25s linear;
    -ms-transition: all 0.25s linear;
    transition: all 0.25s linear;

    * {
      box-sizing: border-box;
      padding-left: 0
    }

    &.active {
      opacity: 1;
      visibility: visible;
    }

    .user-modal {
      position: relative;
      margin: 50px auto;
      width: 90%;
      max-width: 500px;
      background-color: map-get($theme-colors, 'light');
      cursor: initial;
    }
  }

  .user-modal-container ul.form-switcher {
    margin: 0;
    padding: 0;

    li {
      list-style: none;
      display: inline-block;
      width: 50%;
      float: left;
      margin: 0;

      a {
        width: 100%;
        display: block;
        height: 50px;
        line-height: 50px;
        color: #666666;
        background-color: #dddddd;
        text-align: center;

        &.active {
          color: #000000;
          background-color: map-get($theme-colors, 'light');
        }
      }
    }
  }
</style>
