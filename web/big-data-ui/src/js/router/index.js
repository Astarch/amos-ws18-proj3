import Vue from 'vue';
import Router from 'vue-router';
import VueResource from 'vue-resource';
import LandingPage from '../components/onboarding/LandingPage';
import GraphContainer from '../components/engagement/graph/GraphContainer';
import DashboardLayout from '../components/engagement/dashboard/layout/DashboardLayout';
import DashboardOverview from '../components/engagement/dashboard/views/Overview';
import UserProfile from '../components/engagement/dashboard/views/UserProfile';

Vue.use(VueResource);
Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: LandingPage,
    },
    {
      path: '/graph',
      component: GraphContainer,
    },
    {
      path: '/dashboard',
      component: DashboardLayout,
      name: 'dashboard-home',
      redirect: '/dashboard/overview'
    },
    {
      path: '/dashboard',
      component: DashboardLayout,
      redirect: '/dashboard/overview',
      children: [
        {
          path: 'overview',
          name: 'overview',
          component: DashboardOverview
        },
        {
          path: 'stats',
          name: 'stats',
          component: UserProfile
        },

      ]
    },
  ],
});
