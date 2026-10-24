package com.example.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseAdapter
import com.example.kotlin.bean.ShareData
import com.example.kotlin.databinding.ItemHistorySearchBinding

/**
 * @作者 陈忠岳
 * @主要功能 历史搜索列表
 * @创建日期  2019-11-22
 *
 * 调整：删除动作交由上层处理（原来在 Adapter 里 `DBManager.delete(item)` 后直接 `removeItem`，
 * 数据与视图各改一半，一旦删除失败两边就不一致）。
 */
class HistorySearchAdapter(
    private val onItemClick: (ShareData) -> Unit,
    private val onDeleteClick: (ShareData) -> Unit
) : BaseAdapter<ShareData, ItemHistorySearchBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ItemHistorySearchBinding> {
        val binding = ItemHistorySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemHistorySearchBinding>, position: Int) {
        val item = list[position]
        holder.binding.apply {
            ivIcon.bindMarketIcon(item.type)
            tvName.text = item.displayName()
            root.setOnClickListener { onItemClick(item) }
            ivDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}
