const { DataTypes } = require('sequelize');
const sequelize = require('../configs/db');

const User = sequelize.define('User', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  email: {
    type: DataTypes.STRING,
    allowNull: true,
    unique: true,
  },
  phoneNumber: {
    type: DataTypes.STRING,
    allowNull: true,
    validate: {
      isNumeric: true, 
    },
  },
  userName: {
    type: DataTypes.STRING,
    allowNull: true,
    unique: true,
  },
  password: {
    type: DataTypes.STRING,
    allowNull: true,
  },
  typeAccount: {
    type: DataTypes.ENUM('email', 'google', 'facebook'),
    allowNull: false,
    defaultValue: 'email'
  },
  role: {
    type: DataTypes.ENUM('user', 'admin', 'moderator'), 
    allowNull: false,
    defaultValue: 'user', 
  },
}, {
  timestamps: true,
});

module.exports = User;
