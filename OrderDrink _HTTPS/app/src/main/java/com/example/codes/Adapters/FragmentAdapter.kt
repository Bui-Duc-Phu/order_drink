package com.example.codes.Adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.codes.Fragments.ChoGiaoHangFragment
import com.example.codes.Fragments.ChoXacNhanFragment
import com.example.codes.Fragments.DaGiaoFragment
import com.example.codes.Fragments.DaHuyFragment

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle

)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                ChoXacNhanFragment()
            }
            1 -> {
                ChoGiaoHangFragment()
            }
            2 -> {
                DaGiaoFragment()
            }
            3 -> {
                DaHuyFragment()
            }

            else -> {
                ChoXacNhanFragment()}
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}
