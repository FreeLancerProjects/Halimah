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
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Profile_Nursery;
import com.appzone.halimah.models.UserModel;

import java.util.List;
import java.util.Locale;

public class ServiceAdapter_Fragment_Nursery_Profile extends RecyclerView.Adapter<ServiceAdapter_Fragment_Nursery_Profile.MyHolder>{

    private Context context;
    private List<UserModel.Nursery_Service> serviceModelList;
    private Fragment_Profile_Nursery fragment_profile_nursery;
    public ServiceAdapter_Fragment_Nursery_Profile(Context context, List<UserModel.Nursery_Service> serviceModelList,Fragment_Profile_Nursery fragment_profile_nursery) {
        this.context = context;
        this.serviceModelList = serviceModelList;
        this.fragment_profile_nursery = fragment_profile_nursery;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row_profile,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder,int position) {
        UserModel.Nursery_Service serviceModel = serviceModelList.get(position);
        holder.BindData(serviceModel);
        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel.Nursery_Service serviceModel = serviceModelList.get(holder.getAdapterPosition());
                fragment_profile_nursery.deleteItem(serviceModel,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_service_name;
        private ImageView image_delete;
        public MyHolder(View itemView) {
            super(itemView);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
            image_delete = itemView.findViewById(R.id.image_delete);

        }

        public void BindData(UserModel.Nursery_Service serviceModel)
        {
            String lang = Locale.getDefault().getLanguage();
            if (lang.equals("ar"))
            {
                tv_service_name.setText(serviceModel.getAr_service_title());
            }else
            {
                tv_service_name.setText(serviceModel.getEn_service_title());
            }


        }
    }
}
