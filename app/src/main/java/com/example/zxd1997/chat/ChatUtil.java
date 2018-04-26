package com.example.zxd1997.chat;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ChatUtil {
    static String uri;
    static ChatListener chatListener = null;
    private static ChatUtil instance = null;

    private ChatUtil(String uri) {
        this.uri = uri;
    }

    public static ChatUtil getInstance() {
        return instance;
    }

    public static ChatUtil getInstance(String uri) {
        if (instance == null) {
            instance = new ChatUtil(uri);
        }
        return instance;
    }

    public void connect() {
        chatListener = new ChatListener();
        Log.d("connect", "connect: " + uri);
        Request request = new Request.Builder()
                .url(uri)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newWebSocket(request, chatListener);
        client.dispatcher().executorService().shutdown();
    }

    public void disConnect() {
        chatListener.Socket.close(1000, "close");
    }

    public void send(String message) {
        Log.d("send", "send: " + message);
        chatListener.Socket.send(message);
    }
}
