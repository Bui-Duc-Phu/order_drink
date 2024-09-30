package com.example.codes.Administrator.Fragments


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
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
import com.example.codes.Administrator.Adapters.ItemDoanhThuAdapter
import com.example.codes.Administrator.model.DoanhThu
import com.example.codes.Firebase.DataHandler
import com.example.codes.Models.Order
import com.example.codes.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeDoanhThu : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var view: View
    private lateinit var DoanhThuButton: Button
    private lateinit var DoanhThuChartButton: Button
    private lateinit var listOrder: List<Order>
    private lateinit var listDoanhThu: List<DoanhThu>
    private lateinit var DoanhThuRecyclerView: RecyclerView
    private lateinit var DoanhThuHeader: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_home_admin_doanh_thu, container, false)
        setControl()
        DataHandler.getOrderWithState("Đã giao hàng") { orderList ->
            listOrder = orderList
        }
        setDate()
        setDoanhThu()
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDoanhThu() {
        DoanhThuButton.setOnClickListener {
            if (startDateEditText.text.toString().isEmpty() || endDateEditText.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(
                    view.context,
                    getString(R.string.vui_l_ng_ch_n_ng_y_b_t_u_v_ng_y_k_t_th_c),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            DoanhThuRecyclerView.visibility = View.VISIBLE
            DoanhThuHeader.visibility = View.VISIBLE
            barChart.visibility = View.GONE
            DataHandler.getListDoanhThuTheoNgay(
                startDateEditText.text.toString(),
                endDateEditText.text.toString(),
                listOrder
            ) { listDoanhThu ->
                this.listDoanhThu = listDoanhThu
                if(listDoanhThu.isEmpty()) {
                    Toast.makeText(view.context,
                        getString(R.string.kh_ng_c_d_li_u_hi_n_th), Toast.LENGTH_SHORT)
                        .show()
                }else {
                    DoanhThuRecyclerView.adapter = ItemDoanhThuAdapter(listDoanhThu)
                    DoanhThuRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
        DoanhThuChartButton.setOnClickListener {
            if(startDateEditText.text.toString().isEmpty() || endDateEditText.text.toString().isEmpty()) {
                Toast.makeText(view.context, getString(R.string.vui_l_ng_ch_n_ng_y_b_t_u_v_ng_y_k_t_th_c), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            DoanhThuRecyclerView.visibility = View.GONE
            DoanhThuHeader.visibility = View.GONE
            barChart.visibility = View.VISIBLE
            if (listDoanhThu.isEmpty()) {
                Toast.makeText(view.context,
                    getString(R.string.kh_ng_c_d_li_u_hi_n_th), Toast.LENGTH_SHORT)
                    .show()
            } else {
                createChart(listDoanhThu)
            }
        }
    }


    private fun createChart(listDoanhThu: List<DoanhThu>) {
        // Tạo dữ liệu cho biểu đồ từ listDoanhThu
        val entries = listDoanhThu.mapIndexed { index, doanhThu ->
            BarEntry(index.toFloat(), doanhThu.revenue.toFloat())
        }

        // Tạo một BarDataSet với entries
        val barDataSet = BarDataSet(entries, getString(R.string.doanh_thu))

        // Tạo một danh sách màu
        val colors = ArrayList<Int>()
        for (i in listDoanhThu.indices) {
            colors.add(ColorTemplate.MATERIAL_COLORS[i % ColorTemplate.MATERIAL_COLORS.size])
        }

        // Thiết lập màu cho BarDataSet
        barDataSet.colors = colors

        // Tạo một BarData với barDataSet
        val barData = BarData(barDataSet)

        // Thiết lập dữ liệu cho biểu đồ và làm mới biểu đồ
        barChart.data = barData

        // Tạo một danh sách LegendEntry
        val legendEntries = listDoanhThu.mapIndexed { index, doanhThu ->
            LegendEntry().apply {
                formColor = colors[index]
                label = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).run {
                    val date = parse(doanhThu.date)
                    SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)
                }
            }
        }

        // Thiết lập danh sách LegendEntry cho chú thích của biểu đồ
        barChart.legend.setCustom(legendEntries)

        barChart.invalidate()
    }

    private fun setControl() {
        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
        DoanhThuButton = view.findViewById(R.id.DoanhThuButton)
        DoanhThuChartButton = view.findViewById(R.id.DoanhThuChartButton)
        DoanhThuRecyclerView = view.findViewById(R.id.DoanhThuRecyclerView)
        DoanhThuRecyclerView.layoutManager = LinearLayoutManager(view.context)
        DoanhThuHeader = view.findViewById(R.id.DoanhThuHeader)
        barChart = view.findViewById(R.id.DoanhThuBarChart)
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