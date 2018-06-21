package com.ocnyang.cartlayout.listener;

import com.ocnyang.cartlayout.bean.ICartItem;

import java.util.List;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/15 11:27.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public interface OnCheckChangeListener {
    void onCheckedChanged(List<ICartItem> beans, int position, boolean isChecked, int itemType);
}
