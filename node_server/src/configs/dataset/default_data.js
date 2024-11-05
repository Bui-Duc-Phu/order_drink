
const Product = require('../../models/Product');
const Banner = require('../../models/Banner');
const fs = require('fs');
const path = require('path');
const sequelize = require('../db');
const  Size_product  = require('../../models/Size_product');
const Category = require('../../models/Category');


async function create_default_data() {
    const product_data = JSON.parse(fs.readFileSync(path.join(__dirname, 'Product.json'), 'utf8'));
    const banner_data = JSON.parse(fs.readFileSync(path.join(__dirname, 'Banner.json'), 'utf8'));
    const size_data = JSON.parse(fs.readFileSync(path.join(__dirname, 'Size.json'), 'utf8'));
    const category_data = JSON.parse(fs.readFileSync(path.join(__dirname, 'Category.json'), 'utf8'));
    try {
        await Product.bulkCreate(product_data, { ignoreDuplicates: true });
        await Banner.bulkCreate(banner_data, { ignoreDuplicates: true });
        await Size_product.bulkCreate(size_data, { ignoreDuplicates: true });
        await Category.bulkCreate(category_data, { ignoreDuplicates: true });
    } catch (error) {
        console.error('Lỗi khi chèn dữ liệu mặc định:', error);
    } 
}

module.exports = create_default_data;