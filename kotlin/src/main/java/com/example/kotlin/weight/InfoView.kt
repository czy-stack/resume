package com.example.kotlin.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.android.common.utils.LogUtils
import com.example.kotlin.R
import com.example.kotlin.databinding.ViewInfoBinding

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2020/12/15
 */
class InfoView : LinearLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs,0)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr,0)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        binding = ViewInfoBinding.inflate(LayoutInflater.from(context))
//        LayoutInflater.from(context).inflate(R.layout.view_info, this);
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfoView)
        typedArray.apply {
            getString(R.styleable.InfoView_head_text)?.let {
                headText = it
            }
            getString(R.styleable.InfoView_foot_text)?.let {
                footText = it
            }
            getInteger(R.styleable.InfoView_view_type, EDIT).let {
                viewType = it
            }
            getInteger(R.styleable.InfoView_edit_type, WRITE).let {
                editType = it
            }
        }
        typedArray.recycle()
        initView()
    }


    private var list: List<String> = listOf()
    private var headText: String = ""
    private var footText: String = ""
    private var viewType: Int
    private var editType: Int
    private lateinit var binding : ViewInfoBinding

    companion object {
        const val EDIT = 1;//viewType 可编辑
        const val NO_EDIT = 2 //viewType 不可编辑

        const val SCAN = 1 //扫描二维码
        const val WRITE = 2 //输入
        const val CHOOSE = 3 //列表选择
    }

    private fun initView() {
        if (headText.isNotEmpty()) {
            binding.tvHead.text = headText
        }
        if (footText.isNotEmpty()) {
            binding.tvFoot.text = footText
        }
        setEditType(editType)
        setViewType(viewType)
        setOnClickListener { binding.edInfo.performClick()
            binding.tvHead.setOnClickListener {
                LogUtils.i(msg ="tv_head" )
            }
        LogUtils.i(msg ="performClick" )}
        binding.edInfo.setOnTouchListener { v, event ->
            super.onTouchEvent(event)
//            if (v.id == R.id.ed_info&&canScrollVertically(ed_info.id)){
//                v.parent.requestDisallowInterceptTouchEvent(true)
            LogUtils.i(msg ="ed_info" )
//            }
            true
        }
        binding.edInfo.setOnClickListener { LogUtils.i(msg ="ed_setOnClickListener" ) }
//        ed_info.isFocusable = true
//        ed_info.isFocusableInTouchMode = true
//        ed_info.requestFocus();
//        ed_info.dispatchTouchEvent(event:MotionEvent ){
//            super.dispatchTouchEvent(event)
//        }
//        ed_info.setOnTouchListener(object : View.OnTouchListener{
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                LogUtils.i(msg = "OnTouchListener")
//                return true
//            }
//        })
//        setOnTouchListener { v, event ->  LogUtils.i(msg = "LinearLayout")
//            true
//        }
    }

    public fun setList(list: List<String>) {
        this.list = list
    }

    public fun setViewType(viewType: Int) {
        this.viewType = viewType
        when(viewType){
            SCAN -> {
                binding.edInfo.isFocusable = false
            }
            WRITE -> {
                binding.edInfo.isFocusable = true
            }
            CHOOSE ->{
                binding.edInfo.isFocusable = false
            }
        }
    }

    public fun setEditType(editType: Int) {
        this.editType = editType
        binding.edInfo.isEnabled = (editType == EDIT)
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        super.dispatchTouchEvent(ev)
//        return false
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        super.onInterceptTouchEvent(ev)
//        return false
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        super.onTouchEvent(event)
//        return false
//    }
}