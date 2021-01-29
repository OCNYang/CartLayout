package com.ocnyang.cartlayout.listener;

import com.ocnyang.cartlayout.bean.ICartItem;

import java.util.List;

/*******************************************************************
 *    * * * *   * * * *   *     *       @Author: OCN.Yang
 *    *     *   *         * *   *       @CreateDate: 2021/1/29 9:21 AM.
 *    *     *   *         *   * *       @Email: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  @GitHub: https://github.com/OCNYang
 *******************************************************************/

public interface OnChildCollapsingChangeListener {
    void onGroupItemClickCollapsibleChild(List<ICartItem> beans, int position);
}
