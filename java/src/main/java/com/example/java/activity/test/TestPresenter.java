package com.example.java.activity.test;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.android.common.lifecycle.LifecyclePresenter;

import org.jetbrains.annotations.NotNull;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

class TestPresenter extends LifecyclePresenter implements TestContract.Presenter{
    public TestPresenter(Context context , TestContract.View view , @NotNull LifecycleOwner mLifecycleOwner) {
        super(mLifecycleOwner);
    }

    @Override
    public void getShares(String search, int type) {

    }

    @Override
    public void start() {

    }
}
