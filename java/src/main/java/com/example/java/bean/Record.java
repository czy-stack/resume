package com.example.java.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

/*
*{
  "id": 2,
  "typeId": 1,
  "fee": 50.0,
  "profit": 10.0,
  "comFeeType": 3,
  "stopLoss": "264,528,792,1320",
  "ctc": "1911",
  "symbol": "MHI",
  "noPre": "MHI",
  "exchange": "HKFE",
  "tradeTime": "555~719,780~984,1035~54",
  "clearTime": "985~990,55~60",
  "festivalTime": null,
  "ctcPre": null,
  "chineseNm": "小恒指",
  "localSymbol": "MHIX9",
  "tradeTimeWe": 55,
  "lots": "1,2,3,4,5",
  "multipleLots": 1,
  "isOff": 0,
  "imgUri": "https://mob.celzj.com/img/XHZ.png",
  "descriptCont": "50元起投，玩转外盘",
  "minChange": 1.0,
  "sortNum": 1,
  "lgnTkn": null,
  "pinYin": "xhz",
  "tradeTime_winter": "555~719,780~984,1035~54",
  "clearTime_winter": "985~990,55~60",
  "tradeTimeWe_winter": 55,
  "tradeTime_summer": "555~719,780~984,1035~54",
  "clearTime_summer": "985~990,55~60",
  "tradeTimeWe_summer": 55,
  "comTypeId": 2
}
   let chineseNm: String
    let descriptCont: String?
    let imgUri: String?
    let typeId: Int					//0 A股，1期货，2外汇，3美股，4港股
    let id: Int                 // ID
    let lots: String            // 手数
    let stopLoss: String        // 触发止损
    let profit: Double         //每点盈亏
    let comFeeType: Int         //汇率id
    let noPre: String            //编码
    let ctc: String?
    let fee: Double
    let pinYin: String
    let isOff: Int
let symbol: String
**/

public class Record extends LitePalSupport {
    private String noPre;
    @Column(unique = true, defaultValue = "unknown")
    @SerializedName("id")
    private int mId;
    @SerializedName("chineseNm")
    private String name;
    private String pinYin;
    private String symbol;
    private int typeId;
    private Double price = 0.0;
    private Double mFloat = 0.0;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getmFloat() {
        return mFloat;
    }

    public void setmFloat(Double mFloat) {
        this.mFloat = mFloat;
    }

    public String getNoPre() {
        return noPre;
    }

    public void setNoPre(String noPre) {
        this.noPre = noPre;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
