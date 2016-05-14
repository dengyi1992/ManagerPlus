package com.deng.manager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deng.manager.R;
import com.deng.manager.adapter.CardBigUserGvItemAdapter;
import com.deng.manager.adapter.CardBigUserItemAdapter;
import com.deng.manager.bean.clientUserBean;
import com.deng.manager.utils.HttpUtils;
import com.deng.manager.view.PushActivty;
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
    private String[] userType = new String[]{"热点", "视频", "图片", "娱乐", "科技", "汽车", "体育", "财经", "军事", "国际", "时尚", "旅游", "探索", "育儿", "养生", "故事", "美文", "游戏", "历史", "美食"};
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    adapterlv.notifyDataSetChanged();
                    adaptergv.notifyDataSetChanged();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
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
    private AbsListView lv;

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
                showMultiChoiceItems();
            }
        });
        mChangeViewFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.change_view);
        mChangeViewFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                islv = !islv;
                if (islv) {
                    listViewPeople.setVisibility(View.VISIBLE);
                    mGvGridView.setVisibility(View.GONE);
                } else {
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
        if (islv) {
            listViewPeople.setVisibility(View.VISIBLE);
            mGvGridView.setVisibility(View.GONE);
        } else {
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

    private void showMultiChoiceItems() {
        AlertDialog builder = new AlertDialog.Builder(getContext())
                .setTitle("请选择要推送的用户类型：")
                .setMultiChoiceItems(userType,
                        new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                        new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                // TODO Auto-generated method stub

                            }
                        })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String s = "您选择了：";
                        // 扫描所有的列表项，如果当前列表项被选中，将列表项的文本追加到s变量中。
                        double sum = 0;
                        for (int i = 0; i < userType.length; i++) {

                            if (lv.getCheckedItemPositions().get(i)) {
                                s += i + ":" + lv.getAdapter().getItem(i) + " ";
                                sum = sum + Math.pow(2, i);
                            }
                        }

                        // 用户至少选择了一个列表项
                        if (lv.getCheckedItemPositions().size() > 0) {
                            int sum1 = (int) sum;
                            final Intent intent = new Intent(getContext(), PushActivty.class);
                            intent.putExtra("showString",s);
                            intent.putExtra("code",sum1);

                            new AlertDialog.Builder(getContext())
                                    .setMessage("是否前往选择要推送的消息？")
                                    .setTitle(s)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                            System.out.println(lv.getCheckedItemPositions().size());
                        }

                        // 用户未选择任何列表项
                        else if (lv.getCheckedItemPositions().size() <= 0) {
                            new AlertDialog.Builder(getContext())
                                    .setMessage("您未选择任何用户类型").show();
                        }
                    }
                }).setNegativeButton("取消", null).create();
        //
        lv = builder.getListView();
        builder.show();

    }
}
