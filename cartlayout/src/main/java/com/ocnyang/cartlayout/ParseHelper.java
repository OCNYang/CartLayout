package com.ocnyang.cartlayout;

import java.util.ArrayList;
import java.util.List;

public class ParseHelper {
    /**
     * 取出 list 中的 groupBean
     *
     * @param beans
     * @param itemId
     * @return
     */
    public static GroupItemBean getGroupBean(List<CartItemBean> beans, long itemId) {
        for (CartItemBean bean : beans) {
            if (bean.getItemType() == CartItemBean.TYPE_GROUP && bean.getItemId() == itemId){
                return (GroupItemBean) bean;}
        }
        return null;
    }

    /**
     * 获取 group 下的 child list
     *
     * @param beans    整个数据 list
     * @param position 当前 group 的 position
     */
    public static List<ChildItemBean> getChildList(List<CartItemBean> beans, int position) {
        List<ChildItemBean> childList = new ArrayList<>();
        for (CartItemBean bean : beans) {
            //item id 不相同直接跳过
            if (bean.getItemId() != beans.get(position).getItemId())
                continue;

            if (bean.getItemType() == CartItemBean.TYPE_CHILD) {
                childList.add((ChildItemBean) bean);
            }
        }
        return childList;
    }

    /**
     * 根据 itemId 获取 child 所在的 group 的 position
     *
     * @param beans   整个数据 list
     * @param itemId child 的 itemId
     * @return group 的 position
     */
    public static int getGroupPosition(List<CartItemBean> beans, long itemId) {
        for (int i = 0; i < beans.size(); i++) {
            if (beans.get(i).getItemType() == CartItemBean.TYPE_GROUP
                    && beans.get(i).getItemId() == itemId){
                return i;}
        }
        return 0;
    }
}
