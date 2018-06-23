package com.ocnyang.cartlayoutdemo.bean;
import com.ocnyang.cartlayout.bean.ChildItemBean;

public class GoodsBean extends ChildItemBean {
    private String goods_name;
    private double goods_price;
    private int goods_amount = 1;

    public int getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(int goods_amount) {
        this.goods_amount = goods_amount;
    }

    public double getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(double goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }
}
