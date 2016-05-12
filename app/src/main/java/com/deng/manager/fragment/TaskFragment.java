package com.deng.manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.deng.manager.view.TaskEditActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by deng on 16-5-6.
 */
public class TaskFragment extends Fragment {

    private static final int REQUESTCODE = 1;
    private ListView listViewTask;
    private ArrayList<Task.ContentBean> contents;
    private CardBigTaskAdapter cardBigTaskAdapter;
    private ProgressBar mProgressBarProgressBar;
    private ListView mLvListView;
    private FloatingActionButton mAddFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, null);
        mLvListView = (ListView) view.findViewById(R.id.task_lv);
        mAddFloatingActionButton = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.task_add);
        mProgressBarProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mProgressBarProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listViewTask = (ListView) view.findViewById(R.id.task_lv);
        contents = new ArrayList<Task.ContentBean>();
        cardBigTaskAdapter = new CardBigTaskAdapter(getContext(), contents);
        listViewTask.setAdapter(cardBigTaskAdapter);
        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskEditActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mProgressBarProgressBar.setVisibility(View.VISIBLE);
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
                            mProgressBarProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }


}
