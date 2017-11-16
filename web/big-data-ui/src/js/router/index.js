import Vue from 'vue';
import Router from 'vue-router';
import LandingPage from '../components/LandingPage';
import GraphContainer from '../components/GraphContainer';


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
    }
  ],
});