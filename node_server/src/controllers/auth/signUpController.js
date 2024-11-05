const sequelize = require('../../configs/db').default;
const { createToken } = require('../../jwt/createToken');

const bcrypt = require('bcrypt');
const { User } = require('../../models');


const createUser = async (req, res, next) => {
  try {
    const { email, phoneNumber, userName, password, typePassword } = req.body;

    if (password.length < 6) {
      throw new Error('Password must be at least 6 characters long');
    }
    
    const salt = await bcrypt.genSalt(10)
    const hashedPassword = await bcrypt.hash(password, salt)

    const newUser = await User.create({
      email,
      phoneNumber,
      userName,
      password: hashedPassword,
      typePassword 
    });

    const plainUser = newUser.get({ plain: true });

    res.status(201).json({
      message: 'User created successfully',
      result: {
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