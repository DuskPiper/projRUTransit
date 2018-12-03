package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Integer> mDatas;
    private List<String> timelist;
    private List<String> wendulist;
    public GalleryAdapter(Context context, List<Integer> datats, List<String> data,List<String> wendu) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        timelist=data;
        wendulist=wendu;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView weather_item;
        TextView weather_time;
        TextView weather_wendu;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_today_summary_hour_list_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.weather_item = (ImageView) view.findViewById(R.id.weather_item);
        viewHolder.weather_time=(TextView) view.findViewById(R.id.weather_time);
        viewHolder.weather_wendu=(TextView) view.findViewById(R.id.weather_wendu);
        

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.weather_item.setImageResource(mDatas.get(i));
        viewHolder.weather_time.setText(timelist.get(i));
        viewHolder.weather_wendu.setText(wendulist.get(i));
    }



}