package com.example.java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.common.base.BaseAdapter;
import com.example.java.R;
import com.example.java.bean.ShareData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

class SearchAdapter extends BaseAdapter<ShareData> {
    private Context context;
    private List<ShareData> list;

    public SearchAdapter(Context context, List<ShareData> list) {
        super(list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {;
        ShareData shareData = list.get(position);
        switch (shareData.getType()){
//            case MainConstants.HK:
//                break;
//                case Main
        }
    }
}
