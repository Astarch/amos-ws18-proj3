<template>
  <div>

    <!--Stats cards-->
    <div class="row">
      <div class="col-lg-3 col-sm-6" v-for="stats in statsCards">
        <stats-card>
          <div class="icon-big text-center" :class="`icon-${stats.type}`" slot="header">
            <i :class="stats.icon"></i>
          </div>
          <div class="numbers" slot="content">
            <p>{{stats.title}}</p>
            {{stats.value}}
          </div>
          <div class="stats" slot="footer">
            <i :class="stats.footerIcon"></i> {{stats.footerText}}
          </div>
        </stats-card>
      </div>
      <div class="col-lg-6 col-sm-6">
        <graph-card>
          <div slot="title">
            Header
          </div>
          <div slot="subTitle">
            Subheader
          </div>
          <div class="ct-chart" slot="content">
            <graph-container></graph-container>
          </div>
          <div class="stats" slot="footer">
            Footer
          </div>
        </graph-card>
      </div>
    </div>

  </div>
</template>
<script>
  import StatsCard from './StatsCard'
  import GraphCard from './GraphCard'
  import GraphContainer from './../graph/GraphContainer'
  export default {
    components: {
      StatsCard,
      GraphCard,
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
