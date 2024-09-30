package com.example.codes.Firebase

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Adapters.CartAdapter
import com.example.codes.Administrator.model.DoanhThu
import com.example.codes.Administrator.model.DonHang
import com.example.codes.Administrator.model.ItemBill
import com.example.codes.Administrator.model.SanPham
import com.example.codes.Models.CartModel
import com.example.codes.Models.Order
import com.example.codes.Models.ProductModel
import com.example.codes.Models.Shipper
import com.example.codes.Models.SizeModel
import com.example.codes.Models.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DataHandler {
    var rule: String = ""
    val shipper: Shipper = Shipper("Nguyễn Văn A", "0123456789")
    var userInfo = UserInfo("", "", "")
    var orderModelArrayList = ArrayList<CartModel>()
    fun addOrderToFirebase(
        checkOut: String,
        orderId: String,
        paymentMethods1: String,
        dateTime: String,
        shipper: Shipper,
        phoneNumber: String,
        address: String,
        orderModelArrayList: ArrayList<CartModel>,
        totalPrice: String,
        name: String
    ) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(getUID())
        val orderModel = Order(
            "Đang chờ xác nhận",
            checkOut,
            getUID(),
            orderId,
            paymentMethods1,
            dateTime,
            shipper,
            phoneNumber,
            address,
            orderModelArrayList,
            totalPrice,
            name
        )
        ordersRef.child(orderId).setValue(orderModel)
    }

    fun getOrderDetails(orderID: String, uID: String, callback: (Order) -> Unit) {
        val ref = FirebaseDatabase
            .getInstance("https://coffe-app-19ec3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Orders").child(uID).child(orderID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(orderSnapshot: DataSnapshot) {
                val state = orderSnapshot.child("state").value.toString()
                val checkout = orderSnapshot.child("checkout").value.toString()
                val uID = orderSnapshot.child("uID").value.toString()
                val orderID = orderSnapshot.child("orderID").value.toString()
                val pay = orderSnapshot.child("pay").value.toString()
                val time = orderSnapshot.child("time").value.toString()
                val shipperName = orderSnapshot.child("shipper").child("name").value.toString()
                val shipperSDT = orderSnapshot.child("shipper").child("sDT").value.toString()
                val receiverPhone = orderSnapshot.child("receiverPhone").value.toString()
                val receiverLocation = orderSnapshot.child("receiverLocation").value.toString()
                val receiverName = orderSnapshot.child("receiverName").value.toString()
                val productsList = mutableListOf<CartModel>()
                for (productSnapshot in orderSnapshot.child("products").children) {
                    val product = productSnapshot.getValue(CartModel::class.java)
                    if (product != null) {
                        productsList.add(product)
                    }
                }
                val shipper = Shipper(shipperName, shipperSDT)
                val sumPrice = orderSnapshot.child("sumPrice").value.toString()
                val order = Order(
                    state,
                    checkout,
                    uID,
                    orderID,
                    pay,
                    time,
                    shipper,
                    receiverPhone,
                    receiverLocation,
                    productsList,
                    sumPrice,
                    receiverName
                )
                callback(order)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    fun getOrderWithState(state: String, callback: (List<Order>) -> Unit) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders")
        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<Order>()
                for (orderSnapshot in snapshot.children) {
                    for (singleOrderSnapshot in orderSnapshot.children) {
                        val order = singleOrderSnapshot.getValue(Order::class.java)
                        if (order != null && order.state == state) {
                            orderList.add(order)
                        }
                    }
                }
                callback(orderList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }

    fun getOrderWithStateClient(state: String, callback: (List<Order>) -> Unit) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(getUID())
        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<Order>()
                for (singleOrderSnapshot in snapshot.children) {
                    val order = singleOrderSnapshot.getValue(Order::class.java)
                    if (order != null && order.state == state) {
                        orderList.add(order)
                    }
                }
                callback(orderList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }

    fun updateState(uID: String, orderID: String, state: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Orders").child(uID).child(orderID)
        ref.child("state").setValue(state)

    }

    fun addToCart(productModel: ProductModel, selectedSize: SizeModel, quantity: Int) {
        val productID = productModel.name + "_" + selectedSize.size
        val cartReference =
            FirebaseDatabase.getInstance().getReference("Carts").child(getUID())
        cartReference.child(productID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val finalPrice =
                        if (productModel.discount > 0) productModel.finalPrice else productModel.price
                            .toDouble()
                    if (snapshot.exists()) {
                        val cartModel = snapshot.getValue(CartModel::class.java)
                        if (cartModel != null) {
                            cartModel.quantity += quantity
                            val updateData: MutableMap<String, Any> = HashMap()
                            updateData["quantity"] = cartModel.quantity
                            updateData["totalPrice"] =
                                cartModel.quantity * (finalPrice + selectedSize.price)
                            updateData["sizePrice"] = selectedSize.price
                            cartReference.child(productID)
                                .updateChildren(updateData)
                        }
                    } else {
                        val cartModel = CartModel(
                            productModel.name,
                            productModel.imageUrl,
                            quantity,
                            finalPrice + selectedSize.price,
                            finalPrice + selectedSize.price,
                            selectedSize.size,
                            selectedSize.price
                        )
                        cartReference.child(productID)
                            .setValue(cartModel)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("CartHandler", "addToCart onCancelled: " + error.message)
                }
            })
    }

    fun fetchDataForCart(
        recyclerView: RecyclerView,
        txtEmptyCart: TextView,
        txtTotalPrice: TextView,
        llBuy: LinearLayout
    ) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Carts")
        databaseReference.child(getUID())
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val cartModelArrayList = ArrayList<CartModel>()
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.getChildren()) {
                            val cartModel = snapshot.getValue(CartModel::class.java)
                            if (cartModel != null) {
                                cartModelArrayList.add(cartModel)
                            }
                            orderModelArrayList = cartModelArrayList
                        }
                        recyclerView.visibility = View.VISIBLE
                        txtEmptyCart.visibility = View.GONE
                        llBuy.visibility = View.VISIBLE
                        val adapter = recyclerView.adapter as CartAdapter?
                        adapter?.updateData(cartModelArrayList)
                    } else {
                        recyclerView.visibility = View.GONE
                        llBuy.visibility = View.GONE
                        txtEmptyCart.visibility = View.VISIBLE
                    }
                    var totalPrice = 0.0
                    for (cartModel in cartModelArrayList) {
                        totalPrice += cartModel.totalPrice
                    }
                    val vndFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                    txtTotalPrice.text = vndFormat.format(totalPrice) + "đ"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("CartFragment", "onCancelled: " + databaseError.message)
                }
            })
    }

    fun fetchDataForOrder(
        recyclerView: RecyclerView,
        txtTotalPrice: TextView
    ) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Carts")
        databaseReference.child(getUID())
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val cartModelArrayList = ArrayList<CartModel>()
                    for (snapshot in dataSnapshot.getChildren()) {
                        val cartModel = snapshot.getValue(CartModel::class.java)
                        if (cartModel != null) {
                            cartModelArrayList.add(cartModel)
                        }
                        orderModelArrayList = cartModelArrayList
                    }
                    val adapter = recyclerView.adapter as CartAdapter?
                    adapter?.updateData(cartModelArrayList)
                    var totalPrice = 0.0
                    for (cartModel in cartModelArrayList) {
                        totalPrice += cartModel.totalPrice
                    }
                    val vndFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                    totalPrice += 20000.0
                    txtTotalPrice.text = vndFormat.format(totalPrice)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("CartFragment", "onCancelled: " + databaseError.message)
                }
            })
    }

    fun getDoanhThuTheoNgay(startDate: String, endDate: String, orderList: List<Order>): Double {
        var doanhThu = 0.0
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val start = sdf.parse("$startDate 00:00:00")
        val end = sdf.parse("$endDate 23:59:59")

        for (order in orderList) {
            val orderDate = sdf.parse(order.time)
            if (orderDate.after(start) && orderDate.before(end)) {
                doanhThu += order.sumPrice.toDouble()
            }
        }
        return doanhThu
    }

    private fun getDonHangTheoNgay(
        startDate: String,
        endDate: String,
        orderList: List<Order>
    ): Int {
        var donHang = 0
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val start = sdf.parse("$startDate 00:00:00")
        val end = sdf.parse("$endDate 23:59:59")
        for (order in orderList) {
            val orderDate = sdf.parse(order.time)
            if (orderDate.after(start) && orderDate.before(end)) {
                donHang++
            }
        }
        return donHang
    }

    private fun getSanPhamTheoNgay(
        startDate: String,
        endDate: String,
        orderList: List<Order>
    ): HashMap<String, Int> {
        val productQuantityMap = HashMap<String, Int>()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val start = sdf.parse("$startDate 00:00:00")
        val end = sdf.parse("$endDate 23:59:59")
        for (order in orderList) {
            val orderDate = sdf.parse(order.time)

            if (orderDate.after(start) && orderDate.before(end)) {
                for (product in order.products) {
                    val currentQuantity = productQuantityMap.getOrDefault(product.name, 0)
                    var count = 0
                    var name = "${product.name}(${product.size})-$count"
                    while (productQuantityMap.containsKey(name)) {
                        count++
                        name = "${product.name}(${product.size})-$count"
                    }
                    productQuantityMap[name] = currentQuantity + product.quantity
                }
            }
        }
        return productQuantityMap
    }

    fun getUID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }


    fun setTypeAccount(value: String) {
        rule = value
    }

    fun getListDoanhThuTheoNgay(
        startDate: String,
        endDate: String,
        listOrder: List<Order>,
        callback: (List<DoanhThu>) -> Unit
    ) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val calendar = Calendar.getInstance()
        calendar.time = start
        val doanhThuList = mutableListOf<DoanhThu>()
        while (!calendar.time.after(end)) {
            val dateStr = sdf.format(calendar.time)
            val doanhThu = getDoanhThuTheoNgay(dateStr, dateStr, listOrder)
            if (doanhThu > 0) { // Only add the day to the list if the revenue is greater than 0
                doanhThuList.add(DoanhThu(dateStr, doanhThu))
            }
            calendar.add(Calendar.DATE, 1)
        }
        callback(doanhThuList)
    }


    fun getListDonHangTheoNgay(
        startDate: String,
        endDate: String,
        listOrder: List<Order>,
        callback: (List<DonHang>) -> Unit
    ) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val calendar = Calendar.getInstance()
        calendar.time = start
        val donHangList = mutableListOf<DonHang>()
        while (!calendar.time.after(end)) {
            val dateStr = sdf.format(calendar.time)
            val donhang = getDonHangTheoNgay(dateStr, dateStr, listOrder)
            if (donhang > 0) { // Only add the day to the list if the revenue is greater than 0
                donHangList.add(DonHang(dateStr, donhang))
            }
            calendar.add(Calendar.DATE, 1)
        }
        callback(donHangList)
    }

    fun getListSanPhamTheoNgay(
        startDate: String,
        endDate: String,
        listOrder: List<Order>,
        callback: (List<SanPham>) -> Unit
    ) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        val calendar = Calendar.getInstance()
        calendar.time = start
        val productQuantityList = mutableListOf<SanPham>()
        while (!calendar.time.after(end)) {
            val dateStr = sdf.format(calendar.time)
            val productQuantityMap = getSanPhamTheoNgay(dateStr, dateStr, listOrder)
            for ((productName, quantity) in productQuantityMap) {
                val productNameWithoutSuffix = productName.split("-")[0]
productQuantityList.add(SanPham(dateStr, quantity, productNameWithoutSuffix))
            }
            calendar.add(Calendar.DATE, 1)
        }
        callback(productQuantityList)
    }

    fun getAddress(callback: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("ProfileUser").child(getUID())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val address = snapshot.child("location").value.toString()
                callback(address)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }
    fun getAllBill(callback: (List<ItemBill>) -> Unit) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders")
        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = ArrayList<ItemBill>()
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                for (orderSnapshot in snapshot.children) {
                    for (singleOrderSnapshot in orderSnapshot.children) {
                        val order = singleOrderSnapshot.getValue(Order::class.java)
                        if (order != null && order.state == "Đã giao hàng") {
                            val date = order.time
                            val orderID = order.orderID
                            val receiverName = order.receiverName
                            val uID= order.uID
                            val sumPrice = order.sumPrice.toDouble()
                            val itemBill = ItemBill(date,receiverName,sumPrice , orderID, uID)
                            orderList.add(itemBill)
                        }
                    }
                }
                val sortedOrderList =
                    orderList.sortedWith(compareByDescending { sdf.parse(it.date) })
                callback(sortedOrderList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }
    fun getBillByDate(startDate: String, endDate: String, callback: (List<ItemBill>) -> Unit) {
        getAllBill { allBills ->
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val start = sdf.parse("$startDate 00:00:00")
            val end = sdf.parse("$endDate 23:59:59")
            val filteredBills = allBills.filter { bill ->
                val billDate = sdf.parse(bill.date)
                billDate.after(start) && billDate.before(end)
            }
            callback(filteredBills)
        }
    }

    fun getPhoneNumber(callback: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("ProfileUser").child(getUID())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val phoneNumber = snapshot.child("phoneNumber").value.toString()
                callback(phoneNumber)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }



    fun getInforPDF(callback: (UserInfo) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("ProfileUser").child(getUID())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value.toString()
                val phone = snapshot.child("phoneNumber").value.toString()
                val email = snapshot.child("mail").value.toString()
                callback(UserInfo(name, phone, email))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }
    fun countItemsInCart(callback: CartItemCountCallback) {
        val cartReference =
            FirebaseDatabase.getInstance().getReference("Carts").child(getUID())
        cartReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var cartItemCount = 0
                for (snapshot in dataSnapshot.getChildren()) {
                    val cartModel = snapshot.getValue(CartModel::class.java)
                    if (cartModel != null) {
                        cartItemCount += cartModel.quantity
                    }
                }
                callback.onCartItemCount(cartItemCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DataHandler", "countItemsInCart onCancelled: " + databaseError.message)
            }
        })
    }

    fun getAllOrders(callback: (Int) -> Unit) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders")
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var count = 0
                for (userSnapshot in dataSnapshot.children) {
                    for (orderSnapshot in userSnapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        if (order != null && order.state == "Đã giao hàng") {
                            count++
                        }
                    }
                }
                callback(count)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error here
            }
        })
    }

    fun getAllCompletedOrderProducts(callback: (Pair<Int, Double>) -> Unit) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders")
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var sumProduct = 0
                var sumPrice = 0.0
                for (userSnapshot in dataSnapshot.children) {
                    for (orderSnapshot in userSnapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        if (order != null && order.state == "Đã giao hàng") {
                            sumPrice += order.sumPrice.toDouble()
                            for (product in order.products) {
                                sumProduct += product.quantity
                            }
                        }
                    }
                }
                callback(Pair(sumProduct, sumPrice))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error here
            }
        })
    }
    fun interface CartItemCountCallback {
        fun onCartItemCount(count: Int)
    }
    fun getUserNameAndDate(callback: (String) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("ProfileUser").child(getUID())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val phoneNumber = snapshot.child("phoneNumber").value.toString()
                val date = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(Calendar.getInstance().time)
                callback(phoneNumber + date)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeDoanhThu", "onCancelled: " + error.message)
            }
        })
    }
}
