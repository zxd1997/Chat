package com.example.zxd1997.chat;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    Toast.makeText(getApplicationContext(), "Validate Failed!", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1: {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("from", username);
                    startActivity(intent);
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listMessages.add(new ListMessage("all", ""));
        recyclerView = findViewById(R.id.list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<com.example.zxd1997.chat.Message> temp = DataSupport
                .order("id desc")
                .find(com.example.zxd1997.chat.Message.class);
        for (com.example.zxd1997.chat.Message i : temp) {
            boolean flag = false;
            for (ListMessage j : listMessages) {
                if (j.getFrom().equals(i.getFrom()) || j.getFrom().equals(i.getTo())) {
                    j.setLast(i.getContent());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                String user;
                if (i.getType() == MessageAdapter.SELF_MESSAGE) {
                    user = i.getTo();
                } else {
                    user = i.getFrom();
                }
                listMessages.add(1, new ListMessage(user, i.getContent()));
            }
        }
        listAdapter = new ListAdapter(listMessages);
        recyclerView.setAdapter(listAdapter);
        IntentFilter intentFilter = new IntentFilter(MESSAGE_ACTION);
        receiver = new Receiver();
        LocalBroadcastManager.getInstance(MyApplication.getContext()).registerReceiver(receiver, intentFilter);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.design.widget.TextInputLayout textInputLayout = new android.support.design.widget.TextInputLayout(MainActivity.this);
                final TextInputEditText editText = new TextInputEditText(MainActivity.this);
                editText.setHint("Input Username");
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                textInputLayout.addView(editText);
                editText.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                editText.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(editText.getLayoutParams());
                final float scale = getApplication().getResources().getDisplayMetrics().density;
                layoutParams.setMargins((int) (15 * scale + 0.5f), 0, (int) (15 * scale + 0.5f), 0);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                AlertDialog dialog = alertDialog
                        .setMessage("Input the User you want talk to")
                        .setView(textInputLayout)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                username = editText.getText().toString();
                                String uri = "http://" + LoginActivity.ip + ":8080/LoginServlet";
                                Log.d("uri", "onCreate: " + uri);
                                final Request request = new Request.Builder()
                                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), "username=" + username + "&type=" + "Validate"))
                                        .url(uri)
                                        .build();
                                OkHttpClient client = new OkHttpClient();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Message msg = new Message();
                                        msg.what = 0;
                                        handler.sendMessage(msg);
                                    }

                                    public void onResponse(Call call, Response response) throws IOException {
                                        String res = response.body().string();
                                        Log.d("res", "onResponse: " + res);
                                        if (res.equals("false")) {
                                            Message msg = new Message();
                                            msg.what = 0;
                                            handler.sendMessage(msg);
                                        } else if (res.equals("true")) {
                                            Message msg = new Message();
                                            msg.what = 1;
                                            handler.sendMessage(msg);
                                        }
                                    }
                                });
                            }
                        }).create();
                dialog.show();
            }
        });
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
            String from = intent.getStringExtra("from");
            String to = intent.getStringExtra("to");
            String content = intent.getStringExtra("content");
            int type = intent.getIntExtra("type", MessageAdapter.MESSAGE);
            int position = 0;
            boolean flag = false;
            for (ListMessage i : listMessages) {
                if (i.getFrom().equals(from) || i.getFrom().equals(to)) {
                    if (to.equals("all") && !from.equals(MyApplication.getUsername())) {
                        i.setLast(from + ":" + content);
                    } else {
                        i.setLast(content);
                    }
                    listAdapter.notifyItemChanged(position);
                    flag = true;
                    break;
                }
                position++;
            }
            if (!flag) {
                String user;
                if (type == MessageAdapter.SELF_MESSAGE) {
                    user = to;
                } else {
                    user = from;
                }
                listMessages.add(1, new ListMessage(user, content));
                listAdapter.notifyItemInserted(1);
                listAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
