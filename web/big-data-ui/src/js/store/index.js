import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

import user from './modules/user'
import trends from './modules/trends'

Vue.use(Vuex)
const debug = process.env.NODE_ENV !== 'production'


export default new Vuex.Store({
    modules: {
        user,
        trends
    },
    strict: debug,
    plugins: [
        createPersistedState({
            paths: ['user', 'trends']
        }),
    ]
})