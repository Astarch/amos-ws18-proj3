import _ from 'lodash'
import moment from "moment";

import { api } from '../../utils/api';
import { ServerErrors } from '../../utils/constants';
import RequestStatus from '../../models/RequestStatus';
import * as types from '../mutation-types'

import router from 'src/js/router'


// initial state
// shape: {
//  trends:[],
// }
// trend: {
//  query: "",
//  method: "",
//  data: "",
//  timerange: "",
//  lastFetchedMillis: 12345
// }
//
const state = {
    trends: [],
    cachedTrendsList: []
}

// getters
const getters = {
    getTrendByDay: (state) => (query, method, timerange) => {
        let data = state.trends.find(trend => query === trend.query && method === trend.method && timerange === trend.timerange)
        data = Object.assign({}, data)

        // refresh data when it is older than 7 days
        if (moment().diff(moment(data.lastFetchedMillis), 'days') > 7) {
            return {}
        }
        return Object.assign({}, data)
    }
}




// actions
const actions = {
    clearTrends({ commit, state }) {
        return new Promise((resolve, reject) => {
            commit(types.TRENDS_CLEAR)
            resolve()
        })
    },
    getWordcountByDay({ commit, state }, { query, method, timerange, normalize }) {
        return new Promise((resolve, reject) => {
            api.graph.getWordcountByDay(query, method, timerange, normalize)
                .then(response => {
                    if (response != undefined && response.data != undefined) {
                        let data = response.data
                        let reqStatus = Object.assign({}, new RequestStatus(), {
                            pending: false,
                            success: true,
                        })
                        let trend = Object.assign({}, {
                            query,
                            method,
                            data,
                            timerange,
                            lastFetchedMillis: moment().valueOf()
                        })

                        commit(types.TREND_ADD, trend)
                        resolve({ reqStatus, trend })
                    } else {
                        let reqStatus = Object.assign({}, new RequestStatus(), {
                            pending: false,
                            failure: true,
                            failureType: ServerErrors.ERROR_REQUEST
                        })

                        reject(reqStatus)
                    }
                })
                .catch(error => {
                    let failure = ServerErrors.ERROR_NONE
                    let errorCode = 404
                    if (error && error.response && error.response.status > 0) {
                        errorCode = error.response.status
                    }
                    if (errorCode >= 500) {
                        failure = ServerErrors.ERROR_SERVER
                    } else if (errorCode = 401) {
                        failure = ServerErrors.ERROR_AUTHENTICATION
                    } else if (errorCode = 403) {
                        failure = ServerErrors.ERROR_PERMISSION
                    } else {
                        failure = ServerErrors.ERROR_REQUEST
                    }

                    let reqStatus = Object.assign({}, new RequestStatus(), {
                        pending: false,
                        failure: true,
                        failureType: failure
                    })
                    reject(reqStatus)
                });

        })
    },
    getCachedTrends({ commit, state }) {
        return new Promise((resolve, reject) => {
            api.graph.getCachedTrends()
                .then(response => {
                    if (response != undefined && response.data != undefined) {
                        let data = response.data
                        let reqStatus = Object.assign({}, new RequestStatus(), {
                            pending: false,
                            success: true,
                        })

                        commit(types.CACHED_TRENDS_SET, data)
                        resolve({ reqStatus, data })
                    } else {
                        let reqStatus = Object.assign({}, new RequestStatus(), {
                            pending: false,
                            failure: true,
                            failureType: ServerErrors.ERROR_REQUEST
                        })

                        reject(reqStatus)
                    }
                })
                .catch(error => {
                    let failure = ServerErrors.ERROR_NONE
                    let errorCode = 404
                    if (error && error.response && error.response.status > 0) {
                        errorCode = error.response.status
                    }
                    if (errorCode >= 500) {
                        failure = ServerErrors.ERROR_SERVER
                    } else if (errorCode = 401) {
                        failure = ServerErrors.ERROR_AUTHENTICATION
                    } else if (errorCode = 403) {
                        failure = ServerErrors.ERROR_PERMISSION
                    } else {
                        failure = ServerErrors.ERROR_REQUEST
                    }

                    let reqStatus = Object.assign({}, new RequestStatus(), {
                        pending: false,
                        failure: true,
                        failureType: failure
                    })
                    reject(reqStatus)
                });

        })
    },
}

// mutations
const mutations = {
    [types.TREND_ADD](state, trend) {

        let oldTrendIndex = _.findIndex(state.trends, t =>
            t.query === trend.query &&
            t.method === trend.method &&
            t.timerange === trend.timerange)
        if (oldTrendIndex >= 0) {
            state.trends = Object.assign(
                [...state.trends], {
                    [oldTrendIndex]: trend
                }
            )
        } else {
            state.trends = [...state.trends, trend]
        }
    },
    [types.TRENDS_CLEAR](state) {
        state.trends = []
    },
    [types.CACHED_TRENDS_SET](state, trends) {
        state.cachedTrendsList = trends
    },

    
}

export default {
    state,
    getters,
    actions,
    mutations
}