class GetCartProductRes {
    constructor(name, imageUrl, quantity, price, totalPrice, size, sizePrice) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity
        this.price = price
        this.totalPrice = totalPrice
        this.size = size
        this.sizePrice = sizePrice
    }
}
module.exports = GetCartProductRes;
