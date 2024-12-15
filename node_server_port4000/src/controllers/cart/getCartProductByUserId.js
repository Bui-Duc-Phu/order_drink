const { GetCartProductRes } = require("../../dto");
const { Cart, Product } = require("../../models");

const getCartProductByUserId = async (req, res, next) => {
    try {
        const { userId } = req.params;

        if(userId) console.log("user id"+userId)
            else console.log("user id null------------")
        
        const listCartProduct = await Cart.findAll({ where: { userId } });

        if (listCartProduct.length === 0) {  
            throw new Error('cart not found');
        }

       
        const responseData = await Promise.all(
            listCartProduct.map(async (item) => {
                const product = await getProductById(item.productId); 
                if (product) {
                    return new GetCartProductRes({
                        name: product.name,
                        imageUrl: product.imageUrl,
                        quantity: item.quantity,
                        price: item.sizePrice,
                        totalPrice: totalPrice(item.quantity,item.sizePrice),
                        size: item.size, 
                        sizePrice: item.sizePrice, 
                    });
                }
                return null; // Trả về null nếu không tìm thấy sản phẩm
            })
        );

        // Lọc ra các giá trị không null
        const filteredResponseData = responseData.filter(item => item !== null);

        return res.status(200).json({
            message: 'Get product successful',
            result: filteredResponseData
        });
    } catch (error) {
        next(error);
    }
};

const getProductById = async (id) => {
    const product = await Product.findOne({ where: { id } });
    if (!product) return null; 
    return product; 
};

function totalPrice(quantity,sizePrice){
    return quantity*sizePrice
}

module.exports = getCartProductByUserId;
