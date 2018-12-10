package com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileappeng.threegorgeous.projrutransit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mData;
    private List<String> timeList;
    private List<String> temperatureList;
    private Context mContext;
    public GalleryAdapter(Context context, List<String> datats, List<String> data,List<String> wendu) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = datats;
        timeList = data;
        temperatureList = wendu;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        ImageView weatherItem;
        TextView weatherTime;
        TextView weatherTemperature;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_today_summary_hour_list_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.weatherItem = (ImageView) view.findViewById(R.id.weather_item);
        viewHolder.weatherTime = (TextView) view.findViewById(R.id.weather_time);
        viewHolder.weatherTemperature = (TextView) view.findViewById(R.id.weather_wendu);
        

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if(mData.get(i).equals(""))
        {
            Picasso.with(mContext).load("http://openweathermap.org/img/w/02d.png").into(viewHolder.weatherItem);
        }
        else
            {
                Picasso.with(mContext).load(mData.get(i)).into(viewHolder.weatherItem);
            }

        viewHolder.weatherTime.setText(timeList.get(i));
        viewHolder.weatherTemperature.setText(temperatureList.get(i));
    }



}