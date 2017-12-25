import axios from 'axios';
import qs from 'qs';


/******************** Base API / Client Configuration ********************/

let port = "8081/"
let localhostUrl = "http://localhost:" + port;
let stagingUrl = "http://18.216.129.153:" + port;
let liveUrl = "http://18.216.122.218:" + port;
let baseUrl = stagingUrl
baseUrl = (GIT.BRANCH && !GIT.BRANCH.includes("master")) ? liveUrl : stagingUrl;


const axiosInstance = axios.create({
    baseURL: baseUrl,
});

// Intercepts all responses, retrieves the x auth token and sets it to all following requests headers!
axiosInstance.interceptors.response.use(function(response) {
    if (response && response.headers && response.headers['x-auth-token']) {
        let authToken = response.headers['x-auth-token'];
        if (authToken && authToken.length > 5) {
            Object.assign(axiosInstance.defaults, { headers: { 'x-auth-token': authToken } });
        }
    }
    return response;
}, function(error) {
    // Do something with response error
    return Promise.reject(error);
});

export default axiosInstance;


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