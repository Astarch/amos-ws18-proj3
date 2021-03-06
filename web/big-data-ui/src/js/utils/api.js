import realClient, {api as realApi, logoutUser as realLogoutUser, isLoggedIn as realIsLoggedIn}  from './api.real'
import standaloneClient, {api as standaloneApi, logoutUser as standaloneLogoutUser, isLoggedIn as standaloneIsLoggedIn}  from './api.standalone'
console.dir(standaloneApi)

const isStandaloneClient = true

const isLoggedIn = isStandaloneClient? standaloneIsLoggedIn : realIsLoggedIn
const logoutUser = isStandaloneClient? standaloneLogoutUser : realLogoutUser
const api = isStandaloneClient? standaloneApi : realApi

export {
    isLoggedIn,
    logoutUser,
    api
}
