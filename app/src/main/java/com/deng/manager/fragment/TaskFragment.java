package com.deng.manager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigTaskAdapter;
import com.deng.manager.bean.DataBaseInfo;
import com.deng.manager.bean.Task;
import com.deng.manager.utils.HttpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by deng on 16-5-6.
 */
public class TaskFragment extends Fragment {

    private ListView listViewTask;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabButton;
    private ArrayList<Task.ContentBean> contents;
    private CardBigTaskAdapter cardBigTaskAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, null);
        listViewTask = (ListView) view.findViewById(R.id.task_lv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        contents = new ArrayList<Task.ContentBean>();
        cardBigTaskAdapter = new CardBigTaskAdapter(getContext(), contents);
        listViewTask.setAdapter(cardBigTaskAdapter);
        setUpFAB(view);
        return view;
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
        HttpUtils.doGetAsyn("http://120.27.41.245:3001/admin/tasksetting_info", new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                if (result.contains("success")) {
                    System.out.println(result);
                    Gson gson = new Gson();
                    Task task = gson.fromJson(result, Task.class);
                    contents.clear();
                    contents.addAll(task.getContent());
                    listViewTask.post(new Runnable() {
                        @Override
                        public void run() {
                            cardBigTaskAdapter.notifyDataSetChanged();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
