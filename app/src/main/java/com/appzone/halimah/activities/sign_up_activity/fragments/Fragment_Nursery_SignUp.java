package com.appzone.halimah.activities.sign_up_activity.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.activities.sign_up_activity.activity.SignUpActivity;
import com.appzone.halimah.adapters.ServiceAdapter_Nursery_SignUp;
import com.appzone.halimah.models.ServiceModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.preferences.Preferences;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Nursery_SignUp extends Fragment implements TimePickerDialog.OnTimeSetListener{

    private double nursery_lat = 0.0,nursery_lng = 0.0;
    private EditText edt_nursery_name,edt_nursery_phone,edt_nursery_officer_name,edt_nursery_officer_phone,edt_user_name,edt_password,edt_hour_cost;
    private LinearLayout ll_address;
    private TextView tv_address,tv_from,tv_to;
    private LinearLayout ll_choose_service;
    private FrameLayout fl_add_photo,fl_choose_icon;
    private CircleImageView image;
    private ExpandableLayout expand_layout;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<ServiceModel> serviceModelList,selectedServiceList;
    private List<String> service_id_list;
    private Button btn_signup;
    private final String readPerm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 11,img_req=12;
    private Uri uri = null;
    private ProgressBar progBar;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private TimePickerDialog timePickerDialog;
    private String type="";
    private long from_time=0,to_time=0;
    private SignUpActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nursery_sign_up,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Nursery_SignUp getInstance()
    {
        return new Fragment_Nursery_SignUp();
    }

    private void initView(View view)
    {

        activity = (SignUpActivity) getActivity();
        service_id_list = new ArrayList<>();
        serviceModelList = new ArrayList<>();
        selectedServiceList = new ArrayList<>();
        edt_nursery_name = view.findViewById(R.id.edt_nursery_name);
        edt_nursery_phone = view.findViewById(R.id.edt_nursery_phone);
        edt_nursery_officer_name = view.findViewById(R.id.edt_nursery_officer_name);
        edt_nursery_officer_phone = view.findViewById(R.id.edt_nursery_officer_phone);
        tv_address = view.findViewById(R.id.tv_address);
        edt_hour_cost = view.findViewById(R.id.edt_hour_cost);
        ll_address = view.findViewById(R.id.ll_address);

        edt_user_name = view.findViewById(R.id.edt_user_name);
        edt_password = view.findViewById(R.id.edt_password);
        ll_choose_service = view.findViewById(R.id.ll_choose_service);
        tv_from = view.findViewById(R.id.tv_from);
        tv_to = view.findViewById(R.id.tv_to);
        fl_add_photo = view.findViewById(R.id.fl_add_photo);
        fl_choose_icon = view.findViewById(R.id.fl_choose_icon);
        expand_layout = view.findViewById(R.id.expand_layout);
        image = view.findViewById(R.id.image);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        btn_signup = view.findViewById(R.id.btn_signup);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(true);

        adapter = new ServiceAdapter_Nursery_SignUp(getActivity(),this,serviceModelList);
        recView.setAdapter(adapter);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        fl_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkReadPermission();
            }
        });

        ll_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showBottomSheet();
            }
        });
        ll_choose_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceModelList.size()>0)
                {
                    if (expand_layout.isExpanded())
                    {
                        expand_layout.collapse(true);
                        CloseServiceUI();
                    }else
                        {
                            expand_layout.expand(true);
                            openServiceUI();
                        }
                }else
                    {
                        progBar.setVisibility(View.VISIBLE);
                        ll_choose_service.setAlpha(0.8f);
                        getServices();
                    }

            }
        });

        tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="from";
                CreateTimeDialog(type);

            }
        });

        tv_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from_time==0)
                {
                    tv_from.setError(getString(R.string.field_req));
                }else
                    {
                        type = "to";
                        CreateTimeDialog(type);
                    }


            }
        });


    }


    public void UpdateLocation_Data(String address,double lat,double lng)
    {
        tv_address.setText(address);
        nursery_lat = lat;
        nursery_lng=lng;
    }
    private void CheckData()
    {

        String m_nursery_name = edt_nursery_name.getText().toString();
        String m_nursery_phone = edt_nursery_phone.getText().toString();
        String m_nursery_officer_name = edt_nursery_officer_name.getText().toString();
        String m_nursery_officer_phone = edt_nursery_officer_phone.getText().toString();
        String m_address = tv_address.getText().toString();
        String m_hour_cost = edt_hour_cost.getText().toString();
        String m_user_name = edt_user_name.getText().toString();
        String m_password = edt_password.getText().toString();
        String regex = "((([a-zA-Z]+(_)?([0-9]+)?)+)?([0-9]+(_)?([a-zA-Z]+)?)?)+";


        if(!TextUtils.isEmpty(m_nursery_name)&&
                !TextUtils.isEmpty(m_nursery_phone)&&m_nursery_phone.length()>6&&m_nursery_phone.length()<13&&
                !TextUtils.isEmpty(m_nursery_officer_phone)&&m_nursery_officer_phone.length()>6&&m_nursery_officer_phone.length()<13&&
                !TextUtils.isEmpty(m_nursery_officer_name)&&
                !TextUtils.isEmpty(m_address)&&
                !TextUtils.isEmpty(m_hour_cost)&&
                !TextUtils.isEmpty(m_user_name)&&
                !TextUtils.isEmpty(m_password)&&
                from_time!=0&&to_time!=0&&
                selectedServiceList.size()>0&&
                uri!=null
                && m_user_name.matches(regex)

                )
        {
            for (ServiceModel serviceModel :selectedServiceList)
            {
                service_id_list.add(serviceModel.getId_service());
            }
            edt_nursery_name.setError(null);
            edt_nursery_phone.setError(null);
            edt_nursery_officer_name.setError(null);
            edt_nursery_officer_phone.setError(null);
            tv_address.setError(null);
            edt_hour_cost.setError(null);
            edt_user_name.setError(null);
            edt_password.setError(null);

            SignUp(m_nursery_name,m_nursery_phone,m_nursery_officer_name,m_nursery_officer_phone,m_address,m_hour_cost,from_time,to_time,m_user_name,m_password,service_id_list,uri);
        }else
        {
            if (TextUtils.isEmpty(m_nursery_name))
            {
                edt_nursery_name.setError(getString(R.string.name_req));
            }else
            {
                edt_nursery_name.setError(null);
            }

            if (TextUtils.isEmpty(m_nursery_phone))
            {
                edt_nursery_phone.setError(getString(R.string.phone_req));
            }else if (m_nursery_phone.length()<=6||m_nursery_phone.length()>=13)
            {
                edt_nursery_phone.setError(getString(R.string.inv_phone));
            }else
            {
                edt_nursery_phone.setError(null);
            }


            if (TextUtils.isEmpty(m_nursery_officer_name))
            {
                edt_nursery_officer_name.setError(getString(R.string.name_req));
            }else
            {
                edt_nursery_officer_name.setError(null);
            }

            if (TextUtils.isEmpty(m_nursery_officer_phone))
            {
                edt_nursery_officer_phone.setError(getString(R.string.phone_req));
            }else if (m_nursery_officer_phone.length()<=6||m_nursery_officer_phone.length()>=13)
            {
                edt_nursery_officer_phone.setError(getString(R.string.inv_phone));
            }else
            {
                edt_nursery_officer_phone.setError(null);
            }



            if (TextUtils.isEmpty(m_address))
            {
                tv_address.setError(getString(R.string.address_req));
            }else
            {
                tv_address.setError(null);

            }

            if (TextUtils.isEmpty(m_hour_cost))
            {
                edt_hour_cost.setError(getString(R.string.hour_cost_req));
            }else
            {
                edt_hour_cost.setError(null);

            }

            if (TextUtils.isEmpty(m_user_name))
            {
                edt_user_name.setError(getString(R.string.username_req));
            }else if (!m_user_name.matches(regex))
            {
                edt_user_name.setError(getString(R.string.inv_username));

            }else
            {
                edt_user_name.setError(null);

            }

            if (TextUtils.isEmpty(m_password))
            {
                edt_password.setError(getString(R.string.password_req));
            }else
            {
                edt_password.setError(null);
            }

            if (selectedServiceList.size()==0)
            {
                Toast.makeText(getActivity(),R.string.ch_service, Toast.LENGTH_SHORT).show();
            }

            if (uri==null)
            {
                Toast.makeText(getActivity(), R.string.ch_profile_photo, Toast.LENGTH_SHORT).show();
            }

            if (from_time==0)
            {
                tv_from.setError(getString(R.string.field_req));
            }else
                {
                    tv_from.setError(null);
                }

            if (to_time==0)
            {
                tv_to.setError(getString(R.string.field_req));
            }else
            {
                tv_to.setError(null);
            }
        }
    }


    private void SignUp(String m_nursery_name, String m_nursery_phone, String m_nursery_officer_name, String m_nursery_officer_phone, String m_address,String hour_cost,long from_time,long to_time,String m_user_name, String m_password,List<String>service_id_list, Uri uri)
    {
        Common.CloseKeyBoard(getActivity(),edt_nursery_name);

        RequestBody nursery_name_part = Common.getRequestBodyText(m_nursery_name);
        RequestBody nursery_phone_part = Common.getRequestBodyText(m_nursery_phone);
        RequestBody nursery_officer_name_part = Common.getRequestBodyText(m_nursery_officer_name);
        RequestBody nursery_officer_phone_part = Common.getRequestBodyText(m_nursery_officer_phone);
        RequestBody address_part = Common.getRequestBodyText(m_address);
        RequestBody user_name_part = Common.getRequestBodyText(m_user_name);
        RequestBody password_part = Common.getRequestBodyText(m_password);
        RequestBody from_time_part = Common.getRequestBodyText(String.valueOf(from_time));
        RequestBody to_time_part = Common.getRequestBodyText(String.valueOf(to_time));

        RequestBody lat_part = Common.getRequestBodyText(String.valueOf(nursery_lat));
        RequestBody lng_part = Common.getRequestBodyText(String.valueOf(nursery_lng));

        RequestBody hour_cost_part = Common.getRequestBodyText(hour_cost);

        List<RequestBody> requestBodyList_service_id_part = getRequestBodyList(service_id_list);
        RequestBody type_part = Common.getRequestBodyText(Tags.NURSERY_TYPE);
        MultipartBody.Part image_part = Common.getMultiPart(getActivity(),uri,"user_image");

        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.sinng_up));
        dialog.show();

        Api.getService()
                .sign_up_Nursery(nursery_name_part,user_name_part,password_part,nursery_phone_part,lat_part,lng_part,nursery_officer_name_part,from_time_part,to_time_part,type_part,address_part,hour_cost_part,nursery_officer_phone_part,requestBodyList_service_id_part,image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_signup()==1)
                            {
                                preferences = Preferences.getInstance();
                                userSingleTone = UserSingleTone.getInstance();
                                UserModel userModel = response.body();
                                preferences.Create_Update_UserModel(getActivity(),userModel);
                                userSingleTone.setUserModel(userModel);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }else if (response.body().getSuccess_signup()==2)
                            {
                                Toast.makeText(getActivity(), R.string.un_exist, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        try {
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });
    }

    private List<RequestBody> getRequestBodyList(List<String> service_id_list)
    {
        List<RequestBody> requestBodyList = new ArrayList<>();

        for (String id :service_id_list)
        {
            Log.e("service_id",id);
            RequestBody requestBody = Common.getRequestBodyText(id);
            requestBodyList.add(requestBody);
        }

        return requestBodyList;
    }

    private void CreateTimeDialog(String type)
    {

        Calendar calendar = Calendar.getInstance();

        if (type.equals("to"))
        {
            calendar.setTime(new Date(from_time));
            calendar.add(Calendar.HOUR_OF_DAY,1);
        }
        timePickerDialog = TimePickerDialog.newInstance(this,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);


        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            timePickerDialog.setLocale(new Locale("ar"));

        }else
            {
                timePickerDialog.setLocale(Locale.ENGLISH);

            }

        if (type.equals("to"))
        {
            timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
           /* calendar.add(Calendar.HOUR_OF_DAY,8);
            timePickerDialog.setMaxTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
*/
        }
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        timePickerDialog.setCancelColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        timePickerDialog.setOkColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setCancelText(R.string.cancel);
        timePickerDialog.show(getActivity().getFragmentManager(),"Time");
    }
    private void getServices()
    {

        Api.getService()
                .getAllServices()
                .enqueue(new Callback<List<ServiceModel>>() {
                    @Override
                    public void onResponse(Call<List<ServiceModel>> call, Response<List<ServiceModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            ll_choose_service.setAlpha(1.0f);
                            serviceModelList.clear();
                            serviceModelList.addAll(response.body());

                            if (serviceModelList.size()>0)
                            {

                                adapter.notifyDataSetChanged();
                                openServiceUI();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ServiceModel>> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void openServiceUI()
    {
        fl_choose_icon.setVisibility(View.VISIBLE);
        expand_layout.setExpanded(true,true);
    }
    private void CloseServiceUI()
    {
        fl_choose_icon.setVisibility(View.INVISIBLE);
        expand_layout.collapse(true);
    }
    private void checkReadPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),readPerm)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] perm = {readPerm};
            ActivityCompat.requestPermissions(getActivity(),perm,read_req);
        }else {
            SelectImage();
        }
    }
    private void SelectImage() {

        Intent intent;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {

            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent,img_req);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==img_req&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            uri = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri));
            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    SelectImage();
                }else
                {
                    Toast.makeText(getActivity(), R.string.acc_photo_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*public void Location(double lat, double lng)
    {
        myLat = lat;
        myLng = lng;

        Log.e("lat",lat+"_");
        Log.e("lng",lng+"_");

    }*/

    public void AddItem(ServiceModel serviceModel) {
        selectedServiceList.add(serviceModel);
    }

    public void RemoveItem(ServiceModel serviceModel) {
        selectedServiceList.remove(serviceModel);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        String time;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",new Locale("ar"));
            Date date = new Date(calendar.getTimeInMillis());
            time = dateFormat.format(date);

            if (type.equals("from"))
            {
                from_time = calendar.getTimeInMillis();
                tv_from.setText(time);
                to_time=0;


            }else if (type.equals("to"))
            {
                to_time = calendar.getTimeInMillis();
                tv_to.setText(time);
            }

            Log.e("ft",from_time+"_");
            Log.e("tt",to_time+"_");

        }else
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",Locale.ENGLISH);
                Date date = new Date(calendar.getTimeInMillis());
                time = dateFormat.format(date);

                if (type.equals("from"))
                {
                    from_time = calendar.getTimeInMillis();
                    tv_from.setText(time);
                    to_time=0;
                }else if (type.equals("to"))
                {
                    to_time = calendar.getTimeInMillis();
                    tv_to.setText(time);
                }

                Log.e("t",from_time+"_");
                Log.e("t",to_time+"_");
            }


    }
}
