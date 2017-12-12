<template>
  <div id="engagement-layout">
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

    <!--This sidebar appears only for screens smaller than 992px-->
    <side-bar type="navbar" :sidebar-links="$sidebar.sidebarLinks">
      <ul class="nav navbar-nav">
        <li>
          <a class="dropdown-toggle" data-toggle="dropdown">
            <i class="ti-panel"></i>
            <p>Stats</p>
          </a>
        </li>
        <drop-down title="5 Notifications" icon="ti-bell">

          <li><a>Notification 1</a></li>
          <li><a>Notification 2</a></li>
          <li><a>Notification 3</a></li>
          <li><a>Notification 4</a></li>
          <li><a>Another notification</a></li>

        </drop-down>
        <li>
          <a>
            <i class="ti-settings"></i>
            <p>Settings</p>
          </a>
        </li>
        <li class="divider"></li>
      </ul>
    </side-bar>
  </div>
</template>
<script>
  import TopNavbar from './base/TopNavbar'
  import ContentFooter from './base/ContentFooter'
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
  @import '../../../scss/paper-dashboard';
</style>
