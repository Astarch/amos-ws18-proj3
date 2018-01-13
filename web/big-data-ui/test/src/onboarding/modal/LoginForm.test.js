import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import Vuex from 'vuex'

import LoginForm from 'src/js/components/onboarding/modal/LoginForm'
import RequestStatus from 'src/js/models/RequestStatus';
import { api } from 'src/js/utils/api'
jest.mock('src/js/utils/api');

Vue.use(Vuex)

describe('LoginForm.test.js', () => {

    let store
    let actions
    let state
    let loginMock

    beforeEach(() => {
        jest.resetModules()
        jest.clearAllMocks()

        let reqStatus = Object.assign({}, new RequestStatus(), {
            pending: false,
            success: true,
        })
        loginMock = jest.fn()
        loginMock
            .mockReturnValue(reqStatus)

        state = {
            user: {
                user: {
                    id: '',
                    username: '',
                    email: '',
                    encryptedPassword: '',
                    firstname: '',
                    lastname: '',
                    authorities: []
                }
            }
        }
        actions = {
            loginUser: loginMock
        }
        store = new Vuex.Store({
            state,
            actions
        })
    })

    test('has the expected html structure when not active', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.setProps({ active: false })
        expect(cmp.element).toMatchSnapshot('inactive')
    })

    test('has the expected html structure when active', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.setProps({ active: true })
        expect(cmp.element).toMatchSnapshot('active')
    })

    test('to register clicked event is emitted', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('div.links > a').trigger('click')
        expect(cmp.emitted().openRegistration).toBeTruthy()
    })


    test('error is shown when submitting empty inputs', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    let validPassword = "test1234"
    let invalidPassword = ""
    let validUsername = "test"
    let invalidUsername = ""

    test('error is shown when submitting empty password', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('input#form-username').element.value = validUsername
        cmp.find('input#form-username').trigger('input')
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('error is shown when submitting empty username', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('input#form-password').element.value = validPassword
        cmp.find('input#form-password').trigger('input')
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('correct data is submitted', () => {
        const cmp = shallow(LoginForm, { store })

        let username = validUsername
        let password = validPassword
        cmp.find('input#form-password').element.value = password
        cmp.find('input#form-password').trigger('input')
        cmp.find('input#form-username').element.value = username
        cmp.find('input#form-username').trigger('input')
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeFalsy()

        expect(actions.loginUser.mock.calls).toHaveLength(1)
        expect(actions.loginUser.mock.calls[0][1]).toEqual({ username, password })
    })

})