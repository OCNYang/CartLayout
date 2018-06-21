package com.ocnyang.cartlayout.listener;

import com.ocnyang.cartlayout.bean.ICartItem;

import java.util.List;

public interface OnItemChangeListener {
    void normalCheckChange(List<ICartItem> beans, int position, boolean isChecked);

    void groupCheckChange(List<ICartItem> beans, int position, boolean isChecked);

    void childCheckChange(List<ICartItem> beans, int position, boolean isChecked);
}
