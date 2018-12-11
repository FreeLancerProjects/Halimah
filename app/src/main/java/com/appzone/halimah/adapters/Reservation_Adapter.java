package com.appzone.halimah.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Client_Current_Reservations;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Client_New_Reservations;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Nursery_Current_Reservations;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.Fragment_Nursery_New_Reservations;
import com.appzone.halimah.models.ReservationModel;
import com.appzone.halimah.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Reservation_Adapter extends RecyclerView.Adapter<Reservation_Adapter.MyHolder> {

    private Context context;
    private List<ReservationModel> reservationModelList;
    private Fragment fragment;
    public Reservation_Adapter(Context context, List<ReservationModel> notificationModelList, Fragment fragment) {
        this.context = context;
        this.reservationModelList = notificationModelList;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ReservationModel reservationModel = reservationModelList.get(position);
        holder.BindData(reservationModel);
        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationModel reservationModel = reservationModelList.get(holder.getAdapterPosition());
                if (fragment instanceof Fragment_Client_New_Reservations)
                {
                    Fragment_Client_New_Reservations fragment_client_new_reservations = (Fragment_Client_New_Reservations) fragment;
                    fragment_client_new_reservations.setItem(reservationModel);

                }else if (fragment instanceof Fragment_Client_Current_Reservations)
                {
                    Fragment_Client_Current_Reservations fragment_client_current_reservations = (Fragment_Client_Current_Reservations) fragment;
                    fragment_client_current_reservations.setItem(reservationModel);

                }else if (fragment instanceof Fragment_Nursery_New_Reservations)
                {
                    Fragment_Nursery_New_Reservations fragment_nursery_new_reservations = (Fragment_Nursery_New_Reservations) fragment;
                    fragment_nursery_new_reservations.setItem(reservationModel);

                }else if (fragment instanceof Fragment_Nursery_Current_Reservations)
                {
                    Fragment_Nursery_Current_Reservations fragment_nursery_current_reservations = (Fragment_Nursery_Current_Reservations) fragment;
                    fragment_nursery_current_reservations.setItem(reservationModel);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservationModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_not_date,tv_name;
        private Button btn_details;
        private RoundedImageView image;
        public MyHolder(View itemView) {
            super(itemView);
            tv_not_date = itemView.findViewById(R.id.tv_not_date);
            tv_name = itemView.findViewById(R.id.tv_name);
            btn_details = itemView.findViewById(R.id.btn_details);
            image = itemView.findViewById(R.id.image);


        }

        private void BindData(ReservationModel reservationModel)
        {

            long not_date_time_stamp = Long.parseLong(reservationModel.getNotification_date())*1000;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String not_date = dateFormat.format(new Date(not_date_time_stamp));

            tv_not_date.setText(not_date);
            if (reservationModel.getUser_type().equals(Tags.CLIENT_TYPE))
            {
                image.setImageResource(R.drawable.logo);

            }else  if (reservationModel.getUser_type().equals(Tags.NURSERY_TYPE))
            {
                Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+reservationModel.getUser_image())).into(image);
            }
            tv_name.setText(reservationModel.getUser_full_name());

        }
    }


}
