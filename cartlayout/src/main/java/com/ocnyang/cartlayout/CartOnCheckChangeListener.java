package com.ocnyang.cartlayout;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class CartOnCheckChangeListener implements OnCheckChangeListener, OnItemChangeListener {

    RecyclerView recyclerView;
    CartAdapter cartAdapter;

    public CartOnCheckChangeListener(RecyclerView recyclerView, CartAdapter adapter) {
        this.recyclerView = recyclerView;
        this.cartAdapter = adapter;
    }

    @Override
    public void onCheckedChanged(List<CartItemBean> beans, int position, boolean isChecked, int itemType) {
        switch (itemType) {
            case CartItemBean.TYPE_NORMAL:
                normalCheckChange(beans, position, isChecked);
                break;
            case CartItemBean.TYPE_GROUP:
                groupCheckChange(beans, position, isChecked);
                break;
            case CartItemBean.TYPE_CHILD:
                childCheckChange(beans, position, isChecked);
                break;
            default:
                break;
        }
    }

    /**
     * normal 选中状态变化
     *
     * @param beans     数据
     * @param position  group position
     * @param isChecked 选中状态
     */
    @Override
    public void normalCheckChange(List<CartItemBean> beans, int position, boolean isChecked) {
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.isComputingLayout()) {// 避免滑动时刷新数据
            beans.get(position).setChecked(isChecked);
        }
    }

    /**
     * group 选中状态变化
     *
     * @param beans     数据
     * @param position  group position
     * @param isChecked 选中状态
     */
    @Override
    public void groupCheckChange(List<CartItemBean> beans, int position, boolean isChecked) {
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.isComputingLayout()) {// 避免滑动时刷新数据
            beans.get(position).setChecked(isChecked);
            setChildCheck(beans, position, isChecked);
        }
    }

    /**
     * child 选中状态变化
     *
     * @param beans     数据
     * @param position  child position
     * @param isChecked 选中状态
     */
    @Override
    public void childCheckChange(List<CartItemBean> beans, int position, boolean isChecked) {
        long itemId = beans.get(position).getItemId();

        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.isComputingLayout()) {// 避免滑动时刷新数据

            beans.get(position).setChecked(isChecked);

            GroupItemBean groupBean = ParseHelper.getGroupBean(beans, itemId);

            List<ChildItemBean> childList = ParseHelper.getChildList(beans, position);
            for (int i = 0; i < childList.size(); i++) {
                if (!childList.get(i).isChecked()) {// 只要有一个 child 没有选中，group 就不是选中
                    if (groupBean.isChecked() && !isChecked) {//group 为选中状态
                        setGroupCheck(beans, itemId, false);
                        cartAdapter.notifyItemChanged(ParseHelper.getGroupPosition(beans,
                                itemId));
                    }
                    return;
                }
            }

            //child 全部选中，group 设置选中
            setGroupCheck(beans, itemId, true);
            cartAdapter.notifyItemChanged(ParseHelper.getGroupPosition(beans, itemId));
        }
    }

    /**
     * 一次设置 group 下所有 child item 选中状态
     *
     * @param beans     整个数据 list
     * @param position  group position
     * @param isChecked 设置选中状态
     */
    private void setChildCheck(List<CartItemBean> beans, int position, boolean isChecked) {
        for (int i = 0; i < beans.size(); i++) {
            //item id 不相同直接跳过
            if (beans.get(i).getItemId() != beans.get(position).getItemId())
                continue;

            if (beans.get(i).getItemType() == CartItemBean.TYPE_CHILD) {// 让 group 下的所有 child 选中
                if (beans.get(i).isChecked() != isChecked) {
                    beans.get(i).setChecked(isChecked);
                    cartAdapter.notifyItemChanged(i);
                }
            }
        }
    }

    /**
     * 设置 group item 选中状态
     *
     * @param beans     整个数据 list
     * @param itemId    child 的 itemId
     * @param isChecked 设置选中状态
     */
    private void setGroupCheck(List<CartItemBean> beans, long itemId, boolean isChecked) {
        for (CartItemBean bean : beans) {
            if (bean.getItemType() == CartItemBean.TYPE_GROUP
                    && bean.getItemId() == itemId) {
                bean.setChecked(isChecked);
            }
        }
    }

    public abstract void onChildCheckChanged(CartItemBean cartItemBean, boolean isChecked);
}