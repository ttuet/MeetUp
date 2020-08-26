package com.example.meetup.view.home.event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetup.R;
import com.example.meetup.databinding.FragmentDetailEventBinding;
import com.example.meetup.model.dataLocal.Category;
import com.example.meetup.model.dataLocal.Event;
import com.example.meetup.model.dataLocal.Venue;
import com.example.meetup.ulti.Define;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventDetailFragment extends Fragment {
    private Event event;
    private Category category;
    private Venue venue;
    RecyclerView recyclerView;
    NearEventAdapter adapter;
    List<Event> nearList;
    int status;
    private FragmentDetailEventBinding binding;
    public EventViewModel viewModel;
    Context context;

    public EventDetailFragment(Event event, Context context, EventViewModel eventViewModel) {
        this.context = context;
        this.event = event;
        viewModel = eventViewModel;

    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_event, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        status = event.getMyStatus();

        this.category = viewModel.getCategory(event.getCategoryId());
        this.venue = viewModel.getVenue(event.getVenueId());
        nearList = new ArrayList<>();
        binding.setCategory(category);
        binding.setEvent(event);
        binding.setVenue(venue);

        recyclerView = binding.rvEventNear;
        setUpRecyclerView();
        setWithStatus(status);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(new NearEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FragmentManager fm;
                fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                EventDetailFragment fragment = new EventDetailFragment(nearList.get(position), context, viewModel);
                fragmentTransaction.replace(R.id.activity, fragment);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        mapData(context);
        if (viewModel.getCheckLogin() == EventViewModel.GUESS) {
            binding.btnJoined.setVisibility(View.GONE);
            binding.btnCanJoin.setVisibility(View.GONE);
        } else {
            binding.btnJoined.setVisibility(View.VISIBLE);
            binding.btnCanJoin.setVisibility(View.VISIBLE);
        }
        binding.btnJoined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = Define.STATUS_WENT;
                viewModel.updateEvent(status, event.getId());
                setWithStatus(status);
            }
        });

        binding.btnCanJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = Define.STATUS_GOING;
                viewModel.updateEvent(status, event.getId());
                setWithStatus(status);
            }
        });

        return binding.getRoot();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        nearList = viewModel.getEventNearList();
        adapter = new NearEventAdapter(nearList, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void mapData(Context context) {
        Glide.with(context).load(event.getPhoto()).into(binding.ivImageDetail);
        binding.tvAmount.setText(event.getGoingCount() + " sẽ tham gia");
        binding.tvTime.setText(Define.checkDate(event));
    }

    @SuppressLint("ResourceAsColor")
    private void setWithStatus(int status) {
        if (status == Define.STATUS_GOING) {
            setWithGoing();
        } else if (status == Define.STATUS_WENT) {
            setWent();
        } else {
            setNoJoin();
        }
    }

    private void setWithGoing() {
        binding.btnCanJoin.setSelected(false);
        binding.btnCanJoin.setEnabled(false);
        binding.btnJoined.setSelected(true);
        binding.btnJoined.setEnabled(true);
    }

    private void setWent() {
        binding.btnCanJoin.setSelected(false);
        binding.btnCanJoin.setEnabled(false);
        binding.btnJoined.setSelected(false);
        binding.btnJoined.setEnabled(false);
    }

    private void setNoJoin() {
        binding.btnCanJoin.setSelected(true);
        binding.btnCanJoin.setEnabled(true);
        binding.btnJoined.setSelected(false);
        binding.btnJoined.setEnabled(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.refresh();

    }

}