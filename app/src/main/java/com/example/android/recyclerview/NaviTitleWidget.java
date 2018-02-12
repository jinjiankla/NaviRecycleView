package com.example.android.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jinjian
 * @time 2018/1/25  下午6:39
 * @mail jinjian02@corp.netease.com
 * @describe NaviTitleWidget
 */
public class NaviTitleWidget extends LinearLayout {

    private final String[] DEFAULT_TEXT = new String[]{
            "介绍", "老师", "大纲"
    };

    private View[] mActionViews = new View[DEFAULT_TEXT.length];
    private View[] mImageViews = new View[DEFAULT_TEXT.length];
    private TextView[] mTextViews = new TextView[DEFAULT_TEXT.length];

    private OnNavIndexSelect mNavClickListener;
    private int mSelectIndex;


    public NaviTitleWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < DEFAULT_TEXT.length; i++) {
            final int index = i;
            final View contentView = layoutInflater.inflate(R.layout.navi_tab_item, this, false);
            mActionViews[i] = contentView;
            mActionViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNavClickListener != null) {
                        mNavClickListener.onNavItemClicked(index, mActionViews[index]);
                    }
                    mSelectIndex = index;
                    refreshBarStatus();
                }
            });
            mImageViews[i] = contentView.findViewById(R.id.navi_tab_img);
            mImageViews[i].setVisibility(GONE);
            mTextViews[i] = (TextView) contentView.findViewById(R.id.navi_tab_txt);
            mTextViews[i].setText(DEFAULT_TEXT[i]);
            addView(contentView);
        }
        refreshBarStatus();
    }

    private void refreshBarStatus() {
        for (int i = 0; i < DEFAULT_TEXT.length; i++) {
            if (i == mSelectIndex) {
                mImageViews[i].setVisibility(VISIBLE);
            } else {
                mImageViews[i].setVisibility(GONE);
            }
        }
    }

    public void selectShowIndex(int index) {
        this.mSelectIndex = index;
        refreshBarStatus();
    }

    public void setOnNavClickListener(OnNavIndexSelect mNavClickListener) {
        this.mNavClickListener = mNavClickListener;
    }

    public interface OnNavIndexSelect {
        void onNavItemClicked(int index, View tabView);
    }
}