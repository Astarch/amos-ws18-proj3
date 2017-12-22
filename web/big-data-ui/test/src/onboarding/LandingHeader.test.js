import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import LandingHeader from 'src/js/components/onboarding/LandingHeader'

import {FormTypeEnum} from 'src/js/utils/constants';

describe('LandingHeader.test.js', () => {
  let cmp

  beforeEach(() => {
    cmp = shallow(LandingHeader)
    jest.resetModules()
    jest.clearAllMocks()
  })

  test('has the expected html structure', () => {
    expect(cmp.element).toMatchSnapshot()
  })

  test('register clicked event is emitted', () => {
    cmp.find('a#register').trigger('click')
    expect(cmp.emitted().openRegistration).toBeTruthy()
    expect(cmp.emitted().openLogin).toBeFalsy()
  })
  test('login clicked event is emitted', () => {
    cmp.find('a#login').trigger('click')
    expect(cmp.emitted().openRegistration).toBeFalsy()
    expect(cmp.emitted().openLogin).toBeTruthy()
  })

})
