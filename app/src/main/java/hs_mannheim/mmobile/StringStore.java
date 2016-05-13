package hs_mannheim.mmobile;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class StringStore {

    private static final String TAG = "TAG";

    private static final int PORT = 8080;
    private static final String IP = "141.19.142.50";

    public String read(String key) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(String.format("http://%s:%d/string-store/get?key=%s", IP, PORT, key));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = readStream(in);
            in.close();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            url = new URL(String.format("http://%s:%d/string-store/set", IP, PORT));
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
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
