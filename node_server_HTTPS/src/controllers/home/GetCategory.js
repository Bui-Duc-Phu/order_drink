
const { GetBannerRespone, GetCategoryRespone } = require('../../dto');
const Category = require('../../models/Category');

const GetCategory = async (req, res, next) => {
    try {
        const listData = await Category.findAll();
        if (listData.length === 0) {  throw new Error('category not found');}
        const responseData = listData.map(item => new GetCategoryRespone(this.name =  item.name));
        return res.status(200).json({
            message: 'Get category successful' ,
            result: responseData
        });
    } catch (error) {
        next(error)
    }
};
module.exports = GetCategory;
