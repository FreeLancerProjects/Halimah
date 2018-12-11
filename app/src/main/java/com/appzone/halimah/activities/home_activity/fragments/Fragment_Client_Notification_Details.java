package com.appzone.halimah.activities.home_activity.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.adapters.Notification_Client_Order_Details_Adapter;
import com.appzone.halimah.models.NotificationModel;
import com.appzone.halimah.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Fragment_Client_Notification_Details extends Fragment {
    private static final String TAG="DATA";
    private TextView tv_name,tv_phone,tv_cost,tv_msg,tv_accept_counter,tv_refuse_counter;
    private ImageView image;
    private Button btn_delete;
    private LinearLayout ll_call,ll_accept,ll_refuse;
    private RecyclerView recView;
    private CardView card_reservation_declined;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private NotificationModel notificationModel;
    private List<NotificationModel.Reservation_Details_Model> reservationDetailsModelList;
    private double total_cost=0.0;
    private int counter_accept=0,counter_refuse=0;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_notification_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Notification_Details getInstance(NotificationModel notificationModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,notificationModel);
        Fragment_Client_Notification_Details fragment_client_notification_details = new Fragment_Client_Notification_Details();
        fragment_client_notification_details.setArguments(bundle);
        return fragment_client_notification_details;
    }

    private void initView(View view) {
        homeActivity= (HomeActivity) getActivity();
        reservationDetailsModelList = new ArrayList<>();
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_cost = view.findViewById(R.id.tv_cost);
        tv_msg = view.findViewById(R.id.tv_msg);
        tv_accept_counter = view.findViewById(R.id.tv_accept_counter);
        tv_refuse_counter = view.findViewById(R.id.tv_refuse_counter);

        ll_call = view.findViewById(R.id.ll_call);
        ll_accept = view.findViewById(R.id.ll_accept);
        ll_refuse = view.findViewById(R.id.ll_refuse);

        ll_accept.setEnabled(false);
        ll_refuse.setEnabled(false);

        btn_delete = view.findViewById(R.id.btn_delete);



        card_reservation_declined = view.findViewById(R.id.card_reservation_declined);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            notificationModel = (NotificationModel) bundle.getSerializable(TAG);
            UpdateUI(notificationModel);
        }

        ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+notificationModel.getUser_phone()));
                getActivity().startActivity(intent);
            }
        });

        ll_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter_accept=0;
                counter_refuse=0;
                total_cost=0;

                payment();
            }
        });
        ll_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_Reservation();
            }
        });

    }

    public void UpdateUI(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
        this.total_cost = Double.parseDouble(notificationModel.getReservation_cost());
        Log.e("approve",notificationModel.getApproved());
        Log.e("transfere",notificationModel.getTransformated());

        if (notificationModel.getApproved().equals(Tags.APPROVED_DECLINED))
        {
            tv_msg.setText(getString(R.string.reserv_decline));
            ll_accept.setVisibility(View.GONE);
            ll_refuse.setVisibility(View.GONE);
            btn_delete.setVisibility(View.VISIBLE);
            card_reservation_declined.setVisibility(View.VISIBLE);

        }else if (notificationModel.getApproved().equals(Tags.APPROVED_ACCEPTED)&&notificationModel.getTransformated().equals(Tags.CLIENT_CAN_TRANSFORM))
        {

            if (canAccept(notificationModel.getReservation_details()))
            {
                adapter = new Notification_Client_Order_Details_Adapter(getActivity(),reservationDetailsModelList,this);
                reservationDetailsModelList.addAll(notificationModel.getReservation_details());
                recView.setAdapter(adapter);
                ll_accept.setVisibility(View.VISIBLE);
                ll_refuse.setVisibility(View.VISIBLE);
                card_reservation_declined.setVisibility(View.GONE);
            }else
            {
                ll_accept.setVisibility(View.GONE);
                ll_refuse.setVisibility(View.GONE);
                tv_msg.setText(R.string.date_old);
                card_reservation_declined.setVisibility(View.VISIBLE);
            }

        }else if (notificationModel.getApproved().equals(Tags.APPROVED_ACCEPTED)&&notificationModel.getTransformated().equals(Tags.CANNOT_TRANSFORM))
        {
            ll_accept.setVisibility(View.GONE);
            ll_refuse.setVisibility(View.GONE);
            tv_msg.setText(R.string.cost_ransfer_confirmed);
            card_reservation_declined.setVisibility(View.VISIBLE);

        }


        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+notificationModel.getUser_image())).into(image);
        tv_name.setText(notificationModel.getUser_full_name());
        tv_cost.setText(notificationModel.getReservation_cost()+" "+getString(R.string.sar));
        tv_phone.setText(notificationModel.getUser_phone());

    }

    private void payment()
    {
        counter_accept=0;
        counter_refuse=0;
        total_cost=0;
        homeActivity.Display_Fragment_Payment();
    }
    private void Delete_Reservation() {
        homeActivity.client_accept_refuse_all_payment();
    }
    private boolean canAccept(List<NotificationModel.Reservation_Details_Model> reservation_details_modelList)
    {
        int counter=0;

        for (NotificationModel.Reservation_Details_Model reservation_details_model:reservation_details_modelList)
        {
            Calendar now = Calendar.getInstance();

            Date date_now = now.getTime();

            Calendar reserve_calender = Calendar.getInstance();
            reserve_calender.setTime(new Date(Long.parseLong(reservation_details_model.getReservation_date())));
            Date reserve_date = reserve_calender.getTime();

            if (reserve_date.before(date_now))
            {
                counter++;
            }
        }

        if (counter<reservation_details_modelList.size())
        {
            return true;
        }else
        {
            return false;

        }
    }

    public void setAcceptItem(NotificationModel.Reservation_Details_Model reservation_details_model, LinearLayout ll_accept, LinearLayout ll_refuse) {
        ll_accept.setEnabled(false);
        ll_accept.setAlpha(.5f);
        ll_refuse.setEnabled(true);
        ll_refuse.setAlpha(1.0f);
        adapter.notifyDataSetChanged();
        String id = reservation_details_model.getId();

        Log.e("total_cost",notificationModel.getReservation_cost());

        if (homeActivity.refuse_id_list.contains(id))
        {
            counter_refuse--;
            tv_refuse_counter.setText("("+String.valueOf(counter_refuse)+")");
            homeActivity.refuse_id_list.remove(id);
            total_cost +=Double.parseDouble(reservation_details_model.getTotal_hour_cost());
            Log.e("hour_cost",reservation_details_model.getTotal_hour_cost());

            tv_cost.setText(String.valueOf(total_cost));
        }

        if (!homeActivity.accept_id_list.contains(id))
        {
            counter_accept++;
            tv_accept_counter.setText("("+String.valueOf(counter_accept)+")");
            homeActivity.accept_id_list.add(id);
        }

        if ((homeActivity.accept_id_list.size()+homeActivity.refuse_id_list.size())==notificationModel.getReservation_details().size())
        {
            if (homeActivity.accept_id_list.size()==0)
            {
                this.ll_accept.setAlpha(0.5f);
                this.ll_refuse.setAlpha(1.0f);
                this.ll_accept.setEnabled(false);
                this.ll_refuse.setEnabled(true);
            }else if (homeActivity.refuse_id_list.size()==0)
            {
                this.ll_accept.setAlpha(1.0f);
                this.ll_refuse.setAlpha(0.5f);
                this.ll_accept.setEnabled(true);
                this.ll_refuse.setEnabled(false);
            }else
                {
                    this.ll_accept.setAlpha(1.0f);
                    this.ll_refuse.setAlpha(1.0f);
                    this.ll_accept.setEnabled(true);
                    this.ll_refuse.setEnabled(true);
                }
        }

    }
    public void setRefuseItem(NotificationModel.Reservation_Details_Model reservation_details_model, LinearLayout ll_accept, LinearLayout ll_refuse) {

        Log.e("total_cost",notificationModel.getReservation_cost());

        ll_accept.setEnabled(true);
        ll_accept.setAlpha(1.0f);
        ll_refuse.setEnabled(false);
        ll_refuse.setAlpha(0.5f);
        adapter.notifyDataSetChanged();

        String id = reservation_details_model.getId();
        if (homeActivity.accept_id_list.contains(id))
        {
            counter_accept--;
            tv_accept_counter.setText("("+String.valueOf(counter_accept)+")");
            homeActivity.accept_id_list.remove(id);

        }
        if (!homeActivity.refuse_id_list.contains(id))
        {
            counter_refuse++;
            tv_refuse_counter.setText("("+String.valueOf(counter_refuse)+")");
            homeActivity.refuse_id_list.add(id);
            Log.e("hour_cost",reservation_details_model.getTotal_hour_cost());
            total_cost-=Double.parseDouble(reservation_details_model.getTotal_hour_cost());
            tv_cost.setText(String.valueOf(total_cost));
        }



        if ((homeActivity.accept_id_list.size()+homeActivity.refuse_id_list.size())==notificationModel.getReservation_details().size())
        {
            if (homeActivity.accept_id_list.size()==0)
            {
                this.ll_accept.setAlpha(0.5f);
                this.ll_refuse.setAlpha(1.0f);
                this.ll_accept.setEnabled(false);
                this.ll_refuse.setEnabled(true);
            }else if (homeActivity.refuse_id_list.size()==0)
            {
                this.ll_accept.setAlpha(1.0f);
                this.ll_refuse.setAlpha(0.5f);
                this.ll_accept.setEnabled(true);
                this.ll_refuse.setEnabled(false);
            }else
            {
                this.ll_accept.setAlpha(1.0f);
                this.ll_refuse.setAlpha(1.0f);
                this.ll_accept.setEnabled(true);
                this.ll_refuse.setEnabled(true);
            }
        }

    }
    public void setRefuseItem2(NotificationModel.Reservation_Details_Model reservation_details_model,CardView cardRefuse) {

        cardRefuse.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();

        String id = reservation_details_model.getId();
        if (!homeActivity.refuse_id_list.contains(id))
        {
            counter_refuse++;
            tv_refuse_counter.setText("("+String.valueOf(counter_refuse)+")");
            homeActivity.refuse_id_list.add(id);
            total_cost-=Double.parseDouble(reservation_details_model.getTotal_hour_cost());
            tv_cost.setText(String.valueOf(total_cost));
        }


        if ((homeActivity.accept_id_list.size()+homeActivity.refuse_id_list.size())==notificationModel.getReservation_details().size())
        {
            if (homeActivity.accept_id_list.size()==0)
            {
                this.ll_accept.setAlpha(0.5f);
                this.ll_refuse.setAlpha(1.0f);
                this.ll_accept.setEnabled(false);
                this.ll_refuse.setEnabled(true);
            }else if (homeActivity.refuse_id_list.size()==0)
            {
                this.ll_accept.setAlpha(1.0f);
                this.ll_refuse.setAlpha(0.5f);
                this.ll_accept.setEnabled(true);
                this.ll_refuse.setEnabled(false);
            }else
            {
                this.ll_accept.setAlpha(1.0f);
                this.ll_refuse.setAlpha(1.0f);
                this.ll_accept.setEnabled(true);
                this.ll_refuse.setEnabled(true);
            }
        }

    }


}

