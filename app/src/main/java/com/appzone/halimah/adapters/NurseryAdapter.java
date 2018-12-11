package com.appzone.halimah.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.fragments.fragment_home.Fragment_Home;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.appzone.halimah.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NurseryAdapter extends RecyclerView.Adapter<NurseryAdapter.MyHolder> {

    private Context context;
    private List<Slider_Nursery_Model.NurseryModel> nurseryModelList;
    private Fragment_Home fragment_home;
    public NurseryAdapter(Context context, List<Slider_Nursery_Model.NurseryModel> nurseryModelList, Fragment_Home fragment_home) {
        this.context = context;
        this.nurseryModelList = nurseryModelList;
        this.fragment_home = fragment_home;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nursery_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        Slider_Nursery_Model.NurseryModel nurseryModel = nurseryModelList.get(position);
        holder.BindData(nurseryModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Slider_Nursery_Model.NurseryModel nurseryModel = nurseryModelList.get(holder.getAdapterPosition());
                fragment_home.setItem(nurseryModel);

            }
        });
    }

    @Override
    public int getItemCount() {
        return nurseryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RoundedImageView image;
        private TextView tv_name,tv_work_hour,tv_distance,tv_hour_cost;
        private SimpleRatingBar rateBar;
        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_work_hour = itemView.findViewById(R.id.tv_work_hour);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_hour_cost = itemView.findViewById(R.id.tv_hour_cost);
            rateBar = itemView.findViewById(R.id.rateBar);

        }

        private void BindData(Slider_Nursery_Model.NurseryModel nurseryModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_PATH+nurseryModel.getUser_image())).into(image);
            tv_name.setText(nurseryModel.getUser_full_name());
            tv_hour_cost.setText(nurseryModel.getHour_cost()+" "+context.getString(R.string.sar_hour));
            tv_distance.setText(String.valueOf(Math.round(Double.parseDouble(nurseryModel.getDst())))+" "+context.getString(R.string.km));
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
            Date date1 = new Date(Long.parseLong(nurseryModel.getFrom_hour()));
            String from_hour = dateFormat.format(date1);

            Date date2 = new Date(Long.parseLong(nurseryModel.getTo_hour()));
            String to_hour = dateFormat.format(date2);

            String time = from_hour+"-"+to_hour;
            tv_work_hour.setText(time);

            SimpleRatingBar.AnimationBuilder  builder = rateBar.getAnimationBuilder();
            builder.setRepeatCount(0);
            builder.setInterpolator(new AccelerateInterpolator());
            builder.setRatingTarget((float)nurseryModel.getStars_num());
            builder.setRepeatMode(ValueAnimator.REVERSE);
            builder.setDuration(2000);
            builder.start();

        }
    }


}
