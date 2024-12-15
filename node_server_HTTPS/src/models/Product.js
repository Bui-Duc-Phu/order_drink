const { DataTypes } = require('sequelize');
const sequelize = require('../configs/db');

const Product = sequelize.define('Product', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  category: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  price: {
    type: DataTypes.FLOAT,
    allowNull: true,
    validate: {
      min: 0.0,
    },
  },
  discount: {
    type: DataTypes.FLOAT, 
    allowNull: true,
    defaultValue: 0.0,
    validate: {
      min: 0.0,
      max: 100.0,
    },
  },  
  imageUrl: {
    type: DataTypes.STRING,
    allowNull: true,
  },
  name:{
    type: DataTypes.STRING,
    allowNull: false,
    unique: true,
  },
}, {
  timestamps: true,
});

module.exports = Product;
