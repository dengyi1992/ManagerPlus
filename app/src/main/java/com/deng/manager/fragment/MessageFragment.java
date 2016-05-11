package com.deng.manager.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigMessageAdapter;
import com.deng.manager.bean.Message;
import com.deng.manager.constant.ConstantValue;
import com.deng.manager.dao.MessageDBHelper;
import com.deng.manager.view.MessageDetailActivty;
import com.deng.manager.view.MessageSetting;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng on 16-5-6.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private ListView listViewMessage;
    private ProgressBar mProgressBar;
    private List<Message> mContentItems;
    private CardBigMessageAdapter cardBigMessageAdapter;
    private MessageDBHelper messageDBHelper;
    private FloatingActionButton fabClearAll;
    private FloatingActionButton fabMessageSetting;
    public static FloatingActionMenu floatingMessageActionMenu;
    private BroadcastReceiver broadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        initView(view);
        initBR();
        return view;
    }

    private void initView(View view) {

        listViewMessage = (ListView) view.findViewById(R.id.message_lv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        fabClearAll = (FloatingActionButton) view.findViewById(R.id.clear_all);
        fabMessageSetting = (FloatingActionButton) view.findViewById(R.id.message_setting);
        floatingMessageActionMenu = (FloatingActionMenu) view.findViewById(R.id.menu_message);

        fabClearAll.setOnClickListener(this);
        fabMessageSetting.setOnClickListener(this);
        floatingMessageActionMenu.setOnClickListener(this);
        mContentItems = new ArrayList<Message>();
        cardBigMessageAdapter = new CardBigMessageAdapter(getContext(), mContentItems);
        listViewMessage.setAdapter(cardBigMessageAdapter);
        ///长按弹出对话框
        listViewMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(view.getContext()).setTitle("温馨提示")
                        .setMessage("是否删除 " + mContentItems.get(position).getMessageTitle() + "?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cardBigMessageAdapter.notifyDataSetChanged();
                                findByIdAndDelete(mContentItems.get(position).getId());
                                mContentItems.remove(position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();


                return true;
            }
        });
        ///可以进详情。。。。
        listViewMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MessageDetailActivty.class);
                Message message = mContentItems.get(position);
                /**
                 *   public String messageTitle;
                 public String messageBody;
                 public int messageCount;
                 public String time;
                 public int id;
                 */
                intent.putExtra("messageTitle",message.getMessageTitle());
                intent.putExtra("messageBody",message.getMessageBody());
                intent.putExtra("messageCount",message.getMessageCount());
                intent.putExtra("time",message.getTime());
                intent.putExtra("id",message.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        iniData();
    }

    private void iniData() {
        mProgressBar.setVisibility(View.VISIBLE);
        messageDBHelper = new MessageDBHelper(getContext(), "messagedb", null, 1);
        SQLiteDatabase readableDatabase = messageDBHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from Message", null);
        /**
         *   + "id integer primary key autoincrement, "
         + "title text, "
         + "body text, "
         + "count integer, "
         + "time text)";
         */
        mContentItems.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            Message message = new Message();
            message.setTime(time);
            message.setMessageCount(count);
            message.setMessageBody(body);
            message.setMessageTitle(title);
            message.setId(id);
            mContentItems.add(0, message);

        }

        cardBigMessageAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
        readableDatabase.close();
    }

    private void clearAllMessage() {
        SQLiteDatabase writableDatabase = messageDBHelper.getWritableDatabase();
        writableDatabase.execSQL("DELETE FROM Message");
        writableDatabase.close();
        mContentItems.clear();
        cardBigMessageAdapter.notifyDataSetChanged();
    }

    private void findByIdAndDelete(int id) {
        SQLiteDatabase writableDatabase = messageDBHelper.getWritableDatabase();
        String DELETESQL = "DELETE FROM MESSAGE WHERE id = '" + id + "'";
        try {
            writableDatabase.execSQL(DELETESQL);
        } catch (Exception e) {
            Log.d("database exception ", e.toString());
        }
        writableDatabase.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_all:
                new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
                        .setMessage("是否清空所有消息")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clearAllMessage();

                                floatingMessageActionMenu.close(true);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                floatingMessageActionMenu.close(true);
                            }
                        }).show();
                break;
            case R.id.message_setting:
                startActivity(new Intent(getContext(), MessageSetting.class));
                break;
            default:
                break;
        }
    }
    private void initBR() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                iniData();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantValue.UPDATE_MESSAGE_INFO);
        getContext().registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(broadcastReceiver);
    }
}
