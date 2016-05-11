package com.deng.manager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CheckBox;

import com.deng.manager.R;
import com.deng.manager.bean.MessageSetting;
import com.deng.manager.view.MessageSettingActivity;

public class MessagesettingSmallItemAdapter extends BaseAdapter {

    private SharedPreferences.Editor messageEdit;
    private List<MessageSetting> objects = new ArrayList<MessageSetting>();

    private Context context;
    private LayoutInflater layoutInflater;


    public MessagesettingSmallItemAdapter(Context context, List<MessageSetting> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects=objects;
        this.messageEdit=context.getSharedPreferences("messageSetting", Context.MODE_PRIVATE).edit();

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public MessageSetting getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.messagesetting_small_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((MessageSetting)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final MessageSetting object, ViewHolder holder) {
        //TODO implement
        holder.tvName.setText(object.getName());
        holder.checkboxSetting.setChecked(object.isChecked());
        holder.checkboxSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                messageEdit.putBoolean(object.getId(),isChecked).commit();
            }
        });

    }

    protected class ViewHolder {
        private TextView tvName;
    private CheckBox checkboxSetting;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            checkboxSetting = (CheckBox) view.findViewById(R.id.checkbox_setting);

        }
    }
}
