package com.example.meetup.view.category.time;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meetup.R;
import com.example.meetup.view.adapter.ViewPagerAdapter;
import com.example.meetup.view.category.CustomViewPager;
import com.example.meetup.view.home.event.EventViewModel;
import com.example.meetup.view.home.event.EventsFragment;
import com.example.meetup.view.home.news.NewsFragment;
import com.google.android.material.tabs.TabLayout;

public class SearchFragment extends Fragment {
    private int ZERO = 0;
    String keyword;
    ViewPagerAdapter adapter;
    CustomViewPager viewpager;
    TabLayout tabLayout;
    SearchViewModel mViewModel;
    TextView tvNoResult;

    public SearchFragment(String keyword) {
        this.keyword = keyword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mViewModel = new SearchViewModel();
        viewpager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabLayout);
        tvNoResult = view.findViewById(R.id.tvNoResult);
        if (mViewModel.getCountHappen(keyword) + mViewModel.getCountEnd(keyword) == ZERO) {
            tvNoResult.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setupWithViewPager(viewpager);
            adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addFrag(new EventCategoryFragment(0, mViewModel, keyword), getString(R.string.eventEnded)+ " (" + mViewModel.getCountEnd(keyword) + ")");
            adapter.addFrag(new EventCategoryFragment(1, mViewModel, keyword), getString(R.string.eventHappen)+ " (" + mViewModel.getCountHappen(keyword) + ")");
            viewpager.setAdapter(adapter);

        }
        return view;
    }
}