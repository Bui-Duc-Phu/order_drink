const { sendMail } = require('../../configs/SendMail');
const sequelize = require('../../configs/db');
const { createToken } = require('../../jwt/createToken');
const User = require('../../models/User')
const bcrypt = require('bcrypt');





const loginController = async (req, res) => {
  try {
    const { email, password } = req.body;

    const existingUser = await User.findOne({ where: { email } });
    console.log("sdsdsdsdsdsdsd--------",existingUser.dataValues)

    if (!existingUser) {
      return res.status(401).json({ message: 'Invalid email or password' });
    } else {
      const isPasswordValid = await bcrypt.compare(password, existingUser.password);
      if (!isPasswordValid) {
        return res.status(401).json({ message: 'Invalid email or password' });
      }
    }

    if(email){
      sendMail(email)
    }

   
    res.status(200).json({
      message: 'Login Sucessfully',
      userData : {
        email: existingUser.email,
        id : existingUser.id,
        accesstoken: await createToken(existingUser.email,existingUser.id)
      }
    })

  } catch (error) {
    throw new Error(`server err : ${error.message}`)
  }

}

module.exports = { loginController }