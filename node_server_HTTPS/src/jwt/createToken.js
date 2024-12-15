const jwt = require('jsonwebtoken')

const createToken = async (email, id) => {
    const payload = {email, id}
    const token = jwt.sign(payload, process.env.SECRET_KEY, {expiresIn: '1d',})
    return token
}

module.exports = { createToken }