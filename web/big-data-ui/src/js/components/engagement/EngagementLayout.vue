<template>
  <div id="engagement-layout">
    <div class="wrapper">
      <side-bar type="sidebar" :sidebar-links="$sidebar.sidebarLinks">

      </side-bar>
      <div class="main-panel">
        <content-header></content-header>

        <engagement-content @click.native="toggleSidebar">

        </engagement-content>

        <content-footer></content-footer>
      </div>
    </div>

    <!--This sidebar appears only for screens smaller than 992px-->
    <side-bar type="navbar" :sidebar-links="$sidebar.sidebarLinks">
      <ul class="nav navbar-nav">
      </ul>
    </side-bar>
  </div>
</template>
<script>
  import ContentHeader from './base/ContentHeader'
  import ContentFooter from './base/ContentFooter'
  import EngagementContent from './EngagementContent'

  export default {
    components: {
      ContentHeader,
      ContentFooter,
      EngagementContent
    },
    methods: {
      toggleSidebar() {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false)
        }
      }
    }
  }

</script>
<style lang="scss">
</style>
