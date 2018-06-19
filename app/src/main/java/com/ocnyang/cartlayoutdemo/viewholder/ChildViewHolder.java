package com.ocnyang.cartlayoutdemo.viewholder;

import android.view.View;
import android.widget.TextView;

import com.ocnyang.cartlayout.CartViewHolder;
import com.ocnyang.cartlayoutdemo.R;

public class ChildViewHolder extends CartViewHolder{
    public TextView textView;

    public ChildViewHolder(View itemView, int chekbox_id) {
        super(itemView, chekbox_id);
        textView = itemView.findViewById(R.id.tv);
    }
}
