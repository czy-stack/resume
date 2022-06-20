package com.example.kotlin.bean

import com.google.gson.annotations.SerializedName
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport


/**
 *行情请求 自定义转换
 *
 *
 * var hq_str_s_sh601988="中国银行,3.640,0.040,1.11,943238,34177";
 *                            股票码    股票名     现价 增长值 涨幅值
 *
 * 2,002477,雏鹰退,0.17,0.01,6.25,773719,13063057,18.75,0.18,0.15,0.16,0.16,0.00,1.67,3.91,-,-1.20,5329771
43,336699439,-80.68%,-88.96%,6.25,2010-09-15,2019-10-14 15:00:00,773719
 */
data class ShareData(
    @Column(unique = true)
    var symbol: String = "", //股票码
    var name: String = "",  //股票名
    var value: Double = 0.0, //现价
    var change: Double = 0.0, //增长值
    var float: Double = 0.0, //涨幅值
    var describe: String = "",//额外说明
    var add: Boolean = false,//收藏
    var optional: Boolean = false,//是否关注
    var type : Int = 1 //A股 1 美股 3 港股 4
): LitePalSupport(){}