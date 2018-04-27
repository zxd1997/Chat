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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    static final String MESSAGE_ACTION = "com.example.zxd1997.chat.ACTION_MESSAGE";
    ChatUtil chatUtil;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    TextView content;
    List<Message> messages = new ArrayList<Message>();
    Receiver receiver;
    String userfrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        userfrom = intent.getStringExtra("from");
        Log.d("userfrom", "onCreate: " + userfrom);
        chatUtil = ChatUtil.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<Message> temp = DataSupport
                .where("from=? or to=?", userfrom, userfrom)
                .find(Message.class);
        for (Message i : temp) {
            if (!userfrom.equals("all")) {
                if (i.getFrom().equals("all") || i.getTo().equals("all")) {
                    continue;
                } else messages.add(i);
            } else messages.add(i);
        }
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
                    if (userfrom.equals("all")) {
                        chatUtil.send(MyApplication.getUsername() + "//:" + text);
                    } else {
                        chatUtil.send(MyApplication.getUsername() + "//:" + userfrom + "\\\\:" + text);
                    }
                    message.setText("");
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter(MESSAGE_ACTION);
        receiver = new ChatActivity.Receiver();
        LocalBroadcastManager.getInstance(MyApplication.getContext()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String from = intent.getStringExtra("from");
            String to = intent.getStringExtra("to");
            String content = intent.getStringExtra("content");
            int type = intent.getIntExtra("type", MessageAdapter.MESSAGE);
            if (userfrom.equals(from) || userfrom.equals(to)) {
                if (from.equals("all") || to.equals("all")) {
                    if (userfrom.equals("all")) {
                        messages.add(new Message(from, to, content, type));
                        messageAdapter.notifyItemInserted(messageAdapter.getItemCount());
                    } else return;
                } else {
                    messages.add(new Message(from, to, content, type));
                    messageAdapter.notifyItemInserted(messageAdapter.getItemCount());
                }
            }
        }
    }
}
