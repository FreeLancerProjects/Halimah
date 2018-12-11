package com.appzone.halimah.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.nursery_details.fragment.fragment_details.Fragment_Reserve;
import com.appzone.halimah.models.ReserveModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.MyHolder> {

    private Context context;
    private List<ReserveModel> reserveModelList;
    private Fragment_Reserve fragment_reserve;
    public ReserveAdapter(Context context, List<ReserveModel> reserveModelList, Fragment_Reserve fragment_reserve) {
        this.context = context;
        this.reserveModelList = reserveModelList;
        this.fragment_reserve = fragment_reserve;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserve_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ReserveModel reserveModel = reserveModelList.get(position);
        holder.BindData(reserveModel);
        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_reserve.delete(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return reserveModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_date,tv_from,tv_to;
        private ImageView image_delete;
        public MyHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_from = itemView.findViewById(R.id.tv_from);
            tv_to = itemView.findViewById(R.id.tv_to);
            image_delete = itemView.findViewById(R.id.image_delete);


        }

        private void BindData(ReserveModel reserveModel)
        {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa",Locale.getDefault());

            String date = dateFormat.format(new Date(reserveModel.getReservation_date()));
            String from_hour = timeFormat.format(new Date(reserveModel.getReservation_from_hour()));
            String to_hour = timeFormat.format(new Date(reserveModel.getReservation_to_hour()));

            tv_date.setText(date);
            tv_from.setText(from_hour);
            tv_to.setText(to_hour);

        }
    }


}
