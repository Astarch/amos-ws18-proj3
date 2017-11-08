import Vue from 'vue';
import LoginModal from '../../../src/js/components/LoginModal';

describe('LandingPage.vue', () => {

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


});