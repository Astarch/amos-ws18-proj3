import Vue from 'vue';
import Router from 'vue-router';
import VueResource from 'vue-resource';
import LandingPage from '../components/onboarding/LandingPage';
import GraphContainer from '../components/engagement/graph/GraphContainer';
import EngagementLayout from '../components/engagement/EngagementLayout';
import Dashboard from '../components/engagement/dashboard/Dashboard';
import UserProfile from '../components/engagement/user-profile/UserProfile';
import TableList from '../components/engagement/table-list/TableList';

Vue.use(VueResource);
Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: LandingPage,
      redirect: '/public'

    },
    {
      path: '/public',
      component: LandingPage,
    },
    {
      path: '/engagement',
      component: EngagementLayout,
      name: 'dashboard-home',
      redirect: '/engagement/dashboard'
    },
    {
      path: '/graph',
      component: GraphContainer,
    },

    {
      path: '/engagement',
      component: EngagementLayout,
      redirect: '/engagement/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'overview',
          component: Dashboard
        },
        {
          path: 'stats',
          name: 'stats',
          component: UserProfile
        },
        {
          path: 'table-list',
          name: 'table-list',
          component: TableList
        },

      ]
    },
    { path: '', redirect: '/' },
    { path: '*', redirect: '/' }
  ],
});
