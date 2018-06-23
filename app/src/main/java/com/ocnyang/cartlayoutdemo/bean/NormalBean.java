package com.ocnyang.cartlayoutdemo.bean;

import com.ocnyang.cartlayout.bean.CartItemBean;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/23 14:03.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public class NormalBean extends CartItemBean{
    int markdownNumber;

    public int getMarkdownNumber() {
        return markdownNumber;
    }

    public void setMarkdownNumber(int markdownNumber) {
        this.markdownNumber = markdownNumber;
    }
}
