package com.appzone.halimah.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.models.Slider_Nursery_Model;

import java.util.List;
import java.util.Locale;

public class ServiceAdapter_Fragment_Service extends RecyclerView.Adapter<ServiceAdapter_Fragment_Service.MyHolder>{

    private Context context;
    private List<Slider_Nursery_Model.NurseryModel.ServiceModel> serviceModelList;
    public ServiceAdapter_Fragment_Service(Context context, List<Slider_Nursery_Model.NurseryModel.ServiceModel> serviceModelList) {
        this.context = context;
        this.serviceModelList = serviceModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row_details,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder,int position) {
        Slider_Nursery_Model.NurseryModel.ServiceModel serviceModel = serviceModelList.get(position);
        holder.BindData(serviceModel);

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_service_name;
        public MyHolder(View itemView) {
            super(itemView);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
        }

        public void BindData(Slider_Nursery_Model.NurseryModel.ServiceModel serviceModel)
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
