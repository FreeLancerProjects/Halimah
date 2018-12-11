package com.appzone.halimah.activities.home_activity.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.adapters.Fragment_Reservation_Details_Adapter;
import com.appzone.halimah.models.ReservationModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Reservation_Details extends Fragment {
    private static final String TAG="DATA";
    private TextView tv_name,tv_phone,tv_cost;
    private ImageView image;
    private LinearLayout ll_call,ll_accept,ll_refuse;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ReservationModel reservationModel;
    private UserSingleTone userSingleTone;
    private UserModel userModel;

    private List<ReservationModel.Reservation_Details_Model> reservationModelList;

    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_notification_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Reservation_Details getInstance(ReservationModel reservationModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,reservationModel);
        Fragment_Reservation_Details fragment_client_notification_details = new Fragment_Reservation_Details();
        fragment_client_notification_details.setArguments(bundle);
        return fragment_client_notification_details;
    }

    private void initView(View view) {
        homeActivity= (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        reservationModelList = new ArrayList<>();
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_cost = view.findViewById(R.id.tv_cost);
        ll_call = view.findViewById(R.id.ll_call);
        ll_accept = view.findViewById(R.id.ll_accept);
        ll_refuse = view.findViewById(R.id.ll_refuse);

        ll_accept.setVisibility(View.GONE);
        ll_refuse.setVisibility(View.GONE);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            reservationModel = (ReservationModel) bundle.getSerializable(TAG);
            UpdateUI(reservationModel);
        }

        ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+reservationModel.getUser_phone()));
                getActivity().startActivity(intent);
            }
        });

    }

    public void UpdateUI(ReservationModel reservationModel) {
        this.reservationModel = reservationModel;
        if (reservationModel.getUser_type().equals(Tags.CLIENT_TYPE))
        {
            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+userModel.getUser_image())).into(image);



        }else if (reservationModel.getUser_type().equals(Tags.NURSERY_TYPE))
        {
            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+reservationModel.getUser_image())).into(image);

        }
        tv_name.setText(reservationModel.getUser_full_name());
        tv_cost.setText(reservationModel.getReservation_cost()+" "+getString(R.string.sar));
        tv_phone.setText(reservationModel.getUser_phone());
        reservationModelList.addAll(reservationModel.getReservation_details());
        adapter = new Fragment_Reservation_Details_Adapter(getActivity(),reservationModelList);
        recView.setAdapter(adapter);


    }



}

