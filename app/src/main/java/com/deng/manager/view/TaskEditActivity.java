package com.deng.manager.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.constant.ConstantValue;
import com.deng.manager.utils.CharUtil;
import com.deng.manager.utils.HttpUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TaskEditActivity extends AppCompatActivity {


    private static final int COMPLETE = 1;
    private static final int NETWORK_EORR = 2;
    private EditText mEvTagEditText;
    private EditText mEvInterfaceEditText;
    private EditText mEvTypeEditText;
    private EditText mEvCricleEditText;
    private Button mSubmitTaskButton;
    private Button mCancelButton;
    private ProgressBar mProgressBarProgressBar;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String json;
    private String result;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMPLETE:
                    try {

                        JSONObject jsonObject = new JSONObject(success);
                        if (success.contains("success")) {
                            Toast.makeText(TaskEditActivity.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(TaskEditActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NETWORK_EORR:
                    Toast.makeText(TaskEditActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            mProgressBarProgressBar.setVisibility(View.GONE);
        }
    };
    private String success;
    private SharedPreferences cookie;
    private String my_cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        initView();
    }

    private void initView() {
        mEvTagEditText = (EditText) findViewById(R.id.ev_tag);
        mEvInterfaceEditText = (EditText) findViewById(R.id.ev_interface);
        mEvTypeEditText = (EditText) findViewById(R.id.ev_type);
        mEvCricleEditText = (EditText) findViewById(R.id.ev_cricle);
        mSubmitTaskButton = (Button) findViewById(R.id.bt_submit_task);
        mCancelButton = (Button) findViewById(R.id.bt_cancel);
        mProgressBarProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSubmitTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mCricle = mEvCricleEditText.getText().toString();
                String minterface = mEvInterfaceEditText.getText().toString();
                String mtag = mEvTagEditText.getText().toString();
                String mtype = mEvTypeEditText.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(mCricle)) {
                    mEvCricleEditText.setError(getString(R.string.error_no_cycle));
                    focusView = mEvCricleEditText;
                    cancel = true;
                }
                if (TextUtils.isEmpty(minterface)) {
                    mEvInterfaceEditText.setError(getString(R.string.error_no_interface));
                    focusView = mEvInterfaceEditText;
                    cancel = true;
                }
                if (TextUtils.isEmpty(mtag)) {
                    mEvTagEditText.setError(getString(R.string.error_no_tag));
                    focusView = mEvTagEditText;
                    cancel = true;
                }
                if (TextUtils.isEmpty(mtype)) {
                    mEvTypeEditText.setError(getString(R.string.error_no_type));
                    focusView = mEvTypeEditText;
                    cancel = true;
                }
                if (!TextUtils.isEmpty(mCricle) && !isCricleValid(mCricle)) {
                    mEvCricleEditText.setError(getString(R.string.error_invalid_cricle));
                    focusView = mEvCricleEditText;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("cycle", mCricle);
                        jsonObject.put("interfaceurl", minterface);
                        jsonObject.put("interfacetag", mtag);
                        jsonObject.put("type", mtype);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json = jsonObject.toString();
                    mProgressBarProgressBar.setVisibility(View.VISIBLE);
                    new Thread() {
                        @Override
                        public void run() {
                            atempSendTask();
                            super.run();
                        }
                    }.start();


                }
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean isCricleValid(String mCricle) {
        return CharUtil.is_number(mCricle);
    }

    private void atempSendTask() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(ConstantValue.taskSetting)
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
        cookie = getSharedPreferences("cookie", Context.MODE_PRIVATE);
        my_cookie = cookie.getString("my_cookie", null);
    }
}
