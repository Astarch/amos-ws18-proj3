import Vue from 'vue';
import { mount } from 'vue-test-utils'
import LoginModal from '../../../src/js/components/onboarding/modal/LoginModal';
import {FormTypeEnum} from '../../../src/js/utils/constants';

describe('LoginModal.vue', () => {

  it('should open login form', () => {
    const Constructor = Vue.extend(LoginModal);
    const vm = new Constructor().$mount();

    vm.flip(FormTypeEnum.login);
    console.log("test");
  });

  it('should open register form', () => {
    const Constructor = Vue.extend(LoginModal);
    const vm = new Constructor().$mount();

    vm.flip(FormTypeEnum.register);

    Vue.nextTick(() => {
      const defaultData = LoginModal.data();
      expect(defaultData.type).to.be.equal(FormTypeEnum.register);
      done();
    });
  });

  // check if the correct login data is transmitted
  it('should transmit the Input of the login fields', () => {
    const Constructor = Vue.extend(LoginModal);
    const loginData = new Constructor().$mount();

    loginData.loginUser = "test123";
    loginData.loginPassword = "CoolPW";

    const button = loginData.$el.querySelector('#loginSubmit');
    const clickEvent = new window.Event('click');
    button.dispatchEvent(clickEvent);
    loginData._watcher.run();
    expect(loginData.loginUser).to.contain('test123');
    expect(loginData.loginPassword).to.contain('CoolPW');

  });

  // check if the correct register data is transmitted
  it('should transmit the Input of the register fields', () => {
    const Constructor = Vue.extend(LoginModal);
    const registerData = new Constructor().$mount();

    registerData.registerName = "MaxMustermann";
    registerData.registerEmail = "MaxMustermann@gmail.de";
    registerData.registerPassword = "CoolPW";

    const button = registerData.$el.querySelector('#registerSubmit');
    const clickEvent = new window.Event('click');
    button.dispatchEvent(clickEvent);
    registerData._watcher.run();
    expect(registerData.registerName).to.contain('MaxMustermann');
    expect(registerData.registerEmail).to.contain('MaxMustermann@gmail.de');
    expect(registerData.registerPassword).to.contain('CoolPW');

  });

  /*	// check if the you can register with the same username
      it('should not be able to register with an existing username', () => {
          const Constructor = Vue.extend(LoginModal);
          const registerData = new Constructor().$mount();

          registerData.registerName = "MaxMustermann";
          registerData.registerEmail = "MaxMustermann@gmail.de";
          registerData.registerPassword = "CoolPW";

          const button = registerData.$el.querySelector('#registerSubmit');
          const clickEvent = new window.Event('click');
          button.dispatchEvent(clickEvent);
          registerData._watcher.run();

          const Constructor = Vue.extend(LoginModal);
          const registerData1 = new Constructor().$mount();

          registerData1.registerName = "MaxMustermann";
          registerData1.registerEmail = "MaxMustermann@gmail.de";
          registerData1.registerPassword = "CoolPW";

          const button = registerData.$el.querySelector('#registerSubmit');
          const clickEvent = new window.Event('click');
          button.dispatchEvent(clickEvent);

          // TODO
          expect('Some Kind of error!');
      });
  */

});
