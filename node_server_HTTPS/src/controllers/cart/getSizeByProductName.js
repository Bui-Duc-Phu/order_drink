const { SizeModel } = require("../../dto");
const Size_product = require("../../models/Size_product");


const getSizeByProductName =  async(req,res,next) =>{
    try {
        const { productName } = req.body;
        const listSize = await Size_product.findAll({ where: { productName } });

        if (listSize.length === 0) {  throw new Error('size not found');}

        const responseData = listSize.map(item => new SizeModel(
            item.size,
            item.price
        ));

        return res.status(200).json({
            message: 'Get size successful' ,
            result: responseData
        });

        
    } catch (error) {
        next(error)
    }


}
module.exports = getSizeByProductName