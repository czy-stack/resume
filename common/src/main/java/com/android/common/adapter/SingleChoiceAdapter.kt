package com.android.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.R
import com.android.common.base.BaseAdapter
import com.android.common.bean.SimpleMapStringBooleanBean
import kotlinx.android.synthetic.main.item_single_choice.view.*

/**
 * @作者 陈忠岳
 * @主要功能 单选列表
 * @创建日期  2019-11-21
 */
class SingleChoiceAdapter(private val context: Context, list: List<SimpleMapStringBooleanBean>) :
    BaseAdapter<SimpleMapStringBooleanBean>(list) {
    private var selectPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_single_choice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.let {
            it.tv_content.text = item.name
            it.tv_content.isSelected = item.select
            it.setOnClickListener { checkItem(position) }
        }
    }

    private fun checkItem(position: Int) {
        for (index in list.indices) {
            list[index].select = position == index
        }
        selectPosition = position
        notifyDataSetChanged()
    }

    fun getSelectPosition():Int{
        return selectPosition
    }

}