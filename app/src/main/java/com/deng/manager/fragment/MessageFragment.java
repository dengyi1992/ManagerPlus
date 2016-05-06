package com.deng.manager.fragment;

import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigMessageAdapter;
import com.deng.manager.bean.Message;
import com.deng.manager.dao.MessageDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng on 16-5-6.
 */
public class MessageFragment extends Fragment {

    private ListView listViewMessage;
    private ProgressBar mProgressBar;
    private List<Message> mContentItems;
    private CardBigMessageAdapter cardBigMessageAdapter;
    private MessageDBHelper messageDBHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        listViewMessage = (ListView) view.findViewById(R.id.message_lv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mContentItems = new ArrayList<Message>();
        cardBigMessageAdapter = new CardBigMessageAdapter(getContext(), mContentItems);
        listViewMessage.setAdapter(cardBigMessageAdapter);
        return view;
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
            mContentItems.add(0,message);

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
}
