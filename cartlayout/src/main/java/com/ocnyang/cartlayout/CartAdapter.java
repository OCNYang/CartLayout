package com.ocnyang.cartlayout;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ocnyang.cartlayout.bean.ICartItem;
import com.ocnyang.cartlayout.bean.IChildItem;
import com.ocnyang.cartlayout.bean.IGroupItem;
import com.ocnyang.cartlayout.listener.OnCheckChangeListener;
import com.ocnyang.cartlayout.viewholder.CartViewHolder;

import java.util.List;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/15 11:25.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public abstract class CartAdapter<VH extends CartViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<ICartItem> mDatas;
    protected Context mContext;
    private OnCheckChangeListener onCheckChangeListener;

    public CartAdapter(Context context, List<ICartItem> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnCheckChangeListener(OnCheckChangeListener l) {
        onCheckChangeListener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        VH viewHolder = null;
        switch (viewType) {
            case ICartItem.TYPE_NORMAL:
                View normalView = layoutInflater.inflate(getNormalItemLayout(), parent, false);
                viewHolder = getNormalViewHolder(normalView);
                break;
            case ICartItem.TYPE_GROUP:
                View groupView = layoutInflater.inflate(getGroupItemLayout(), parent, false);
                viewHolder = getGroupViewHolder(groupView);
                break;
            case ICartItem.TYPE_CHILD:
                View childView = layoutInflater.inflate(getChildItemLayout(), parent, false);
                viewHolder = getChildViewHolder(childView);
                break;
            default:
                viewHolder = getOtherViewHolder();
                break;
        }
        return viewHolder;
    }

    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        if (holder.mCheckBox != null) {
            holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(position,
                    mDatas.get(position).getItemType()));
            holder.mCheckBox.setChecked(mDatas.get(position).isChecked());
        }
    }

    private class OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        int mPosition, mItemType;

        public OnCheckedChangeListener(int position, int itemType) {
            mPosition = position;
            mItemType = itemType;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCheckedChanged(mDatas, mPosition, isChecked, mItemType);
            }
        }
    }

    /**
     * delete all checked item
     */
    public void removeChecked() {
        int iMax = mDatas.size() - 1;
        for (int i = iMax; i >= 0; i--) {
            if (mDatas.get(i).isChecked()) {
                mDatas.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mDatas.size());
            }
        }
    }

    /**
     * make all item's status change
     *
     * @param isCheck
     */
    public void checkedAll(boolean isCheck) {
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setChecked(isCheck);
        }
        notifyDataSetChanged();
    }

    private void addItem(int addPosition, ICartItem itemBean) {
        mDatas.add(addPosition, itemBean);

        //通知演示插入动画
        notifyItemInserted(addPosition);

        //通知数据与界面重新绑定
        notifyItemRangeChanged(addPosition, mDatas.size() - addPosition);
    }

    /**
     * add normal
     *
     * @param addPosition
     * @param itemBean
     */
    public void addNormal(int addPosition, ICartItem itemBean) {
        addItem(addPosition, itemBean);
    }

    public void addNormal(ICartItem itemBean) {
        if (itemBean.getItemType() != ICartItem.TYPE_NORMAL) {
            throw new IllegalArgumentException("The field itemType of the incoming parameter is not a TYPE_NORMAL");
        }

        int addPosition = -1;

        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                //得到要插入的position
                addPosition = i;
                break;
            }
        }
        if (addPosition == -1) {
            addPosition = mDatas.size();
        }
        addNormal(addPosition, itemBean);
    }

    /**
     * add group
     *
     * @param addPosition
     * @param groupItemBean
     */
    public void addGroup(int addPosition, IGroupItem<IChildItem> groupItemBean) {
        addGroup(addPosition, groupItemBean, false);
    }

    public void addGroup(int addPosition, IGroupItem<IChildItem> groupItemBean, boolean isStrict) {
        if (isStrict) {
            if (groupItemBean.getChilds() == null || groupItemBean.getChilds().size() == 0) {
                Log.e("CartAdapter", "This GroupItem have no one ChildItem");
            } else {
                addItem(addPosition, groupItemBean);
                for (int i = 0; i < groupItemBean.getChilds().size(); i++) {
                    addItem(addPosition + i + 1, groupItemBean.getChilds().get(i));
                }
            }
        } else {
            addItem(addPosition, groupItemBean);
            if (groupItemBean.getChilds() != null && groupItemBean.getChilds().size() != 0) {
                for (int i = 0; i < groupItemBean.getChilds().size(); i++) {
                    addItem(addPosition + i + 1, groupItemBean.getChilds().get(i));
                }
            }
        }
    }

    public void addGroup(IGroupItem<IChildItem> groupItemBean) {
        addGroup(groupItemBean, false);
    }

    public void addGroup(IGroupItem<IChildItem> groupItemBean, boolean isStrict) {
        addGroup(mDatas.size(), groupItemBean, isStrict);
    }

    /**
     * add child
     *
     * @param addPosition
     * @param childItemBean
     */
    public void addChild(int addPosition, IChildItem childItemBean) {
        addItem(addPosition, childItemBean);
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCheckedChanged(mDatas, addPosition,
                    mDatas.get(addPosition).isChecked(), ICartItem.TYPE_CHILD);
        }
    }

    public void addChild(IChildItem childItemBean) {
        if (!isHaveGroup() || mDatas.get(mDatas.size() - 1).getItemType() == ICartItem.TYPE_NORMAL) {
            Log.e("CartAdapter", "addChild is fail,have no group");
            return;
        }
        addChild(mDatas.size(), childItemBean);
    }

    /**
     * @return false:have no group.
     */
    private boolean isHaveGroup() {
        boolean isHaveGroup = false;

        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                isHaveGroup = true;
                break;
            }
        }
        return isHaveGroup;
    }

    /**
     * If you have other styles of layout, you can return through this.
     *
     * @return
     */
    protected VH getOtherViewHolder() {
        return null;
    }

    /**
     * Returns a ViewHolder(extent CartViewHolder) for different items.
     *
     * @param itemView
     * @return
     */
    protected abstract VH getNormalViewHolder(View itemView);

    protected abstract VH getGroupViewHolder(View itemView);

    protected abstract VH getChildViewHolder(View itemView);

    /**
     * item's layout's id.
     *
     * @return
     */
    protected abstract @LayoutRes
    int getChildItemLayout();

    protected abstract @LayoutRes
    int getGroupItemLayout();

    protected abstract @LayoutRes
    int getNormalItemLayout();


    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


}
