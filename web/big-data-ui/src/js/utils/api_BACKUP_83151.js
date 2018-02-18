import realClient, {api as realApi, logoutUser as realLogoutUser, isLoggedIn as realIsLoggedIn}  from './api.real'
import standaloneClient, {api as standaloneApi, logoutUser as standaloneLogoutUser, isLoggedIn as standaloneIsLoggedIn}  from './api.standalone'
console.dir(standaloneApi)

const isStandaloneClient = false

if(isStandaloneClient){
    //export default standaloneClient;
}else{
    //export default realClient;
}

<<<<<<< HEAD
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

const postRegistration = (username, firstname,lastname,
    email,
    password) => axiosInstance.post('/auth/register', {
    "username": username,
    "firstname": firstname,
    "lastname": lastname,
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
const getWordcountByDay = (query, method, timerange, normalize) =>
    axiosInstance.get(`/api/gdelt/wordcountByDay/${query}?${timerange}&normalize=${normalize? 'true': 'false'}&method=${method}`);

const graph = {
    getWordcountByDay
};

const uploadDataToFirestore = (query, method, timerange, data) => {
    const uploadData = {
      query,
      method,
      timerange,
      data,
    };
    return axios.post('https://us-central1-pretrendr-d5939.cloudfunctions.net/addWordcountData', uploadData)    
  }

const api = {
    auth,
    user,
    graph,
    uploadDataToFirestore
}
=======
const isLoggedIn = isStandaloneClient? standaloneIsLoggedIn : realIsLoggedIn
const logoutUser = isStandaloneClient? standaloneLogoutUser : realLogoutUser
const api = isStandaloneClient? standaloneApi : realApi
>>>>>>> initial commit to use frontend without backend

export {
    isLoggedIn,
    logoutUser,
    api
}
