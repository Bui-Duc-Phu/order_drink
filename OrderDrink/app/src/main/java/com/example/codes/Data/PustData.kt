package com.example.codes.Data

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.codes.R
import com.google.firebase.auth.FirebaseAuth

class PustData : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pust_data)

        val button : Button = findViewById(R.id.button)
        auth = FirebaseAuth.getInstance()


        button.setOnClickListener {
          //  pushData()
        }
    }

    /*private fun pushData() {
        val orderId = CustomString.idOrder()
        val ref = FirebaseDatabase
            .getInstance("https://coffe-app-19ec3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Orders").child(orderId)
        val order = Order(
            state = "1",
            checkout = "0",
            uID = "mCqwwqYj8HhXgSpQkOiRPoRCr123",
            orderID = orderId,
            pay = "COD",
            day = "10/04/2024",
            time = "09:00",
            shipper = Shipper("bui duc phu", "0947945596"),
            receiverPhone = "0947945596",
            receiverLocation = "97man thien quan 9 hcm",
            products = listOf(
                Order_product("cafe1", "M", "10", "20000", orderId),
                Order_product("cafe2", "L", "20", "5000", orderId),
                Order_product("cafe2", "L", "20", "5000", orderId),
                Order_product("cafe2", "L", "20", "5000", orderId),
            )
        )
        val orderMap = mapOf(
            "state" to order.state,
            "checkout" to order.checkout,
            "uID" to order.uID,
            "orderID" to order.orderID,
            "pay" to order.pay,
            "day" to order.day,
            "time" to order.time,
            "shipper" to mapOf(
                "name" to order.shipper.name,
                "sDT" to order.shipper.sDT
            ),
            "receiverPhone" to order.receiverPhone,
            "receiverLocation" to order.receiverLocation,
            "products" to order.products.map { product ->
                mapOf(
                    "productName" to product.productName,
                    "size" to product.size,
                    "quantity" to product.quantity,
                    "price" to product.price,
                    "orderID" to product.orderID
                )
            }
        )
        ref.setValue(orderMap)
    }
*/

}