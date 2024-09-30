package com.example.codes.Administrator.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Administrator.Activitys.DetailOrderAdmin
import com.example.codes.Administrator.model.ItemBill
import com.example.codes.Firebase.DataHandler
import com.example.codes.R

class ItemBillAdapter(private var billList: List<ItemBill>) : RecyclerView.Adapter<ItemBillAdapter.ItemBillViewHolder>() {

    class ItemBillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBillViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
        return ItemBillViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemBillViewHolder, position: Int) {
        val currentItem = billList[position]
        holder.tvDate.text = currentItem.date
        holder.tvName.text = currentItem.name
        holder.tvPrice.text = currentItem.price.toString()
        holder.itemView.setOnClickListener {
            DataHandler.getOrderDetails(currentItem.orderID, currentItem.uID) { orderDetail ->
                val intent = Intent(it.context, DetailOrderAdmin::class.java)
                intent.putExtra("orderDetail", orderDetail)
                it.context.startActivity(intent)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newOrderList: List<ItemBill>) {
        billList = newOrderList
        notifyDataSetChanged()
    }
    override fun getItemCount() = billList.size
}