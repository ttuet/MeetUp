package com.example.meetup.view.category;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.meetup.R;
import com.example.meetup.model.dataLocal.Category;
import com.example.meetup.repository.EventsRepository;
import com.example.meetup.view.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class EventByCategoryFragment extends Fragment {
    public String POPULAR = "PHỔ BIẾN";
    ViewPagerAdapter adapter_category;
    ViewPager viewpager;
    TabLayout tabLayout;
    TextView tvCategory;
    Category category;
    ImageView iconBack;
    EventsRepository repository = EventsRepository.getInstance();

    public int getCount(int categoryId) {
        return repository.getCountEventByCategory(categoryId);
    }

    public EventByCategoryFragment(Category category) {
        this.category = category;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_category, container, false);

        viewpager = view.findViewById(R.id.vpListEvent);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewpager);
        tvCategory = view.findViewById(R.id.tvCategory);
        iconBack = view.findViewById(R.id.ivIconBack);
        tvCategory.setText(category.getName() + " (" + getCount(category.getId()) + ")");
        adapter_category = new ViewPagerAdapter(getChildFragmentManager());
        adapter_category.addFrag(new PopularEventFragment(category.getId()), POPULAR);
        viewpager.setAdapter(adapter_category);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }
}