const FormTypeEnum = { "login": 1, "register": 2 };
Object.freeze(FormTypeEnum);

const ERROR_SERVER = 'ERROR_SERVER' // -> 500 status code
const ERROR_AUTHENTICATION = 'ERROR_AUTHENTICATION' // -> 401 status code
const ERROR_PERMISSION = 'ERROR_PERMISSION' // -> 403 status code
const ERROR_REQUEST = 'ERROR_REQUEST' // -> 404 status code / everything else
const ERROR_NONE = 'ERROR_NONE' // -> 404 status code / everything else

const ServerErrors = {
    ERROR_AUTHENTICATION,
    ERROR_SERVER,
    ERROR_PERMISSION,
    ERROR_REQUEST,
    ERROR_NONE
}

export {
    FormTypeEnum,
    ServerErrors
}