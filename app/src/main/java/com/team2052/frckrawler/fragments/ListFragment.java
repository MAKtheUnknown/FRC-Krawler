package com.team2052.frckrawler.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.team2052.frckrawler.R;
import com.team2052.frckrawler.listeners.ListUpdateListener;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * @author Adam
 */

@Deprecated
public abstract class ListFragment extends BaseFragment implements ListUpdateListener {
    @Bind(R.id.list_layout)
    protected ListView mListView;

    @Bind(R.id.error)
    protected View mErrorView;

    protected ListAdapter mAdapter;
    private Parcelable mListState;
    private boolean mShowAction = true;

    protected void setShowAddAction(boolean state) {
        mShowAction = state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.list_view, null);
        ButterKnife.bind(this, v);
        preUpdateList();
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mListView.onRestoreInstanceState(mListState);
        } else {
            updateList();
        }
        return v;
    }

    public void preUpdateList() {

    }

    @Override
    public void onPause() {
        if (mListView != null) {
            if (mAdapter == null) {
                mAdapter = mListView.getAdapter();
            }
            mListState = mListView.onSaveInstanceState();
        }
        super.onPause();
    }

    protected void showError(boolean shown) {
        if (shown) {
            mListView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
        }
    }
}
