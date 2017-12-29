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
        <a href="" class="simple-text">
          <div class="logo-img">
            <img src="~assets/logo/icon_white.svg" alt="">
          </div>
          {{user.firstname}} {{user.lastname}}
        </a>
      </div>
      <slot>

      </slot>
      <ul :class="navClasses">
        <!--By default vue-router adds an active class to each route link. This way the links are colored when clicked-->
        <router-link
          v-for="(link, index) in sidebarLinks"
          :to="link.path"
          tag="li"
          :ref="link.name"
          :key="link.name + index"
          active-class="active">
          <a>
            <i :class="link.icon"></i>

            <p>{{link.name}}
            </p>
          </a>
        </router-link>
        <li>
          <a href="" v-on:click.prevent.stop="logout">
            <i class="ti-power-off"></i>
            <p>Logout</p>
            </a>
          </li>
      </ul>
      <moving-arrow :move-y="arrowMovePx">

      </moving-arrow>
    </div>
  </div>
</template>
<script>
import MovingArrow from "./MovingArrow.vue";
import { mapState } from "vuex";

export default {
  props: {
    type: {
      type: String,
      default: "sidebar",
      validator: value => {
        let acceptedValues = ["sidebar", "navbar"];
        return acceptedValues.indexOf(value) !== -1;
      }
    },
    backgroundColor: {
      type: String,
      default: "pretrendr",
      validator: value => {
        let acceptedValues = ["white", "black", "darkblue", "pretrendr"];
        return acceptedValues.indexOf(value) !== -1;
      }
    },
    activeColor: {
      type: String,
      default: "pretrendr",
      validator: value => {
        let acceptedValues = [
          "primary",
          "info",
          "success",
          "warning",
          "danger",
          "pretrendr"
        ];
        return acceptedValues.indexOf(value) !== -1;
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
      if (this.type === "sidebar") {
        return "sidebar";
      } else {
        return "collapse navbar-collapse off-canvas-sidebar";
      }
    },
    navClasses() {
      if (this.type === "sidebar") {
        return "nav";
      } else {
        return "nav navbar-nav";
      }
    },
    /**
       * Styles to animate the arrow near the current active sidebar link
       * @returns {{transform: string}}
       */
    arrowMovePx() {
      return this.linkHeight * this.activeLinkIndex;
    },

    ...mapState({
      // arrow functions can make the code very succinct!
      user: state => state.user.user
    })
  },
  data() {
    return {
      linkHeight: 60,
      activeLinkIndex: 0,

      windowWidth: 0,
      isWindows: false,
      hasAutoHeight: false
    };
  },
  methods: {
    findActiveLink() {
      this.sidebarLinks.find((element, index) => {
        let found = element.path === this.$route.path;
        if (found) {
          this.activeLinkIndex = index;
        }
        return found;
      });
    },
    logout(){
      this.$store.dispatch('logoutUser').then( event =>{
        console.log("logged out")
         this.$router.push({
              path: "/"
            });
      }).catch()
    }
  },
  mounted() {
    this.findActiveLink();
  },
  watch: {
    $route: function(newRoute, oldRoute) {
      this.findActiveLink();
    }
  }
};
</script>
<style lang="scss" scoped>
.logo-img {
  background: transparent !important;
}
</style>
