/******************** Pretrendr Backend API Endpoints ********************/

/***** auth *****/

const auth = {
  postLogin: jest.fn(() => Promise.resolve({success: true})),
  postRegistration: jest.fn(() => Promise.resolve({success: true, response:{status: 401}})),
  postCompleteRegistration: jest.fn(() => Promise.resolve({success: true})),
  doLogout: jest.fn(() => Promise.resolve({success: true}))
};

/***** user *****/

const user = {
  getAll: jest.fn(() => Promise.resolve({success: true}))
};

/***** graph *****/

const graph = {
  getData: jest.fn(() => Promise.resolve({success: true}))
};

const api = {
  auth: auth,
  user: user,
  graph: graph
}

export  {
  api
};

