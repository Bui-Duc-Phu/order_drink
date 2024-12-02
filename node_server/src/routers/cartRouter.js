const express = require('express')
const bodyParser = require("body-parser");
const { getProductById, getSizeByProductName, addProductToCart,  } = require('../controllers');
const getCartProductByUserId = require('../controllers/cart/getCartProductByUserId');

const cartRouter = express.Router()
cartRouter.use(bodyParser.json());
cartRouter.use(bodyParser.urlencoded({ extended: true }));




cartRouter.post('/productById', getProductById)
cartRouter.post('/getSize', getSizeByProductName)
cartRouter.post('/addToCart', addProductToCart)
cartRouter.get('/getCartByUID/:userId', getCartProductByUserId)





module.exports = cartRouter