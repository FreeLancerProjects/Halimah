package com.appzone.halimah.activities.home_activity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Client_Notification_Details;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_ContactUs;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Nursery_Notification_Details;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Payment;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Reservation_Details;
import com.appzone.halimah.activities.home_activity.fragments.fragment_home.Fragment_Home;
import com.appzone.halimah.activities.home_activity.fragments.fragment_my_reservations.my_reservations.Fragment_MyReservations;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Notifications;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Profile_Client;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Profile_Nursery;
import com.appzone.halimah.activities.home_activity.fragments.Fragment_Terms_Condition;
import com.appzone.halimah.activities.sign_in_activity.SignInActivity;
import com.appzone.halimah.models.LocationModel;
import com.appzone.halimah.models.NotificationModel;
import com.appzone.halimah.models.NotificationReadModel;
import com.appzone.halimah.models.ReservationModel;
import com.appzone.halimah.models.ResponseModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.preferences.Preferences;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.service.LocationUpdateService;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView tv_title;
    public  FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Profile_Client fragment_profile_client;
    private Fragment_Profile_Nursery fragment_profile_nursery;
    private Fragment_MyReservations fragment_myReservations;
    private Fragment_Reservation_Details fragment_reservation_details;
    private Fragment_Notifications fragment_notifications;
    private Fragment_ContactUs fragment_contactUs;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Client_Notification_Details fragment_client_notification_details;
    private Fragment_Nursery_Notification_Details fragment_nursery_notification_details;
    private Fragment_Payment fragment_payment;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private Preferences preferences;
    private RoundedImageView nursery_image;
    private ImageView image_logo;
    private TextView tv_name,tv_not;
    private FrameLayout fl_not;
    private int lastSelectNavItem;
    private Intent intentService;
    private final int gps_req = 102,loc_req=103;
    private String fineLoc = Manifest.permission.ACCESS_FINE_LOCATION;

    private boolean canRead=true;
    private double lat=0.0,lng=0.0;

    private BottomSheetBehavior behavior;
    private View root;
    private ImageView image_close_sheet;
    private TextView tv_sheet_title;

    //fragment_notification_details
    public List<String> accept_id_list,refuse_id_list;

    private NotificationModel notificationModel;

    private int lastNotificationItemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initViewSheet();

    }



    private void initView()
    {
        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer =  findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ////////////////////////////////////////////////////
        View view = navigationView.getHeaderView(0);
        nursery_image = view.findViewById(R.id.image);
        image_logo = view.findViewById(R.id.image_logo);
        tv_name = view.findViewById(R.id.tv_name);
        ///////////////////////////////////////////////////
        fragmentManager = getSupportFragmentManager();

        tv_title = findViewById(R.id.tv_title);
        tv_not = findViewById(R.id.tv_not);
        fl_not = findViewById(R.id.fl_not);

        fl_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Display_FragmentNotifications(userModel);
                ReadNotification();
            }
        });
        Display_FragmentHome();
        updateUi(userModel);
    }

    private void initViewSheet()
    {
        accept_id_list = new ArrayList<>();
        refuse_id_list = new ArrayList<>();
        root = findViewById(R.id.root);
        image_close_sheet = findViewById(R.id.image_close_sheet);
        tv_sheet_title = findViewById(R.id.tv_sheet_title);
        behavior = BottomSheetBehavior.from(root);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState==BottomSheetBehavior.STATE_DRAGGING)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        image_close_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
    }
    private void UpdateSheetTitle(String title)
    {
        tv_sheet_title.setText(title);
    }
    private void openSheet()
    {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void closeSheet()
    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if (fragment_client_notification_details!=null&&fragment_client_notification_details.isAdded())
        {
            fragmentManager.popBackStack("fragment_client_notification_details",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else if (fragment_nursery_notification_details!=null&&fragment_nursery_notification_details.isAdded())
        {
            fragmentManager.popBackStack("fragment_nursery_notification_details",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else if (fragment_reservation_details!=null&&fragment_reservation_details.isAdded())
        {
            fragmentManager.popBackStack("fragment_reservation_details",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }


    }


    public void updateUi(UserModel userModel)
    {

        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);

        }
        CheckPermission();

        if (userModel==null)
        {
            image_logo.setVisibility(View.VISIBLE);
            fl_not.setVisibility(View.GONE);

        }else
        {

            fl_not.setVisibility(View.VISIBLE);
            UpdateToken();
            getUnReadNotificationCount(userModel.getUser_id());
            if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
            {
                image_logo.setVisibility(View.VISIBLE);


            }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
            {
                nursery_image.setVisibility(View.VISIBLE);
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_PATH+userModel.getUser_image())).into(nursery_image);
                tv_name.setText(userModel.getUser_full_name());
            }
        }
    }
    public void UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        userSingleTone.setUserModel(userModel);
        preferences.Create_Update_UserModel(this,userModel);
        if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
        {
            image_logo.setVisibility(View.VISIBLE);


        }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
        {
            nursery_image.setVisibility(View.VISIBLE);
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_PATH+userModel.getUser_image())).into(nursery_image);
            tv_name.setText(userModel.getUser_full_name());
        }

    }
    private void getUnReadNotificationCount(String user_id)
    {
        if (userModel.getUser_type().equals(Tags.CLIENT_TYPE)){
            Api.getService()
                    .getUnreadClientNotificationCount(user_id)
                    .enqueue(new Callback<NotificationReadModel>() {
                        @Override
                        public void onResponse(Call<NotificationReadModel> call, Response<NotificationReadModel> response) {
                            if (response.isSuccessful())
                            {
                                canRead=true;
                                UpdateNotificationUI(response.body().getAlert_count());
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationReadModel> call, Throwable t) {
                            try {
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
        {
            Api.getService()
                    .getUnreadNurseryNotificationCount(user_id)
                    .enqueue(new Callback<NotificationReadModel>() {
                        @Override
                        public void onResponse(Call<NotificationReadModel> call, Response<NotificationReadModel> response) {
                            if (response.isSuccessful())
                            {
                                canRead=true;
                                UpdateNotificationUI(response.body().getAlert_count());

                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationReadModel> call, Throwable t) {
                            try {
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }

    }
    public void ReadNotification()
    {
        if (canRead)
        {
            if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
            {
                Api.getService()
                        .readClientNotificationCount(userModel.getUser_id(),"1")
                        .enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                if (response.isSuccessful())
                                {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.cancelAll();
                                    canRead=false;
                                    if (response.body().getSuccess_read()==1)
                                    {
                                        UpdateNotificationUI(0);
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                try {
                                    Log.e("Error",t.getMessage());
                                }catch (Exception e) {}
                            }
                        });
            }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
            {
                Api.getService()
                        .readNurseryNotificationCount(userModel.getUser_id(),"1")
                        .enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                if (response.isSuccessful())
                                {
                                    canRead=false;
                                    if (response.body().getSuccess_read()==1)
                                    {
                                        UpdateNotificationUI(0);

                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                try {
                                    Log.e("Error",t.getMessage());
                                }catch (Exception e) {}
                            }
                        });
            }

        }

    }
    public void UpdateNotificationUI(int count)
    {
        Log.e("Notification Count",count+"_");
        if (count>0)
        {
            tv_not.setText(String.valueOf(count));
            tv_not.setVisibility(View.VISIBLE);
        }else
            {
                tv_not.setVisibility(View.GONE);
            }
    }
    private void StartLocationUpdate()
    {

        Log.e("ssssssssssssssssss","ssssssssssssssssssss");
        intentService = new Intent(this, LocationUpdateService.class);
        startService(intentService);
    }
    private void StopLocationUpdate()
    {
        if (intentService!=null)
        {
            stopService(intentService);
        }
    }
    public void UpdateTitle(String title)
    {
        tv_title.setText(title);
    }

    private void CheckPermission()
    {
        Log.e("eeee","eeee");
        if (ContextCompat.checkSelfPermission(this,fineLoc)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] perm = {fineLoc};
            ActivityCompat.requestPermissions(this,perm,loc_req);
        }else
        {
            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
            {
                CreateGpsDialog();
            }
        }
    }
    private boolean isGpsOpen()
    {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (manager!=null&&manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }

        return false;
    }
    private void OpenGps()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,gps_req);
    }
    private void CreateGpsDialog()
    {
        final AlertDialog gps_dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(R.string.app_open_gps);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps_dialog.dismiss();
                OpenGps();
            }
        });

        gps_dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog;
        gps_dialog.setView(view);
        gps_dialog.setCanceledOnTouchOutside(false);
        gps_dialog.show();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateLocationListning(LocationModel locationModel)
    {
        lat = locationModel.getLat();
        lng = locationModel.getLng();
        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragment_home.setLatLong(lat,lng);
        }

        if (userModel!=null)
        {
            if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
            {
                UpdateLocation(locationModel);

            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToNotification(NotificationReadModel notificationReadModel)
    {

        if (fragment_notifications!=null&&fragment_notifications.isAdded())
        {
            if (fragment_notifications.isVisible())
            {
                ReadNotification();
                if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
                {
                    fragment_notifications.getClientNotification(userModel.getUser_id());
                }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
                {
                    fragment_notifications.getNurseryNotification(userModel.getUser_id());

                }
            }else
                {
                    canRead=true;
                    getUnReadNotificationCount(userModel.getUser_id());
                    if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
                    {
                        fragment_notifications.getClientNotification(userModel.getUser_id());
                    }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
                    {
                        fragment_notifications.getNurseryNotification(userModel.getUser_id());

                    }
                }
        }else
            {
                canRead=true;
                getUnReadNotificationCount(userModel.getUser_id());

            }
    }
    private void UpdateLocation(LocationModel locationModel)
    {
        Log.e("home_lat",locationModel.getLat()+"_");
        Api.getService()
                .updateLocation(userModel.getUser_id(),locationModel.getLat(),locationModel.getLng())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_location()==1)
                            {
                                Log.e("Location_update","Location updated successfully");
                            }else
                            {
                                Log.e("Location_update","Location updated failed");

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdateToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (task.isSuccessful())
                {
                    String token = task.getResult().getToken();

                    Api.getService()
                            .updateToken(userModel.getUser_id(),token)
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful())
                                    {
                                        if (response.body().getSuccess_token_id()==1)
                                        {
                                            Log.e("token updated","Token updated successfully");
                                        }else
                                        {
                                            Log.e("token updated","Token updated failed");

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    try {
                                        Log.e("Error",t.getMessage());

                                    }catch (Exception e){}
                                }
                            });
                }
            }
        });
    }
    public void Display_FragmentHome()
    {
        if (fragment_home==null)
        {
            fragment_home = Fragment_Home.getInstance();
        }

        if (!fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container,fragment_home,"fragment_home").addToBackStack("fragment_home").commit();

        }else
        {
            fragment_home = (Fragment_Home) fragmentManager.findFragmentByTag("fragment_home");
            fragmentManager.beginTransaction().show(fragment_home).commit();
        }


        lastSelectNavItem =0;
        UpdateTitle(getString(R.string.home));
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigationView.getMenu().getItem(0).setChecked(true);
                    }
                },500);


        if (fragment_profile_client !=null&& fragment_profile_client.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_client).commit();
        }

        if (fragment_profile_nursery !=null&& fragment_profile_nursery.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_nursery).commit();
        }

        if (fragment_myReservations !=null&& fragment_myReservations.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myReservations).commit();
        }
        if (fragment_notifications!=null&&fragment_notifications.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_contactUs!=null&&fragment_contactUs.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contactUs).commit();
        }

        if (fragment_terms_condition!=null&&fragment_terms_condition.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_terms_condition).commit();
        }


    }
    public void Display_FragmentProfile_Client()
    {
            lastSelectNavItem =1;
            UpdateTitle(getString(R.string.profile));
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigationView.getMenu().getItem(1).setChecked(true);
                        }
                    },500);


            if (fragment_home!=null&&fragment_home.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }

            if (fragment_myReservations !=null&& fragment_myReservations.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_myReservations).commit();
            }

            if (fragment_notifications!=null&&fragment_notifications.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }

            if (fragment_contactUs!=null&&fragment_contactUs.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_contactUs).commit();
            }

            if (fragment_terms_condition!=null&&fragment_terms_condition.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_terms_condition).commit();
            }

            if (fragment_profile_client ==null)
            {
                fragment_profile_client = Fragment_Profile_Client.getInstance();
                //fragment_profile_client.setUserModel(userModel);
            }else
            {
               // fragment_profile_client.setUserModel(userModel);

            }
            if (fragment_profile_client.isAdded())
            {
                fragment_profile_client = (Fragment_Profile_Client) fragmentManager.findFragmentByTag("fragment_profile_client");
                fragmentManager.beginTransaction().show(fragment_profile_client).commit();
            }else
            {

                fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container, fragment_profile_client,"fragment_profile_client").addToBackStack("fragment_profile_client").commit();

            }

    }
    public void Display_FragmentProfile_Nursery()
    {
        lastSelectNavItem =1;
        UpdateTitle(getString(R.string.profile));
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigationView.getMenu().getItem(1).setChecked(true);
                    }
                },500);


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_myReservations !=null&& fragment_myReservations.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myReservations).commit();
        }

        if (fragment_notifications!=null&&fragment_notifications.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_contactUs!=null&&fragment_contactUs.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contactUs).commit();
        }

        if (fragment_terms_condition!=null&&fragment_terms_condition.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_terms_condition).commit();
        }

        if (fragment_profile_nursery ==null) {
            fragment_profile_nursery = Fragment_Profile_Nursery.getInstance();
        }
        if (fragment_profile_nursery.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_profile_nursery).commit();
        }else
        {

            fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container, fragment_profile_nursery,"fragment_profile_nursery").addToBackStack("fragment_profile_nursery").commit();

        }
        }
    public void Display_FragmentNotifications(UserModel userModel)
    {
        UpdateTitle(getString(R.string.notification));

        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_myReservations !=null&& fragment_myReservations.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myReservations).commit();
        }

        if (fragment_profile_client !=null&& fragment_profile_client.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_client).commit();
        }
        if (fragment_profile_nursery !=null&& fragment_profile_nursery.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_nursery).commit();
        }

        if (fragment_contactUs!=null&&fragment_contactUs.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contactUs).commit();
        }

        if (fragment_terms_condition!=null&&fragment_terms_condition.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_terms_condition).commit();
        }

        if (fragment_notifications==null)
        {
            fragment_notifications = Fragment_Notifications.getInstance(userModel);
        }
        if (fragment_notifications.isAdded())
        {
            fragment_notifications = (Fragment_Notifications) fragmentManager.findFragmentByTag("fragment_notifications");
            fragmentManager.beginTransaction().show(fragment_notifications).commit();
        }else
        {

            fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container,fragment_notifications,"fragment_notifications").addToBackStack("fragment_notifications").commit();

        }
    }
    public void Display_FragmentMyReservations()
    {
        if (userModel==null)
        {
            Common.CreateUserNotSignInAlertDialog(this,getString(R.string.si_su));
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigationView.getMenu().getItem(0).setChecked(true);
                        }
                    },500);

        }else
        {
            lastSelectNavItem =2;
            UpdateTitle(getString(R.string.my_reservations));
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigationView.getMenu().getItem(2).setChecked(true);
                        }
                    },500);

            if (fragment_home!=null&&fragment_home.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }

            if (fragment_profile_client !=null&& fragment_profile_client.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_profile_client).commit();
            }
            if (fragment_profile_nursery !=null&& fragment_profile_nursery.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_profile_nursery).commit();
            }

            if (fragment_contactUs!=null&&fragment_contactUs.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_contactUs).commit();
            }

            if (fragment_terms_condition!=null&&fragment_terms_condition.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_terms_condition).commit();
            }
            if (fragment_notifications!=null&&fragment_notifications.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }
            if (fragment_myReservations ==null)
            {
                fragment_myReservations = fragment_myReservations.getInstance();
                fragment_myReservations.setUserModel(userModel);
                fragment_myReservations.setLat_Lng(lat,lng);
            }else
            {
                fragment_myReservations.setUserModel(userModel);
                fragment_myReservations.setLat_Lng(lat,lng);

            }
            if (fragment_myReservations.isAdded())
            {
                fragment_myReservations = (Fragment_MyReservations) fragmentManager.findFragmentByTag("fragment_my_reservation");
                fragmentManager.beginTransaction().show(fragment_myReservations).commit();
            }else
            {

                fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container, fragment_myReservations,"fragment_my_reservation").addToBackStack("fragment_my_reservation").commit();

            }
        }

    }

    public void DisplayFragmentReservationDetails(ReservationModel reservationModel)
    {
        fragment_reservation_details = Fragment_Reservation_Details.getInstance(reservationModel);
        fragmentManager.beginTransaction().add(R.id.fragment_home_sheet_container,fragment_reservation_details,"fragment_reservation_details").addToBackStack("fragment_reservation_details").commit();
        openSheet();
    }
    public void Display_FragmentContactUs()
    {
        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_profile_client !=null&& fragment_profile_client.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_client).commit();
        }

        if (fragment_profile_nursery !=null&& fragment_profile_nursery.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_nursery).commit();
        }

        if (fragment_myReservations !=null&& fragment_myReservations.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myReservations).commit();
        }



        if (fragment_notifications !=null&& fragment_notifications.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_terms_condition !=null&& fragment_terms_condition.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_terms_condition).commit();
        }

        if (fragment_contactUs==null)
        {
            fragment_contactUs = Fragment_ContactUs.getInstance();
        }

        if (fragment_contactUs.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_contactUs).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container,fragment_contactUs,"fragment_contactUs").addToBackStack("fragment_contactUs").commit();
        }
        UpdateTitle(getString(R.string.contact_us));

    }
    public void Display_FragmentTermsCondition()
    {
        if (fragment_terms_condition==null)
        {
            fragment_terms_condition = Fragment_Terms_Condition.getInstance();
        }

        if (fragment_terms_condition.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_terms_condition).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_activity_home_container,fragment_terms_condition,"fragment_terms_condition").addToBackStack("fragment_terms_condition").commit();
        }
        UpdateTitle(getString(R.string.terms_and_conditions));


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_profile_client !=null&& fragment_profile_client.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_client).commit();
        }

        if (fragment_profile_nursery !=null&& fragment_profile_nursery.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile_nursery).commit();
        }

        if (fragment_myReservations !=null&& fragment_myReservations.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myReservations).commit();
        }



        if (fragment_notifications !=null&& fragment_notifications.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }


        if (fragment_contactUs!=null&&fragment_contactUs.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contactUs).commit();

        }
    }
    public void Display_Fragment_Notification_Details(NotificationModel notificationModel)
    {
        this.refuse_id_list.clear();
        this.accept_id_list.clear();
        this.notificationModel = notificationModel;
        if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
        {

            if (fragment_payment!=null&&fragment_payment.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_payment).commit();
            }
            fragment_client_notification_details = Fragment_Client_Notification_Details.getInstance(notificationModel);


            if (fragment_client_notification_details.isAdded())
            {
                fragment_client_notification_details.UpdateUI(notificationModel);
                fragmentManager.beginTransaction().show(fragment_client_notification_details).commit();

            }else
                {
                    fragmentManager.beginTransaction().add(R.id.fragment_home_sheet_container,fragment_client_notification_details,"fragment_client_notification_details").addToBackStack("fragment_client_notification_details").commit();
                }
            UpdateSheetTitle(getString(R.string.details));
            openSheet();
        }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
        {


            fragment_nursery_notification_details = Fragment_Nursery_Notification_Details.getInstance(notificationModel);


            if (fragment_nursery_notification_details.isAdded())
            {
                fragment_nursery_notification_details.UpdateUI(notificationModel);
                fragmentManager.beginTransaction().show(fragment_nursery_notification_details).commit();
            }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_sheet_container,fragment_nursery_notification_details,"fragment_nursery_notification_details").addToBackStack("fragment_nursery_notification_details").commit();
            }
            UpdateSheetTitle(getString(R.string.details));
            openSheet();
        }

    }

    public void Display_Fragment_Payment()
    {
        if (accept_id_list.size()>0&&refuse_id_list.size()>0)
        {
            UpdateSheetTitle(getString(R.string.payment));
            if (fragment_client_notification_details!=null&&fragment_client_notification_details.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_client_notification_details).commit();
            }

            fragment_payment = Fragment_Payment.getInstance();


            if (fragment_payment.isAdded())
            {
                fragmentManager.beginTransaction().show(fragment_payment).commit();
            }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_sheet_container,fragment_payment,"fragment_payment").addToBackStack("fragment_payment").commit();
            }
        }else if (accept_id_list.size()>0)
        {
            UpdateSheetTitle(getString(R.string.payment));
            if (fragment_client_notification_details!=null&&fragment_client_notification_details.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_client_notification_details).commit();
            }

            fragment_payment = Fragment_Payment.getInstance();


            if (fragment_payment.isAdded())
            {
                fragmentManager.beginTransaction().show(fragment_payment).commit();
            }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_sheet_container,fragment_payment,"fragment_payment").addToBackStack("fragment_payment").commit();
            }
        }else if (refuse_id_list.size()>0)
        {
            client_accept_refuse_all_payment();

        }


    }


    public void setLastSelectedNotificationItem(int pos)
    {
        this.lastNotificationItemSelected = pos;
    }
    public void nursery_accept_refuse_reservations()
    {
        if (refuse_id_list.size()>0&&accept_id_list.size()>0)
        {
            nursery_accept_refuse_part_reservation();
        }else if (accept_id_list.size()>0)
        {
            nursery_accept_refuse_all_reservation(Tags.NURSERY_ACCEPT_ALL_RESERVATION);
        }else if (refuse_id_list.size()>0)
        {
            nursery_accept_refuse_all_reservation(Tags.NURSERY_REFUSE_ALL_RESERVATION);

        }
    }

    public void client_payment(String amount, Uri transfer_photo)
    {
        if (refuse_id_list.size()>0&&accept_id_list.size()>0)
        {
            client_accept_refuse_part_payment(Tags.CLIENT_ACCEPT_REFUSE_PART_PAYMENT,amount,transfer_photo);
        }else if (accept_id_list.size()>0)
        {
            client_accept_refuse_part_payment(Tags.CLIENT_ACCEPT_ALL_PAYMENT,amount,transfer_photo);
        }else if (refuse_id_list.size()>0)
        {
            client_accept_refuse_all_payment();

        }
    }

    private void client_accept_refuse_part_payment(String type, String amount, Uri transfer_photo)
    {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        List<RequestBody> accepted_ids_part  = getRequestBodyList(accept_id_list);
        List<RequestBody> refused_ids_part  = getRequestBodyList(refuse_id_list);
        RequestBody transform_type_part = Common.getRequestBodyText(type);
        RequestBody name_part = Common.getRequestBodyText(userModel.getUser_full_name());
        RequestBody phone_part = Common.getRequestBodyText(userModel.getUser_phone());
        RequestBody amount_part = Common.getRequestBodyText(amount);
        MultipartBody.Part image_part = Common.getMultiPart(this,transfer_photo,"transformation_image");
        Api.getService()
                .client_accept_refuse_part_payment(notificationModel.getId_reservation(),transform_type_part,accepted_ids_part,refused_ids_part,name_part,phone_part,amount_part,image_part)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            Log.e("status",response.body().isStatus()+"_");

                            if (response.body().getSuccess_transformation()==1)
                            {
                                Toast.makeText(HomeActivity.this,R.string.succ, Toast.LENGTH_SHORT).show();
                                fragment_notifications.removeItem_Refresh_Adapter(lastNotificationItemSelected);
                                closeSheet();

                            }else
                                {
                                    Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });


    }
    public void client_accept_refuse_all_payment()
    {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .client_accept_refuse_all_payment(notificationModel.getId_reservation(),Tags.CLIENT_REFUSE_ALL_PAYMENT)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            Log.e("trans",response.body().getSuccess_transformation()+"_");
                            if (response.body().getSuccess_transformation()==1)
                            {
                                Toast.makeText(HomeActivity.this,R.string.succ, Toast.LENGTH_SHORT).show();
                                fragment_notifications.removeItem_Refresh_Adapter(lastNotificationItemSelected);
                                closeSheet();
                            }else
                            {
                                Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void nursery_accept_refuse_all_reservation(String type)
    {
        final ProgressDialog dialog =Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .nursery_accept_refuse_all_reservations(userModel.getUser_id(),notificationModel.getId_reservation(),type)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_confirm()==1)
                            {
                                Toast.makeText(HomeActivity.this, R.string.succ, Toast.LENGTH_SHORT).show();
                                fragment_notifications.removeItem_Refresh_Adapter(lastNotificationItemSelected);
                                closeSheet();

                            }else
                                {
                                    Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void nursery_accept_refuse_part_reservation()
    {
        final ProgressDialog dialog =Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .nursery_accept_refuse_part_reservations(userModel.getUser_id(),notificationModel.getId_reservation(),Tags.NURSERY_ACCEPT_REFUSE_PART_RESERVATION,accept_id_list,refuse_id_list)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_confirm()==1)
                            {
                                Toast.makeText(HomeActivity.this, R.string.succ, Toast.LENGTH_SHORT).show();
                                fragment_notifications.removeItem_Refresh_Adapter(lastNotificationItemSelected);
                                closeSheet();

                            }else
                            {
                                Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private List<RequestBody> getRequestBodyList(List<String> idsList)
    {
        List<RequestBody> requestBodyList = new ArrayList<>();

        for (String id :idsList)
        {
            RequestBody requestBody = Common.getRequestBodyText(id);
            requestBodyList.add(requestBody);
        }

        return requestBodyList;
    }

    public void nursery_accept_refuse_payment()
    {
        if (refuse_id_list.size()>0&&accept_id_list.size()>0)
        {
            nursery_accept_refuse_part_payment();
        }else if (accept_id_list.size()>0)
        {
            nursery_accept_refuse_all_payment(Tags.NURSERY_ACCEPT_ALL_PAYMENT);
        }else if (refuse_id_list.size()>0)
        {
            nursery_accept_refuse_all_payment(Tags.NURSERY_REFUSE_ALL_PAYMENT);

        }
    }

    public void nursery_accept_refuse_all_payment(String type)
    {
        final ProgressDialog dialog =Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .nursery_accept_refuse_all_payment(userModel.getUser_id(),notificationModel.getId_reservation(),type)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_confirm()==1)
                            {
                                Toast.makeText(HomeActivity.this, R.string.succ, Toast.LENGTH_SHORT).show();
                                fragment_notifications.removeItem_Refresh_Adapter(lastNotificationItemSelected);
                                closeSheet();

                            }else
                            {
                                Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void nursery_accept_refuse_part_payment()
    {
        final ProgressDialog dialog =Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .nursery_accept_refuse_part_payment(userModel.getUser_id(),notificationModel.getId_reservation(),Tags.NURSERY_ACCEPT_PART_PAYMENT,accept_id_list,refuse_id_list)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_confirm()==1)
                            {
                                Toast.makeText(HomeActivity.this, R.string.succ, Toast.LENGTH_SHORT).show();
                                fragment_notifications.removeItem_Refresh_Adapter(lastNotificationItemSelected);
                                closeSheet();

                            }else
                            {
                                Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.home) {
            Display_FragmentHome();
        } else if (id == R.id.profile) {
            if (userModel==null)
            {
                Common.CreateUserNotSignInAlertDialog(this,getString(R.string.si_su));
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navigationView.getMenu().getItem(0).setChecked(true);
                            }
                        },500);
            }else
                {
                    if (userModel.getUser_type().equals(Tags.CLIENT_TYPE))
                    {
                        Display_FragmentProfile_Client();

                    }else if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
                    {
                        Display_FragmentProfile_Nursery();
                    }
                }


        } else if (id == R.id.reservation) {
            if (userModel==null)
            {
                Common.CreateUserNotSignInAlertDialog(this,getString(R.string.si_su));
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navigationView.getMenu().getItem(0).setChecked(true);
                            }
                        },500);

            }else
            {
                Display_FragmentMyReservations();

            }

        } else if (id == R.id.contact_us) {
            Display_FragmentContactUs();
        } else if (id == R.id.terms) {
            Display_FragmentTermsCondition();
        } else if (id == R.id.logout) {
            logout();

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout()
    {
        if (userModel==null)
        {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }else
        {
            final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.loggng_out));
            dialog.show();
            Api.getService()
                    .logout(userModel.getUser_id())
                    .enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful())
                            {
                                dialog.dismiss();
                                if (response.body().getSuccess_logout()==1)
                                {
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.cancelAll();
                                    userModel = null;
                                    userSingleTone.ClearUserModel();
                                    preferences.ClearData(HomeActivity.this);
                                    Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else
                                {
                                    Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            try {
                                dialog.dismiss();

                                Log.e("Error",t.getMessage());
                                Toast.makeText(HomeActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            }catch (Exception e){}
                        }
                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode==gps_req)
        {
            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
            {
                CreateGpsDialog();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode==loc_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (isGpsOpen())
                    {
                        StartLocationUpdate();
                    }else
                    {
                        CreateGpsDialog();
                    }
                }else
                {
                    Toast.makeText(this, R.string.acc_loc_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onBackPressed()
    {
       Back();
    }

    private void Back() {
        Log.e("back_stack",fragmentManager.getBackStackEntryCount()+"count");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            if (fragment_client_notification_details!=null&&fragment_client_notification_details.isVisible())
            {
                closeSheet();

            }else if (fragment_nursery_notification_details!=null&&fragment_nursery_notification_details.isVisible())
            {
                closeSheet();

            }else if (fragment_payment!=null&&fragment_payment.isVisible())
            {
                Display_Fragment_Notification_Details(this.notificationModel);

            }else if (fragment_reservation_details!=null&&fragment_reservation_details.isVisible())
            {
                closeSheet();

            }

        }


        else if (fragment_home!=null&&fragment_home.isVisible())
        {
            fragmentManager.popBackStack("fragment_home",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            finish();
        }else if (fragment_home!=null&&!fragment_home.isVisible())
        {
            Display_FragmentHome();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navigationView.getMenu().getItem(lastSelectNavItem).setChecked(true);
                        }
                    },500);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }

        StopLocationUpdate();
    }
}
