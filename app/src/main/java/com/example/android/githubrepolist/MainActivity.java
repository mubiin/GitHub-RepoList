package com.example.android.githubrepolist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.githubrepolist.utilities.NetworkUtils;

import org.json.JSONArray;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RepoAdapter mRepoAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private int since = -1;
    private String[] lastItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_repo);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRepoAdapter = new RepoAdapter();

        mRecyclerView.setAdapter(mRepoAdapter);

        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            final LinearLayoutManager mLayoutManager = layoutManager;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    int i = 2 + 1;
                    loadRepoData();
                }
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadRepoData();
    }

    /**
     * Calls showRepoDataView that makes the view visible.
     * Calls execute on the async task.
     */
    private void loadRepoData() {
        new FetchRepoTask().execute("");
    }

    /**
     * If there's an error, show error message.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchRepoTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            URL repoRequestUrl = NetworkUtils.buildUrl(since);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(repoRequestUrl);

                JSONArray rootObject = new JSONArray(jsonResponse);
                since = rootObject.getJSONObject(rootObject.length() - 1).getInt("id");

                String[] parsedData;
                if (lastItemsList == null) {
                    parsedData = new String[rootObject.length()];
                    for(int i = 0; i < rootObject.length(); i++) {
                        parsedData[i] = rootObject.getJSONObject(i).getString("name");
                    }

                    lastItemsList = parsedData;
                } else {
                    parsedData = new String[rootObject.length() + lastItemsList.length];
                    for(int i = 0; i < lastItemsList.length; i++) {
                        parsedData[i] = lastItemsList[i];
                    }

                    for(int i = 0; i < rootObject.length(); i++) {
                        parsedData[i+lastItemsList.length] = rootObject.getJSONObject(i).getString("name");
                    }

                }
                return parsedData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] repoData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (repoData != null) {
                mRepoAdapter.setRepoData(repoData);
            } else {
                showErrorMessage();
            }
        }
    }
}