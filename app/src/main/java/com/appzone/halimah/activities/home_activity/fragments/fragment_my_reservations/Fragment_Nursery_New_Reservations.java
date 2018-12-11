package com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.adapters.Reservation_Adapter;
import com.appzone.halimah.models.ReservationModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.remote.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Nursery_New_Reservations extends Fragment {
    private static final String TAG="DATA";
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<ReservationModel> reservationModelList;
    private ProgressBar progBar;
    private LinearLayout ll_no_not;
    private UserModel userModel;
    private HomeActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_current_reservations,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Nursery_New_Reservations getInstance(UserModel userModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,userModel);
        Fragment_Nursery_New_Reservations fragment_nursery_new_reservations = new Fragment_Nursery_New_Reservations();
        fragment_nursery_new_reservations.setArguments(bundle);
        return fragment_nursery_new_reservations;
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();

        reservationModelList = new ArrayList<>();
        recView =view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter  = new Reservation_Adapter(getActivity(),reservationModelList,this);
        recView.setAdapter(adapter);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_not = view.findViewById(R.id.ll_no_not);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            userModel = (UserModel) bundle.getSerializable(TAG);
            UpdateUI(userModel);
        }

    }

    private void UpdateUI(UserModel userModel) {
        getData(userModel.getUser_id());
    }

    private void getData(String user_id) {
        Api.getService()
                .getNurseryNewReservations(user_id)
                .enqueue(new Callback<List<ReservationModel>>() {
                    @Override
                    public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            reservationModelList.addAll(response.body());
                            if (reservationModelList.size()>0)
                            {
                                ll_no_not.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }else
                            {
                                ll_no_not.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e)
                        {

                        }
                    }
                });
    }

    public void setItem(ReservationModel reservationModel) {
        activity.DisplayFragmentReservationDetails(reservationModel);

    }
}
