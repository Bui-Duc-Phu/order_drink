package com.example.codes.Administrator.Fragments

import androidx.fragment.app.Fragment
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Administrator.Adapters.ItemSanPhamAdapter
import com.example.codes.Administrator.model.SanPham
import com.example.codes.Firebase.DataHandler
import com.example.codes.Models.Order
import com.example.codes.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeSanPham : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var view: View
    private lateinit var SanPhamButton: Button
    private lateinit var SanPhamChartButton: Button
    private lateinit var listOrder: List<Order>
    private lateinit var listSanPham: List<SanPham>
    private lateinit var SanPhamRecyclerView: RecyclerView
    private lateinit var SanPhamHeader: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_home_admin_san_pham, container, false)
        setControl()
        DataHandler.getOrderWithState("Đã giao hàng") { orderList ->
            listOrder = orderList
        }
        setDate()
        setSanPham()
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setSanPham() {
        SanPhamButton.setOnClickListener {
            if(startDateEditText.text.toString().isEmpty() || endDateEditText.text.toString().isEmpty()) {
                Toast.makeText(view.context,   getString(R.string.vui_l_ng_ch_n_ng_y_b_t_u_v_ng_y_k_t_th_c), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SanPhamRecyclerView.visibility = View.VISIBLE
            SanPhamHeader.visibility = View.VISIBLE
            pieChart.visibility = View.GONE
            DataHandler.getListSanPhamTheoNgay(startDateEditText.text.toString(), endDateEditText.text.toString(), listOrder) { listSanPham ->
                this.listSanPham = listSanPham
                if(listSanPham.isEmpty()) {
                    Toast.makeText(view.context,   getString(R.string.kh_ng_c_d_li_u_hi_n_th), Toast.LENGTH_SHORT).show()
                }else {
                    SanPhamRecyclerView.adapter = ItemSanPhamAdapter(listSanPham)
                    SanPhamRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
        SanPhamChartButton.setOnClickListener {
            if(startDateEditText.text.toString().isEmpty() || endDateEditText.text.toString().isEmpty()) {
                Toast.makeText(view.context,   getString(R.string.vui_l_ng_ch_n_ng_y_b_t_u_v_ng_y_k_t_th_c), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SanPhamRecyclerView.visibility = View.GONE
            SanPhamHeader.visibility = View.GONE
            pieChart.visibility = View.VISIBLE
            if (listSanPham.isEmpty()) {
                Toast.makeText(view.context,   getString(R.string.kh_ng_c_d_li_u_hi_n_th), Toast.LENGTH_SHORT).show()
            } else {
                setPieChart(listSanPham)
            }
        }
    }
    private fun setPieChart(listSanPham: List<SanPham>) {
        val productQuantityMap = HashMap<String, Float>()

        for (sanPham in listSanPham) {
            val currentQuantity = productQuantityMap.getOrDefault(sanPham.name, 0f)
            productQuantityMap[sanPham.name] = currentQuantity + sanPham.quantity.toFloat()
        }

        val entries = ArrayList<PieEntry>()
        for ((productName, quantity) in productQuantityMap) {
            entries.add(PieEntry(quantity, productName))
        }

        val dataSet = PieDataSet(entries, "Sản phẩm")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.setDrawEntryLabels(false)
        pieChart.invalidate()
    }

    private fun setControl() {
        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
        SanPhamButton = view.findViewById(R.id.SanPhamButton)
        SanPhamChartButton = view.findViewById(R.id.SanPhamChartButton)
        SanPhamRecyclerView = view.findViewById(R.id.SanPhamRecyclerView)
        SanPhamRecyclerView.layoutManager = LinearLayoutManager(view.context)
        SanPhamHeader = view.findViewById(R.id.SanPhamHeader)
        pieChart = view.findViewById(R.id.SanPhamBarChart)
    }

    private fun setDate() {

        startDateEditText.setOnClickListener { view ->
            val now = Calendar.getInstance()
            DatePickerDialog(
                view.context,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, monthOfYear)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateStr = SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(selectedDate.time)
                    if (endDateEditText.text.toString().isNotEmpty()) {
                        val endDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(
                            endDateEditText.text.toString()
                        )
                        if (selectedDate.time.after(endDate)) {
                            Toast.makeText(
                                view.context,
                                getString(R.string.ng_y_b_t_u_kh_ng_th_sau_ng_y_k_t_th_c),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@DatePickerDialog
                        }
                    }
                    startDateEditText.setText(dateStr)
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        endDateEditText.setOnClickListener { view ->
            val now = Calendar.getInstance()
            DatePickerDialog(
                view.context,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, monthOfYear)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateStr = SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(selectedDate.time)
                    if (startDateEditText.text.toString().isNotEmpty()) {
                        val startDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(
                            startDateEditText.text.toString()
                        )
                        if (selectedDate.time.before(startDate)) {
                            Toast.makeText(
                                view.context,
                                getString(R.string.ng_y_k_t_th_c_kh_ng_th_tr_c_ng_y_b_t_u),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@DatePickerDialog
                        }
                    }
                    endDateEditText.setText(dateStr)
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}