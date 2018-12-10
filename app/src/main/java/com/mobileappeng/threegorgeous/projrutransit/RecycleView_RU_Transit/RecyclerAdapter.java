package com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobileappeng.threegorgeous.projrutransit.R;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
 
    private Context context;
    private List<DataBean> dataBeanList;
    private LayoutInflater mInflater;
    private OnScrollListener mOnScrollListener;
 
    public RecyclerAdapter(Context context, List<DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.mInflater = LayoutInflater.from(context);
    }
 
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case DataBean.PARENT_ITEM:
                view = mInflater.inflate(R.layout.recycleview_item_parent, parent, false);
                return new ParentViewHolder(context, view);
            case DataBean.CHILD_ITEM:
                view = mInflater.inflate(R.layout.recycleview_item_child, parent, false);
                return new ChildViewHolder(context, view);
            default:
                view = mInflater.inflate(R.layout.recycleview_item_parent, parent, false);
                return new ParentViewHolder(context, view);
        }
    }
 

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case DataBean.PARENT_ITEM:
                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                parentViewHolder.bindView(dataBeanList.get(position), position, itemClickListener);
                break;
            case DataBean.CHILD_ITEM:
                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                childViewHolder.bindView(dataBeanList.get(position), position);
                break;
        }
    }
 
    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }
 
    @Override
    public int getItemViewType(int position) {
        return dataBeanList.get(position).getType();
    }
 
    private ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onExpandChildren(DataBean bean) {
            int position = getCurrentPosition(bean.getID());
            DataBean children = getChildDataBean(bean);
            if (children == null) {
                return;
            }
            add(children, position + 1);
            if (position == dataBeanList.size() - 2 && mOnScrollListener != null) {
                mOnScrollListener.scrollTo(position + 1);
            }
        }
 
        @Override
        public void onHideChildren(DataBean bean) {
            int position = getCurrentPosition(bean.getID());
            DataBean children = bean.getChildBean();
            if (children == null) {
                return;
            }
            remove(position + 1);
            if (mOnScrollListener != null) {
                mOnScrollListener.scrollTo(position);
            }
        }
    };
 

    public void add(DataBean bean, int position) {
        dataBeanList.add(position, bean);
        notifyItemInserted(position);
    }
 

    protected void remove(int position) {
        dataBeanList.remove(position);
        notifyItemRemoved(position);
    }
 

    protected int getCurrentPosition(String uuid) {
        for (int i = 0; i < dataBeanList.size(); i++) {
            if (uuid.equalsIgnoreCase(dataBeanList.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    private DataBean getChildDataBean(DataBean bean){
        DataBean child = new DataBean();
        child.setType(1);
        child.setParentLeftTxt(bean.getParentLeftTxt());
        child.setChildLeftTxt(bean.getChildLeftTxt());
        return child;
    }
 

    public interface OnScrollListener{
        void scrollTo(int pos);
    }
 
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.mOnScrollListener = onScrollListener;
    }






}
