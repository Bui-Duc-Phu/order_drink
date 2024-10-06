

const SignupError = (err, req, res, next) => {
    console.error("Error occurred 123:", err);
    res.status(500).json({
        message: err.message || 'Internal server error',
        result: null
    });
}

module.exports = { SignupError }