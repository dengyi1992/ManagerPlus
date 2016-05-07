package com.deng.manager.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.deng.manager.R;
import com.deng.manager.constant.ConstantValue;
import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private static final int POSTED = 1;
    private static final int NETWORK_EORR = 2;
    private EditText mNameEditText;
    private EditText mPassEditText;
    private Button mLoginButton;
    private ProgressBar mProgressProgressBar;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String json;
    private String success;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POSTED:
                    showProgress(false);
                    if (success.contains("success")) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        resetAccount();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case NETWORK_EORR:
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private String name;
    private String password;

    private void resetAccount() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("isLogin", true);
            edit.putString("name", name);
            edit.putString("password", password);
            edit.putInt("account", 0);
            edit.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mNameEditText = (EditText) findViewById(R.id.et_name);
        mPassEditText = (EditText) findViewById(R.id.et_pass);
        mLoginButton = (Button) findViewById(R.id.bt_login);
        mProgressProgressBar = (ProgressBar) findViewById(R.id.login_progress);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        mNameEditText.setError(null);
        mPassEditText.setError(null);

        // Store values at the time of the login attempt.
        name = mNameEditText.getText().toString();
        password = mPassEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            mNameEditText.setError(getString(R.string.error_no_name));
            focusView = mNameEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPassEditText.setError(getString(R.string.error_no_pass));
            focusView = mPassEditText;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPassEditText;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(name) && !isNameValid(name)) {
            mNameEditText.setError(getString(R.string.error_invalid_name));
            focusView = mPassEditText;
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
                jsonObject.put("name", name);
                jsonObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = jsonObject.toString();
            showProgress(true);
            //http://119.29.191.229:2888/users/login
            new Thread() {
                @Override
                public void run() {
//                    postJson();
                }
            }.start();

        }
    }

//    private void postJson() {
//        //申明给服务端传递一个json串
//        //创建一个OkHttpClient对象
//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//        RequestBody requestBody = RequestBody.create(JSON, json);
//        //创建一个请求对象
//        Request request = new Request.Builder()
//                .url(ConstantValue.LoginUrl)
//                .post(requestBody)
//                .build();
//        //发送请求获取响应
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            //判断请求是否成功
//            if (response.isSuccessful()) {
//                //打印服务端返回结果
//                success = response.body().string();
//                String header = response.header("set-cookie");
//                SharedPreferences cookie = getSharedPreferences("cookie", Context.MODE_PRIVATE);
//                SharedPreferences.Editor edit = cookie.edit();
//                edit.clear();
//                edit.putString("my_cookie",header);
//                edit.commit();
//                Log.i("success", success);
//                handler.sendEmptyMessage(POSTED);
//            } else {
//                handler.sendEmptyMessage(NETWORK_EORR);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private boolean isNameValid(String name) {
        return name.length() >= 1;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
