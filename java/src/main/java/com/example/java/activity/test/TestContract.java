package com.example.java.activity.test;

import com.android.common.base.BasePresenter;
import com.android.common.base.BaseView;
import com.example.java.bean.ShareData;

import java.util.List;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

interface TestContract {
    interface View extends BaseView<Presenter> {
        void setShares(List<ShareData> list,Boolean history);
    }
    interface Presenter extends BasePresenter {
        void getShares(String search ,int type);
    }

}
