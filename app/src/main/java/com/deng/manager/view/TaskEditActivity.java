package com.deng.manager.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deng.manager.R;
import com.deng.manager.utils.CharUtil;
import com.deng.manager.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskEditActivity extends AppCompatActivity {


    private EditText mEvTagEditText;
    private EditText mEvInterfaceEditText;
    private EditText mEvTypeEditText;
    private EditText mEvCricleEditText;
    private Button mSubmitTaskButton;
    private Button mCancelButton;
    private ProgressBar mProgressBarProgressBar;
    private String json;

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
                        jsonObject.put("mCricle", mCricle);
                        jsonObject.put("minterface", minterface);
                        jsonObject.put("mtag", mtag);
                        jsonObject.put("mtype", mtype);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json = jsonObject.toString();

//                    atempSendTask();

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
        HttpUtils.doGetAsynWithCookie(getString(R.string.edittask), TaskEditActivity.this, new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {

            }
        });
    }
}
