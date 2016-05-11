package com.deng.manager.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.constant.ConstantValue;
import com.deng.manager.utils.ChangePassActivity;
import com.deng.manager.utils.HttpUtils;
import com.deng.manager.utils.ToolFor9Ge;


public class AccountActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mTitlebarTextView;
    private TextView mTvchangepassTextView;
    private Button mUnloginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initView();
    }

    private void initView() {
        mTvchangepassTextView = (TextView) findViewById(R.id.tvchangepass);
        mUnloginButton = (Button) findViewById(R.id.bt_unlogin);
        mTvchangepassTextView.setOnClickListener(this);
        mUnloginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvchangepass:
                if (ToolFor9Ge.checkNetworkInfo(this)){
                    startActivity(new Intent(AccountActivity.this, ChangePassActivity.class));

                }else {
                    Toast.makeText(this,"没有网络!",Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.bt_unlogin:
                unLogin();
                break;
            default:
                break;
        }
    }

    private void unLogin() {
        /**
         * 登录信息清空
         */
        SharedPreferences sharedPreferences = this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        /**
         * cookie清空
         */
        SharedPreferences sharedPreferences1 = this.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sharedPreferences1.edit();
        edit1.clear();
        edit1.commit();

        Thread thread = new Thread() {
            @Override
            public void run() {
                String s = HttpUtils.doGetWithCookie(ConstantValue.LogOutUrl, getApplicationContext());
                finish();
            }
        };
        thread.start();
        Toast.makeText(AccountActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
