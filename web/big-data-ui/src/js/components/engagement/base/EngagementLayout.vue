<template>
  <div class="wrapper">
    <side-bar type="sidebar" :sidebar-links="$sidebar.sidebarLinks">

    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <engagement-content @click.native="toggleSidebar">

      </engagement-content>

      <content-footer></content-footer>
    </div>
  </div>
</template>
<script>
  import TopNavbar from './TopNavbar'
  import ContentFooter from './ContentFooter'
  import EngagementContent from './EngagementContent'

  export default {
    components: {
      TopNavbar,
      ContentFooter,
      EngagementContent
    },
    methods: {
      toggleSidebar () {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false)
        }
      }
    }
  }

</script>
<style lang="scss">

</style>
