package com.example.zxd1997.chat;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatListener extends WebSocketListener {
    WebSocket Socket;
    String username;
    LocalBroadcastManager localBroadcastManager;

    public ChatListener() {
        this.username = MyApplication.getUsername();
    }
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        Socket = webSocket;
        Socket.send("Connect//"+username);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.d("message", "onMessage: " + text);
        broadcast(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }

    public void broadcast(String message) {
        Intent intent = new Intent(MainActivity.MESSAGE_ACTION);
        intent.putExtra("message", message);
        localBroadcastManager.sendBroadcast(intent);
    }
}
