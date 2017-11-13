// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'bootstrap';
import Vue from 'vue';
import App from './App';
import router from './router';
import Header from './components/Header';
import Footer from './components/Footer';



Vue.config.productionTip = false;

Vue.component('header-nav', Header);
Vue.component('footer-fix',Footer)



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {
    App,
  },
});

