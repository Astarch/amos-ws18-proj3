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
        requestAnswer: '',
        FormTypeEnum
      }
    })
  })

  it('equals modalActive to false', () => {
    expect(cmp.vm.modalActive).toBeFalsy()
  })

  it('has the expected html structure', () => {
    expect(cmp.element).toMatchSnapshot()
  })
})
