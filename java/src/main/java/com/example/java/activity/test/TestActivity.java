package com.example.java.activity.test;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.android.common.base.BaseActivity;
import com.android.common.inter.OnItemClickListener;
import com.example.java.bean.ShareData;
import com.example.java.databinding.ActivityTestBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

class TestActivity extends BaseActivity<TestContract.Presenter, ActivityTestBinding> implements TestContract.View, LifecycleOwner, OnItemClickListener {
    @NotNull
    @Override
    public TestContract.Presenter getPresenter() {
        return new TestPresenter(this, this, this);
    }

    @NonNull
    @Override
    protected ActivityTestBinding setBinding() {
        return ActivityTestBinding.inflate(getLayoutInflater());
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onItemClick(int position, int type) {

    }

    @Override
    public void setShares(List<ShareData> list, Boolean history) {

    }


}
