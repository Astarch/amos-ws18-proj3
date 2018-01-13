import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import Vuex from 'vuex'

import RegisterForm from 'src/js/components/onboarding/modal/RegisterForm'
import RequestStatus from 'src/js/models/RequestStatus';
import { api } from 'src/js/utils/api'
jest.mock('src/js/utils/api');

Vue.use(Vuex)

describe('RegisterForm.test.js', () => {

    let store
    let actions
    let state

    let registerMock

    beforeEach(() => {
        jest.resetModules()
        jest.clearAllMocks()

        let reqStatus = Object.assign({}, new RequestStatus(), {
            pending: false,
            success: true,
        })
        registerMock = jest.fn()
        registerMock
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
            registerUser: registerMock
        }
        store = new Vuex.Store({
            state,
            actions
        })

    })

    test('has the expected html structure when not active', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.setProps({ active: false })
        expect(cmp.element).toMatchSnapshot('inactive')
    })

    test('has the expected html structure when active', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.setProps({ active: true })
        expect(cmp.element).toMatchSnapshot('active')
    })

    test('to login clicked event is emitted', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.find('div.links > a').trigger('click')
        expect(cmp.emitted().openLogin).toBeTruthy()
    })


    test('error is shown when submitting empty inputs', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.find('#registerSubmit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })


    let validPassword = "test1234"
    let invalidPassword = ""
    let validUsername = "test"
    let invalidUsername = ""
    let validEmail = "test@test.de"
    let invalidEmail = "testtest.de"

    test('error is shown when submitting empty password', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.find('input#register-username').element.value = validUsername
        cmp.find('input#register-username').trigger('input')
        cmp.find('input#register-email').element.value = validEmail
        cmp.find('input#register-email').trigger('input')
        cmp.find('#registerSubmit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('error is shown when submitting empty username', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.find('input#register-password').element.value = validPassword
        cmp.find('input#register-password').trigger('input')
        cmp.find('input#register-email').element.value = validEmail
        cmp.find('input#register-email').trigger('input')
        cmp.find('#registerSubmit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('error is shown when submitting empty mail', () => {
        const cmp = shallow(RegisterForm, { store })
        cmp.find('input#register-password').element.value = validPassword
        cmp.find('input#register-password').trigger('input')
        cmp.find('input#register-username').element.value = validUsername
        cmp.find('input#register-username').trigger('input')
        cmp.find('#registerSubmit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('correct data is submitted', () => {
        const cmp = shallow(RegisterForm, { store })
        let email = validEmail
        let username = validUsername
        let password = validPassword
        cmp.find('input#register-password').element.value = password
        cmp.find('input#register-password').trigger('input')
        cmp.find('input#register-username').element.value = username
        cmp.find('input#register-username').trigger('input')
        cmp.find('input#register-email').element.value = email
        cmp.find('input#register-email').trigger('input')
        cmp.find('#registerSubmit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeFalsy()

        expect(actions.registerUser.mock.calls).toHaveLength(1)
        expect(actions.registerUser.mock.calls[0][1]).toEqual({ username, email, password })
    })

})