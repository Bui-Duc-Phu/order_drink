package com.example.codes.Fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.codes.Adapters.BannerAdapter
import com.example.codes.Adapters.CategoryAdapter
import com.example.codes.Adapters.ProductAdapter
import com.example.codes.Models.CategoryModel
import com.example.codes.Models.ProductModel
import com.example.codes.R
import com.example.codes.databinding.FragmentHomeBinding
import com.example.codes.network.service.homeService
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private  lateinit var  productAdapter: ProductAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        _init()
        return binding.root
    }

    private fun _init() {
        bannnerHandler()
        productListHandler()
    }

    private fun bannnerHandler() {
        val imageList = ArrayList<SlideModel>() // Create image list
        homeService.getBanner(requireContext(), { listBaner ->
            // Kiểm tra xem listBaner có null không
            listBaner?.forEach { item ->
                imageList.add(SlideModel(item.url))
            }
            // Nếu listBaner null, không làm gì hoặc hiển thị thông báo lỗi
            if (listBaner.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No banners available", Toast.LENGTH_SHORT).show()
            } else {
                binding.recyclerViewBanner.setImageList(imageList)
            }
        }, {
            // Nếu có lỗi từ API hoặc gọi hàm, xử lý tại đây
            Toast.makeText(requireContext(), "Error fetching banners", Toast.LENGTH_SHORT).show()
            Log.e("HomeFragment", "Error in bannerHandler: ${it}")
        })
    }


    private  fun productListHandler() {
        homeService.GetProduct(requireContext(),{listProduct->

         productAdapter = ProductAdapter(listProduct!!)
            binding.recyclerViewProducts.adapter =productAdapter
        },{err-> println(" from productListHandler: "+err)})
    }





}