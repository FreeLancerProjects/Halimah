package com.appzone.halimah.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.models.ReservationModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Reservation_Details_Adapter extends RecyclerView.Adapter<Fragment_Reservation_Details_Adapter.MyHolder> {

    private Context context;
    private List<ReservationModel.Reservation_Details_Model> reservationModelList;
    public Fragment_Reservation_Details_Adapter(Context context, List<ReservationModel.Reservation_Details_Model> notificationModelList) {
        this.context = context;
        this.reservationModelList = notificationModelList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_details_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ReservationModel.Reservation_Details_Model reservation_details_model = reservationModelList.get(position);
        holder.BindData(reservation_details_model);


    }

    @Override
    public int getItemCount() {
        return reservationModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_date,tv_from,tv_to,tv_cost;
        public MyHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_from = itemView.findViewById(R.id.tv_from);
            tv_to = itemView.findViewById(R.id.tv_to);
            tv_cost = itemView.findViewById(R.id.tv_cost);


        }

        private void BindData(ReservationModel.Reservation_Details_Model reservation_details_model)
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


}
