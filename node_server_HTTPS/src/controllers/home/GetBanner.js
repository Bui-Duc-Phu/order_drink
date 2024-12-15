
const { GetBannerRespone } = require('../../dto');
const Banner = require('../../models/Banner');

const GetBanner = async (req, res, next) => {
    try {
        const images = await Banner.findAll();

        if (images.length === 0) {  throw new Error('image not found');}

        const responseData = images.map(image => new GetBannerRespone(image.id, image.url));

        return res.status(200).json({
            message: 'Get banner successful' ,
            result: responseData
        });
    } catch (error) {
        next(error)
    }
};
module.exports = GetBanner;
