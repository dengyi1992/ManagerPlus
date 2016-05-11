package com.deng.manager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.deng.manager.service.MessageService;

/**
 * Created by deng on 16-5-11.
 */
public class MessageReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MessageService.class));
    }
}
