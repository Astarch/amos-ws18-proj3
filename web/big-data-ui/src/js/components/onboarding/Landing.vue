<template>
  <div id="landing-page">
  
    <landing-header v-on:openRegistration="open(FormTypeEnum.register)" v-on:openLogin="open(FormTypeEnum.login)">
      <git-info :isActive="isGitInfoShown"></git-info>
    </landing-header>
    <!-- Header -->
    <a id="home"></a>
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
                  <a href="#register" ref="register" class="btn btn-default btn-lg register" v-on:click.prevent="open(FormTypeEnum.register)">Register Now</a>
                </li>
                <li>
                  <a href="#login" class="btn btn-default btn-lg login" v-on:click.prevent="open(FormTypeEnum.login)">Login</a>
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
    <a id="about-pretrendr"></a>
    <div class="content-section-a">  
      <div class="container">
        <div class="row display-flex">
          <div class="col-lg-5 col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About pretrendr</h2>
            <p class="lead">Pretrendr is an open source project to predict technology trends out of open data sources. 
              It is a web-based solution to extract trend data out of the worlds broadcast, print and web news and predict 
              if it will become a trend.</p>
            <p class="lead">Pretrendr simplifies the identification of existing and emerging trends that reshape industries. 
              News are linked and refined to evaluate trends in order to deliver accurate results. We provide an easy-to-use 
              webpage where trends can be monitored
              and identified.</p>
          </div>
          <div class="col-lg-5 col-lg-offset-2 col-sm-6">
            <img class="img-responsive" src="~assets/landing-page/aboutpretrendr.jpg" alt="">
          </div>
        </div>
  
      </div>
      <!-- /.container -->
  
    </div>
    <!-- /.content-section-a -->
    <a id="about-us"/>
    <div class="content-section-b">  
      <div class="container">  
        <div class="row display-flex">
          <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Us</h2>
            <p class="lead">
              We are a team of seven students from the TU Berlin who came together in a course about agile methods and 
              open source software development. Each team member is studying towards the master of science degree in a 
              software related field. Lara, Tristan, Joud and Scott are studying Computer Science, Florian and Patrick 
              Information Systems Management and Antonia Computer Engineering.
            </p>
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
            <img class="img-responsive" src="~assets/landing-page/aboutus.jpg" alt="">
          </div>
          <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student1.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Patrick</h2>
            <p class="lead">
              Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.
            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
           <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student2.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Lara</h2>
            <p class="lead">
               Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.
            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
           <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student1.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Tristan</h2>
            <p class="lead">
               Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.
            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
           <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student2.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Antonia</h2>
            <p class="lead">
               Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.
            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
           <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student1.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Florian</h2>
            <p class="lead">
               Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.
            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
           <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student2.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Joud</h2>
            <p class="lead">
               Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.
            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
           <div class="col-lg-5 col-lg-offset-1 col-sm-push-6  col-sm-6">
             <img class="resize" src="~assets/landing-page/student1.jpg" alt="">
          </div>
          <div class="col-lg-5 col-sm-pull-6  col-sm-6">
          <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">About Scott</h2>
            <p class="lead">
               Hi my name is .... I'm a super cool developer of this Website and study something really awesome. Frontend? Backend?
              No problem for me because I can do everything. I'm a graduate student persuing a degree in being a professional developer.            </p>
            <p class="lead">
              Get connected on LinkedIn :
            </p>
            <p>
                <a href="#" class="btn btn-primary btn-lg"><i
                    class="fa fa-linkedin fa-fw" aria-hidden="true"></i></a>
            </p>
          </div>
        </div>
  
      </div>
      <!-- /.container -->
  
    </div>
    <!-- /.content-section-b -->
  
    <!--<div class="content-section-a">
  
      <div class="container">
  
        <div class="row">
          <div class="col-lg-5 col-sm-6">
            <hr class="section-heading-spacer">
            <div class="clearfix"></div>
            <h2 class="section-heading">Google Web Fonts and<br>Font Awesome Icons</h2>
            <p class="lead">This template features the 'Lato' font, part of the <a target="_blank" href="http://www.google.com/fonts">Google Web Font library</a>, as well as <a target="_blank" href="http://fontawesome.io">icons from Font Awesome</a>.</p>
          </div>
          <div class="col-lg-5 col-lg-offset-2 col-sm-6">
            <img class="img-responsive" src="~assets/landing-page/phones.png" alt="">
          </div>
        </div>
  
      </div>
       /.container
  
    </div>-->
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
  
    <onboarding-modal :is-active="modalActive" :type="modalType" v-on:close="modalActive = false" v-on:changeType="newType => modalType = newType"></onboarding-modal>
  
  
  </div>
</template>

<script>
  import GitInfo from "./../../components/common/GitInfo";
  import OnboardingModal from "./modal/OnboardingModal";
  import LandingHeader from "./LandingHeader";
  import LandingFooter from "./LandingFooter";
  
  import {
    FormTypeEnum
  } from "./../../utils/constants";
  
  export default {
    name: "Landing",
    components: {
      GitInfo,
      OnboardingModal,
      LandingHeader,
      LandingFooter
    },
    data: () => ({
      modalActive: false,
      modalType: FormTypeEnum.login,
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
      }
    },
    computed: {
      isGitInfoShown() {
        if (GIT === undefined) {
          return false;
        }
        return GIT && GIT.BRANCH && !GIT.BRANCH.includes("master");
      }
    }
  };
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
    padding-top: 50px;
    /* If you're making other pages, make sure there is 50px of padding to make sure the navbar doesn't overlap content! */
    padding-bottom: 50px;
    text-align: center;
    color: #f8f8f8;
    background: url(~assets/blur.png) no-repeat center center;
    background-size: cover;
  }
  
  .intro-logo>img {
    position: relative;
    max-width: 200px;
  }
  
  .intro-message {
    position: relative;
    padding-bottom: 20%;
  }
  
  .intro-message>h1 {
    margin: 0;
    text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
    font-size: 5em;
  }
  
  .intro-divider {
    width: 400px;
    border-top: 1px solid #f8f8f8;
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  }
  
  .intro-message>h3 {
    text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
  }

 .row.display-flex {
  display: flex;
  flex-wrap: wrap;
}
.row.display-flex > [class*='col-'] {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
  
  @media (max-width: 767px) {
    .intro-message {
      padding-bottom: 15%;
    }
    .intro-message>h1 {
      font-size: 3em;
    }
    ul.intro-social-buttons>li {
      display: block;
      margin-bottom: 20px;
      padding: 0;
    }
    ul.intro-social-buttons>li:last-child {
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

  .content-section-a .img-responsive,
  .content-section-b .img-responsive{
    box-shadow: 1px 1px 10px 0px rgba(50, 50, 50, 0.5);
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

  .resize,
  .thumbnail > img,
  .thumbnail a > img,
  .carousel-inner > .item > img,
  .carousel-inner > .item > a > img {
    display: block;
    max-width: 50%;
    height: 80%;
}

  @media (max-width: 1199px) {
    ul.banner-social-buttons {
      float: left;
      margin-top: 15px;
    }
  }
  
  @media (max-width: 767px) {
    .banner h2 {
      margin: 0;
      text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.6);
      font-size: 3em;
    }
    ul.banner-social-buttons>li {
      display: block;
      margin-bottom: 20px;
      padding: 0;
    }
    ul.banner-social-buttons>li:last-child {
      margin-bottom: 0;
    }
  }
</style>
