<template>
  <div id="landing-page">
      <header-nav></header-nav>
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
            <div id=loginButtons>
              <a href="#register" ref="register" class="btn btn-lg btn-primary mx-1" v-on:click.prevent="open('register')">Register</a>
              <a href="#login" class="btn btn-lg btn-primary mx-1" v-on:click.prevent="open('login')">Log In</a>
            </div>
          </div>
        </div>
      </div>
    </div>
   <footer-fix></footer-fix>

    <login-modal
      :is-active="modalActive"
      :type="modalType"
      v-on:close="modalActive = false"
      v-on:changeType="newType => modalType = newType"/>

  </div>


</template>


<script>
  import LoginModal from './LoginModal';


  export default {
    name: 'landing',
    components: { LoginModal },
    data: () => ({
      modalActive: false,
      modalType: 'login',
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
