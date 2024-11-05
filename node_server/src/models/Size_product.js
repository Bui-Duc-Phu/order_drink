
const { DataTypes } = require('sequelize');
const sequelize = require('../configs/db');

const Size_product = sequelize.define('Size_product', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  productName: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  size: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  price: {
    type: DataTypes.FLOAT,
    allowNull: false,
    validate: {
      min: 0.0,
    },
  },
}, {
  timestamps: true,
});

module.exports = Size_product;
