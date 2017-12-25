import { ServerErrors } from './../utils/constants';

export default class RequestStatus {
    constructor(name = '', success = false, failure = false, pending = false, failureType = ServerErrors.ERROR_NONE) {
        this.name = name;
        this.success = success;
        this.failure = failure;
        this.pending = pending;
        this.failureType = failureType;
    }

    toString() {
        return `RequestStatus(${this.name})[ success:${this.success}, failure:${this.failure}, pending:${this.pending}, failureType:${this.failureType} ]`
    }
}