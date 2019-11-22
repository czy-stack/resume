package com.android.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.common.inter.OnItemClickListener
import com.android.common.inter.OnItemLongClickListener

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-07-11
 */
abstract class BaseAdapter<T>(var list: List<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var itemClick: OnItemClickListener? = null
    protected var itemLongClick: OnItemLongClickListener? = null
    override fun getItemCount(): Int {
        return list.size
    }

    fun addItemClickListener(itemClick: OnItemClickListener) {
        this.itemClick = itemClick
    }

    fun addItemLongClickListener(itemLongClick: OnItemLongClickListener) {
        this.itemLongClick = itemLongClick
    }

    fun addItem(item: T) {
        list = mutableListOf<T>().apply {
            addAll(list)
            add(item)
        }
        notifyDataSetChanged()
    }

    fun addItem(item: T, position: Int) {
        list = mutableListOf<T>().apply {
            addAll(list)
            add(position, item)
        }
        notifyItemInserted(position)
         if (position != list.size) {
            notifyItemRangeChanged(position, list.size - position)
        }
    }

    fun addAll(data: List<T>) {
        val temp = list.size
        list = list.plus(data)
        notifyItemRangeInserted(temp,list.size)
//        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list = mutableListOf<T>().apply {
            addAll(list)
            removeAt(position)
        }
        notifyItemRemoved(position)
        if (position != list.size) {
            notifyItemRangeChanged(position, list.size - position)
        }
    }

    fun clear() {
        val size = list.size
        list = listOf()
        notifyItemRangeRemoved(0,size)
    }

    protected class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}