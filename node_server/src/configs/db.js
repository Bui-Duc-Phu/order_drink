const { Sequelize } = require('sequelize');

const sequelize = new Sequelize('order-drink', 'root', 'phuhk123', {
  host: 'localhost',
  dialect: 'mysql',
  port: '3333',
  pool: {
    max: 10,
    min: 0,
    acquire: 30000,
    idle: 10000 
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
