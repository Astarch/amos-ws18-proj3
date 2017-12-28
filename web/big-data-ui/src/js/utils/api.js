import axios from 'axios';
import qs from 'qs';
import store from 'store2'


/******************** Base API / Client Configuration ********************/

let port = "8081/"
let localhostUrl = "http://localhost:" + port;
let stagingUrl = "http://18.216.129.153:" + port;
let liveUrl = "http://18.216.122.218:" + port;
let baseUrl = stagingUrl
baseUrl = (GIT.BRANCH && !GIT.BRANCH.includes("master")) ? liveUrl : stagingUrl;



function writeAuthToken(token) {
    store.session('authToken', token)
    return token
}

function getAuthToken() {
    return store.session('authToken')
}


const axiosInstance = axios.create({
    baseURL: baseUrl,
});

if (getAuthToken() && getAuthToken().length > 5) {
    Object.assign(axiosInstance.defaults, { headers: { 'x-auth-token': getAuthToken() } });
}

// Intercepts all responses, retrieves the x auth token and sets it to all following requests headers!
axiosInstance.interceptors.response.use(response => {
    let authToken = getAuthToken()
    if (response.status == 401) {
        authToken = writeAuthToken("")
    } else if (response && response.headers && response.headers['x-auth-token']) {
        let authToken = writeAuthToken(response.headers['x-auth-token']);
        if (authToken && authToken.length > 5) {
            Object.assign(axiosInstance.defaults, { headers: { 'x-auth-token': authToken } });
        }
    }

    return response;
}, error => {
    // Do something with response error
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
const getData = (path) => axiosInstance.get(path);

const graph = {
    getData
};

const api = {
    auth,
    user,
    graph
}

export {
    api
};