
const { DataTypes } = require('sequelize');
const sequelize = require('../configs/db');

const Banner = sequelize.define('Banner', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  url: {
    type: DataTypes.STRING,
    allowNull: true,
    unique:true
  },
}, {
  timestamps: true, 
});

module.exports = Banner;
