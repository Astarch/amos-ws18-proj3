import Vue from 'vue';
import Router from 'vue-router';
import LandingPage from '../components/LandingPage';
import parent from '../components/Parent';


Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Landing',
      component: LandingPage,
    },
    {
      path: '/graph',
      name: 'graph',
      component: parent,
    }
  ],
});
