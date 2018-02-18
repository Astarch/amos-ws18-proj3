import Vue from 'vue';
import Router from 'vue-router';
import VueResource from 'vue-resource';
import store from 'src/js/store';
import { isLoggedIn } from 'src/js/utils/api';

// components
import LandingPage from '../components/onboarding/Landing';
import EngagementLayout from '../components/engagement/EngagementLayout';
import Dashboard from '../components/engagement/dashboard/Dashboard';
import AlertHome from '../components/engagement/alerts/AlertHome';
import ErrorNotFound from '../components/common/ErrorNotFound';
import RelatedHome from '../components/engagement/related/AlertHome';
import CachedTerms from '../components/engagement/cachedterms/CachedTerms';

Vue.use(VueResource);
Vue.use(Router);

const router = new Router({
    mode: 'history',
    routes: [{
            path: '/',
            component: LandingPage,
            redirect: '/public'

        },
        {
            path: '/public',
            component: LandingPage,
        },
        {
            path: '/error',
            component: LandingPage,
            meta: { isPublic: true }
        },
        {
            path: '/engagement',
            component: EngagementLayout,
            name: 'dashboard-home',
            redirect: '/engagement/dashboard',
            meta: { requiresAuth: true }
        },

        {
            path: '/engagement',
            component: EngagementLayout,
            redirect: '/engagement/dashboard',
            meta: { requiresAuth: true },
            children: [{
                    path: 'dashboard',
                    name: 'Dashboard',
                    component: Dashboard
                },
                {
                    path: 'alerts',
                    name: 'Alerts',
                    component: AlertHome
                },
                {
                    path: 'cached',
                    name: 'Cached Terms',
                    component: CachedTerms
                },
                {
                    path: 'related',
                    name: 'Related Terms',
                    component: RelatedHome
                },
            ]
        },
        { path: '*', component: ErrorNotFound }
    ],
});

router.beforeResolve((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        console.log(`${to.path} requires auth`)

        // this route requires auth, check if logged in
        // if not, redirect to login page.
        if (!isLoggedIn()) {
            next({
                path: '/'
            })
        } else {
            next()
        }
    } else if (!to.matched.some(record => record.meta.isPublic) && isLoggedIn()) {
        next({
            path: '/engagement'
        })
    } else {
        next()
    }
    // next(false)
    // next('/')
    // next({ path: '/' }) //  normal path object (incl replace ...)
})

export default router