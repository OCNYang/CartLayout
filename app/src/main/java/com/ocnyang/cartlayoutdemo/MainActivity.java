package com.ocnyang.cartlayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ocnyang.cartlayout.RecyclerViewWithContextMenu;
import com.ocnyang.cartlayout.bean.CartItemBean;
import com.ocnyang.cartlayout.bean.ICartItem;
import com.ocnyang.cartlayout.listener.CartOnCheckChangeListener;
import com.ocnyang.cartlayoutdemo.bean.GoodsBean;
import com.ocnyang.cartlayoutdemo.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView mTvTitle;
    private TextView mTvEdit;
    private CheckBox mCheckBoxAll;
    private TextView mTvTotal;
    private Button mBtnSubmit;

    MainAdapter mAdapter;

    private boolean isEditing;//是否处于编辑状态
    private int totalCount;//购物车商品ChildItem的总数量，店铺条目不计算在内
    private int totalCheckedCount;//勾选的商品总数量，店铺条目不计算在内
    private double totalPrice;//勾选的商品总价格

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
        mCheckBoxAll.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);

        mTvTitle.setText(getString(R.string.cart, 0));
        mBtnSubmit.setText(getString(R.string.go_settle_X, 0));
        mTvTotal.setText(getString(R.string.rmb_X, 0.00));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this, getData());
        mAdapter.setOnCheckChangeListener(new CartOnCheckChangeListener(recyclerView, mAdapter) {
            @Override
            public void onCalculateChanged(ICartItem cartItemBean) {
                calculate();
            }
        });
        recyclerView.setAdapter(mAdapter);

        // 给列表注册 ContextMenu 事件。
        // 同时如果想让ItemView响应长按弹出菜单，需要在item xml布局中设置 android:longClickable="true"
        registerForContextMenu(recyclerView);
    }

    /**
     * 添加选项菜单
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.main_contextmenu, menu);
    }

    /**
     * 选项菜单点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取到的是 listView 里的条目信息
        RecyclerViewWithContextMenu.RecyclerViewContextInfo info = (RecyclerViewWithContextMenu.RecyclerViewContextInfo) item.getMenuInfo();
        Log.d("ContentMenu", "onCreateContextMenu position = " + (info != null ? info.getPosition() : "-1"));
        if (info != null && info.getPosition() != -1) {
            switch (item.getItemId()) {
                case R.id.action_remove:
                    mAdapter.removeChild(info.getPosition());
                    Toast.makeText(this, "成功移入收藏", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_findmore:
                    Toast.makeText(this, "查找与" + ((GoodsBean) mAdapter.getData().get(info.getPosition())).getGoods_name() + "相似的产品", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_delete:
                    mAdapter.removeChild(info.getPosition());
                    break;
                default:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给相关的 textView 进行数据填充
     */
    private void calculate() {
        totalCheckedCount = 0;
        totalCount = 0;
        totalPrice = 0.00;
        int notChildTotalCount = 0;
        if (mAdapter.getData() != null) {
            for (ICartItem iCartItem : mAdapter.getData()) {
                if (iCartItem.getItemType() == ICartItem.TYPE_CHILD) {
                    totalCount++;
                    if (iCartItem.isChecked()) {
                        totalCheckedCount++;
                        totalPrice += ((GoodsBean) iCartItem).getGoods_price() * ((GoodsBean) iCartItem).getGoods_amount();
                    }
                } else {
                    notChildTotalCount++;
                }
            }
        }

        mTvTitle.setText(getString(R.string.cart, totalCount));
        mBtnSubmit.setText(getString(isEditing ? R.string.delete_X : R.string.go_settle_X, totalCheckedCount));
        mTvTotal.setText(getString(R.string.rmb_X, totalPrice));
        if (mCheckBoxAll.isChecked() && (totalCheckedCount == 0 || (totalCheckedCount + notChildTotalCount) != mAdapter.getData().size())) {
            mCheckBoxAll.setChecked(false);
        }
        if (totalCheckedCount != 0 && (!mCheckBoxAll.isChecked()) && (totalCheckedCount + notChildTotalCount) == mAdapter.getData().size()) {
            mCheckBoxAll.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //编辑按钮事件
            case R.id.tv_edit:
                isEditing = !isEditing;
                mTvEdit.setText(getString(isEditing ? R.string.edit_done : R.string.edit));
                mBtnSubmit.setText(getString(isEditing ? R.string.delete_X : R.string.go_settle_X, totalCheckedCount));
                break;
            //提交订单 & 删除选中（编辑状态）
            case R.id.btn_go_to_pay:
                submitEvent();
                break;
            case R.id.checkbox_all:
                mAdapter.checkedAll(((CheckBox) v).isChecked());
                break;
            default:
                break;
        }
    }

    private void submitEvent() {
        if (isEditing) {
            if (totalCheckedCount == 0) {
                Toast.makeText(this, "请勾选你要删除的商品", Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.removeChecked();
            }

        } else {
            if (totalCheckedCount == 0) {
                Toast.makeText(this, "你还没有选择任何商品", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        new StringBuilder().append("你选择了").append(totalCheckedCount).append("件商品")
                                .append("共计 ").append(totalPrice).append("元"),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 数据初始化尤其重要
     * 1. childItem 数据全部在 GroupItem 数据的下方，数据顺序严格按照对应关系；
     * 2. GroupItem 下的 ChildItem 数据不能为空；
     * 3. 初始化时如果不需要，所有类型的条目都可以不设置ID，GroupItem也不用设置setChilds()；
     *
     * 列表操作时数据动态的变化设置：
     * 1. 通过 CartAdapter 的 addData、setNewData；
     * 2. 单个添加各个条目可以通过对应的 add 方法；
     * 3. 单独添加一个 GroupItem ,可以把它的 ChildItem 数据放到 setChilds 中。
     * @return
     */
    private List<CartItemBean> getData() {
        ArrayList<CartItemBean> cartItemBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ShopBean shopBean = new ShopBean();
            shopBean.setShop_name("解忧杂货铺 第" + (i + 1) + "分店");
            shopBean.setItemType(CartItemBean.TYPE_GROUP);
//            shopBean.setItemId(i);
            cartItemBeans.add(shopBean);

//            ArrayList<ChildItemBean> goodsBeans = new ArrayList<>();
            for (int j = 0; j < (i + 5); j++) {
                GoodsBean goodsBean = new GoodsBean();
                goodsBean.setGoods_name("忘忧水 " + (j + 1) + " 代");
                goodsBean.setItemType(CartItemBean.TYPE_CHILD);
                goodsBean.setItemId((j + 1) * 10 + j);
                goodsBean.setGoods_price(j + 1);
                goodsBean.setGroupId(i);
//                goodsBeans.add(goodsBean);
                cartItemBeans.add(goodsBean);
            }
//            shopBean.setChilds(goodsBeans);
        }
        return cartItemBeans;
    }

}
