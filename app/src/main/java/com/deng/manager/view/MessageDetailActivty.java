package com.deng.manager.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.deng.manager.R;

public class MessageDetailActivty extends AppCompatActivity {

    private String messageTitle;
    private String messageBody;
    private String time;
    private int messageCount;
    private int id;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mCountTextView;
    private TextView mTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail_activty);
        initView();




    }



    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
    private void initView() {
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mContentTextView = (TextView) findViewById(R.id.tv_content);
        mCountTextView = (TextView) findViewById(R.id.tv_count);
        mTimeTextView = (TextView) findViewById(R.id.tv_time);
    }
    private void initData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        messageTitle = extras.getString("messageTitle", null);
        messageBody = extras.getString("messageBody", null);
        time = extras.getString("time", null);
        messageCount = extras.getInt("messageCount", 0);
        id = extras.getInt("id", 0);
        mTimeTextView.setText("更新时间："+time);
        mContentTextView.setText(messageBody);
        mCountTextView.setText("更新" + messageCount + "条");
        mTitleTextView.setText(messageTitle);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
