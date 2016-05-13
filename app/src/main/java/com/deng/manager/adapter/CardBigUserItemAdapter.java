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
import com.deng.manager.bean.clientUserBean;

public class CardBigUserItemAdapter extends BaseAdapter {

    private List<clientUserBean.SuccessBean> objects = new ArrayList<clientUserBean.SuccessBean>();

    private Context context;
    private LayoutInflater layoutInflater;

    public CardBigUserItemAdapter(Context context,List<clientUserBean.SuccessBean> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects=objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public clientUserBean.SuccessBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_big_user_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((clientUserBean.SuccessBean)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(clientUserBean.SuccessBean object, ViewHolder holder) {
        //TODO implement
        holder.tvDevid.setText(object.getDevId());
        holder.tvType.setText(object.getType());
        holder.tvtime.setText(object.getTime());
    }

    protected class ViewHolder {
    private TextView tvDevid;
    private TextView tvType;
    private TextView tvtime;

        public ViewHolder(View view) {
            tvDevid = (TextView) view.findViewById(R.id.tv_devid);
            tvType = (TextView) view.findViewById(R.id.tv_type);
            tvtime = (TextView) view.findViewById(R.id.tvtime);
        }
    }
}
