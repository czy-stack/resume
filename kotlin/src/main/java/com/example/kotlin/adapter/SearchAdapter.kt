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
import com.example.kotlin.databinding.ItemSearchBinding
import com.example.kotlin.db.DBManager

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-22
 */
class SearchAdapter(private val context: Context, list: List<ShareData>) :
    BaseAdapter<ShareData,ItemSearchBinding>(list) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ItemSearchBinding> {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder:ViewHolder<ItemSearchBinding>, position: Int) {
        val item = list[position]
        holder.binding.apply {
            when (item.type) {
                MainConstants.HK -> {
                    ivIcon.setImageDrawable(context.getDrawable(R.mipmap.icon_hk))
                }
                MainConstants.USA -> {
                    ivIcon.setImageDrawable(context.getDrawable(R.mipmap.icon_m))
                }
            }
            tvName.text = StringBuilder(item.name).append("        ").append(item.describe)
            holder.itemView.setOnClickListener {
                DBManager.save(item)
                Event(type = EventConstants.FUTURES_TAB,message = item.name).post()
            }
        }
    }
}