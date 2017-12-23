import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import Vuex from 'vuex'
import LoginForm from 'src/js/components/onboarding/modal/LoginForm'
import { api } from 'src/js/utils/api'
jest.mock('src/js/utils/api');

Vue.use(Vuex)

describe('LoginForm.test.js', () => {

    let store
    let actions
    let state

    beforeEach(() => {
        jest.resetModules()
        jest.clearAllMocks()
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
                },
                loginStatus: {
                    pending: false,
                    success: false,
                    failure: false,
                    failureType: ''
                }
            }
        }
        actions = {
            loginUser: jest.fn()
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

    test('error is shown when submitting empty password', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('input#form-username').element.value = "test"
        cmp.find('input#form-username').trigger('input')
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('error is shown when submitting empty username', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('input#form-password').element.value = "test"
        cmp.find('input#form-password').trigger('input')
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeTruthy()
    })

    test('correct data is submitted', () => {
        const cmp = shallow(LoginForm, { store })
        cmp.find('input#form-password').element.value = "test1234"
        cmp.find('input#form-password').trigger('input')
        cmp.find('input#form-username').element.value = "test"
        cmp.find('input#form-username').trigger('input')
        cmp.find('#form-login-submit').trigger('click')
        let errorAlert = cmp.find('.alert')
        expect(errorAlert.exists()).toBeFalsy()

        expect(actions.loginUser.mock.calls).toHaveLength(1)
    })

})