import Sidebar from './SideBar.vue'

const SidebarStore = {
    showSidebar: false,
    sidebarLinks: [{
            name: 'Dashboard',
            icon: 'ti-panel',
            path: '/engagement/dashboard'
        },
        {
            name: 'Alerts',
            icon: 'ti-alarm-clock',
            path: '/engagement/alerts'
        },
    ],
    displaySidebar(value) {
        this.showSidebar = value
    }
}

const SidebarPlugin = {

    install(Vue) {
        Vue.mixin({
            data() {
                return {
                    sidebarStore: SidebarStore
                }
            }
        })

        Object.defineProperty(Vue.prototype, '$sidebar', {
            get() {
                return this.$root.sidebarStore
            }
        })
        Vue.component('side-bar', Sidebar)
    }
}

export default SidebarPlugin