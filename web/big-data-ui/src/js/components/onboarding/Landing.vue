<template>
  <div id="landing-page">

    <landing-header
      v-on:openRegistration="open(FormTypeEnum.register)"
      v-on:openLogin="open(FormTypeEnum.login)">
      <git-info :isActive="isGitInfoShown"></git-info>
    </landing-header>
    <!-- Header -->
    <a name="about"></a>
    <div class="intro-header" v-bind:style="{'padding-top': isGitInfoShown ? '80px': '50px'}">
      <div class="container">

        <div class="row">
          <div class="col-lg-12">
            <div class="intro-logo">
              <img class="img-fluid" src="~assets/logo/logo.png">
            </div>
            <div class="intro-message">
              <h1>pretrendr</h1>
              <h3>Analyse and Visualize your data and identify trends</h3>
              <hr class="intro-divider">
              <ul class="list-inline intro-social-buttons">
                <li>
                  <a href="#register" ref="register"
                     class="btn btn-default btn-lg"
                     v-on:click.prevent="open(FormTypeEnum.register)">Register Now</a>
                </li>
                <li>
                  <a href="#login" class="btn btn-default btn-lg"
                     v-on:click.prevent="open(FormTypeEnum.login)">Login</a>
                </li>
              </ul>
            </div>
          </div>
        </div>

      </div>
      <!-- /.container -->
    </div>
    <!-- /.intro-header -->

    <!-- Page Content -->
    <a name="services"></a>
    <div class="content-section-a">

      <div class="container">
        <div class="row">
          <div class="col-lg-5 col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">Death to the Stock Photo:<br>Special Thanks</h2>
            <p class="lead">A special thanks to <a target="_blank" href="http://join.deathtothestockphoto.com/">Death to the Stock Photo</a>
              for providing the photographs that you see in this template. Visit their website to become a member.</p>
          </div>
          <div class="col-lg-5 col-lg-offset-2 col-sm-6">
            <img class="img-responsive" src="~assets/landing-page/ipad.png" alt="">
          </div>
        </div>

      </div>
      <!-- /.container -->

    </div>
    <!-- /.content-section-a -->

    <div class="content-section-b">

      <div class="container">

        <div class="row">
          <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">3D Device Mockups<br>by PSDCovers</h2>
            <p class="lead">
              Turn your 2D designs into high quality, 3D product shots in seconds using free Photoshop actions by <a
              target="_blank" href="http://www.psdcovers.com/">PSDCovers</a>! Visit their website to download some of their awesome, free photoshop actions!
            </p>
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
            <img class="img-responsive" src="~assets/landing-page/dog.png" alt="">
          </div>
        </div>

      </div>
      <!-- /.container -->

    </div>
    <!-- /.content-section-b -->

    <div class="content-section-a">

      <div class="container">

        <div class="row">
          <div class="col-lg-5 col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">Google Web Fonts and<br>Font Awesome Icons</h2>
            <p class="lead">This template features the 'Lato' font, part of the <a target="_blank"
                                                                                   href="http://www.google.com/fonts">Google Web Font library</a>, as well as <a
              target="_blank" href="http://fontawesome.io">icons from Font Awesome</a>.</p>
          </div>
          <div class="col-lg-5 col-lg-offset-2 col-sm-6">
            <img class="img-responsive" src="~assets/landing-page/phones.png" alt="">
          </div>
        </div>

      </div>
      <!-- /.container -->

    </div>
    <!-- /.content-section-a -->

    <a name="contact"></a>
    <div class="banner">

      <div class="container">

        <div class="row">
          <div class="col-lg-6">
            <h2>Connect to pretrendr:</h2>
          </div>
          <div class="col-lg-6">
            <ul class="list-inline banner-social-buttons">
              <li>
                <a href="https://github.com/Astarch/amos-ws18-proj3" class="btn btn-default btn-lg"><i
                  class="fa fa-github fa-fw"></i> <span class="network-name">Github</span></a>
              </li>
            </ul>
          </div>
        </div>

      </div>
      <!-- /.container -->

    </div>
    <!-- /.banner -->

    <landing-footer></landing-footer>

    <onboarding-modal
      :is-active="modalActive"
      :type="modalType"
      v-on:close="modalActive = false"
      v-on:changeType="newType => modalType = newType"></onboarding-modal>


  </div>
</template>

<script>
  import GitInfo from './../../components/common/GitInfo';
  import OnboardingModal from './modal/OnboardingModal';
  import LandingHeader from './LandingHeader';
  import LandingFooter from './LandingFooter';

  import {FormTypeEnum} from './../../utils/constants';

  export default {
    name: 'Landing',
    components: {
      GitInfo, OnboardingModal, LandingHeader, LandingFooter
    },
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
    computed:{
      isGitInfoShown(){
        if(GIT === undefined){
          return false
        }
        return GIT && GIT.BRANCH && !GIT.BRANCH.includes("master")
      }
    }
  }
</script>

<style lang='scss' scoped>
  body,
  html {
    width: 100%;
    height: 100%;
  }


  .lead {
    font-size: 18px;
    font-weight: 400;
  }

  .intro-header {
    padding-top: 50px; /* If you're making other pages, make sure there is 50px of padding to make sure the navbar doesn't overlap content! */
    padding-bottom: 50px;
    text-align: center;
    color: #f8f8f8;
    background: url(~assets/blur.png) no-repeat center center;
    background-size: cover;
  }

  .intro-logo > img {
    position: relative;
    max-width: 200px;

  }

  .intro-message {
    position: relative;
    padding-bottom: 20%;
  }

  .intro-message > h1 {
    margin: 0;
    text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
    font-size: 5em;
  }

  .intro-divider {
    width: 400px;
    border-top: 1px solid #f8f8f8;
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  }

  .intro-message > h3 {
    text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
  }

  @media(max-width: 767px) {
    .intro-message {
      padding-bottom: 15%;
    }

    .intro-message > h1 {
      font-size: 3em;
    }

    ul.intro-social-buttons > li {
      display: block;
      margin-bottom: 20px;
      padding: 0;
    }

    ul.intro-social-buttons > li:last-child {
      margin-bottom: 0;
    }

    .intro-divider {
      width: 100%;
    }
  }

  .network-name {
    text-transform: uppercase;
    font-size: 14px;
    font-weight: 400;
    letter-spacing: 2px;
  }

  .content-section-a {
    padding: 50px 0;
    background-color: #f8f8f8;
  }

  .content-section-b {
    padding: 50px 0;
    border-top: 1px solid #e7e7e7;
    border-bottom: 1px solid #e7e7e7;
  }

  .section-heading {
    margin-bottom: 30px;
  }

  .section-heading-spacer {
    float: left;
    width: 200px;
    border-top: 3px solid #e7e7e7;
  }

  .banner {
    padding: 100px 0;
    color: #f8f8f8;
    background: url(~assets/landing-page/banner-bg.jpg) no-repeat center center;
    background-size: cover;
  }

  .banner h2 {
    margin: 0;
    text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
    font-size: 3em;
  }

  .banner ul {
    margin-bottom: 0;
  }

  .banner-social-buttons {
    float: right;
    margin-top: 0;
  }

  @media(max-width: 1199px) {
    ul.banner-social-buttons {
      float: left;
      margin-top: 15px;
    }
  }

  @media(max-width: 767px) {
    .banner h2 {
      margin: 0;
      text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
      font-size: 3em;
    }

    ul.banner-social-buttons > li {
      display: block;
      margin-bottom: 20px;
      padding: 0;
    }

    ul.banner-social-buttons > li:last-child {
      margin-bottom: 0;
    }
  }

</style>
