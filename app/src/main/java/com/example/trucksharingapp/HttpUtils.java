package com.example.trucksharingapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    public static void getRequest(String url, Map<String, String> params, String encode, OnResponseListener listener) {
        StringBuffer sb = new StringBuffer(url);
        sb.append("?");
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            if (listener != null) {
                try {
                    URL path = new URL(sb.toString());
                    if (path != null) {
                        HttpURLConnection con = (HttpURLConnection) path.openConnection();
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(3000);
                        con.setDoOutput(true);
                        con.setDoInput(true);
                        OutputStream os = con.getOutputStream();
                        os.write(sb.toString().getBytes(encode));
                        os.close();
                        if (con.getResponseCode() == 200) {
                            onSuccess(encode, listener, con);
                        }
                    }
                } catch (Exception error) {
                    error.printStackTrace();
                    onError(listener, error);
                }
            }
        }
    }

    private static void onError(OnResponseListener listener, Exception onError) {
        listener.onError(onError.toString());
    }

    private static void onSuccess(String encode, OnResponseListener listener, HttpURLConnection con) throws IOException {
        InputStream inputStream = con.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] bytes = new byte[1024];
        if (inputStream != null) {
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            String str = new String(baos.toByteArray(), encode);
            listener.onSuccess(str);
        }
    }
}