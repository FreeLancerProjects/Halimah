package com.appzone.halimah.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Notifications;
import com.appzone.halimah.models.NotificationModel;
import com.appzone.halimah.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyHolder> {

    private Context context;
    private List<NotificationModel> notificationModelList;
    private Fragment_Notifications fragment_notifications;
    public Notification_Adapter(Context context, List<NotificationModel> notificationModelList, Fragment_Notifications fragment_notifications) {
        this.context = context;
        this.notificationModelList = notificationModelList;
        this.fragment_notifications = fragment_notifications;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        NotificationModel notificationModel = notificationModelList.get(position);
        holder.BindData(notificationModel);
        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationModel notificationModel = notificationModelList.get(holder.getAdapterPosition());
                fragment_notifications.setItemData(notificationModel,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
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

        private void BindData(NotificationModel notificationModel)
        {

            long not_date_time_stamp = Long.parseLong(notificationModel.getNotification_date())*1000;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String not_date = dateFormat.format(new Date(not_date_time_stamp));

            tv_not_date.setText(not_date);
            if (notificationModel.getUser_type().equals(Tags.CLIENT_TYPE))
            {
                image.setImageResource(R.drawable.logo);

            }else  if (notificationModel.getUser_type().equals(Tags.NURSERY_TYPE))
            {
                Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+notificationModel.getUser_image())).into(image);
            }
            tv_name.setText(notificationModel.getUser_full_name());

        }
    }


}
