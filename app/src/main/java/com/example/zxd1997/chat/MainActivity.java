package com.example.zxd1997.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    static final String MESSAGE_ACTION = "com.example.zxd1997.chat.ACTION_MESSAGE";
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    TextView content;
    String uri = "ws://echo.websocket.org";
    ChatListener chatListener = null;
    Reciver reciver;
    List<Message> messages = new ArrayList<Message>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MyApplication.getContext()).unregisterReceiver(reciver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
        final EditText message = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = message.getText().toString();
                if (!text.equals("")) {
                    chatListener.Socket.send(text);
                    message.setText("");
                    messages.add(new Message("self", text, MessageAdapter.SELF_MESSAGE));
                    messageAdapter.notifyItemInserted(messageAdapter.getItemCount() + 1);
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter(MESSAGE_ACTION);
        reciver = new Reciver();
        LocalBroadcastManager.getInstance(MyApplication.getContext()).registerReceiver(reciver, intentFilter);
        connect();
    }

    public void connect() {
        chatListener = new ChatListener();
        Request request = new Request.Builder()
                .url(uri)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newWebSocket(request, chatListener);
        client.dispatcher().executorService().shutdown();
    }

    public class Reciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d("receive", "onReceive: " + message);
            messages.add(new Message("echo", message, MessageAdapter.MESSAGE));
            messageAdapter.notifyItemInserted(messageAdapter.getItemCount() + 1);
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }
}
