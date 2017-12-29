<template>
  <div>

    <!--Stats cards-->
    <div class="row">
      <div class="col-lg-6 col-sm-6">
        <card>
          <div slot="title">
            Header
          </div>
          <div slot="subTitle">
            Subheader
          </div>
          <div slot="content">
            <graph-container></graph-container>
          </div>
          <div slot="footer">
            Footer
          </div>
        </card>
      </div>
      <div class="col-lg-6 col-sm-6">
        <card>
          <div slot="title">
            Header
          </div>
          <div slot="content">
            Some Content whatever
            Some Content whatever
          </div>
          <div  slot="footer">
            Footer
          </div>
        </card>
      </div>
    </div>

  </div>
</template>
<script>
  import Card from './../common/Card'
  import GraphContainer from './../graph/GraphContainer'
  export default {
    components: {
      Card,
      GraphContainer
    },
    /**
     * Chart data used to render stats, charts. Should be replaced with server data
     */
    data () {
      return {
        statsCards: [
          {
            type: 'warning',
            icon: 'ti-server',
            title: 'Capacity',
            value: '105GB',
            footerText: 'Updated now',
            footerIcon: 'ti-reload'
          },
          {
            type: 'success',
            icon: 'ti-wallet',
            title: 'Revenue',
            value: '$1,345',
            footerText: 'Last day',
            footerIcon: 'ti-calendar'
          },
          {
            type: 'danger',
            icon: 'ti-pulse',
            title: 'Errors',
            value: '23',
            footerText: 'In the last hour',
            footerIcon: 'ti-timer'
          },
          {
            type: 'info',
            icon: 'ti-twitter-alt',
            title: 'Followers',
            value: '+45',
            footerText: 'Updated now',
            footerIcon: 'ti-reload'
          }
        ],
      }
    }
  }

</script>
<style>

</style>
