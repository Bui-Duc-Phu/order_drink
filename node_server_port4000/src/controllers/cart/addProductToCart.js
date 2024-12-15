const { Product, Cart } = require("../../models");



const addProductToCart = async (req, res, next) => {
    try {
        const { productId, userId, quantity, sizePrice,size } = req.body;


        const product = await Product.findOne({ where: { id: productId } });
        if (!product) {
            throw new Error('Product not found');
        }

        const existingCartItem = await Cart.findOne({
            where: {
                productId: productId,
                userId: userId
            }
        });

        if (existingCartItem) {
            existingCartItem.quantity += quantity; 
            await existingCartItem.save(); 

        } else {
           
            const newCartItem = await Cart.create({
                productId: productId,
                userId: userId,
                quantity: quantity,
                sizePrice: sizePrice,
                size:size
            });
        }
        return res.status(200).json({
            message: 'Product added to cart successfully',
            result: {
                productId: productId,
                userId: userId,
                quantity: quantity,
                sizePrice: sizePrice,
                size:size
            }
        });
    } catch (error) {
        next(error);
    }
};

module.exports = addProductToCart;
