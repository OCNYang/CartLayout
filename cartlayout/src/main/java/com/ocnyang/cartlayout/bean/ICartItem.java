package com.ocnyang.cartlayout.bean;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/21 9:03.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public interface ICartItem {
    int TYPE_NORMAL = 0;
    int TYPE_GROUP = 1;
    int TYPE_CHILD = 2;

    boolean isChecked();

    void setChecked(boolean checked);

    long getItemId();

    void setItemId(long itemId);

    int getItemType();

    void setItemType(int itemType);

    boolean isCollapsing();

    void setCollapsing(boolean isCollapsing);
}
