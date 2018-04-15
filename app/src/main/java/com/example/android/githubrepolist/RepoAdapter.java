package com.example.android.githubrepolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoAdapterViewHolder> {

    private String[] mRepoData;

    public RepoAdapter() {}

    /**
     * ViewHolder class for repos
     */
    public class RepoAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mRepoTextView;

        public RepoAdapterViewHolder(View view) {
            super(view);
            mRepoTextView = (TextView) view.findViewById(R.id.tv_repo_data);
        }
    }

    @Override
    public RepoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.repo_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RepoAdapterViewHolder(view);
    }

    /**
     * Displays the data at the specified position.
     *
     * @param repoAdapterViewHolder The ViewHolder to be updated
     * @param pos                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RepoAdapterViewHolder repoAdapterViewHolder, int pos) {
        String weatherForThisDay = mRepoData[pos];
        repoAdapterViewHolder.mRepoTextView.setText(weatherForThisDay);
    }

    /**
     * Computes the number of items to show.
     * @return The number of items to show
     */
    @Override
    public int getItemCount() {
        if (null == mRepoData) return 0;
        return mRepoData.length;
    }

    /**
     * Sets the repo data to a RepoAdapter if there is one
     * @param repoData The new repo data to be displayed.
     */
    public void setRepoData(String[] repoData) {
        mRepoData = repoData;
        notifyDataSetChanged();
    }
}