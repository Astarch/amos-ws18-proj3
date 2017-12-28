import { api, logoutUser } from '../../utils/api';
import { ServerErrors } from '../../utils/constants';
import RequestStatus from '../../models/RequestStatus';
import * as types from '../mutation-types'


// initial state
// shape: {
//  user:{ },
//  loginStatus
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
}

// getters
const getters = {

}

// actions
const actions = {
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
    registerUser({ commit, state }, { username, email, password }) {
        return new Promise((resolve, reject) => {
            api.auth.postRegistration(username, email, password)
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
}

export default {
    state,
    getters,
    actions,
    mutations
}