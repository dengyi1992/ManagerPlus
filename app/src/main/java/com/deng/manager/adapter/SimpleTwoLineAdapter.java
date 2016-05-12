package com.deng.manager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deng.manager.R;
import com.deng.manager.bean.DataDetailbean;

public class SimpleTwoLineAdapter extends BaseAdapter {

    private List<DataDetailbean> objects;

    private Context context;
    private LayoutInflater layoutInflater;

    public SimpleTwoLineAdapter(Context context,List<DataDetailbean> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects=objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public DataDetailbean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.simple_two_line, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((DataDetailbean)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(DataDetailbean object, ViewHolder holder) {
        //TODO implement
        holder.head.setText(object.getKey());
        holder.desc.setText(object.getValue());
    }

    protected class ViewHolder {
        private TextView head;
    private TextView desc;

        public ViewHolder(View view) {
            head = (TextView) view.findViewById(R.id.head);
            desc = (TextView) view.findViewById(R.id.desc);
        }
    }
}
