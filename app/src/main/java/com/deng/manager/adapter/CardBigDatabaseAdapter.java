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
import com.deng.manager.bean.DataBaseInfo;

public class CardBigDatabaseAdapter extends BaseAdapter {

    private List<DataBaseInfo.ContentEntity> objects = new ArrayList<DataBaseInfo.ContentEntity>();

    private Context context;
    private LayoutInflater layoutInflater;

    public CardBigDatabaseAdapter(Context context, List<DataBaseInfo.ContentEntity> contents) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        objects=contents;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public DataBaseInfo.ContentEntity getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_big_database, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((DataBaseInfo.ContentEntity)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(DataBaseInfo.ContentEntity object, ViewHolder holder) {
        //TODO implement
        holder.tvTableName.setText("表名：" + object.getTABLE_NAME());
        holder.tvTableRows.setText("行数：" + object.getTABLE_ROWS());
        holder.tvTableAvgLength.setText("平均行长度：" + object.getAVG_ROW_LENGTH());
        holder.tvTableDataLength.setText("数据长度" + object.getDATA_LENGTH());
        holder.tvTableAutoIncrement.setText("自增长数:" + object.getAUTO_INCREMENT());
    }

    protected class ViewHolder {
        private CardView cardView;
    private TextView tvTableName;
    private TextView tvTableRows;
    private TextView tvTableAvgLength;
    private TextView tvTableDataLength;
    private TextView tvTableAutoIncrement;

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvTableName = (TextView) view.findViewById(R.id.tv_table_name);
            tvTableRows = (TextView) view.findViewById(R.id.tv_table_rows);
            tvTableAvgLength = (TextView) view.findViewById(R.id.tv_table_avg_length);
            tvTableDataLength = (TextView) view.findViewById(R.id.tv_table_data_length);
            tvTableAutoIncrement = (TextView) view.findViewById(R.id.tv_table_auto_increment);
        }
    }
}
