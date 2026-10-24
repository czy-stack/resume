package com.example.kotlin.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.kotlin.R
import com.example.kotlin.databinding.ViewInfoBinding

/**
 * @作者 陈忠岳
 * @主要功能 通用「标签 + 输入框 + 后缀」表单行
 * @创建日期  2020/12/15
 *
 * 相对早期实现的修正：
 * 1. `ViewInfoBinding.inflate(...)` 之后从未 `addView`，控件根本没被挂到 InfoView 上，
 *    自定义 View 在布局里是空白的；改为 inflate 时直接 attach 到自身；
 * 2. 读属性用 `getInteger` 读 `format="integer"` 的属性，语义不符，改为 `getInt`；
 * 3. `setViewType` 里用的是 edit_type 那组常量（SCAN/WRITE/CHOOSE），
 *    与 view_type 的 EDIT/NO_EDIT 常量组混用，导致两个方法职责相反；
 * 4. 去掉了层层嵌套的日志与 `performClick` 转发，行为更容易预期。
 */
class InfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        /** view_type：可编辑 */
        const val EDIT = 1
        /** view_type：不可编辑 */
        const val NO_EDIT = 2

        /** edit_type：扫码录入 */
        const val SCAN = 1
        /** edit_type：手动输入 */
        const val WRITE = 2
        /** edit_type：列表选择 */
        const val CHOOSE = 3
    }

    private val binding = ViewInfoBinding.inflate(LayoutInflater.from(context), this, true)

    var headText: String = ""
        set(value) {
            field = value
            binding.tvHead.text = value
        }

    var footText: String = ""
        set(value) {
            field = value
            binding.tvFoot.text = value
        }

    private var viewType: Int = EDIT
    private var editType: Int = WRITE

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfoView)
        try {
            headText = typedArray.getString(R.styleable.InfoView_head_text).orEmpty()
            footText = typedArray.getString(R.styleable.InfoView_foot_text).orEmpty()

            val type = typedArray.getInt(R.styleable.InfoView_view_type, EDIT)
            val edit = typedArray.getInt(R.styleable.InfoView_edit_type, WRITE)
            setViewType(type)
            setEditType(edit)
        } finally {
            typedArray.recycle()
        }
    }

    /** 是否可编辑 */
    fun setViewType(type: Int) {
        viewType = type
        binding.edInfo.isEnabled = type == EDIT
    }

    fun getViewType(): Int = viewType

    /** 交互方式：扫码 / 输入 / 选择 */
    fun setEditType(type: Int) {
        editType = type
        val editable = type == WRITE
        binding.edInfo.isFocusable = editable
        binding.edInfo.isFocusableInTouchMode = editable
    }

    fun getEditType(): Int = editType

    fun setContent(text: CharSequence) {
        binding.edInfo.setText(text)
    }

    fun getContent(): CharSequence = binding.edInfo.text
}
