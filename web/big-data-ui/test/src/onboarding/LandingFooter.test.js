import { shallow, mount } from 'vue-test-utils'
import Vue from 'vue'
import LandingFooter from 'src/js/components/onboarding/LandingFooter'

import {FormTypeEnum} from 'src/js/utils/constants';

describe('LandingFooter.test.js', () => {
  let cmp

  beforeEach(() => {
    cmp = shallow(LandingFooter)
    jest.resetModules()
    jest.clearAllMocks()
  })

  test('has the expected html structure', () => {
    expect(cmp.element).toMatchSnapshot()
  })

})
