package com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobileappeng.threegorgeous.projrutransit.OnRecyclerviewItemClickListener;
import com.mobileappeng.threegorgeous.projrutransit.R;


import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> implements View.OnClickListener {
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private Context mContext;
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;

    public FavouriteAdapter(Context context, List<String> datats,OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;


    }

    @Override
    public void onClick(View v) {
        mOnRecyclerviewItemClickListener.onItemClickListener(v, ((int) v.getTag()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        TextView recycleview_item_bus;

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycleview_bus_name_and_stop,viewGroup, false);
        view.setOnClickListener(this);
        FavouriteAdapter.ViewHolder viewHolder = new FavouriteAdapter.ViewHolder(view);
        viewHolder.recycleview_item_bus = (TextView) view.findViewById(R.id.recycleview_item_bus);



        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final FavouriteAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.recycleview_item_bus.setText(mDatas.get(i));
        viewHolder.itemView.setTag(i);
    }


}


