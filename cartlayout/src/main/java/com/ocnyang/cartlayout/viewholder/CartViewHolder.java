package com.ocnyang.cartlayout.viewholder;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.ocnyang.cartlayout.bean.ICartItem;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/15 12:15.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class CartViewHolder extends RecyclerView.ViewHolder {
    public CheckBox mCheckBox;
    protected ICartItem mICartItem;

    public CartViewHolder(View itemView) {
        this(itemView, -1);
    }

    public CartViewHolder(View itemView, @IdRes int chekbox_id) {
        super(itemView);
        if (chekbox_id != -1) {
            mCheckBox = itemView.findViewById(chekbox_id);
        }
    }

    public void bindData(ICartItem ICartItem) {
        mICartItem = ICartItem;
    }
}
