
const { createToken } = require('../../jwt/createToken');
const bcrypt = require('bcrypt');
const LoginResponse = require('../../dto/responeResult/LoginRespone');
const { User } = require('../../models');


const loginController = async (req,res,next) => {
  try {
    const {email,password} = req.body;
    const existingUser = await User.findOne({ where: { email } });
    !existingUser && res.status(401).json({ message: 'Invalid email or password' });
    if (!(await bcrypt.compare(password, existingUser.password))){
      throw new Error("Invalid email or password");
    }     


    const token =  await createToken(existingUser.email, existingUser.id)

    console.log("token " + token)
    res.status(200).json({
      message: 'Login Sucessfully',
      result: new LoginResponse(
        existingUser.email,
        existingUser.id,
        await token
      )
    })
  } catch (error) {
    next(error)
  }
}
module.exports = { loginController }