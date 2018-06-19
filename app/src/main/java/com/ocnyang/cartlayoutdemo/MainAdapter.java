package com.ocnyang.cartlayoutdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ocnyang.cartlayout.CartAdapter;
import com.ocnyang.cartlayout.CartItemBean;
import com.ocnyang.cartlayout.CartViewHolder;
import com.ocnyang.cartlayout.GroupItemBean;
import com.ocnyang.cartlayoutdemo.bean.GoodsBean;
import com.ocnyang.cartlayoutdemo.bean.ShopBean;
import com.ocnyang.cartlayoutdemo.viewholder.ChildViewHolder;
import com.ocnyang.cartlayoutdemo.viewholder.GroupViewHolder;

import java.util.List;

public class MainAdapter extends CartAdapter {

    public MainAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    protected CartViewHolder getNormalViewHolder(View itemView) {
        return null;
    }

    @Override
    protected CartViewHolder getGroupViewHolder(View itemView) {
        return new GroupViewHolder(itemView, R.id.checkbox);
    }

    @Override
    protected CartViewHolder getChildViewHolder(View itemView) {
        return new ChildViewHolder(itemView, R.id.checkbox);
    }

    @Override
    protected int getChildItemLayout() {
        return R.layout.acitvity_main_item_child;
    }

    @Override
    protected int getGroupItemLayout() {
        return R.layout.acitvity_main_item_group;
    }

    @Override
    protected int getNormalItemLayout() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ChildViewHolder){
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            childViewHolder.textView.setText(((GoodsBean) mDatas.get(position)).getGoods_name());
        }else if (holder instanceof GroupViewHolder){
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            groupViewHolder.textView.setText(((ShopBean) mDatas.get(position)).getShop_name());
        }

    }
}
