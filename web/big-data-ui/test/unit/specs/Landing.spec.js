import Vue from 'vue';
import Landing from '../../../src/js/components/LandingPage';


describe('LandingPage.vue', () => {
  // Check Component Preconditions first!
  // Check default data
  it('sets the correct default data', () => {
    expect(Landing.data()).to.be.a('object');
    const defaultData = Landing.data();
    expect(defaultData.modalActive).to.be.false;
    expect(defaultData.modalType).to.be.equal('login');
  });

  // Check rendered output
  it('should render correct contents', () => {
    const Constructor = Vue.extend(Landing);
    const vm = new Constructor().$mount();
    expect(vm.$el.querySelector('h1.display-3').textContent)
      .to.equal('AMOS-Supercrunch');
  });

  // test open and close function
  it('should open modal', () => {
    const Constructor = Vue.extend(Landing);
    const vm = new Constructor().$mount();

    vm.open('register');

    Vue.nextTick(() => {
      const defaultData = Landing.data();
      expect(defaultData.modalActive).to.be.true;
      done();
    });
  });
});