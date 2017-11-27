import axios from 'axios';
import qs from 'qs';


/******************** Base API / Client Configuration ********************/

let port = "8081/"
let localhostUrl = "http://localhost:" + port
let stagingUrl = "http://staging.pretrendr.org:" + port


const axiosInstance = axios.create({
  baseURL: stagingUrl,
});

// Intercepts all responses, retrieves the x auth token and sets it to all following requests headers!
axiosInstance.interceptors.response.use(function (response) {
  if (response && response.headers && response.headers['x-auth-token']) {
    let authToken = response.headers['x-auth-token'];
    if (authToken && authToken.length > 5) {
      Object.assign(axiosInstance.defaults, {headers: {'x-auth-token': authToken}});
    }
  }
  return response;
}, function (error) {
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

const postRegister = (username,
                      password,
                      firstname = username,
                      lastname = username,
                      email = username + "@test.com",
                      address = "addres1",
                      phone = "phoneNr") => axiosInstance.post('/auth/register', qs.stringify({
  "username": username,
  "password": password,
  "firstname": firstname,
  "lastname": lastname,
  "email": email,
  "address": address,
  "phone": phone
}));

const getLogout = () => axiosInstance.get('/auth/logout');

const auth = {
  postLogin,
  postRegister,
  getLogout
};

/***** user *****/
const getAll = () => axiosInstance.get('/api/user/getAll');

const user = {
  getAll
};

const api = {
  auth,
  user
}

export {
  api
};

