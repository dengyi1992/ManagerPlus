package com.deng.manager.utils;

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
import android.widget.TextView;
import android.widget.Toast;


import com.deng.manager.R;
import com.deng.manager.constant.ConstantValue;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangePassActivity extends AppCompatActivity {

    private static final int PASSCHANGED = 1;
    private static final int NETWORK_EORR = 2;
    private TextView mTitlebarTextView;
    private TextView mNameTextView;
    private EditText mPassEditText;
    private EditText mNewpassEditText;
    private EditText mRenewpassEditText;
    private Button mSendChangePassButton;
    private String name;
    private String json;
    private String success;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PASSCHANGED:
                    showProgress(false);
                    if (success.contains("success")) {
                        Toast.makeText(ChangePassActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                        clearSP();
                        finish();
                    } else {
                        Toast.makeText(ChangePassActivity.this, "密码或帐号错误", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case NETWORK_EORR:
                    showProgress(false);
                    Toast.makeText(ChangePassActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void clearSP() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        finish();
    }

    private ProgressBar mProgressProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        boolean isLogin = preferences.getBoolean("isLogin", false);
        name = preferences.getString("name", null);
        String userNo = preferences.getString("userNo", null);
        int account = preferences.getInt("account", 0);
        if (isLogin) {
            mNameTextView.setText("您的帐号名称为 " + name);
        }

    }

    private void initView() {
        mNameTextView = (TextView) findViewById(R.id.tv_name);
        mPassEditText = (EditText) findViewById(R.id.et_pass);
        mNewpassEditText = (EditText) findViewById(R.id.et_newpass);
        mRenewpassEditText = (EditText) findViewById(R.id.et_renewpass);
        mProgressProgressBar =(ProgressBar)findViewById(R.id.changepass_progress);
        mSendChangePassButton = (Button) findViewById(R.id.sendChangePass);
        mSendChangePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChanged();
            }
        });
    }

    private void attemptChanged() {
        // Reset errors.
        mPassEditText.setError(null);
        mRenewpassEditText.setError(null);
        mNewpassEditText.setError(null);

        // Store values at the time of the login attempt.
        String pass = mPassEditText.getText().toString();
        String newPass = mNewpassEditText.getText().toString();
        String reNewPass = mRenewpassEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;
        //是否为空判断
        if (TextUtils.isEmpty(pass)) {
            mPassEditText.setError(getString(R.string.error_no_pass));
            focusView = mPassEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(newPass)) {
            mNewpassEditText.setError(getString(R.string.error_no_newpass));
            focusView = mNewpassEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(reNewPass)) {
            mRenewpassEditText.setError(getString(R.string.error_no_renewpass));
            focusView = mRenewpassEditText;
            cancel = true;
        }
        //是否为符合要求
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            mPassEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPassEditText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(newPass) && !isPasswordValid(newPass)) {
            mNewpassEditText.setError(getString(R.string.error_invalid_password));
            focusView = mNewpassEditText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(reNewPass) && !isPasswordValid(reNewPass)) {
            mRenewpassEditText.setError(getString(R.string.error_invalid_password));
            focusView = mRenewpassEditText;
            cancel = true;
        }
        //是否一致
        if (!TextUtils.equals(reNewPass, newPass)) {
            mRenewpassEditText.setError(getString(R.string.error_nosame_password));
            focusView = mRenewpassEditText;
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
                jsonObject.put("password", pass);
                jsonObject.put("newpassword", newPass);
                jsonObject.put("newpassword-repeat",reNewPass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json = jsonObject.toString();
            showProgress(true);
            //http://119.29.191.229:2888/users/login
            new Thread() {
                @Override
                public void run() {

                    postJson();
                }
            }.start();

//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }


    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    private void postJson() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)

        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(ConstantValue.changePassUrl)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                success = response.body().string();
                Log.i("success", success);
                handler.sendEmptyMessage(PASSCHANGED);
            } else {
                handler.sendEmptyMessage(NETWORK_EORR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
