const { GetBannerRespone, GetCategoryRespone, GetProductRespone } = require('../../dto');
const { Product } = require('../../models');

const GetProduct = async (req, res, next) => {
    try {
        const listData = await Product.findAll();
        if (listData.length === 0) {  throw new Error('Product not found');}

        const responseData = listData.map(item => new GetProductRespone(
            item.id,
            item.name,
            item.price,
            item.discount,
            item.imageUrl,
            item.category
        ));
            
        return res.status(200).json({
            message: 'Get all Product successful' ,
            result: responseData
        });
    } catch (error) {
        next(error)
    }
};
module.exports = GetProduct;
