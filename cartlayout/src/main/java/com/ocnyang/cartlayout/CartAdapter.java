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
import android.widget.CheckBox;

import com.ocnyang.cartlayout.bean.ICartItem;
import com.ocnyang.cartlayout.bean.IChildItem;
import com.ocnyang.cartlayout.bean.IGroupItem;
import com.ocnyang.cartlayout.listener.OnCheckChangeListener;
import com.ocnyang.cartlayout.listener.OnChildCollapsingChangeListener;
import com.ocnyang.cartlayout.viewholder.CartViewHolder;

import java.util.List;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time: 2018/6/15 11:25.
 *    *     *   *         *   * *       Email address: ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site: www.ocnyang.com
 *******************************************************************/

public abstract class CartAdapter<VH extends CartViewHolder> extends RecyclerView.Adapter<VH> implements OnChildCollapsingChangeListener {
    public static final int PAYLOAD_CHECKBOX = 0x000001;
    public static final int PAYLOAD_COLLAPSING = 0x000002;

    protected List<ICartItem> mData;
    protected Context mContext;
    protected OnCheckChangeListener onCheckChangeListener;
    protected boolean isCanCollapsing;

    public CartAdapter(Context context, List<ICartItem> data) {
        this(context, data, false);
    }

    public CartAdapter(Context context, List<ICartItem> data, boolean canCollapsing) {
        mContext = context;
        mData = data;
        isCanCollapsing = canCollapsing;
    }

    public boolean isCanCollapsing() {
        return isCanCollapsing;
    }

    public void setCanCollapsing(boolean canCollapsing) {
        isCanCollapsing = canCollapsing;
        if (mData != null) {
            notifyDataSetChanged();
        }
    }

    public void setOnCheckChangeListener(OnCheckChangeListener l) {
        onCheckChangeListener = l;
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
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
        ICartItem cartItemBean = mData.get(position);
        holder.bindData(cartItemBean);
        if (holder.mCheckBox != null) {
            holder.mCheckBox.setOnClickListener(new OnCheckBoxClickListener(position,
                    cartItemBean.getItemType()));
            if (holder.mCheckBox.isChecked() != cartItemBean.isChecked()) {
                holder.mCheckBox.setChecked(cartItemBean.isChecked());
            }
        }
        if (isCanCollapsing) {
            if (ICartItem.TYPE_CHILD == cartItemBean.getItemType()) {
                changeItemViewShow(holder.itemView, cartItemBean.isCollapsing());
            }

            if (ICartItem.TYPE_GROUP == cartItemBean.getItemType()) {
                bindItemViewCollapsingListener(holder.itemView, position);
            }
        } else {
            if (ICartItem.TYPE_CHILD == cartItemBean.getItemType()) {
                if (holder.itemView.getVisibility() != View.VISIBLE)
                    changeItemViewShow(holder.itemView, false);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            this.onBindViewHolder(holder, position);
        } else {
            if (PAYLOAD_CHECKBOX == ((int) payloads.get(0))) {
                if (holder.mCheckBox != null) {
                    if ((holder.mCheckBox.isChecked() != mData.get(position).isChecked())) {
                        holder.mCheckBox.setChecked(mData.get(position).isChecked());
                    }
                }
            } else if (PAYLOAD_COLLAPSING == ((int) payloads.get(0))) {
                if (ICartItem.TYPE_CHILD == mData.get(position).getItemType()) {
                    changeItemViewShow(holder.itemView, mData.get(position).isCollapsing());
                }
            }
        }
    }

    /**
     * 给 itemView 设置点击展开折叠事件
     * 如果想把事件的触发设置到其他控件或者其他方式触发，请重写下面方法
     *
     * @param itemView 事件源
     * @param position 事件源的下标
     */
    protected void bindItemViewCollapsingListener(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGroupItemClickCollapsibleChild(mData, position);
            }
        });
    }


    /**
     * 折叠 itemView
     *
     * @param itemView childItem
     * @param hide     是否隐藏
     */
    private void changeItemViewShow(View itemView, boolean hide) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (hide) {
            itemView.setVisibility(View.GONE);
            layoutParams.height = 0;
            layoutParams.width = 0;
        } else {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }
        itemView.setLayoutParams(layoutParams);
    }


    private class OnCheckBoxClickListener implements View.OnClickListener {
        int mPosition, mItemType;

        private OnCheckBoxClickListener(int position, int itemType) {
            mPosition = position;
            mItemType = itemType;
        }

        @Override
        public void onClick(View v) {
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCheckedChanged(mData, mPosition, ((CheckBox) v).isChecked(), mItemType);
                onCheckChangeListener.onCalculateChanged(mData.get(mPosition));
            }
        }
    }

    /**
     * delete all checked item
     */
    public void removeChecked() {
        int iMax = mData.size() - 1;
        for (int i = iMax; i >= 0; i--) {
            if (mData.get(i).isChecked()) {
                mData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mData.size());
            }
        }
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    /**
     * 移除其中一条 childItem
     *
     * @param position
     */
    public void removeChild(int position) {
        boolean isLastOne = false;
        if ((ICartItem.TYPE_GROUP == mData.get(position - 1).getItemType()) &&
                (((position + 1) == mData.size()) ||
                        (ICartItem.TYPE_GROUP == mData.get(position + 1).getItemType()))) {
            isLastOne = true;
        }
        if (mData.get(position).isChecked()) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
            if (isLastOne) {
                mData.remove(position - 1);
                notifyItemRemoved(position - 1);
                notifyItemRangeChanged(position - 1, mData.size());
            }
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCalculateChanged(null);
            }
        } else {
            if (!isLastOne) {
                //当删除一条未被勾选的条目时，去让组条目做出相应的变化
                if (onCheckChangeListener != null) {
                    onCheckChangeListener.onCheckedChanged(mData, position, true, mData.get(position).getItemType());
                }
            }
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
            if (isLastOne) {
                mData.remove(position - 1);
                notifyItemRemoved(position - 1);
                notifyItemRangeChanged(position - 1, mData.size());
            }
        }
    }

    /**
     * make all item's status change
     *
     * @param isCheck
     */
    public void checkedAll(boolean isCheck) {
        //是否有条目状态被改变的标志。如果没有就不再执行刷新和条目统计。
        boolean noOneChange = true;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked() != isCheck) {
                mData.get(i).setChecked(isCheck);
                noOneChange = false;
            }
        }
        if (!noOneChange) {
            notifyDataSetChanged();
            if (onCheckChangeListener != null) {
                onCheckChangeListener.onCalculateChanged(null);
            }
        }
    }

    public void setNewData(List<ICartItem> datas) {
        mData.clear();
        addData(datas);
    }

    public void addData(List<ICartItem> datas) {
        mData.addAll(datas);
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    private void addItem(int addPosition, ICartItem itemBean) {
        mData.add(addPosition, itemBean);

        //通知演示插入动画
        notifyItemInserted(addPosition);

        //通知数据与界面重新绑定
        notifyItemRangeChanged(addPosition, mData.size() - addPosition);
    }

    /**
     * add normal
     *
     * @param addPosition
     * @param itemBean
     */
    public void addNormal(int addPosition, ICartItem itemBean) {
        addItem(addPosition, itemBean);
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(itemBean);
        }
    }

    public void addNormal(ICartItem itemBean) {
        if (itemBean.getItemType() != ICartItem.TYPE_NORMAL) {
            throw new IllegalArgumentException("The field itemType of the incoming parameter is not a TYPE_NORMAL");
        }

        int addPosition = -1;

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                //得到要插入的position
                addPosition = i;
                break;
            }
        }
        if (addPosition == -1) {
            addPosition = mData.size();
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

    public void addGroup(int addPosition, IGroupItem<IChildItem> groupItemBean,
                         boolean isStrict) {
        if (isStrict) {
            if (groupItemBean.getChilds() == null || groupItemBean.getChilds().size() == 0) {
                Log.i("CartAdapter", "This GroupItem have no one ChildItem");
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
        if (onCheckChangeListener != null) {
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    public void addGroup(IGroupItem<IChildItem> groupItemBean) {
        addGroup(groupItemBean, false);
    }

    public void addGroup(IGroupItem<IChildItem> groupItemBean, boolean isStrict) {
        addGroup(mData.size(), groupItemBean, isStrict);
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
            onCheckChangeListener.onCheckedChanged(mData, addPosition,
                    mData.get(addPosition).isChecked(), ICartItem.TYPE_CHILD);
            onCheckChangeListener.onCalculateChanged(null);
        }
    }

    public void addChild(IChildItem childItemBean) {
        if (!isHaveGroup() || mData.get(mData.size() - 1).getItemType() == ICartItem.TYPE_NORMAL) {
            Log.i("CartAdapter", "addChild is fail,have no group");
            return;
        }
        addChild(mData.size(), childItemBean);
    }

    /**
     * @return false:have no group.
     */
    private boolean isHaveGroup() {
        boolean isHaveGroup = false;

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                isHaveGroup = true;
                break;
            }
        }
        return isHaveGroup;
    }

    @Override
    public void onGroupItemClickCollapsibleChild(List<ICartItem> beans, int position) {
        boolean collapsing = !beans.get(position).isCollapsing();
        beans.get(position).setCollapsing(collapsing);
        for (int i = (position + 1); i < beans.size(); i++) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                if (beans.get(i).isCollapsing() != collapsing) {
                    beans.get(i).setCollapsing(collapsing);
//                    notifyItemChanged(i, CartAdapter.PAYLOAD_COLLAPSING);
                    notifyItemChanged(i);
                }
            }
        }
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
        return mData.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 获取 Adapter 的真实数据
     *
     * @return
     */
    public List<ICartItem> getData() {
        return mData;
    }

}
