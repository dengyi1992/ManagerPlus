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
import com.deng.manager.adapter.CardBigUserItemAdapter;
import com.deng.manager.bean.clientUserBean;
import com.deng.manager.utils.HttpUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by deng on 16-5-6.
 */
public class PeopleFragment extends Fragment {

    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private ListView listViewPeople;
    private ProgressBar mProgressBar;
    private ArrayList<clientUserBean.SuccessBean> successBeens;
    private CardBigUserItemAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    adapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
            mProgressBar.setVisibility(View.GONE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, null);
        successBeens = new ArrayList<>();
        initView(view);

        return view;
    }

    private void initView(View view) {
        listViewPeople = (ListView) view.findViewById(R.id.people_lv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        adapter = new CardBigUserItemAdapter(getContext(), successBeens);
        listViewPeople.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mProgressBar.setVisibility(View.VISIBLE);
        HttpUtils.doGetAsynWithCookie(getString(R.string.getuserinfo), getContext(), new HttpUtils.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                if (result.contains("success")) {
                    Gson gson = new Gson();
                    clientUserBean clientUserBean = gson.fromJson(result, clientUserBean.class);
                    successBeens.clear();
                    successBeens.addAll(clientUserBean.getSuccess());
                    handler.sendEmptyMessage(SUCCESS);

                } else {
                    handler.sendEmptyMessage(ERROR);
                }
            }
        });
    }
}
