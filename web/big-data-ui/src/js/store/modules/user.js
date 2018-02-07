import { api, logoutUser } from '../../utils/api';
import { ServerErrors } from '../../utils/constants';
import RequestStatus from '../../models/RequestStatus';
import * as types from '../mutation-types'

import router from 'src/js/router'


// initial state
// shape: {
//  user:{ },
//  currentSearchQueries: []
// }
const state = {
    user: {
        id: '',
        username: '',
        email: '',
        firstname: '',
        lastname: '',
        authorities: [],
        isLoggedIn: false
    },
    currentSearchQueries: []
}

// getters
const getters = {

}

// actions
const actions = {
    clearCurrentSearchQuery({ commit, state }) {
        return new Promise((resolve, reject) => {
            commit(types.CLEAR_CURRENT_QUERIES)
            resolve()
        })
    },
    updateCurrentSearchQueries({ commit, state }, queries) {
        return new Promise((resolve, reject) => {
            commit(types.SET_CURRENT_QUERIES, queries)
            resolve()
        })
    },
    loginUser({ commit, state }, { username, password }) {
        return new Promise((resolve, reject) => {

            api.auth.postLogin(username, password)
                .then(response => {
                    if (response != undefined && response.data != undefined && response.data.id != undefined) {
                        let authorities = []
                        if (response.data.authorities != undefined && response.data.authorities.size >= 1) {
                            authorities = response.data.authorities.reduce((total, item) => {
                                total.push(item.authority)
                                return total;
                            }, [])
                        }
                        let userdata = Object.assign({}, {
                            id: response.data.id,
                            username: response.data.username,
                            email: response.data.email,
                            firstname: response.data.firstname,
                            lastname: response.data.lastname,
                            authorities: authorities,
                            isLoggedIn: true
                        })

                        let reqStatus = Object.assign({}, new RequestStatus(), {
                            pending: false,
                            success: true,
                        })


                        commit(types.USER_SET, userdata)
                        resolve(reqStatus)
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
    registerUser({ commit, state }, { username,firstname,lastname, email, password }) {
        return new Promise((resolve, reject) => {
            api.auth.postRegistration(username, firstname,lastname, email, password)
                .then(response => {
                    if (response != undefined && response.data != undefined && response.data.id != undefined) {
                        let authorities = []
                        if (response.data.authorities != undefined && response.data.authorities.size >= 1) {
                            authorities = response.data.authorities.reduce((total, item) => {
                                total.push(item.authority)
                                return total;
                            }, [])
                        }
                        let userdata = Object.assign({}, {
                            id: response.data.id,
                            username: response.data.username,
                            email: response.data.email,
                            firstname: response.data.firstname,
                            lastname: response.data.lastname,
                            authorities: authorities,
                            isLoggedIn: false
                        })

                        let reqStatus = Object.assign({}, new RequestStatus(), {
                            pending: false,
                            success: true,
                        })

                        commit(types.USER_SET, userdata)
                        resolve(reqStatus)
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
    logoutUser({ commit, state }) {
        return new Promise((resolve, reject) => {
            logoutUser()
            commit(types.USER_CLEAR)
            commit(types.TRENDS_CLEAR)
            commit(types.CLEAR_CURRENT_QUERIES)
            router.push({
                path: "/"
            });
            resolve()
        })
    }
}

// mutations
const mutations = {
    [types.USER_SET](state, user) {
        //set new user
        state.user = Object.assign({}, state.user, user)
    },
    [types.USER_CLEAR](state) {
        //clear user
        state.user = {}
    },
    [types.SET_CURRENT_QUERIES](state, queries) {
        state.currentSearchQueries = [...queries]
    },
    [types.CLEAR_CURRENT_QUERIES](state) {
        //clear queries
        state.currentSearchQueries = {}
    },
}

export default {
    state,
    getters,
    actions,
    mutations
}