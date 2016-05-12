package com.deng.manager.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.deng.manager.R;
import com.deng.manager.adapter.SimpleTwoLineAdapter;
import com.deng.manager.bean.DataBaseInfo;
import com.deng.manager.bean.DataDetailbean;
import com.deng.manager.fragment.DatabaseFragment;

import java.util.ArrayList;

public class DataBaseActivity extends AppCompatActivity {

    private ListView mDetailLvListView;
    private ArrayList<DataDetailbean> objects;
    private SimpleTwoLineAdapter adapter;
    private TextView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        mDetailLvListView = (ListView) findViewById(R.id.database_detail_lv);
        head = (TextView) findViewById(R.id.database_titlebar);
        objects = new ArrayList<>();
        adapter = new SimpleTwoLineAdapter(this, objects);
        mDetailLvListView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        if (position >= 0) {
            DataBaseInfo.ContentEntity contentEntity = DatabaseFragment.contents.get(position);
            head.setText("表：" +contentEntity.getTABLE_NAME());
            objects.clear();
            objects.add(new DataDetailbean("AUTO_INCREMENT", contentEntity.getAUTO_INCREMENT() + ""));
            objects.add(new DataDetailbean("AVG_ROW_LENGTH", contentEntity.getAVG_ROW_LENGTH() + ""));
            objects.add(new DataDetailbean("CHECKSUM", contentEntity.getCHECKSUM() + ""));
            objects.add(new DataDetailbean("CHECK_TIME", contentEntity.getCHECK_TIME() + ""));
            objects.add(new DataDetailbean("DATA_FREE", contentEntity.getDATA_FREE() + ""));
            objects.add(new DataDetailbean("DATA_LENGTH", contentEntity.getDATA_LENGTH() + ""));
            objects.add(new DataDetailbean("ENGINE", contentEntity.getENGINE() + ""));
            objects.add(new DataDetailbean("INDEX_LENGTH", contentEntity.getINDEX_LENGTH() + ""));
            objects.add(new DataDetailbean("MAX_DATA_LENGTH", contentEntity.getMAX_DATA_LENGTH() + ""));
            objects.add(new DataDetailbean("ROW_FORMAT", contentEntity.getROW_FORMAT() + ""));
            objects.add(new DataDetailbean("TABLE_CATALOG", contentEntity.getTABLE_CATALOG() + ""));
            objects.add(new DataDetailbean("TABLE_COLLATION", contentEntity.getTABLE_COLLATION() + ""));
            objects.add(new DataDetailbean("TABLE_COMMENT", contentEntity.getTABLE_COMMENT() + ""));
            objects.add(new DataDetailbean("TABLE_NAME", contentEntity.getTABLE_NAME() + ""));
            objects.add(new DataDetailbean("TABLE_ROWS", contentEntity.getTABLE_ROWS() + ""));
            objects.add(new DataDetailbean("TABLE_SCHEMA", contentEntity.getTABLE_SCHEMA() + ""));
            objects.add(new DataDetailbean("TABLE_TYPE", contentEntity.getTABLE_TYPE() + ""));
            objects.add(new DataDetailbean("UPDATE_TIME", contentEntity.getUPDATE_TIME() + ""));
            objects.add(new DataDetailbean("VERSION", contentEntity.getVERSION() + ""));
            adapter.notifyDataSetChanged();
        }
    }
}
