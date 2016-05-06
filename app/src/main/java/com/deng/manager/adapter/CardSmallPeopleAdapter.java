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
import com.deng.manager.bean.Member;

public class CardSmallPeopleAdapter extends BaseAdapter {

    private List<Member> objects = new ArrayList<Member>();

    private Context context;
    private LayoutInflater layoutInflater;

    public CardSmallPeopleAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Member getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_small_people, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((Member)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(Member object, ViewHolder holder) {
        //TODO implement
    }

    protected class ViewHolder {
        private CardView cardView;
    private TextView tvName;
    private TextView tvFirsttime;

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvFirsttime = (TextView) view.findViewById(R.id.tv_firsttime);
        }
    }
}
