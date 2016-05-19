package com.deng.manager.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;


import com.deng.manager.R;
import com.deng.manager.constant.ConstantValue;
import com.deng.manager.dao.MessageDBHelper;
import com.deng.manager.view.MainActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Random;


/**
 * Created by deng on 16-3-13.
 */
public class MessageService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private Socket mSocket;
    private LocalBroadcastManager broadcaster;
    private NotificationManager mNotifMan;
    private NotificationManager systemService;
    private SharedPreferences messageSetting;


    {
        try {
//            mSocket = IO.socket("http://192.168.199.127:2999/");
            mSocket = IO.socket("http://120.27.41.245:3002/");
        } catch (URISyntaxException e) {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSocket.on("dengyi", onNewMessage);
        mSocket.on("taskfinish", onTaskFinish);
        mSocket.connect();
        messageSetting = getSharedPreferences("messageSetting", MODE_PRIVATE);
        mNotifMan = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        attemptSend();

    }

    private void attemptSend() {

        mSocket.emit("new admin", "test test");
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                String deng = jsonObject.getString("deng");
                System.out.println("###############################################33" + deng);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    };
    private Emitter.Listener onTaskFinish = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                String name = jsonObject.getString("iname");
                String time = jsonObject.getString("time");
                String type = jsonObject.getString("type");
                String messageType = jsonObject.getString("messageType");
                if (!messageSetting.getBoolean(messageType,true))
                {
                    return;
                }
                System.out.println("++++++++++++++++" + name + time + "++++++++++++++++++++");
                MessageDBHelper messagedb = new MessageDBHelper(MessageService.this, "messagedb", null, 1);
                SQLiteDatabase writableDatabase = messagedb.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                /**
                 *   + "id integer primary key autoincrement, "
                 + "title text, "
                 + "body text, "
                 + "count integer, "
                 + "time text)";
                 */
                contentValues.put("title", name);
                contentValues.put("body", type);
                contentValues.put("count", 1);
                contentValues.put("time", time);
                mNtify(name, time);
                writableDatabase.insert("Message", null, contentValues);
                writableDatabase.close();
                messagedb.close();
                Intent intent = new Intent();
                intent.setAction(ConstantValue.UPDATE_MESSAGE_INFO);
                sendBroadcast(intent);
                //弹出新的通知
//                System.out.println("##更新完的接口##"+iname+"##更新完成时间##"+time);
                //任务完成，将所发任务的消息加入数据列表，并显示，可以加通知栏显示
                //更新消息列表有难度，并加入已读与否的标记
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("三人行"))
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle("三人行在后台正在运行");

        PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        mBuilder.setContentIntent(pendingintent);
        mNotifMan.notify(NOTIFICATION_ID, mBuilder.build());
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);

        stopForeground(true);
        Intent intent = new Intent("com.srx.manager.receriver");
        sendBroadcast(intent);
    }

    private void mNtify(String title, String message) {

        Intent intent = new Intent();
        intent.putExtra("content", title + ":" + message);
        intent.setClass(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle("您有新的消息...")
                        .setContentText(title + "：" + message);

        mBuilder.setContentIntent(contentIntent);
        int i = new Random().nextInt(100);
        mNotifMan.notify(i, mBuilder.build());
    }

}
