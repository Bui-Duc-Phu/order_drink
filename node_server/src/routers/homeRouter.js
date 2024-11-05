const express = require('express')
const bodyParser = require("body-parser");
const { GetBanner, GetCategory, GetProduct } = require('../controllers');
const homeRouter = express.Router()
homeRouter.use(bodyParser.json());
homeRouter.use(bodyParser.urlencoded({ extended: true }));




homeRouter.get('/banner',GetBanner);
homeRouter.get('/category',GetCategory);
homeRouter.get('/product',GetProduct);



module.exports = homeRouter