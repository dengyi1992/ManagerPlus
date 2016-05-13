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
import com.deng.manager.bean.Task;

public class CardBigTaskAdapter extends BaseAdapter {

    private List<Task.ContentBean> objects = new ArrayList<Task.ContentBean>();

    private Context context;
    private LayoutInflater layoutInflater;

    public CardBigTaskAdapter(Context context, ArrayList<Task.ContentBean> contents) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects=contents;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Task.ContentBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_big_task, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((Task.ContentBean)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(Task.ContentBean object, ViewHolder holder) {
        //TODO implement

        holder.tvName.setText("名字："+object.getInterfacetag());
        holder.tvDesc.setText("描述："+object.getInterfaceurl());
        holder.tvCycle.setText("周期："+object.getCycle()*5+"分钟");
        holder.tvIsrepeat.setText("重复");
        holder.tvType.setText("类型："+object.getType());
    }

    protected class ViewHolder {
        private CardView cardView;
    private TextView tvName;
    private TextView tvDesc;
    private TextView tvCycle;
    private TextView tvIsrepeat;
    private TextView tvType;

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            tvCycle = (TextView) view.findViewById(R.id.tv_cycle);
            tvIsrepeat = (TextView) view.findViewById(R.id.tv_isrepeat);
            tvType = (TextView) view.findViewById(R.id.tv_type);
        }
    }
}
