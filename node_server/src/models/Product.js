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
  quantity: {
    type: DataTypes.INTEGER,
    allowNull: false,
    validate: {
      min: 0,
    },
  },
  price: {
    type: DataTypes.FLOAT,
    allowNull: false,
    validate: {
      min: 0.0,
    },
  },
  finalPrice: {
    type: DataTypes.FLOAT,
    allowNull: false,
    validate: {
      min: 0.0,
    },
  },
  image: {
    type: DataTypes.STRING,
    allowNull: true,
  },
  name: {
    type: DataTypes.STRING,
    allowNull: false,
    unique: true,
  },
  size: {
    type: DataTypes.ENUM('S', 'M', 'L', 'XL'),  
    allowNull: false,
    defaultValue: 'S',  
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

module.exports = Product;
