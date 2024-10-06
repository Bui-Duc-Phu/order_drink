
function Response(status, message, result) {
    return {
        status: status || 200,
        message: message || 'No message provided',
        result: result || {}
    };
}







module.exports = { Response }
