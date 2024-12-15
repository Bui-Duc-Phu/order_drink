
const AllRouter = require('./allRouter_Factory');
const authRouter = require('./authRouter');
const cartRouter = require('./cartRouter');
const homeRouter = require('./homeRouter');

module.exports = {
  authRouter,
  homeRouter,
  cartRouter,
  AllRouter
};
