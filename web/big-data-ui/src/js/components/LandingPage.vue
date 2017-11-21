<template>
  <div id="landing-page">
    <pretrendr-header/>
    <div class="py-5 gradient-overlay"
         style="background-image: url(&quot;https://pingendo.github.io/templates/sections/assets/cover_event.jpg&quot;);">
      <div class="container py-5">
        <div class="row">
          <div class="col-md-3 text-white">
            <img class="img-fluid d-block mx-auto mb-5"
                 src="../../assets/logo.png"></div>
          <div class="col-md-9 text-white align-self-center">
            <h1 class="display-3 mb-4">pretrendr</h1>
            <p class="lead mb-5">To analyse and visualise your Data and find trends in it.&nbsp;
              <br></p>
            <div id=loginButtons>
              <a href="#register" ref="register" class="btn btn-lg btn-primary mx-1"
                 v-on:click.prevent="open('register')">Register</a>
              <a href="#login" class="btn btn-lg btn-primary mx-1" v-on:click.prevent="open('login')">Log In</a>
            </div>

            <div id=apiTestButtons>
              <a href="#getAll" ref="getAll" class="btn btn-lg btn-primary mx-1" v-on:click.prevent="getAllUsers()">doLogin</a>
              <div class="card">
                <div class="card-body text-dark">{{requestAnswer}}</div>
              </div>
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
      v-on:changeType="newType => modalType = newType"/>

  </div>


</template>


<script>
  import LoginModal from './LoginModal';
  import PretrendrHeader from './Header.vue';
  import PretrendrFooter from './Footer.vue';
  import axios from 'axios';
  import qs from 'qs';


  export default {
    name: 'landing',
    components: {LoginModal, PretrendrHeader, PretrendrFooter},
    data: () => ({
      modalActive: false,
      modalType: 'login',
      requestAnswer: ''
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

      getAllUsers() {
        let port = "8081/"
        let localhostUrl = "http://localhost:" + port
        let stagingUrl = "http://staging.pretrendr.com:" + port

        var username = 'user2';
        var password = 'pass2';

        let http = axios.create({
          baseURL: localhostUrl,
          withCredentials: true,
          auth: qs.stringify({
            username: username,
            password: password
          }),
        });

        http.post('/auth/login', qs.stringify({
          username: username,
          password: password
        }))
            .then(response => {
              console.log(response);
              this.requestAnswer = response.data;
            })
            .catch(function (error) {
              console.log(error);
            });


      },
    },
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>


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
