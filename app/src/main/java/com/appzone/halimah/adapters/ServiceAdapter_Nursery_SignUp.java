package com.appzone.halimah.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.sign_up_activity.fragments.Fragment_Nursery_SignUp;
import com.appzone.halimah.models.ServiceModel;

import java.util.List;
import java.util.Locale;

public class ServiceAdapter_Nursery_SignUp extends RecyclerView.Adapter<ServiceAdapter_Nursery_SignUp.MyHolder>{

    private Context context;
    private Fragment fragment;
    private List<ServiceModel> serviceModelList;
    private SparseBooleanArray sparseBooleanArray;
    public ServiceAdapter_Nursery_SignUp(Context context, Fragment fragment, List<ServiceModel> serviceModelList) {
        this.context = context;
        this.fragment = fragment;
        this.serviceModelList = serviceModelList;
        sparseBooleanArray = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder,int position) {
        ServiceModel serviceModel = serviceModelList.get(position);
        holder.BindData(serviceModel);

        if (sparseBooleanArray.size()>0)
        {
            if (sparseBooleanArray.get(position))
            {
                holder.checkBox.setChecked(true);
            }else
                {
                    holder.checkBox.setChecked(false);

                }
        }else
            {
                holder.checkBox.setChecked(false);
            }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceModel serviceModel = serviceModelList.get(holder.getAdapterPosition());

                if (holder.checkBox.isChecked())
                {

                    sparseBooleanArray.put(holder.getAdapterPosition(),true);
                    if (fragment instanceof Fragment_Nursery_SignUp)
                    {
                        Fragment_Nursery_SignUp fragment_nursery_signUp = (Fragment_Nursery_SignUp) fragment;
                        fragment_nursery_signUp.AddItem(serviceModel);
                    }




                }else
                {
                    sparseBooleanArray.put(holder.getAdapterPosition(),false);

                    if (fragment instanceof Fragment_Nursery_SignUp)
                    {
                        Fragment_Nursery_SignUp fragment_nursery_signUp = (Fragment_Nursery_SignUp) fragment;
                        fragment_nursery_signUp.RemoveItem(serviceModel);
                    }

                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        public MyHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void BindData(ServiceModel serviceModel)
        {
            String lang = Locale.getDefault().getLanguage();
            if (lang.equals("ar"))
            {
                checkBox.setText(serviceModel.getAr_service_title());
            }else
            {
                checkBox.setText(serviceModel.getEn_service_title());
            }


        }
    }
}
