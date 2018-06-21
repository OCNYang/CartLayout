package com.ocnyang.cartlayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ocnyang.cartlayout.bean.CartItemBean;
import com.ocnyang.cartlayout.bean.ChildItemBean;
import com.ocnyang.cartlayout.bean.ICartItem;
import com.ocnyang.cartlayout.listener.CartOnCheckChangeListener;
import com.ocnyang.cartlayoutdemo.bean.GoodsBean;
import com.ocnyang.cartlayoutdemo.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RecyclerView recyclerView;
    private TextView mTvTitle;
    private TextView mTvEdit;
    private CheckBox mCheckBoxAll;
    private TextView mTvTotal;
    private Button mBtnSubmit;

    MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = ((RecyclerView) findViewById(R.id.recycler));
        mTvTitle = ((TextView) findViewById(R.id.tv_title));
        mTvEdit = ((TextView) findViewById(R.id.tv_edit));
        mCheckBoxAll = ((CheckBox) findViewById(R.id.checkbox_all));
        mTvTotal = ((TextView) findViewById(R.id.tv_total_price));
        mBtnSubmit = ((Button) findViewById(R.id.btn_go_to_pay));

        mTvEdit.setOnClickListener(this);
        mCheckBoxAll.setOnCheckedChangeListener(this);
        mBtnSubmit.setOnClickListener(this);

        mTvTitle.setText(getString(R.string.cart,0));
        mBtnSubmit.setText(getString(R.string.go_settle_X,0));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this, getData());
        mAdapter.setOnCheckChangeListener(new CartOnCheckChangeListener(recyclerView, mAdapter) {
            @Override
            public void onChildCheckChanged(ICartItem cartItemBean, boolean isChecked) {

            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private List<CartItemBean> getData() {
        ArrayList<CartItemBean> cartItemBeans = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            ShopBean shopBean = new ShopBean();
//            long shopNo = System.currentTimeMillis();
//            shopBean.setShop_name("SHOP-NO" + shopNo);
//            shopBean.setItemType(CartItemBean.TYPE_GROUP);
//            shopBean.setItemId(i);
//            cartItemBeans.add(shopBean);
//
//            ArrayList<ChildItemBean> goodsBeans = new ArrayList<>();
//            for (int j = 0; j < 15; j++) {
//                GoodsBean goodsBean = new GoodsBean();
//                goodsBean.setGoods_name("GOOD-CODE" + j);
//                goodsBean.setItemType(CartItemBean.TYPE_CHILD);
//                goodsBean.setItemId(i);
//                goodsBean.setGroupId(i);
//                goodsBeans.add(goodsBean);
//                cartItemBeans.add(goodsBean);
//            }
//            shopBean.setChilds(goodsBeans);
//        }

        for (int i = 0; i < 10; i++) {
            ShopBean shopBean = new ShopBean();
            long shopNo = System.currentTimeMillis();
            shopBean.setShop_name("解忧杂货铺 第" + (i + 1) + "分店");
            shopBean.setItemType(CartItemBean.TYPE_GROUP);
            shopBean.setItemId(i);
            cartItemBeans.add(shopBean);

            ArrayList<ChildItemBean> goodsBeans = new ArrayList<>();
            for (int j = 0; j < 15; j++) {
                GoodsBean goodsBean = new GoodsBean();
                goodsBean.setGoods_name("忘忧水 " + (j + 1) + " 代");
                goodsBean.setItemType(CartItemBean.TYPE_CHILD);
                goodsBean.setItemId((j + 1) * 10 + j);
                goodsBean.setGroupId(i);
                goodsBeans.add(goodsBean);
                cartItemBeans.add(goodsBean);
            }
            shopBean.setChilds(goodsBeans);
        }
        return cartItemBeans;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //编辑按钮事件
            case R.id.tv_edit:
                break;
            //提交订单 & 删除选中（编辑状态）
            case R.id.btn_go_to_pay:
                break;
            default:
                break;
        }
    }

    /**
     * 全选按钮的状态监听
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mAdapter.checkedAll(isChecked);
    }
}
