package com.android.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @作者 陈忠岳
 * @主要功能 viewpager adapter
 * @创建日期  2019-11-21
 */
class FragmentOldListAdapter(private var list: List<Fragment>,private var strings: List<String>, fm: FragmentManager, behavior: Int =  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) :
    FragmentPagerAdapter(fm,behavior) {
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return strings[position]
    }
}