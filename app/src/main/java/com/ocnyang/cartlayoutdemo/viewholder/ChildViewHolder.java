package com.ocnyang.cartlayoutdemo.viewholder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ocnyang.cartlayout.viewholder.CartViewHolder;
import com.ocnyang.cartlayoutdemo.R;
import com.ocnyang.cartlayoutdemo.bean.GoodsBean;

public abstract class ChildViewHolder extends CartViewHolder implements View.OnClickListener {
    public TextView textViewReduce;
    public TextView textView;
    public TextView textViewPrice;
    public TextView textViewNum;
    public TextView textViewAdd;

    public ChildViewHolder(View itemView, int chekbox_id) {
        super(itemView, chekbox_id);

        textView = itemView.findViewById(R.id.tv);
        textViewPrice = itemView.findViewById(R.id.tv_price);
        textViewReduce = ((TextView) itemView.findViewById(R.id.tv_reduce));
        textViewNum = itemView.findViewById(R.id.tv_num);
        textViewAdd = itemView.findViewById(R.id.tv_add);

        itemView.setOnClickListener(this);
        textViewReduce.setOnClickListener(this);
        textViewAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item:
                Toast.makeText(v.getContext(), ((GoodsBean) mICartItem).getGoods_name(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_reduce:
                int intValue = Integer.valueOf(textViewNum.getText().toString()).intValue();
                if (intValue > 1) {
                    intValue--;
                    textViewNum.setText(String.valueOf(intValue));
                    ((GoodsBean) mICartItem).setGoods_amount(intValue);
                    onNeedCalculate();
                }
                break;
            case R.id.tv_add:
                int intValue2 = Integer.valueOf(textViewNum.getText().toString()).intValue();
                intValue2++;
                textViewNum.setText(String.valueOf(intValue2));
                ((GoodsBean) mICartItem).setGoods_amount(intValue2);
                onNeedCalculate();
                break;
            default:
                break;
        }
    }

    /**
     * 这里因为把 ViewHolder 没有写到 adapter 中作为内部类，所以对事件写了一个回调的抽象方法。
     * 如果不想这样写，你可以在以下方式中选其一：
     * 1. 将 ViewHolder 写到 Adapter 中作为内部类，这样你就可以访问 Adapter 中的一些方法属性了；
     * 2. 或者，你把 ItemView & ItemChildView 的事件放到 Adapter 中的 onBindViewHolder() 方法中设置。
     */
    public abstract void onNeedCalculate();
}
