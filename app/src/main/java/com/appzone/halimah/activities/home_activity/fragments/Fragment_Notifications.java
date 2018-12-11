package com.appzone.halimah.activities.home_activity.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.adapters.Notification_Adapter;
import com.appzone.halimah.models.NotificationModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Notifications extends Fragment {
    private static final String TAG="DATA";
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<NotificationModel> notificationModelList;
    private ProgressBar progBar;
    private LinearLayout ll_no_not;
    private UserModel userModel;
    private HomeActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Notifications getInstance(UserModel userModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,userModel);
        Fragment_Notifications fragment_notifications = new Fragment_Notifications();
        fragment_notifications.setArguments(bundle);
        return fragment_notifications;
    }
    private void initView(View view)
    {
        activity = (HomeActivity) getActivity();
        notificationModelList = new ArrayList<>();
        recView =view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new Notification_Adapter(getActivity(),notificationModelList,this);
        recView.setAdapter(adapter);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_not = view.findViewById(R.id.ll_no_not);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            userModel = (UserModel) bundle.getSerializable(TAG);
            UpdateUI(userModel);
        }

    }
    private void UpdateUI(UserModel userModel)
    {

        if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
        {
            getClientNotification(userModel.getUser_id());
        }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
        {
            getNurseryNotification(userModel.getUser_id());
        }
    }

    public void getNurseryNotification(String user_id) {
        Api.getService()
                .getNurseryNotifications(user_id)
                .enqueue(new Callback<List<NotificationModel>>() {
                    @Override
                    public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            notificationModelList.clear();
                            notificationModelList.addAll(response.body());
                            if (notificationModelList.size()>0)
                            {
                                ll_no_not.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }else
                                {
                                    ll_no_not.setVisibility(View.VISIBLE);

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                            Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });
    }

    public void getClientNotification(String user_id) {
        Api.getService()
                .getClientNotifications(user_id)
                .enqueue(new Callback<List<NotificationModel>>() {
                    @Override
                    public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            notificationModelList.clear();
                            notificationModelList.addAll(response.body());
                            if (notificationModelList.size()>0)
                            {
                                ll_no_not.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }else
                            {
                                ll_no_not.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                            Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });
    }


    public void setItemData(NotificationModel notificationModel, int pos) {
        Log.e("s111",notificationModel.getUser_full_name());
        activity.Display_Fragment_Notification_Details(notificationModel);
        activity.setLastSelectedNotificationItem(pos);
    }

    public void removeItem_Refresh_Adapter(int pos)
    {
        notificationModelList.remove(pos);
        adapter.notifyItemRemoved(pos);

        if (notificationModelList.size()>0)
        {
            ll_no_not.setVisibility(View.GONE);
        }else
            {
                ll_no_not.setVisibility(View.VISIBLE);

            }
    }
}
