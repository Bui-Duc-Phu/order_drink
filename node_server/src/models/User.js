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
  typePassword: {
    type: DataTypes.ENUM('admin', 'user', 'shipper'),
    allowNull: true,
    defaultValue: 'user'
  },
  typeAccount: {
    type: DataTypes.ENUM('email', 'google', 'facebook'),
    allowNull: false,
    defaultValue: 'email'
  },
  createdAt: {
    type: DataTypes.DATE,
    allowNull: false,
    defaultValue: DataTypes.NOW,
  },
  updatedAt: {
    type: DataTypes.DATE,
    allowNull: false,
    defaultValue: DataTypes.NOW,
  },
}, {
  timestamps: true,
});

module.exports = User;
