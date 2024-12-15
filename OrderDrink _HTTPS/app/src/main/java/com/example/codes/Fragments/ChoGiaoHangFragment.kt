package com.example.codes.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Adapters.OrderClientAdapter
import com.example.codes.Firebase.DataHandler
import com.example.codes.R

class ChoGiaoHangFragment : Fragment() {

    private val dataHandler = DataHandler

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cho_giao_hang, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = OrderClientAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        dataHandler.getOrderWithStateClient("Đang giao hàng") { orderList ->
            adapter.submitList(orderList)
        }

        return view
    }
}