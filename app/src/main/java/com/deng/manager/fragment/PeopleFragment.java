package com.deng.manager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.deng.manager.R;

/**
 * Created by deng on 16-5-6.
 */
public class PeopleFragment extends Fragment {

    private ListView listViewPeople;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, null);
        listViewPeople = (ListView) view.findViewById(R.id.people_lv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        return view;
    }
}
