package com.example.android.githubrepolist.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Used to communicate with the GitHub server.
 */
public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String GITHUB_BASE_URL = "https://api.github.com/repositories";

    /**
     * Builds the URL used to communicate with the GitHub server
     * @return The URL we will use to query the server.
     */
    public static URL buildUrl(int since) {
        Uri builtUri;
        if (since < 0) {
            builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                    .build();
        } else {
            builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().appendQueryParameter("since", Integer.toString(since))
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Used to get the result from HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}