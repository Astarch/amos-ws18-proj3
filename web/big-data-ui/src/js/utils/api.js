import axios from 'axios';
import qs from 'qs';
import storage from 'store2'
import store from 'src/js/store';


/******************** Base API / Client Configuration ********************/

let port = "8081/"
let localhostUrl = "http://localhost:" + port;
let stagingUrl = "https://amos-backend-staging.flmuchow.com/"
let liveUrl = "https://amos-backend-prod.flmuchow.com/";

let baseUrl = stagingUrl
baseUrl = (GIT.BRANCH && GIT.BRANCH.includes("master")) ? liveUrl : stagingUrl;



function writeAuthToken(token) {
    storage.set('authToken', token)
    return token
}

function getAuthToken() {
    return storage.get('authToken')
}


const axiosInstance = axios.create({
    baseURL: baseUrl,
    //baseUrl,
});

if (getAuthToken() && getAuthToken().length > 5) {
    Object.assign(axiosInstance.defaults, { headers: { 'x-auth-token': getAuthToken() } });
}

// Intercepts all responses, retrieves the x auth token and sets it to all following requests headers!
axiosInstance.interceptors.response.use(response => {
    let authToken = getAuthToken()
    if (response.status == 401) {
        console.log("backend session is expired, logout user")
        store.dispatch('logoutUser').then(event => {}).catch()
    } else if (response && response.headers && response.headers['x-auth-token']) {
        let authToken = writeAuthToken(response.headers['x-auth-token']);
        if (authToken && authToken.length > 5) {
            Object.assign(axiosInstance.defaults, { headers: { 'x-auth-token': authToken } });
        }
    }

    return response;
}, error => {
    console.log(error.response)
    if (error.response.status == 401) {
        console.log("backend session is expired, logout user")
        store.dispatch('logoutUser').then(event => {}).catch()
    }
    return Promise.reject(error);
});
export default axiosInstance;

export const isLoggedIn = () => {
    let authToken = getAuthToken() ? getAuthToken() : authToken;
    return authToken && authToken.length > 5
}

export const logoutUser = () => {
    writeAuthToken("")
}



/******************** Pretrendr Backend API Endpoints ********************/

/***** auth *****/
const postLogin = (username, password) => axiosInstance.post('/auth/login', qs.stringify({
    username: username,
    password: password
}));

const postCompleteRegistration = (username,
    password,
    firstname = username,
    lastname = username,
    email = username + "@test.com",
    address = "addres1",
    phone = "phoneNr") => axiosInstance.post('/auth/register', {
    "username": username,
    "password": password,
    "firstname": firstname,
    "lastname": lastname,
    "email": email,
    "address": address,
    "phone": phone
});

const postRegistration = (username,
    email,
    password) => axiosInstance.post('/auth/register', {
    "username": username,
    "password": password,
    "email": email,
});

const doLogout = () => axiosInstance.get('/auth/logout');

const auth = {
    postLogin,
    postRegistration,
    postCompleteRegistration,
    doLogout
};

/***** user *****/
const getAll = () => axiosInstance.get('/api/user/getAll');

const user = {
    getAll
};

/***** graph *****/
const getWordcountByDay = (query, method) =>
    axiosInstance.get(`/api/gdelt/wordcountByDay/${query}?from=20170101&to=20170331&method=${method}`);

const graph = {
    getWordcountByDay
};

const api = {
    auth,
    user,
    graph
}

export {
    api
};