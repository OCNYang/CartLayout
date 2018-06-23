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
    /**
     * 每次当 item 的 CheckBox 状态被点击改变时，触发的回调
     *
     * @param beans adapter 的数据集
     * @param position 当前点击的 item 的下标
     * @param isChecked item checkable 的状态
     * @param itemType item 类型
     */
    void onCheckedChanged(List<ICartItem> beans, int position, boolean isChecked, int itemType);

    /**
     * 触发时机：
     * 1. 当第一次给 adapter 设置当前监听时触发；
     * 2. 当 item 的 CheckBox 状态被点击改变时；如果点击的是 groupItem 也只会触发一次；
     * 3. 当删除一个 item 时、或删除选中的所有 item、或删除所有 item；
     * 4. 当添加一个 item 时、同上；
     * 5. 当 setNewData(), addData(),时。
     * @param cartItemBean 参数没有
     */
    void onCalculateChanged(ICartItem cartItemBean);
}
