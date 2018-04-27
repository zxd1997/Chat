package com.example.zxd1997.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.litepal.LitePal;

public class ChatService extends Service {
    ChatUtil chatUtil;

    public ChatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String uri = intent.getStringExtra("uri");
        chatUtil = ChatUtil.getInstance(uri);
        chatUtil.connect();
        LitePal.getDatabase();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chatUtil.disConnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
