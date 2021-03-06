package com.example.zxd1997.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    final static int LOGIN_SUCCESS = 1;
    final static int LOGIN_FAILED = 0;
    final static int REGISTER_FAILED = 2;
    final static int REGISTER_SUCCESS = 3;
    final static int ALREADY_EXISTS = 4;
    EditText username;
    SimpleDraweeView bigheader;
    static String ip = "";
    String tmp;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_FAILED: {
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                    break;
                }
                case LOGIN_SUCCESS: {
                    Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    Intent intent1 = new Intent(LoginActivity.this, ChatService.class);
                    String uri = "ws://123.207.165.210:8080/Chat_war_exploded/chat";
                    intent1.putExtra("uri", uri);
                    startService(intent1);
                    MyApplication.setUsername(tmp);
                    finish();
                    break;
                }
                case REGISTER_SUCCESS: {
                    Toast.makeText(getApplicationContext(), "Register Success!Loged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    Intent intent1 = new Intent(LoginActivity.this, ChatService.class);
                    String uri = "ws://123.207.165.210:8080/Chat_war_exploded/chat";
                    intent1.putExtra("uri", uri);
                    startService(intent1);
                    MyApplication.setUsername(tmp);
                    finish();
                    break;
                }
                case REGISTER_FAILED: {
                    Toast.makeText(getApplicationContext(), "Register Failed!", Toast.LENGTH_SHORT).show();
                    break;
                }
                case ALREADY_EXISTS: {
                    Toast.makeText(getApplicationContext(), "Account Already exists!", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Fresco.initialize(this);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        bigheader = findViewById(R.id.bigHeader);
        final Button login = findViewById(R.id.login);
        final Button register = findViewById(R.id.register);
        final Button showreg = findViewById(R.id.showRegister);
        final Button showlogin = findViewById(R.id.showlogin);
//        final ImageButton ipconfig = findViewById(R.id.ip);
        final LinearLayout l3 = findViewById(R.id.l3);
        final LinearLayout l4 = findViewById(R.id.l4);
        username = findViewById(R.id.editUsername);
        final EditText password = findViewById(R.id.editPassword);
        final EditText repassword = findViewById(R.id.reeditPassword);
        final EditText email = findViewById(R.id.editEmail);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        View rootView = findViewById(R.id.rootlayout);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = sharedPreferences.getBoolean("remember", false);
//        ip = sharedPreferences.getString("ip", "");
        if (isRemember) {
            username.setText(sharedPreferences.getString("username", ""));
            password.setText(sharedPreferences.getString("password", ""));
            checkBox.setChecked(true);
        }
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d("change", "onLayoutChange: " + top + " " + oldTop + " " + bottom + " " + oldBottom);
                if (bottom < oldBottom) {
                    RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) bigheader.getLayoutParams();
                    lp.setMargins(0,0,0,0);
                    bigheader.setLayoutParams(lp);
                } else if (bottom > oldBottom) {
                    RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) bigheader.getLayoutParams();
                    final float scale = getApplication().getResources().getDisplayMetrics().density;
                    lp.setMargins(0,0,0, (int)(70 * scale+0.5f));
                    bigheader.setLayoutParams(lp);
                }
            }
        });
//        ipconfig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.support.design.widget.TextInputLayout textInputLayout = new android.support.design.widget.TextInputLayout(LoginActivity.this);
//                final TextInputEditText editText = new TextInputEditText(LoginActivity.this);
//                editText.setHint("Input IP Address");
//                editText.setFocusable(true);
//                editText.setFocusableInTouchMode(true);
//                textInputLayout.addView(editText);
//                editText.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//                editText.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(editText.getLayoutParams());
//                final float scale = getApplication().getResources().getDisplayMetrics().density;
//                layoutParams.setMargins((int) (15 * scale + 0.5f), 0, (int) (15 * scale + 0.5f), 0);
//                editText.setLayoutParams(layoutParams);
//                editText.setText(ip);
//                if (ip.equals("")) {
//                    editText.getText().clear();
//                }
//                editText.clearFocus();
//                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
//                AlertDialog dialog = alertDialog
//                        .setMessage("IP Config")
//                        .setView(textInputLayout)
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ip = editText.getText().toString();
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("ip", ip);
//                                editor.apply();
//                                String uri = "http://123.207.165.210:8080/Chat_war_exploded/LoginServlet";
//                                Log.d("uri", "onCreate: " + uri);
//                            }
//                        }).create();
//                dialog.show();
//            }
//        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = username.getText().toString();
                final String passWord = password.getText().toString();
                Log.d("username", "onClick: " + userName + "\n" + passWord);
//                if (ip.equals("")) {
//                    Toast.makeText(getApplicationContext(), "You need to config ip first!", Toast.LENGTH_SHORT).show();
//                } else
                if (userName.equals("")) {
                    username.requestFocus();
                } else if (passWord.equals("")) {
                    password.requestFocus();
                } else {
                    String uri = "http://123.207.165.210:8080/Chat_war_exploded/LoginServlet";
                    Log.d("uri", "onCreate: " + uri);
                    Request request = new Request.Builder()
                            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), "username=" + userName + "&password=" + passWord + "&type=" + "Login"))
                            .url(uri)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = LOGIN_FAILED;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            Message message = new Message();
                            Log.d("response", "onResponse: " + res);
                            if (res.equals("true")) {
                                tmp = userName;
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if (checkBox.isChecked()) {
                                    editor.putBoolean("remember", true);
                                    editor.putString("username", userName);
                                    editor.putString("password", passWord);
                                } else {
                                    editor.putBoolean("remember", false);
                                    editor.putString("username", "");
                                    editor.putString("password", "");
                                }
                                editor.apply();
                                message.what = LOGIN_SUCCESS;
                            } else {
                                message.what = LOGIN_FAILED;
                            }
                            handler.sendMessage(message);
                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = username.getText().toString();
                final String passWord = password.getText().toString();
                final String cfpassWord = repassword.getText().toString();
                final String Email = email.getText().toString();
                Log.d("username", "onClick: " + userName + "\n" + passWord + "\n" + Email);
//                if (ip.equals("")) {
//                    Toast.makeText(getApplicationContext(), "You need to config ip first!", Toast.LENGTH_SHORT).show();
//                } else
                if (userName.equals("")) {
                    username.requestFocus();
                } else if (passWord.equals("")) {
                    password.requestFocus();
                } else if (cfpassWord.equals("")) {
                    repassword.requestFocus();
                } else if (Email.equals("")) {
                    email.requestFocus();
                } else if (!passWord.equals(cfpassWord)) {
                    Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_SHORT).show();
                    repassword.requestFocus();
                } else {
                    String uri = "http://123.207.165.210:8080/Chat_war_exploded/LoginServlet";
                    Log.d("uri", "onCreate: " + uri);
                    Request request = new Request.Builder()
                            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), "username=" + userName + "&password=" + passWord + "&email=" + Email + "&type=" + "Register"))
                            .url(uri)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = REGISTER_FAILED;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            Message message = new Message();
                            Log.d("response", "onResponse: " + res);
                            if (res.equals("true")) {
                                tmp = userName;
                                message.what = REGISTER_SUCCESS;
                            } else if (res.equals("Already exists!")) {
                                message.what = ALREADY_EXISTS;
                            } else {
                                message.what = REGISTER_FAILED;
                            }
                            handler.sendMessage(message);
                        }
                    });
                }
            }
        });
        register.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom<oldBottom){
                    register.setVisibility(View.INVISIBLE);
                }else if(bottom>oldBottom&&login.getVisibility()==View.INVISIBLE){
                    register.setVisibility(View.VISIBLE);
                }
            }
        });
        showreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setVisibility(View.VISIBLE);
                l4.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                showreg.setVisibility(View.INVISIBLE);
                register.setVisibility(View.VISIBLE);
                showlogin.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.INVISIBLE);
            }
        });
        showlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setVisibility(View.INVISIBLE);
                l4.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
                showreg.setVisibility(View.VISIBLE);
                register.setVisibility(View.INVISIBLE);
                showlogin.setVisibility(View.INVISIBLE);
                checkBox.setVisibility(View.VISIBLE);
            }
        });
    }
}
