package com.example.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.base.BaseAdapter
import com.android.common.constants.EventConstants
import com.android.common.event.Event
import com.android.common.utils.post
import com.example.kotlin.R
import com.example.kotlin.bean.ShareData
import com.example.kotlin.constants.MainConstants
import com.example.kotlin.db.DBManager


import kotlinx.android.synthetic.main.item_history_search.view.*

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-2
 */
class HistorySearchAdapter(private val context: Context, list: List<ShareData>) :
    BaseAdapter<ShareData>(list) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.apply {
            when (item.type) {
                MainConstants.HK -> {
                    iv_icon.setImageDrawable(context.getDrawable(R.mipmap.icon_hk))
                }
                MainConstants.USA -> {
                    iv_icon.setImageDrawable(context.getDrawable(R.mipmap.icon_m))
                }
            }
            tv_name.text = StringBuilder(item.name).append("        ").append(item.describe)

            setOnClickListener { Event(type = EventConstants.FUTURES_TAB,message = item.name).post() }

            iv_delete.setOnClickListener {
                DBManager.delete(item)
                removeItem(position)
            }
        }
    }
}