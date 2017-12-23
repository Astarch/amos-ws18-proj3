// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'bootstrap';
import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store';

import SideBar from './components/engagement/common/sidebar'
import GlobalComponents from './globalComponents'


Vue.config.productionTip = false;

Vue.use(SideBar);
Vue.use(GlobalComponents);


/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: {
        App,
    }
});