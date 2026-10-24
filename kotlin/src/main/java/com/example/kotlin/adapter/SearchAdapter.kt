package com.example.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.android.common.base.BaseAdapter
import com.example.kotlin.R
import com.example.kotlin.bean.ShareData
import com.example.kotlin.constants.MainConstants
import com.example.kotlin.databinding.ItemSearchBinding

/**
 * @作者 陈忠岳
 * @主要功能 搜索结果列表
 * @创建日期  2019-11-22
 *
 * 调整：不再直接操作数据库或发送事件，只把点击回传给上层，
 * 数据写入与事件广播由 Presenter 负责（原来 Adapter 里 `DBManager.save` + `Event.post`
 * 让 View 层跨越了 Presenter）。
 */
class SearchAdapter(
    private val onItemClick: (ShareData) -> Unit
) : BaseAdapter<ShareData, ItemSearchBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ItemSearchBinding> {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemSearchBinding>, position: Int) {
        val item = list[position]
        holder.binding.apply {
            ivIcon.bindMarketIcon(item.type)
            tvName.text = item.displayName()
            root.setOnClickListener { onItemClick(item) }
        }
    }
}

/** 结果行的显示文案：名称 + 代码 */
internal fun ShareData.displayName(): String =
    if (describe.isBlank()) name else "$name    $describe"

/** 按市场类型显示国旗/市场图标 */
internal fun ImageView.bindMarketIcon(type: Int) {
    when (type) {
        MainConstants.HK -> setImageResource(R.mipmap.icon_hk)
        MainConstants.USA -> setImageResource(R.mipmap.icon_m)
        else -> setImageDrawable(null)
    }
}
