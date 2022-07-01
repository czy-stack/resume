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
import com.example.java.databinding.ItemHistorySearchBinding;

import java.util.List;

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期 6/2/22
 */

class HistorySearchAdapter extends BaseAdapter<ShareData, ItemHistorySearchBinding> {
    private Context context;
    private List<ShareData> list;
    public HistorySearchAdapter(Context context, List<ShareData> list){
        super(list);
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder<ItemHistorySearchBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistorySearchBinding view = ItemHistorySearchBinding.inflate(LayoutInflater.from(context),parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemHistorySearchBinding> holder, int position) {

    }

}
