
var morgan = require('morgan')
const chalk = require('chalk');


const conFigViewEngine = (app) => {
  require('dotenv').config()
  app.use(morgan((tokens, req, res) => {
      return [
          '',  // Dòng trắng trước
          chalk.green('-------------> requests have just been sent'),
          chalk.green(`HTTP:`) + chalk.white(` ${tokens.method(req, res)}`),
          chalk.green(`URL:`) + chalk.white(` ${req.originalUrl}`),
          chalk.green(`Status:`) + chalk.white(` ${tokens.status(req, res)}`),
          chalk.green(`Hostname:`) + chalk.white(` ${req.hostname}`),
          req.body && Object.keys(req.body).length > 0 
              ? chalk.green(`Request Body:`) + chalk.white(` ${JSON.stringify(req.body, null, 2)}`) 
              : chalk.green(`Request Body:`) + chalk.white(` No body`), 
          req.params && Object.keys(req.params).length > 0 
              ? chalk.green(`Params:`) + chalk.white(` ${JSON.stringify(req.params, null, 2)}`)  // In ra params nếu có
              : chalk.green(`Params:`) + chalk.white(` No params`), // Thông báo nếu không có params
      ].join('\n'); 
  }));
}


module.exports = conFigViewEngine