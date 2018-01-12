import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import OnboardingModal from 'src/js/components/onboarding/modal/OnboardingModal'

import {FormTypeEnum} from 'src/js/utils/constants';

describe('OnboardingModal.test.js', () => {
  let cmp

  beforeEach(() => {
    cmp = shallow(OnboardingModal, {
      data: {
        FormTypeEnum
      },
      propsData:{
        isActive: false,
        type: FormTypeEnum.login
      }
    })

    jest.resetModules()
    jest.clearAllMocks()
  })

  test('has the expected html structure', () => {
    expect(cmp.element).toMatchSnapshot()
  })

  test('onboarding modal can be opened to login', () => {
    cmp.setProps({isActive: true, type: FormTypeEnum.login})
    expect(cmp.vm.isActive).toBeTruthy()
    expect(cmp.vm.type).toBe(FormTypeEnum.login)
    expect(cmp.find('.user-modal-container').classes()).toContain('active')
    expect(cmp.find('#login-form').classes()).toContain('active')
    expect(cmp.find('#register-form').classes()).not.toContain('active')
  })

  test('onboarding modal can be opened to register', () => {
    cmp.setProps({isActive: true, type: FormTypeEnum.register})
    expect(cmp.vm.isActive).toBeTruthy()
    expect(cmp.vm.type).toBe(FormTypeEnum.register)
    expect(cmp.find('.user-modal-container').classes()).toContain('active')
    expect(cmp.find('#login-form').classes()).not.toContain('active')
    expect(cmp.find('#register-form').classes()).toContain('active')
  })

})
