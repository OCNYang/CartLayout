package com.ocnyang.cartlayout.bean;

import java.util.List;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/21 9:11.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public interface IGroupItem<CHILD extends IChildItem> extends ICartItem {
    List<CHILD> getChilds();

    void setChilds(List<CHILD> childs);
}
