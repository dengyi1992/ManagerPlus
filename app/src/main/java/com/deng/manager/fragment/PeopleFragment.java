package com.deng.manager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigUserGvItemAdapter;
import com.deng.manager.adapter.CardBigUserItemAdapter;
import com.deng.manager.bean.clientUserBean;
import com.deng.manager.utils.HttpUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
    private CardBigUserItemAdapter adapterlv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    adapterlv.notifyDataSetChanged();
                    adaptergv.notifyDataSetChanged();
                    break;
                case ERROR:
                    Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                    break;
            }
            mProgressBar.setVisibility(View.GONE);
        }
    };

    private FloatingActionButton mPushMessageFloatingActionButton;
    private FloatingActionButton mChangeViewFloatingActionButton;
    private FloatingActionMenu mMessageFloatingActionMenu;
    private GridView mGvGridView;
    private CardBigUserGvItemAdapter adaptergv;
    private boolean islv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, null);
        islv = true;
        successBeens = new ArrayList<>();
        initView(view);

        return view;
    }

    private void initView(View view) {
        listViewPeople = (ListView) view.findViewById(R.id.people_lv);
        mGvGridView = (GridView) view.findViewById(R.id.people_gv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mPushMessageFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.push_message);
        mPushMessageFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mChangeViewFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.change_view);
        mChangeViewFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               islv=!islv;
                if (islv){
                    listViewPeople.setVisibility(View.VISIBLE);
                    mGvGridView.setVisibility(View.GONE);
                }else {
                    listViewPeople.setVisibility(View.GONE);
                    mGvGridView.setVisibility(View.VISIBLE);
                }
            }
        });
        mMessageFloatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.menu_message);

        adapterlv = new CardBigUserItemAdapter(getContext(), successBeens);
        adaptergv = new CardBigUserGvItemAdapter(getContext(), successBeens);
        listViewPeople.setAdapter(adapterlv);
        mGvGridView.setAdapter(adaptergv);
        if (islv){
            listViewPeople.setVisibility(View.VISIBLE);
            mGvGridView.setVisibility(View.GONE);
        }else {
            listViewPeople.setVisibility(View.GONE);
            mGvGridView.setVisibility(View.VISIBLE);
        }
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
