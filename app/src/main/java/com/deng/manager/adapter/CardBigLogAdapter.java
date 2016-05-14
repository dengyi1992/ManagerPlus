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
import com.deng.manager.bean.LogBean;

public class CardBigLogAdapter extends BaseAdapter {

    private List<LogBean.SuccessBean> objects = new ArrayList<LogBean.SuccessBean>();

    private Context context;
    private LayoutInflater layoutInflater;

    public CardBigLogAdapter(Context context, List<LogBean.SuccessBean> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public LogBean.SuccessBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_big_log_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((LogBean.SuccessBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(LogBean.SuccessBean object, ViewHolder holder) {
        //TODO implement
        holder.tvOperator.setText("操作人："+object.getOperator());
        holder.tvOperatortype.setText("操作类型：" +object.getOperatortype());
        holder.tvOperate.setText("操作详情"+object.getOperate());
        holder.tvTime.setText(object.getTime());
    }

    protected class ViewHolder {
        private CardView cardView;
        private TextView tvOperator;
        private TextView tvOperatortype;
        private TextView tvOperate;
        private TextView tvTime;

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvOperator = (TextView) view.findViewById(R.id.tv_operator);
            tvOperatortype = (TextView) view.findViewById(R.id.tv_operatortype);
            tvOperate = (TextView) view.findViewById(R.id.tv_operate);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
