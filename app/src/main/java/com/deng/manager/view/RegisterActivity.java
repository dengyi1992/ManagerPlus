package com.deng.manager.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private static final int POSTED = 1;
    private static final int NETWORK_EORR = 2;
    private EditText mUsernameEditText;
    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private EditText mRePassEditText;
    private EditText mPassEditText;
    private Button mRegisterButton;
    private String json;
    private ProgressBar mProgressProgressBar;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String success;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POSTED:
                    showProgress(false);
                    if (success.contains("success")) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(success);
                            String error = jsonObject.getString("error");
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    break;
                case NETWORK_EORR:
                    showProgress(false);
                    Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);
        initView();


    }

    private void initView() {
        mProgressProgressBar = (ProgressBar) findViewById(R.id.login_progress);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mPhoneEditText = (EditText) findViewById(R.id.et_phone);
        mEmailEditText = (EditText) findViewById(R.id.et_email);
        mPassEditText = (EditText) findViewById(R.id.et_pass);
        mRePassEditText = (EditText) findViewById(R.id.et_re_pass);
        mRegisterButton = (Button) findViewById(R.id.bt_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {


        // Reset errors.
        mUsernameEditText.setError(null);
        mPhoneEditText.setError(null);
        mEmailEditText.setError(null);
        mPassEditText.setError(null);
        mRePassEditText.setError(null);
        // Store values at the time of the login attempt.
        String name = mUsernameEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String pass = mPassEditText.getText().toString();
        String re_pass = mRePassEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            mUsernameEditText.setError(getString(R.string.error_no_name));
            focusView = mUsernameEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            mPhoneEditText.setError(getString(R.string.error_no_phone));
            focusView = mPhoneEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.error_no_email));
            focusView = mEmailEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(pass)) {
            mPassEditText.setError(getString(R.string.error_no_pass));
            focusView = mPassEditText;
            cancel = true;
        }
        if (TextUtils.isEmpty(re_pass)) {
            mRePassEditText.setError(getString(R.string.error_no_repass));
            focusView = mRePassEditText;
            cancel = true;
        }


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            mPassEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPassEditText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(re_pass) && !isPasswordValid(re_pass)) {
            mPassEditText.setError(getString(R.string.error_invalid_repassword));
            focusView = mPassEditText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(name) && !isNameValid(name)) {
            mUsernameEditText.setError(getString(R.string.error_invalid_name));
            focusView = mUsernameEditText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            mEmailEditText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEditText;
            cancel = true;
        }
        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            mPhoneEditText.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneEditText;
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
                jsonObject.put("password-repeat", re_pass);
                jsonObject.put("email", email);
                jsonObject.put("phone", phone);
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

    private boolean isPhoneValid(String phone) {
        return true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

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

    private void postJson() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)

        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(ConstantValue.RegUrl)
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
                handler.sendEmptyMessage(POSTED);
            } else {
                handler.sendEmptyMessage(NETWORK_EORR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
