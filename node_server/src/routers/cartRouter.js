const express = require('express')
const bodyParser = require("body-parser");
const { getProductById, getSizeByProductName,  } = require('../controllers');

const cartRouter = express.Router()
cartRouter.use(bodyParser.json());
cartRouter.use(bodyParser.urlencoded({ extended: true }));




cartRouter.post('/productById', getProductById)
cartRouter.post('/getSize', getSizeByProductName)




module.exports = cartRouter