
require('dotenv').config();



const config = {
  port: process.env.PORT || 3000,
  hostname: process.env.HOST_NAME || 'localhost',
  printColoredConsole: require('../utils/coloredConsole'),
  conFigViewEngine: require("./viewEngine"),
  express: require("express"),
  sequelize: require('./db'),
};

module.exports = config;
