package hs_mannheim.mmobile.Model;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class StringStore {

    private static final String TAG = "TAG";

    private final String mIp;
    private final int mPort;

    public StringStore(String IP, int port) {
        mIp = IP;
        mPort = port;
    }

    public String read(String key) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(String.format(Locale.GERMANY, "http://%s:%d/string-store/get?key=%s", mIp, mPort, key));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "text/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = readStream(in);
            in.close();
            return result;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return "";
    }

    public void write(String key, String value) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(String.format(Locale.GERMANY, "http://%s:%d/string-store/set", mIp, mPort));
            String data = String.format("key=%s&value=%s", key, value);
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setDoOutput(true);

            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);

            Log.d(TAG, "Posting " + data);

            urlConnection.getOutputStream().write(postData);
            urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String readStream(InputStream in) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}