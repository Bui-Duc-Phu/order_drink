const sequelize = require('../../configs/db').default;
const { createToken } = require('../../jwt/createToken');
const User = require('../../models/User')
const bcrypt = require('bcrypt');





const createUser = async (req, res, next) => {
  try {
    const { email, phoneNumber, fullName, password, typePassword } = req.body;

    const salt = await bcrypt.genSalt(10)
    const hashedPassword = await bcrypt.hash(password, salt)

    const newUser = await User.create({
      email,
      phoneNumber,
      fullName,
      password: hashedPassword,
      typePassword
    });

    const plainUser = newUser.get({ plain: true });

    res.status(201).json({
      message: 'User created successfully',
      userData: {
        id: plainUser.id,
        email: plainUser.email,
        phoneNumber: plainUser.phoneNumber,
        fullName: plainUser.fullName,
        accesstoken: await createToken(plainUser.email, plainUser.id),
      }
    });


  } catch (error) {
    next(error)

  }

}

module.exports = { createUser }