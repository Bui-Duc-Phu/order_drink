package com.example.codes.Administrator.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.codes.Firebase.DataHandler
import com.example.codes.databinding.FragmentHomeAdminBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.text.DecimalFormat


class Home : Fragment() {
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var homePageViewAdapter: HomePageViewAdapter

    lateinit var binding : FragmentHomeAdminBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeAdminBinding.inflate(inflater,container,false)
        tablayout = binding.TabLayout
        viewPager2 = binding.ViewPage
        homePageViewAdapter = HomePageViewAdapter(this)
        viewPager2.setAdapter(homePageViewAdapter)
        tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tablayout.getTabAt(position)!!.select()
            }
        })
        setThongKeAll()
        return binding.root
    }

    private fun setThongKeAll() {
        val decimalFormat = DecimalFormat("#.#")

        DataHandler.getAllOrders { count->
            binding.tvTotalOrders.text = "Tổng đơn hàng đã hoàn thành: "+ count.toString()
        }
        DataHandler.getAllCompletedOrderProducts { result->
            binding.tvTotalProducts.text ="Tổng sản phẩm đã đặt: "+ result.first
            val formattedSumPrice = decimalFormat.format(result.second)
            binding.tvTotalRevenue.text ="Tổng doanh thu:" + formattedSumPrice
        }
    }
}
