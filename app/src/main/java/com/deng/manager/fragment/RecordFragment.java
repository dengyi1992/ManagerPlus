package com.deng.manager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigLogAdapter;
import com.deng.manager.bean.LogBean;
import com.deng.manager.utils.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by deng on 16-5-12.
 */
public class RecordFragment extends Fragment {
    private static final int GETREQUEST = 1;
    private ListView mLogLvListView;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETREQUEST:
                    if (result.contains("success")){
                        Gson gson = new Gson();
                        LogBean logBean = gson.fromJson(result, LogBean.class);
                        successBeen.addAll(logBean.getSuccess());
                        adapter.notifyDataSetChanged();
                    }else {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String err = jsonObject.getString("error");
                            Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mProgressBarProgressBar.setVisibility(View.GONE);
                    break;

            }
        }
    };
    private ProgressBar mProgressBarProgressBar;
    private String result;
    private ArrayList<LogBean.SuccessBean> successBeen;
    private CardBigLogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        successBeen = new ArrayList<>();
        mLogLvListView = (ListView) view.findViewById(R.id.log_lv);
        mProgressBarProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        adapter = new CardBigLogAdapter(getContext(), successBeen);
        mLogLvListView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mProgressBarProgressBar.setVisibility(View.VISIBLE);
        HttpUtils.doGetAsynWithCookie(getString(R.string.getrecord), getContext(), new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                RecordFragment.this.result = result;
                handler.sendEmptyMessage(GETREQUEST);
            }
        });
    }
}
