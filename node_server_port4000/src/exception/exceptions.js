// exceptions.js
class CustomError extends Error {
    constructor(statusCode, message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
    }
}

class NotFoundError extends CustomError {
    constructor(message = 'Not Found') {
        super(404, message);
    }
}

class BadRequestError extends CustomError {
    constructor(message = 'Bad Request') {
        super(400, message);
    }
}

module.exports = {
    CustomError,
    NotFoundError,
    BadRequestError,
};
