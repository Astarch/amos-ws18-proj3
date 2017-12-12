<template>
  <div id="landing-page">
    <git-info></git-info>
    <pretrendr-header/>
    <div class="py-5 gradient-overlay landing-background">
      <div class="container py-5">
        <div class="row">
          <div class="col-md-3 text-white">
            <img class="img-fluid d-block mx-auto mb-5"
                 src="~assets/logo/logo.png"></div>
          <div class="col-md-9 text-white align-self-center">
            <h1 class="display-3 mb-4">pretrendr</h1>
            <p class="lead mb-5">To analyse and visualise your Data and find trends in it.&nbsp;
              <br></p>
            <div id=loginButtons>
              <a href="#register" ref="register" class="btn btn-lg btn-primary mx-1"
                 v-on:click.prevent="open(FormTypeEnum.register)">Register</a>
              <a href="#login" class="btn btn-lg btn-primary mx-1" v-on:click.prevent="open(FormTypeEnum.login)">Log In</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <pretrendr-footer/>

    <login-modal
      :is-active="modalActive"
      :type="modalType"
      v-on:close="modalActive = false"
      v-on:changeType="newType => modalType = newType"></login-modal>

  </div>


</template>


<script>
  import GitInfo from './../../components/common/GitInfo';
  import LoginModal from './modal/LoginModal';
  import PretrendrHeader from './Header.vue';
  import PretrendrFooter from './Footer.vue';
  import {FormTypeEnum} from './../../utils/constants';
  import http, {api} from '../../utils/api';
  import qs from 'qs';


  export default {
    name: 'landing',
    components: {GitInfo, LoginModal, PretrendrHeader, PretrendrFooter},
    data: () => ({
      modalActive: false,
      modalType: FormTypeEnum.login,
      requestAnswer: '',
      FormTypeEnum
    }),
    methods: {
      open(which) {
        this.modalActive = true;
        this.modalType = which;
      },
      close(event) {
        event.preventDefault();
        this.modalActive = false;
      },
    },
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>


  body > * {
    background-size: cover;
  }
  .landing-background{
    background-image: url(~assets/background.png);
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
