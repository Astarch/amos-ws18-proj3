import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import Landing from 'src/js/components/onboarding/Landing'

import {FormTypeEnum} from 'src/js/utils/constants';

describe('Landing.test.js', () => {
  let cmp

  beforeEach(() => {
    cmp = shallow(Landing, {
      data: { // Replace data value with this fake data
        modalActive: false,
        modalType: FormTypeEnum.login,
        FormTypeEnum
      }
    })
    jest.resetModules()
    jest.clearAllMocks()
  })

  test('equals modalActive to false', () => {
    expect(cmp.vm.modalActive).toBeFalsy()
  })

  test('has the expected html structure', () => {
    expect(cmp.element).toMatchSnapshot()
  })

  test('onboarding register modal can be opened', () => {
    expect(cmp.vm.modalActive).toBeFalsy()
    cmp.find('a.register').trigger('click')
    expect(cmp.vm.modalActive).toBeTruthy()
    expect(cmp.vm.modalType).toBe(FormTypeEnum.register)
  })

  test('onboarding login modal can be opened', () => {
    expect(cmp.vm.modalActive).toBeFalsy()
    cmp.find('a.login').trigger('click')
    expect(cmp.vm.modalActive).toBeTruthy()
    expect(cmp.vm.modalType).toBe(FormTypeEnum.login)
  })

})
