package com.ocnyang.cartlayout;

import java.util.List;

public interface OnItemChangeListener {
    void normalCheckChange(List<CartItemBean> beans, int position, boolean isChecked);

    void groupCheckChange(List<CartItemBean> beans, int position, boolean isChecked);

    void childCheckChange(List<CartItemBean> beans, int position, boolean isChecked);
}
