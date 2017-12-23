import { api } from '../../utils/api';
import { ServerErrors } from '../../utils/constants';
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
        encryptedPassword: '',
        firstname: '',
        lastname: '',
        authorities: []
    },
    loginStatus: {
        pending: false,
        success: false,
        failure: false,
        failureType: ServerErrors.ERROR_NONE
    }
}

// getters
const getters = {

}

// actions
const actions = {
    loginUser({ commit, state }, { username, password }) {
        return new Promise((resolve, reject) => {
            commit(types.LOGIN_REQUEST)

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
                            authorities: authorities
                        })

                        commit(types.LOGIN_SUCCESS, userdata)
                        resolve()
                    } else {
                        commit(types.LOGIN_FAILURE, ServerErrors.ERROR_REQUEST)
                        reject()
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
                    commit(types.LOGIN_FAILURE, failure)
                    reject()
                });

        })
    }
}

// mutations
const mutations = {
    [types.LOGIN_REQUEST](state) {
        // clear login status
        state.loginStatus = Object.assign({}, state.loginStatus, {
            pending: true,
            success: false,
            failure: false,
            failureType: ServerErrors.ERROR_NONE
        })

    },

    [types.LOGIN_SUCCESS](state) {
        state.loginStatus = Object.assign({}, state.loginStatus, {
            pending: false,
            success: true,
            failure: false,
            failureType: ServerErrors.ERROR_NONE
        })
    },

    [types.LOGIN_FAILURE](state, failureType) {
        state.loginStatus = Object.assign({}, state.loginStatus, {
            pending: false,
            success: false,
            failure: true,
            failureType: failureType
        })
    },
}

export default {
    state,
    getters,
    actions,
    mutations
}