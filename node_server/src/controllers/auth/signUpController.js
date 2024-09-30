const sequelize = require('../../configs/db');
const { createToken } = require('../../jwt/createToken');
const User = require('../../models/User')
const bcrypt = require('bcrypt');





const createUser = async (req, res) => {
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
    if (error.name === 'SequelizeValidationError') {
      const validationErrors = error.errors.map(err => err.message);
      res.status(400).json({
        message: 'Validation errors',
        data: {
          SQL_Error: validationErrors
        }

      });
    } else if (error.name === 'SequelizeUniqueConstraintError') {
      res.status(400).json({
        message: 'Email already exists',
        data: {
          SQL_Error: error.errors.map(err => err.message)
        }


      });
    } else {
      res.status(500).json({
        message: 'Internal server error',
        error: error.message
      });
    }
  }

}

module.exports = { createUser }