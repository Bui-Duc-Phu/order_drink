const { sendMail } = require('../../configs/SendMail');
const sequelize = require('../../configs/db');
const { createToken } = require('../../jwt/createToken');
const User = require('../../models/User')
const bcrypt = require('bcrypt');
const LoginResponse = require('../../dto/responeResult/LoginRespone')

const loginController = async (req,res,next) => {
  try {
    const {email,password} = req.body;
    const existingUser = await User.findOne({ where: { email } });
    !existingUser && res.status(401).json({ message: 'Invalid email or password' });
    if (!(await bcrypt.compare(password, existingUser.password))){
      throw new Error("Invalid email or password");
    }     
    res.status(200).json({
      message: 'Login Sucessfully',
      result: new LoginResponse(
        existingUser.email,
        existingUser.id,
        await createToken(existingUser.email, existingUser.id)
      )
    })
  } catch (error) {
    next(error)
  }
}
module.exports = { loginController }