const addProductToCart = require("./cart/addProductToCart");
const getProductById = require("./cart/getProductById");
const getSizeByProductName = require("./cart/getSizeByProductName");

const GetBanner = require("./home/GetBanner");
const GetCategory = require("./home/GetCategory");
const GetProduct = require("./home/GetProduct");


module.exports = {
GetBanner,
GetCategory,
GetProduct,
getProductById,
getSizeByProductName,
addProductToCart
}
