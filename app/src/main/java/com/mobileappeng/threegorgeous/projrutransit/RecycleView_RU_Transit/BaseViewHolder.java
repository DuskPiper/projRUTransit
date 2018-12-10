package com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorLong;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {


        private SparseArray<View> mItemViews;

        private View mView;


        public BaseViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mItemViews = new SparseArray<>();
        }

        public View getView(@IdRes int viewId) {
            View view = mItemViews.get(viewId);
            if (view == null) {
                view = mView.findViewById(viewId);
                mItemViews.put(viewId, view);
            }
            return view;
        }


        public BaseViewHolder setText(@IdRes int viewId, @StringRes int resId) {
            TextView textView = (TextView) getView(viewId);
            textView.setText(resId);
            return this;
        }


        public BaseViewHolder setText(@IdRes int viewId, String text) {
            TextView textView = (TextView) getView(viewId);
            if (text != null) {
                textView.setText(text);
            } else {
                textView.setText("");
            }
            return this;
        }


        public BaseViewHolder setText(@IdRes int viewId, SpannableStringBuilder text) {
            TextView textView = (TextView) getView(viewId);
            if (text != null) {
                textView.setText(text);
            } else {
                textView.setText("");
            }
            return this;
        }


        public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
            ImageView imageView = (ImageView) getView(viewId);
            imageView.setImageResource(resId);
            return this;
        }


        public BaseViewHolder setImageBitmap(@IdRes int viewId, @NonNull Bitmap bitmap) {
            ImageView imageView = (ImageView) getView(viewId);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            return this;
        }


        public BaseViewHolder setImageDrawable(@IdRes int viewId, @NonNull Drawable drawable) {
            ImageView imageView = (ImageView) getView(viewId);
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
            }
            return this;
        }


        public BaseViewHolder setBackgroundColor(@IdRes int viewId, @ColorLong int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }


        public BaseViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int backgroundRes) {
            View view = getView(viewId);
            view.setBackgroundResource(backgroundRes);
            return this;
        }



        public BaseViewHolder setBackgroundDrawable(@IdRes int viewId, Drawable drawable) {
            View view = getView(viewId);
            if (drawable != null) {
                view.setBackground(drawable);
            }
            return this;
        }


        public BaseViewHolder setTextColor(@IdRes int viewId, @ColorLong int textColor) {
            TextView textView = (TextView) getView(viewId);
            textView.setTextColor(textColor);
            return this;
        }

        public BaseViewHolder setAlpha(@IdRes int viewId, @FloatRange(from = 0.0, to = 1.0) float
                value) {
            getView(viewId).setAlpha(value);
            return this;
        }


        public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
            View view = getView(viewId);
            view.setVisibility(visible ? view.VISIBLE : View.GONE);
            return this;
        }


        public BaseViewHolder setTag(@IdRes int viewId, Object tag) {
            View view = getView(viewId);
            view.setTag(tag);
            return this;
        }


        public BaseViewHolder setTag(@IdRes int viewId, int key, Object tag) {
            View view = getView(viewId);
            view.setTag(key, tag);
            return this;
        }


        public BaseViewHolder setChecked(@IdRes int viewId, boolean checked) {
            Checkable checkable = (Checkable) getView(viewId);
            checkable.setChecked(checked);
            return this;
        }

        public BaseViewHolder setOnClickListener(@IdRes int viewId, @NonNull View.OnClickListener
                listener) {
            View view = getView(viewId);
            if (listener != null) {
                view.setOnClickListener(listener);
            }
            return this;
        }


        public BaseViewHolder setOnTouchListener(@IdRes int viewId, @NonNull View.OnTouchListener
                listener) {
            View view = getView(viewId);
            if (listener != null) {
                view.setOnTouchListener(listener);
            }
            return this;
        }


        public BaseViewHolder setOnLongClickListener(@IdRes int viewId, @NonNull View
                .OnLongClickListener
                listener) {
            View view = getView(viewId);
            if (listener != null) {
                view.setOnLongClickListener(listener);
            }
            return this;
        }
    }