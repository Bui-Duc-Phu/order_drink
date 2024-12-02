package com.example.codes.Fragments


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Adapters.SizeAdapter
import com.example.codes.Administrator.MyApp
import com.example.codes.Firebase.DataHandler.addToCart
import com.example.codes.Models.ProductModel
import com.example.codes.Models.SizeModel
import com.example.codes.R
import com.example.codes.network.dto.request.AddCartRequest
import com.example.codes.network.dto.request.ProductName
import com.example.codes.network.service.cartService

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SizeDialogFragment : BottomSheetDialogFragment() {
    private var sizeAdapter: SizeAdapter? = null
    private val sizeList1 = ArrayList<SizeModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_size_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sizeAdapter = SizeAdapter(sizeList1)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        recyclerView.setAdapter(sizeAdapter)
        assert(arguments != null)

        val product = (requireArguments().getSerializable("product") as ProductModel?)!!
        val productId = product.name


        val productName = productId?.let { ProductName(it) }

        cartService.getSize(requireContext(),productName!!,{ listSize ->
            sizeList1.clear()
            listSize.forEach { item->
                sizeList1.add(SizeModel(item.size, item.price))
            }
            sizeAdapter!!.notifyDataSetChanged()
        },{err->
            println("get size err : " + err)

        })


        val btn_add = view.findViewById<Button>(R.id.btn_add_product_to_cart)
        val buttonDecrease = view.findViewById<ImageView>(R.id.buttonDecrease)
        val buttonIncrease = view.findViewById<ImageView>(R.id.buttonIncrease)
        val textQuantity = view.findViewById<TextView>(R.id.textQuantity)

        buttonDecrease.setOnClickListener { v: View? ->
            var quantity = textQuantity.getText().toString().toInt()
            if (quantity > 1) {
                quantity--
                textQuantity.text = quantity.toString()
            }
        }

        buttonIncrease.setOnClickListener { v: View? ->
            var quantity = textQuantity.getText().toString().toInt()
            quantity++
            textQuantity.text = quantity.toString()
        }
        btn_add.setOnClickListener { v: View? ->
            val selectedSize = sizeAdapter!!.selectedSize
            if (selectedSize != null) {
                val quantity = textQuantity.getText().toString().toInt()


                val productCart = product.id?.let {
                    MyApp.UID?.let { it1 ->
                    AddCartRequest(it,
                        it1, quantity.toString(), selectedSize.price.toString(),selectedSize.size)
                } }
                if (productCart != null) {
                    cartService.addCart(requireContext(),productCart,{ addCartResult ->
                        println("add cart sucessfull "+ addCartResult)


                    },{err->
                        println("add cart false ")

                    })
                }

                addToCart(product, selectedSize, quantity)
                dismiss()
            } else {
                Toast.makeText(context, R.string.chon_size_khi_them_vao_cart, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }




    companion object {
        fun newInstance(product: ProductModel?): SizeDialogFragment {
            val fragment = SizeDialogFragment()
            val args = Bundle()
            args.putSerializable("product", product)
            fragment.setArguments(args)
            return fragment
        }
    }
}