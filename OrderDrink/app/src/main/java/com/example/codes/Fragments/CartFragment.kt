package com.example.codes.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Activitys.AddOrder
import com.example.codes.Adapters.CartAdapter
import com.example.codes.Firebase.DataHandler
import com.example.codes.Firebase.DataHandler.fetchDataForCart
import com.example.codes.R

class CartFragment : Fragment() {
    private var txtEmptyCart: TextView? = null
    private var txtTotalPrice: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var btnCreateOrder: Button? = null
    private var llBuy: LinearLayout? = null
    private val myCartAdapter = CartAdapter(ArrayList())

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        setControl(view)
        setRecyclerView()
        setDataForCart()
        btnCreateOrder!!.setOnClickListener { v: View? ->
            val intent = Intent(activity, AddOrder::class.java)
            startActivity(intent)
        }
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val position = viewHolder.getAdapterPosition()
                    myCartAdapter.deleteCartItem(DataHandler.orderModelArrayList, position)
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return view
    }

    private fun setDataForCart() {
        fetchDataForCart(recyclerView!!, txtEmptyCart!!, txtTotalPrice!!, llBuy!!)
    }

    private fun setRecyclerView() {
        recyclerView!!.setLayoutManager(LinearLayoutManager(activity))
        recyclerView!!.setAdapter(myCartAdapter)
    }

    private fun setControl(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        txtEmptyCart = view.findViewById(R.id.txtEmptyCart)
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice)
        btnCreateOrder = view.findViewById(R.id.btnCreateOrder)
        llBuy = view.findViewById(R.id.llBuy)
    }
}
