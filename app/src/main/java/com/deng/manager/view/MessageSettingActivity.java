package com.deng.manager.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.deng.manager.R;
import com.deng.manager.adapter.MessagesettingSmallItemAdapter;
import com.deng.manager.bean.MessageSetting;

import java.util.ArrayList;

public class MessageSettingActivity extends AppCompatActivity {

    private ListView mLvmSettingListView;
    private ArrayList<MessageSetting> messageSettings;
    private MessagesettingSmallItemAdapter messagesettingSmallItemAdapter;
    private SharedPreferences messageSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        mLvmSettingListView = (ListView) findViewById(R.id.lvm_setting);
        messageSettings = new ArrayList<>();
        messagesettingSmallItemAdapter = new MessagesettingSmallItemAdapter(this, messageSettings);
        mLvmSettingListView.setAdapter(messagesettingSmallItemAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        messageSetting = getSharedPreferences("messageSetting", MODE_PRIVATE);
        messageSettings.clear();
        MessageSetting dataUpdate = new MessageSetting("数据更新消息", "dataUpdate", messageSetting.getBoolean("dataUpdate", true));
        messageSettings.add(dataUpdate);
        MessageSetting userReg = new MessageSetting("管理员用户注册消息", "userReg", messageSetting.getBoolean("userReg", true));
        messageSettings.add(userReg);
        MessageSetting userActvie = new MessageSetting("管理员用户激活消息", "userActvie", messageSetting.getBoolean("userActvie", true));
        messageSettings.add(userActvie);
        MessageSetting seriverError = new MessageSetting("服务器错误消息", "seriverError", messageSetting.getBoolean("seriverError", true));
        messageSettings.add(seriverError);
        messagesettingSmallItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
