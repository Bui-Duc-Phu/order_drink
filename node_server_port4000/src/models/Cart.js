const { DataTypes } = require('sequelize');
const sequelize = require('../configs/db');

const Cart = sequelize.define('Cart', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  productId: {
    type: DataTypes.UUID,
    allowNull: false,
  },
  userId: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  quantity: {
    type: DataTypes.INTEGER,
    allowNull: false,
    defaultValue: 1, 
  },
  sizePrice: {
    type: DataTypes.FLOAT,
    allowNull: false,
  },
  size: { 
    type: DataTypes.STRING, 
    allowNull: true, 
  },
}, {
  timestamps: true,
});

module.exports = Cart;
