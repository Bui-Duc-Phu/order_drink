const { Sequelize } = require('sequelize');

const chalk = require('chalk');
const sequelize = new Sequelize('order_drink', 'root', 'phuhk123', {
  host: 'localhost',
  dialect: 'mysql',
  port: '3306',
  pool: {
    max: 10,
    min: 0,
    acquire: 30000,
    idle: 10000 
  },
  logging: (msg) => {
    const log = {
      query: msg,
    };
  console.log(chalk.white(`Query: ${log.query}`)); 
  }
});

sequelize.authenticate()
  .then(() => {
    console.log('Connection has been established successfully.');
  })
  .catch(err => {
    console.error('Unable to connect to the database:', err);
  });

module.exports = sequelize;
