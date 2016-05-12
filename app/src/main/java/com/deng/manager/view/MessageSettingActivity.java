package com.deng.manager.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.deng.manager.R;
import com.deng.manager.adapter.MessagesettingSmallItemAdapter;
import com.deng.manager.bean.MessageSetting;

import java.util.ArrayList;

public class MessageSettingActivity extends AppCompatActivity {


    private SharedPreferences messageSetting;
    private CheckBox mDataupdateCheckBox;
    private CheckBox mUserregisterCheckBox;
    private CheckBox mUserActivedCheckBox;
    private CheckBox mServerErrorCheckBox;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        messageSetting = getSharedPreferences("messageSetting", MODE_PRIVATE);
        edit = messageSetting.edit();
        initView();


    }

    private void initView() {
        mDataupdateCheckBox = (CheckBox) findViewById(R.id.cb_dataupdate);
        mDataupdateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("dataupdate", isChecked).commit();
            }
        });
        mUserregisterCheckBox = (CheckBox) findViewById(R.id.cb_userregister);
        mUserregisterCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("userregister", isChecked).commit();
            }
        });
        mUserActivedCheckBox = (CheckBox) findViewById(R.id.cb_user_actived);
        mUserActivedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("useractived", isChecked).commit();
            }
        });
        mServerErrorCheckBox = (CheckBox) findViewById(R.id.cb_server_error);
        mServerErrorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("servererror", isChecked).commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mDataupdateCheckBox.setChecked(messageSetting.getBoolean("dataupdate", true));
        mUserActivedCheckBox.setChecked(messageSetting.getBoolean("useractived", true));
        mUserregisterCheckBox.setChecked(messageSetting.getBoolean("userregister", true));
        mServerErrorCheckBox.setChecked(messageSetting.getBoolean("servererror", true));
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
