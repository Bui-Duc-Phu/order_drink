const { Product } = require("../../models");


const getProductById = async (req, res, next) => {
    try {
        const { id } = req.body;
        const product = await Product.findOne({ where: { id } });

        if (product.length === 0) {  throw new Error('Product not found');}

        const responseData =  {
            id : product.id,
            name : product.name,
            price : product.price,
            imageUrl : product.imageUrl,
            discount : product.discount,
            category: product.category
        };

        return res.status(200).json({
            message: 'Get product successful' ,
            result: responseData
        });
    } catch (error) {
        next(error)
    }
   
};
module.exports = getProductById;
