package com.deng.manager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng on 16-5-6.
 */
public class DatabaseFragment extends Fragment {

    private ListView listViewDatabase;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabButton;
    private List<DataBaseInfo.ContentEntity> contents;
    private CardBigDatabaseAdapter cardBigDatabaseAdapter;

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
        setUpFAB(view);
        contents = new ArrayList<DataBaseInfo.ContentEntity>();
        cardBigDatabaseAdapter = new CardBigDatabaseAdapter(getContext(), contents);
        listViewDatabase.setAdapter(cardBigDatabaseAdapter);
        listViewDatabase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("-----单击------");
            }
        });
        listViewDatabase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(view.getContext()).setTitle("选择对已选表的操作")
                        .setMessage("表名： " + contents.get(position).getTABLE_NAME())
                        .setPositiveButton("查看详情", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                /**
                                 * 跳转到详情页面
                                 * 先判断是否是行数大于0如果大于0的话就可以跳转
                                 */
                                if (contents.get(position).getTABLE_ROWS() > 0) {
                                    //跳转到详情页面
                                } else {
                                    Toast.makeText(getContext(), "该数据库没有数据！！！", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
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
                                Toast.makeText(getContext(), "清空数据", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return true;
            }
        });
    }

    private void setUpFAB(View view) {
        mFabButton = (FloatingActionButton) view.findViewById(R.id.fab_normal);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mProgressBar.setVisibility(View.VISIBLE);
        HttpUtils.doGetAsyn("http://120.27.41.245:3000/database_info", new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                if (result.contains("success")) {
                    System.out.println(result);
                    Gson gson = new Gson();
                    DataBaseInfo dataBaseInfo = gson.fromJson(result, DataBaseInfo.class);
                    contents.clear();
                    contents.addAll(dataBaseInfo.getContent());
                    listViewDatabase.post(new Runnable() {
                        @Override
                        public void run() {
                            cardBigDatabaseAdapter.notifyDataSetChanged();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
