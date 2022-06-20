package com.example.java.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

public class ShareData extends LitePalSupport {
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getmFloat() {
        return mFloat;
    }

    public void setmFloat(Double mFloat) {
        this.mFloat = mFloat;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Boolean getAdd() {
        return add;
    }

    public void setAdd(Boolean add) {
        this.add = add;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(unique = true)
    private String symbol = "";//股票码
    private String name = "";//股票名
    private Double value = 0.0;//现价
    private Double change = 0.0;//增长值
    private Double mFloat = 0.0;//涨幅值
    private String describe = "";//额外说明
    private Boolean add = false;//收藏
    private Boolean optional = false;//是否关注
    private int type = 1;//A股 1 美股 3 港股 4
}
