package com.ocnyang.cartlayoutdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ocnyang.cartlayout.CartAdapter;
import com.ocnyang.cartlayout.CartItemBean;
import com.ocnyang.cartlayout.CartOnCheckChangeListener;
import com.ocnyang.cartlayout.CartViewHolder;
import com.ocnyang.cartlayout.ChildItemBean;
import com.ocnyang.cartlayout.GroupItemBean;
import com.ocnyang.cartlayoutdemo.bean.GoodsBean;
import com.ocnyang.cartlayoutdemo.bean.ShopBean;
import com.ocnyang.cartlayoutdemo.viewholder.ChildViewHolder;
import com.ocnyang.cartlayoutdemo.viewholder.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = ((RecyclerView) findViewById(R.id.recycler));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(this, getData());
        adapter.setOnCheckChangeListener(new CartOnCheckChangeListener(recyclerView, adapter) {
            @Override
            public void onChildCheckChanged(CartItemBean cartItemBean, boolean isChecked) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private List<CartItemBean> getData() {
        ArrayList<CartItemBean> cartItemBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ShopBean shopBean = new ShopBean();
            long shopNo = System.currentTimeMillis();
            shopBean.setShop_name("SHOP-NO" + shopNo);
            shopBean.setItemType(CartItemBean.TYPE_GROUP);
            shopBean.setItemId(i);
            cartItemBeans.add(shopBean);

            ArrayList<ChildItemBean> goodsBeans = new ArrayList<>();
            for (int j = 0; j < 15; j++) {
                GoodsBean goodsBean = new GoodsBean();
                goodsBean.setGoods_name("GOOD-CODE" + j);
                goodsBean.setItemType(CartItemBean.TYPE_CHILD);
                goodsBean.setItemId(i);
                goodsBean.setGroupId(i);
                goodsBeans.add(goodsBean);
                cartItemBeans.add(goodsBean);
            }
            shopBean.setChilds(goodsBeans);
        }

        return cartItemBeans;
    }


}
