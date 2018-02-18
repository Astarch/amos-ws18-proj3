import axios from 'axios';
import qs from 'qs';
import storage from 'store2'
import store from 'src/js/store';

import trump from 'src/assets/wordcounts/trump'
const availableTerms = ["trump"]



/******************** Base API / Client Configuration ********************/

function writeAuthToken(token) {
  storage.set('authToken', token)
  return token
}

function getAuthToken() {
  return storage.get('authToken')
}

export const isLoggedIn = () => {
  let authToken = getAuthToken() ? getAuthToken() : authToken;
  return authToken && authToken.length > 5
}

export const logoutUser = () => {
  writeAuthToken("")
}


/******************** Pretrendr Backend API Endpoints ********************/

/***** auth *****/
const postLogin = (username, password) => {
  return new Promise((resolve, reject) => {
    if (username == "user2" && password == "pass2") {
      const response = {
        data: {
          id: 123,
          username: "user2",
          email: "user2@test.de",
          firstname: "John",
          lastname: "Doee",
          authorities: [],
          isLoggedIn: true
        }
      }
      writeAuthToken("loggedIn")
      resolve(response)
    } else {
      const errorResponse = {
        status: 401
      }
      reject(errorResponse)
    }
  })
}
//axiosInstance.post('/auth/login', qs.stringify({
//    username: username,
//    password: password
//}));

const postCompleteRegistration = (username,
  password,
  firstname = username,
  lastname = username,
  email = username + "@test.com",
  address = "addres1",
  phone = "phoneNr") => {
  return new Promise((resolve, reject) => {
    const errorResponse = {
      status: 401
    }
    reject(errorResponse)
  })
}
//axiosInstance.post('/auth/register', {
//    "username": username,
//    "password": password,
//    "firstname": firstname,
//    "lastname": lastname,
//    "email": email,
//    "address": address,
//    "phone": phone
//});

const postRegistration = (username, firstname, lastname,
  email,
  password) => {
  return new Promise((resolve, reject) => {
    const errorResponse = {
      status: 401
    }
    reject(errorResponse)
  })
}
//axiosInstance.post('/auth/register', {
//    "username": username,
//    "firstname": firstname,
//    "lastname": lastname,
//    "password": password,
//    "email": email,
//});



const auth = {
  postLogin,
  postRegistration,
  postCompleteRegistration
};

const localFirebaseUrl = "http://localhost:5000/pretrendr-d5939/us-central1"
const remoteFirebaseUrl = "https://us-central1-pretrendr-d5939.cloudfunctions.net"
const firebaseUrl = remoteFirebaseUrl
/***** graph *****/
const getWordcountByDay = (query, method, timerange, normalize) => {
  /*return new Promise((resolve, reject)=> {
      if(query.indexOf("trump")>= 0){
          const response = {
              data: trump
          }
          resolve(response)
      }else{
          const errorResponse = {
              status: 404
          }
          reject(errorResponse)
      }
  })*/
  return axios.get(`${firebaseUrl}/getWordcountData?query=${query}&method=${method}`)

  //axiosInstance.get(`/api/gdelt/wordcountByDay/${query}?${timerange}&normalize=${normalize? 'true': 'false'}&method=${method}`);
}
const uploadDataToFirestore = (query, method, timerange, data) => {
  const uploadData = {
    query,
    method,
    timerange,
    data,
  };
  return axios.post(`${firebaseUrl}/addWordcountData`, uploadData)    
}

const getCachedTrends = () => axios.get(`${firebaseUrl}/getAllCachedTerms`)


const graph = {
  getWordcountByDay,
  getCachedTrends
};

const api = {
  auth,
  graph
}

export {
  api
};
