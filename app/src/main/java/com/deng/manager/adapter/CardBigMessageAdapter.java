package com.deng.manager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.deng.manager.R;
import com.deng.manager.bean.Message;

public class CardBigMessageAdapter extends BaseAdapter {

    private List<Message> objects = new ArrayList<Message>();

    private Context context;
    private LayoutInflater layoutInflater;

    public CardBigMessageAdapter(Context context, List<Message> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Message getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_big_message, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((Message)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(Message object, ViewHolder holder) {
        //TODO implement
    }

    protected class ViewHolder {
        private CardView cardView;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCount;
    private TextView tvTime;

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvCount = (TextView) view.findViewById(R.id.tv_count);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
