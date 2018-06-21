package com.ocnyang.cartlayout;

import android.util.Log;

import com.ocnyang.cartlayout.bean.ICartItem;
import com.ocnyang.cartlayout.bean.IChildItem;
import com.ocnyang.cartlayout.bean.IGroupItem;

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
    public static IGroupItem<IChildItem> getGroupBean(List<ICartItem> beans, long itemId) {
        for (ICartItem bean : beans) {
            if (bean.getItemType() == ICartItem.TYPE_GROUP && bean.getItemId() == itemId) {
                return (IGroupItem) bean;
            }
        }
        return null;
    }

    public static IGroupItem<IChildItem> getGroupBean(List<ICartItem> beans, int childPosition) {
        for (int i = childPosition; i >= 0; i--) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                return ((IGroupItem) beans.get(i));
            }
        }
        return null;
    }

    /**
     * 获取 group 下的 child list
     *
     * @param beans    整个数据 list
     * @param position 当前 group 的 position
     */
    public static List<ICartItem> getChildList(List<ICartItem> beans, int position) {
        List<ICartItem> childList = new ArrayList<>();
//        for (ICartItem bean : beans) {
////            //item id 不相同直接跳过
////            if (bean.getItemId() != beans.get(position).getItemId())
////                continue;
////
////            if (bean.getItemType() == ICartItem.TYPE_CHILD) {
////                childList.add((ChildItemBean) bean);
////            }
////        }
////        return childList;
        for (int i = position; i < beans.size(); i++) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                childList.add(beans.get(i));
            }
        }

        for (int i = position - 1; i >= 0; i--) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                childList.add(beans.get(i));
            }
        }

        return childList;
    }

    /**
     * 根据 itemId 获取 child 所在的 group 的 position
     *
     * @param beans  整个数据 list
     * @param itemId child 的 itemId
     * @return group 的 position
     */
    public static int getGroupPosition(List<ICartItem> beans, long itemId) {
        for (int i = 0; i < beans.size(); i++) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP
                    && beans.get(i).getItemId() == itemId) {
                return i;
            }
        }
        return 0;
    }

    public static int getGroupPosition(List<ICartItem> beans, int childPosition) {
        int groupPosition = 0;
        for (int i = childPosition; i >= 0; i--) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                groupPosition = i;
                break;
            }
        }
        Log.e("返回group下标", groupPosition + "");
        return groupPosition;
    }
}
