package com.ocnyang.cartlayout.bean;

import java.util.List;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/15 15:09.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class GroupItemBean extends CartItemBean implements IGroupItem<ChildItemBean> {
    private List<ChildItemBean> childs;

    public List<ChildItemBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildItemBean> childs) {
        this.childs = childs;
    }
}
