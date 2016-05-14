package com.deng.manager.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.bean.Pushitem;
import com.deng.manager.constant.ConstantValue;
import com.deng.manager.utils.HttpUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PushActivty extends AppCompatActivity {

    private static final int COMPLETE = 1;
    private static final int NETWORK_EORR = 2;
    private TextView mShowSelectedTextView;
    private ListView mLvListView;
    private int code;
    private String showString;
    private ArrayList<String> jsonStringBeens;
    private ArrayAdapter<String> adapter;
    private Button mSureButton;
    private ProgressBar mProgressBarProgressBar;
    private List<Pushitem.DataBean> data;
    private String json;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private SharedPreferences cookie;
    private String my_cookie;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMPLETE:
                    try {

                        JSONObject jsonObject = new JSONObject(success);
                        if (success.contains("success")) {
                            Toast.makeText(PushActivty.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PushActivty.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NETWORK_EORR:
                    Toast.makeText(PushActivty.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_activty);
        mProgressBarProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
        showString = intent.getStringExtra("showString");
        jsonStringBeens = new ArrayList<String>();
        initView();

    }

    private void initView() {
        mShowSelectedTextView = (TextView) findViewById(R.id.show_selected);
        mSureButton = (Button) findViewById(R.id.bt_sure);
        mSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = mLvListView.getCheckedItemPosition();
                if (selectedItemPosition < 0) {
                    Toast.makeText(PushActivty.this, "您未选中任何选项", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("TO", code);
                    Gson gson = new Gson();
                    Pushitem.DataBean.JsonStringBean jsonString = data.get(selectedItemPosition).getJsonString();
                    String content = gson.toJson(jsonString, Pushitem.DataBean.JsonStringBean.class);
                    jsonObject.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                json = jsonObject.toString();
                System.out.println(selectedItemPosition);
                new AlertDialog.Builder(PushActivty.this)
                        .setTitle("马上推送？")
                        .setMessage("您选中了:" + jsonStringBeens.get(selectedItemPosition))
                        .setPositiveButton("现在就发", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        atemptSend();
                                        super.run();
                                    }
                                }.start();
                            }
                        })
                        .setNegativeButton("在考虑下", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        mShowSelectedTextView.setText(showString);
        mLvListView = (ListView) findViewById(R.id.push_lv);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, jsonStringBeens);
        mLvListView.setAdapter(adapter);
    }

    private void atemptSend() {
//申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(ConstantValue.pushUrl)
                .addHeader("Cookie", my_cookie)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                success = response.body().string();
                handler.sendEmptyMessage(COMPLETE);
            } else {
                handler.sendEmptyMessage(NETWORK_EORR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        cookie = getSharedPreferences("cookie", Context.MODE_PRIVATE);
        my_cookie = cookie.getString("my_cookie", null);
        mProgressBarProgressBar.setVisibility(View.VISIBLE);
        HttpUtils.doGetAsyn("http://120.27.41.245:3001/api/yidian?page=1", new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                if (result.contains("success")) {
                    Gson gson = new Gson();
                    Pushitem pushitem = gson.fromJson(result, Pushitem.class);
                    data = pushitem.getData();
                    jsonStringBeens.clear();
                    for (Pushitem.DataBean d :
                            data) {
                        jsonStringBeens.add(d.getJsonString().getTitle());
                    }
                    mLvListView.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                    mProgressBarProgressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
