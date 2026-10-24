package com.android.common.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.android.common.inter.OnItemClickListener
import com.android.common.inter.OnItemLongClickListener

/**
 * @作者 陈忠岳
 * @主要功能 基于 ViewBinding 的 RecyclerView 适配器基类
 * @创建日期  2019-07-11
 *
 * 相对早期实现的修正：
 * 1. [addAll] 的 `notifyItemRangeInserted` 第二个参数原本传的是「合并后的总长度」，
 *    应为「本次新增数量」，否则会触发 RecyclerView 的越界/不一致崩溃；
 * 2. 列表改为 `protected set`，避免外部直接赋值后忘记通知刷新；
 * 3. 各增量方法补上边界检查，并使用不可变列表重建，避免复用同一个 MutableList 导致状态错乱。
 */
abstract class BaseAdapter<T, B : ViewBinding>(initial: List<T> = emptyList()) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder<B>>() {

    var list: List<T> = initial.toList()
        protected set

    protected var itemClick: OnItemClickListener? = null
    protected var itemLongClick: OnItemLongClickListener? = null

    override fun getItemCount(): Int = list.size

    fun addItemClickListener(itemClick: OnItemClickListener) {
        this.itemClick = itemClick
    }

    fun addItemLongClickListener(itemLongClick: OnItemLongClickListener) {
        this.itemLongClick = itemLongClick
    }

    fun addItem(item: T) {
        list = list + item
        notifyItemInserted(list.size - 1)
    }

    fun addItem(item: T, position: Int) {
        if (position !in 0..list.size) return
        list = list.toMutableList().apply { add(position, item) }
        notifyItemInserted(position)
    }

    fun addAll(data: List<T>) {
        if (data.isEmpty()) return
        val start = list.size
        list = list + data
        notifyItemRangeInserted(start, data.size)
    }

    fun removeItem(position: Int) {
        if (position !in list.indices) return
        list = list.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size - position)
    }

    fun clear() {
        val size = list.size
        if (size == 0) return
        list = emptyList()
        notifyItemRangeRemoved(0, size)
    }

    /**
     * 整体替换数据。
     * 数据量小时用 notifyDataSetChanged 足够；列表较大时可在此处换成 DiffUtil。
     */
    fun submitList(data: List<T>) {
        list = data.toList()
        notifyDataSetChanged()
    }

    class ViewHolder<B : ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
}
