package com.android.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class FragmentListAdapter(activity: FragmentActivity, private val list: List<Fragment>) :FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}