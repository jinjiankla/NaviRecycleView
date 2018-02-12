/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.recyclerview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class RecyclerViewFragment extends Fragment implements NaviTitleWidget.OnNavIndexSelect {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected NaviTitleWidget mNaviTitleWidget;
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected String[] mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mNaviTitleWidget = rootView.findViewById(R.id.home_detail_detail_navi);
        mNaviTitleWidget.setOnNavClickListener(this);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new CustomAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                applyNaviFunction(dy);
            }
        });

        return rootView;
    }

    private void applyNaviFunction(int dy) {
        RecyclePos[] pos = RecyclePos.values();
        int size = pos.length;
        int[] NaviTopBottom = getNaviTopBottom();
        for (int index = 0; index < size; index++) {
            int targetPos = pos[index].getIndex();
            int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition();
            int lastVisiblePos = mLayoutManager.findLastVisibleItemPosition();
            if (targetPos >= firstVisiblePos && targetPos <= lastVisiblePos) {
                View targetView = mLayoutManager.getChildAt(targetPos - firstVisiblePos);
                int windowsPos[] = new int[2];
                targetView.getLocationInWindow(windowsPos);
                if (dy > 0 && windowsPos[1] < NaviTopBottom[1]) {
                    if (index == 0) {
                        mNaviTitleWidget.setVisibility(View.VISIBLE);
                    }
                    mNaviTitleWidget.selectShowIndex(Math.max(0, index - 1));
                } else if (dy < 0 && windowsPos[1] > NaviTopBottom[1]) {
                    if (index == 0) {
                        mNaviTitleWidget.setVisibility(View.GONE);
                    }
                    mNaviTitleWidget.selectShowIndex(Math.max(0, index - 2));
                }
            }
        }

    }

    private int[] getNaviTopBottom() {
        int windowsPos[] = new int[2];
        mNaviTitleWidget.getLocationInWindow(windowsPos);
        windowsPos[0] = windowsPos[0];
        windowsPos[1] = windowsPos[0] + mNaviTitleWidget.getLayoutParams().height;
        return windowsPos;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    private void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is Lesson #" + i;
        }
    }

    @Override
    public void onNavItemClicked(int index, View tabView) {
        int targetPosition = 0;
        int remainedSpace = -20;
        if (index == 0) {
            targetPosition = RecyclePos.TITLE1.getIndex();
            mRecyclerView.stopScroll();
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(targetPosition, remainedSpace);
        } else if (index == 1) {
            targetPosition = RecyclePos.TITLE2.getIndex();
            mRecyclerView.stopScroll();
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(targetPosition, remainedSpace);

        } else if (index == 2) {
            targetPosition = RecyclePos.TITLE3.getIndex();
            mRecyclerView.stopScroll();
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(targetPosition, remainedSpace);

        }
        mNaviTitleWidget.selectShowIndex(index);
    }
}
