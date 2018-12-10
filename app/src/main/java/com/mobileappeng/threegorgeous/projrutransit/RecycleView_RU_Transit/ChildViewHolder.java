package com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mobileappeng.threegorgeous.projrutransit.R;


public class ChildViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;
    private TextView childLeftText;


    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final DataBean dataBean, final int pos){

        childLeftText = (TextView) view.findViewById(R.id.child_left_text);

        childLeftText.setText(dataBean.getChildLeftTxt());

    }
}
