import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import RegisterForm from 'src/js/components/onboarding/modal/RegisterForm'
import {api} from 'src/js/utils/api'
jest.mock('src/js/utils/api');


describe('RegisterForm.test.js', () => {
  let cmp

  beforeEach(() => {
    cmp = shallow(RegisterForm, {
      data: {
        username: '',
        email: '',
        password: '',
        isSubmitting: false,
        errorText: '',
        hasError: false,

        // Style Password Input Field
        regPwStyle: {
          backgroundColor: ''
        },

        // Style Email Input Field
        regEmailStyle: {
          backgroundColor: ''
        }

      },
      propsData: {
        active: false
      }
    })

    jest.resetModules()
    jest.clearAllMocks()

  })

  test('has the expected html structure when not active', () => {
    cmp.setProps({active: false})
    expect(cmp.element).toMatchSnapshot('inactive')
  })

  test('has the expected html structure when active', () => {
    cmp.setProps({active: true})
    expect(cmp.element).toMatchSnapshot('active')
  })

  test('to login clicked event is emitted', () => {
    cmp.find('div.links > a').trigger('click')
    expect(cmp.emitted().openLogin).toBeTruthy()
  })


 test('error is shown when submitting empty inputs', () => {
    cmp.find('#registerSubmit').trigger('click')
    let errorAlert = cmp.find('.alert')
    expect(errorAlert.exists()).toBeTruthy()
  })

  test('error is shown when submitting empty password', () => {
    cmp.find('input#username').element.value = "test"
    cmp.find('input#username').trigger('input')
    cmp.find('input#email').element.value = "test@test.de"
    cmp.find('input#email').trigger('input')
    cmp.find('#registerSubmit').trigger('click')
    let errorAlert = cmp.find('.alert')
    expect(errorAlert.exists()).toBeTruthy()
  })

  test('error is shown when submitting empty username', () => {
    cmp.find('input#password').element.value = "test1234"
    cmp.find('input#password').trigger('input')
    cmp.find('input#email').element.value = "test@test.de"
    cmp.find('input#email').trigger('input')
    cmp.find('#registerSubmit').trigger('click')
    let errorAlert = cmp.find('.alert')
    expect(errorAlert.exists()).toBeTruthy()
  })

  test('error is shown when submitting empty mail', () => {
    cmp.find('input#password').element.value = "test1234"
    cmp.find('input#password').trigger('input')
    cmp.find('input#username').element.value = "test"
    cmp.find('input#username').trigger('input')
    cmp.find('#registerSubmit').trigger('click')
    let errorAlert = cmp.find('.alert')
    expect(errorAlert.exists()).toBeTruthy()
  })

  test('correct data is submitted', () => {
    cmp.find('input#password').element.value = "test1234"
    cmp.find('input#password').trigger('input')
    cmp.find('input#username').element.value = "test"
    cmp.find('input#username').trigger('input')
    cmp.find('input#email').element.value = "test@test.de"
    cmp.find('input#email').trigger('input')
    cmp.find('#registerSubmit').trigger('click')
    let errorAlert = cmp.find('.alert')
    expect(errorAlert.exists()).toBeFalsy()

    expect(api.auth.postRegistration).toHaveBeenCalledWith("test", "test@test.de", "test1234")
  })

})
