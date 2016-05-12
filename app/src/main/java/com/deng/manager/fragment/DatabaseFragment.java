package com.deng.manager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigDatabaseAdapter;
import com.deng.manager.bean.DataBaseInfo;
import com.deng.manager.utils.HttpUtils;
import com.deng.manager.view.DataBaseActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng on 16-5-6.
 */
public class DatabaseFragment extends Fragment {

    private static final int DATAUPDATE = 1;
    private static final int DELETED = 2;
    private ListView listViewDatabase;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabButton;
    public static List<DataBaseInfo.ContentEntity> contents;
    private CardBigDatabaseAdapter cardBigDatabaseAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATAUPDATE:
                    cardBigDatabaseAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case DELETED:
                    if (result.contains("success")) {
                        Toast.makeText(getContext(), "清空成功", Toast.LENGTH_SHORT).show();
                        initData();
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String err = jsonObject.getString("error");
                            Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    };
    private String result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        listViewDatabase = (ListView) view.findViewById(R.id.database_lv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        contents = new ArrayList<DataBaseInfo.ContentEntity>();
        cardBigDatabaseAdapter = new CardBigDatabaseAdapter(getContext(), contents);
        listViewDatabase.setAdapter(cardBigDatabaseAdapter);
        listViewDatabase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 跳转到详情页面
                 * 先判断是否是行数大于0如果大于0的话就可以跳转
                 */
                //跳转到详情页面
                Intent intent = new Intent(getContext(), DataBaseActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });
        listViewDatabase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(view.getContext()).setTitle("选择对已选表的操作")
                        .setMessage("表名： " + contents.get(position).getTABLE_NAME())
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("清空数据", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /**
                                 * 此处需要输入密码进行验证
                                 */
                                clearTable(contents.get(position).getTABLE_NAME());

                            }
                        }).show();
                return true;
            }
        });
    }

    private void clearTable(String tableName) {

        HttpUtils.doGetAsynWithCookie(getString(R.string.clear_table) + tableName, getContext(), new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                DatabaseFragment.this.result = result;
                handler.sendEmptyMessage(DELETED);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mProgressBar.setVisibility(View.VISIBLE);
        HttpUtils.doGetAsyn(getString(R.string.databaseinfo), new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                if (result.contains("success")) {
                    System.out.println(result);
                    Gson gson = new Gson();
                    DataBaseInfo dataBaseInfo = gson.fromJson(result, DataBaseInfo.class);
                    contents.clear();
                    contents.addAll(dataBaseInfo.getContent());
                    handler.sendEmptyMessage(DATAUPDATE);
                }
            }
        });
    }
}
