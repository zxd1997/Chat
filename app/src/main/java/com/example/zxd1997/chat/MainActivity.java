package com.example.zxd1997.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String MESSAGE_ACTION = "com.example.zxd1997.chat.ACTION_MESSAGE";
    RecyclerView recyclerView;
    //    MessageAdapter messageAdapter;
//    TextView content;
    String uri = "ws://"+LoginActivity.ip+":8080/chat";
    ChatUtil chatUtil = null;
    Receiver receiver;
    ListAdapter listAdapter;
    //    List<Message> messages = new ArrayList<Message>();
    List<ListMessage> listMessages = new ArrayList<ListMessage>();
    String username;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MyApplication.getContext()).unregisterReceiver(receiver);
        stopService(new Intent(MainActivity.this, ChatService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("username", "onCreate: " + username);
//        recyclerView = findViewById(R.id.recyclerView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        messageAdapter = new MessageAdapter(messages);
//        recyclerView.setAdapter(messageAdapter);
//        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//            }
//        });
//        final EditText message = findViewById(R.id.editText);
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String text = message.getText().toString();
//                if (!text.equals("")) {
//                    chatUtil.send(text);
//                    message.setText("");
//                }
//            }
//        });
        listMessages.add(new ListMessage("all", ""));
        listMessages.add(new ListMessage("1", "2"));
        recyclerView = findViewById(R.id.list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(listMessages);
        recyclerView.setAdapter(listAdapter);
        IntentFilter intentFilter = new IntentFilter(MESSAGE_ACTION);
        receiver = new Receiver();
        LocalBroadcastManager.getInstance(MyApplication.getContext()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MyApplication.setUsername("");
            this.finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            String message = intent.getStringExtra("message");
//            Log.d("receive", "onReceive: " + message);
//            int t;
//            if (message.indexOf("//:") != -1) {
//                if (message.substring(0, message.indexOf("//:")).equals(username)){
//                    t=MessageAdapter.SELF_MESSAGE;
//                }else {
//                    t=MessageAdapter.MESSAGE;
//                }
//                messages.add(new Message(message.substring(0, message.indexOf("//:")), message.substring(message.indexOf("//:") + 3), t));
//                messageAdapter.notifyItemInserted(messageAdapter.getItemCount() + 1);
//                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
