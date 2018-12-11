package com.appzone.halimah.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Client_Notification_Details;
import com.appzone.halimah.models.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Notification_Client_Order_Details_Adapter extends RecyclerView.Adapter {
    private final int ITEM_ACCEPT_REFUSE=1;
    private final int ITEM_REFUSE=2;

    private Context context;
    private List<NotificationModel.Reservation_Details_Model> reservationDetailsModelList;
    private Fragment_Client_Notification_Details fragment;

    public Notification_Client_Order_Details_Adapter(Context context, List<NotificationModel.Reservation_Details_Model> reservationDetailsModelList,Fragment_Client_Notification_Details fragment) {
        this.context = context;
        this.reservationDetailsModelList = reservationDetailsModelList;
        this.fragment=fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_ACCEPT_REFUSE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.accept_refuse_row,parent,false);
            return new Holder_Accept_Refuse(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.refuse_row,parent,false);
                return new Holder_Refuse(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        NotificationModel.Reservation_Details_Model reservation_details_model = reservationDetailsModelList.get(position);

        if (holder instanceof Holder_Accept_Refuse)
        {
            final Holder_Accept_Refuse holder_accept_refuse = (Holder_Accept_Refuse) holder;
            holder_accept_refuse.BindData(reservation_details_model);
            holder_accept_refuse.ll_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationModel.Reservation_Details_Model reservation_details_model = reservationDetailsModelList.get(holder_accept_refuse.getAdapterPosition());
                    fragment.setAcceptItem(reservation_details_model,holder_accept_refuse.ll_accept,holder_accept_refuse.ll_refuse);
                }
            });
            holder_accept_refuse.ll_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationModel.Reservation_Details_Model reservation_details_model = reservationDetailsModelList.get(holder_accept_refuse.getAdapterPosition());
                    fragment.setRefuseItem(reservation_details_model,holder_accept_refuse.ll_accept,holder_accept_refuse.ll_refuse);

                }
            });
        }else if (holder instanceof Holder_Refuse)
        {
            final Holder_Refuse holder_refuse = (Holder_Refuse) holder;
            holder_refuse.BindData(reservation_details_model);

            holder_refuse.ll_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationModel.Reservation_Details_Model reservation_details_model = reservationDetailsModelList.get(holder_refuse.getAdapterPosition());
                    fragment.setRefuseItem2(reservation_details_model,holder_refuse.card_refuse);

                }
            });
        }
    }


    public class Holder_Accept_Refuse extends RecyclerView.ViewHolder {
        private TextView tv_date,tv_from,tv_to,tv_cost;
        private LinearLayout ll_accept,ll_refuse;
        public Holder_Accept_Refuse(View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tv_from = itemView.findViewById(R.id.tv_from);
            tv_to = itemView.findViewById(R.id.tv_to);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            ll_accept = itemView.findViewById(R.id.ll_accept);
            ll_refuse = itemView.findViewById(R.id.ll_refuse);



        }

        public void BindData(NotificationModel.Reservation_Details_Model reservation_details_model)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
            String reserve_date = dateFormat.format(new Date(Long.parseLong(reservation_details_model.getReservation_date())));
            String from = timeFormat.format(new Date(Long.parseLong(reservation_details_model.getReservation_from_hour())));
            String to = timeFormat.format(new Date(Long.parseLong(reservation_details_model.getReservation_to_hour())));

            tv_date.setText(reserve_date);
            tv_from.setText(from);
            tv_to.setText(to);
            tv_cost.setText(reservation_details_model.getTotal_hour_cost()+" "+context.getString(R.string.sar));

        }
    }

    public class Holder_Refuse extends RecyclerView.ViewHolder {
        private TextView tv_date,tv_from,tv_to,tv_cost,tv_state;
        private CardView card_refuse;
        private LinearLayout ll_refuse;

        public Holder_Refuse(View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tv_from = itemView.findViewById(R.id.tv_from);
            tv_to = itemView.findViewById(R.id.tv_to);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_state = itemView.findViewById(R.id.tv_state);
            ll_refuse = itemView.findViewById(R.id.ll_refuse);
            card_refuse = itemView.findViewById(R.id.card_refuse);

        }

        public void BindData(NotificationModel.Reservation_Details_Model reservation_details_model)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
            String reserve_date = dateFormat.format(new Date(Long.parseLong(reservation_details_model.getReservation_date())));
            String from = timeFormat.format(new Date(Long.parseLong(reservation_details_model.getReservation_from_hour())));
            String to = timeFormat.format(new Date(Long.parseLong(reservation_details_model.getReservation_to_hour())));

            tv_date.setText(reserve_date);
            tv_from.setText(from);
            tv_to.setText(to);
            tv_cost.setText(reservation_details_model.getTotal_hour_cost()+" "+context.getString(R.string.sar));
            tv_state.setText(R.string.out_of_date);
        }
    }



    @Override
    public int getItemCount() {
        return reservationDetailsModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        NotificationModel.Reservation_Details_Model reservation_details_model = reservationDetailsModelList.get(position);
        if (isNewDate(reservation_details_model))
        {
            return ITEM_ACCEPT_REFUSE;

        }else
            {
                return ITEM_REFUSE;

            }
    }

    private boolean isNewDate (NotificationModel.Reservation_Details_Model reservation_details_model)
    {
        Calendar now_calender = Calendar.getInstance();
        Calendar reserve_calender = Calendar.getInstance();
        reserve_calender.setTime(new Date(Long.parseLong(reservation_details_model.getReservation_date())));

        Date date_now = now_calender.getTime();

        Date date_reserve = reserve_calender.getTime();

        if (date_reserve.before(date_now))
        {

            return false;
        }else
            {

                return true;
            }


    }
}
