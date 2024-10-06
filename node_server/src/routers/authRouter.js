
const express = require('express')
const bodyParser = require("body-parser");
const authRouter = express.Router()

authRouter.use(bodyParser.json());
authRouter.use(bodyParser.urlencoded({ extended: true }));


const { createUser } = require('../controllers/auth/signUpController')
const { loginController } = require('../controllers/auth/login_Controller')
const { verification } = require('../controllers/auth/verification')
const { resetPasswrod } = require('../controllers/auth/resetPasswrod')
const { signInGoogle } = require('../controllers/auth/signInGoogle')
const { signInFacebook } = require('../controllers/auth/signInFacebook')


const {
    SignupError
} = require('../middleware/signUpMiddleware')



authRouter.post('/create-user', createUser, SignupError);
authRouter.post('/login', loginController)
authRouter.post('/verification', verification)
authRouter.patch('/reset', resetPasswrod)
authRouter.post('/google-signIn', signInGoogle)
authRouter.post('/facebook-signIn', signInFacebook)


module.exports = { authRouter }