<template>
  <div :class="sidebarClasses"
       :data-background-color="backgroundColor"
       :data-active-color="activeColor">
    <!--
            Tip 1: you can change the color of the sidebar's background using: data-background-color="white | black | darkblue"
            Tip 2: you can change the color of the active button using the data-active-color="primary | info | success | warning | danger"
        -->
    <!-- -->
    <div class="sidebar-wrapper" id="style-3">
      <div class="logo">
        <a href="#" class="simple-text">
          <div class="logo-img">
            <img src="../../../../assets/icon_white.svg" alt="">
          </div>
          pretrendr Dashboard
        </a>
      </div>
      <slot>

      </slot>
      <ul :class="navClasses">
        <!--By default vue-router adds an active class to each route link. This way the links are colored when clicked-->
        <router-link tag="li"
                     v-for="(link, index) in sidebarLinks"
                     :to="link.path"
                     :ref="link.name"
                     :key="link.name + index"
                     active-class="active"
                     class="nav-item">
          <a class="nav-link">
            <i :class="link.icon"></i>
            <p>{{link.name}}</p>
          </a>
        </router-link>
      </ul>
      <moving-arrow :move-y="arrowMovePx">

      </moving-arrow>
    </div>
  </div>
</template>
<script>
  import MovingArrow from './MovingArrow.vue'

  export default {
    props: {
      type: {
        type: String,
        default: 'sidebar',
        validator: (value) => {
          let acceptedValues = ['sidebar', 'navbar']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      backgroundColor: {
        type: String,
        default: 'black',
        validator: (value) => {
          let acceptedValues = ['white', 'black', 'darkblue']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      activeColor: {
        type: String,
        default: 'success',
        validator: (value) => {
          let acceptedValues = ['primary', 'info', 'success', 'warning', 'danger']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      sidebarLinks: {
        type: Array,
        default: () => []
      }
    },
    components: {
      MovingArrow
    },
    computed: {
      sidebarClasses() {
        if (this.type === 'sidebar') {
          return 'sidebar'
        } else {
          return 'collapse navbar-collapse off-canvas-sidebar'
        }
      },
      navClasses() {
        if (this.type === 'sidebar') {
          return 'nav'
        } else {
          return 'nav navbar-nav'
        }
      },
      /**
       * Styles to animate the arrow near the current active sidebar link
       * @returns {{transform: string}}
       */
      arrowMovePx() {
        return this.linkHeight * this.activeLinkIndex
      }
    },
    data() {
      return {
        linkHeight: 60,
        activeLinkIndex: 0,

        windowWidth: 0,
        isWindows: false,
        hasAutoHeight: false
      }
    },
    methods: {
      findActiveLink() {
        this.sidebarLinks.find((element, index) => {
          let found = element.path === this.$route.path
          if (found) {
            this.activeLinkIndex = index
          }
          return found
        })
      }
    },
    mounted() {
      this.findActiveLink()
    },
    watch: {
      $route: function (newRoute, oldRoute) {
        this.findActiveLink()
      }
    }
  }

</script>
<style lang="scss" scoped>
  @import '../../../../scss/paper-dashboard';

  .sidebar {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    z-index: 1;
    background-size: cover;
    background-position: center center;
    .sidebar-wrapper {
      position: relative;
      height: 100%;
      overflow-y: auto;
      overflow-x: hidden;
      width: 260px;
      z-index: 4;
      box-shadow: inset -1px 0px 0px 0px $medium-gray;
    }
    .sidebar-background {
      position: absolute;
      z-index: 1;
      height: 100%;
      width: 100%;
      display: block;
      top: 0;
      left: 0;
      background-size: cover;
      background-position: center center;
    }

  }

  .sidebar,
  .off-canvas-sidebar {
    width: 260px;
    display: block;
    font-weight: 200;

    .logo {
      padding: 13px 0;
      margin: 0 20px;

      p {
        float: left;
        font-size: 20px;
        margin: 10px 10px;
        line-height: 20px;
      }

      .simple-text {
        padding: $padding-small-vertical $padding-zero;
        display: block;
        font-size: $font-size-base;
        text-align: center;
        font-weight: $font-weight-bold;
        line-height: 40px;
        text-align: left;

        .logo-img {
          width: 40px;
          display: inline-block;
          height: 40px;
          margin-left: 0px;
          margin-right: 10px;
          border-radius: 40px;
          text-align: center;

          img {
            max-width: 35px;
          }
        }
      }
    }

    .nav {
      //margin-top: 20px;

      .nav-item {
        flex-grow: 1;
        .nav-link {
          margin: 10px 0px;
          padding-left: 25px;
          padding-right: 25px;

          opacity: .7;
        }

        &:hover .nav-link {
          opacity: 1;
        }

        &.active .nav-link {
          color: $primary-color;
          opacity: 1;
        }
      }

      p {
        margin: 0;
        line-height: 30px;
        font-size: 12px;
        font-weight: 600;
        text-transform: uppercase;
        text-align: left;
      }

      i {
        font-size: 24px;
        float: left;
        margin-right: 15px;
        line-height: 30px;
        width: 30px;
        text-align: center;
      }
    }

    &:after,
    &:before {
      display: block;
      content: "";
      position: absolute;
      width: 100%;
      height: 100%;
      top: 0;
      left: 0;
      z-index: 2;
      background: $white-background-color;
    }

    &,
    &[data-background-color="white"] {
      @include sidebar-background-color($white-background-color, $default-color);
    }
    &[data-background-color="black"] {
      @include sidebar-background-color($black-background-color, $white-color);
    }
    &[data-background-color="darkblue"] {
      @include sidebar-background-color($darkblue-background-color, $white-color);
    }

    &[data-active-color="primary"] {
      @include sidebar-active-color($primary-color);
    }
    &[data-active-color="info"] {
      @include sidebar-active-color($info-color);
    }
    &[data-active-color="success"] {
      @include sidebar-active-color($success-color);
    }
    &[data-active-color="warning"] {
      @include sidebar-active-color($warning-color);
    }
    &[data-active-color="danger"] {
      @include sidebar-active-color($danger-color);
    }

  }

  .sidebar {
    -webkit-transition-property: top, bottom;
    transition-property: top, bottom;
    -webkit-transition-duration: .2s, .2s;
    transition-duration: .2s, .2s;
    -webkit-transition-timing-function: linear, linear;
    transition-timing-function: linear, linear;
    -webkit-overflow-scrolling: touch;
  }

  .sidebar {
    max-height: 100%;
    height: 100%;
    overflow: hidden;
    overflow-y: hidden;
  }
</style>
