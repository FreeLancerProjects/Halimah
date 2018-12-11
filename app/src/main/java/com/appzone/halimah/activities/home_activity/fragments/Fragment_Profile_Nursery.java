package com.appzone.halimah.activities.home_activity.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.adapters.ServiceAdapter_Fragment_Nursery_Profile;
import com.appzone.halimah.adapters.ServiceAdapter_Nursery_Profile;
import com.appzone.halimah.models.ServiceModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile_Nursery extends Fragment {

    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private final String readPerm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 11,img_req=12;
    private KenBurnsView image_bg;
    private RoundedImageView image,img1,img2,img3,img4,img5;
    private ImageView image_update_photo,img1_delete,img2_delete,img3_delete,img4_delete,img5_delete;
    private TextView tv_name,tv_update_name,tv_phone,tv_username,tv_update_phone,tv_update_username,tv_update_password;
    private LinearLayout ll_add_photo,ll_add_services;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<RoundedImageView> galleryList;
    private List<ImageView>galleryImageDelete;
    private List<UserModel.Nursery_Service> nursery_serviceList;
    private HomeActivity homeActivity;
    private List<Uri> uriList;
    private List<UserModel.Nursery_Gallery> gallery;
    private int whichSelected=-1;
    private AlertDialog serviceDialog,updatedialog;
    private List<ServiceModel> selected_serviceModelList;
    private List<String> serviceIdsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_nursery,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Profile_Nursery getInstance()
    {
        return new Fragment_Profile_Nursery();
    }
    private void initView(View view)
    {
        homeActivity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        uriList = new ArrayList<>();
        galleryList = new ArrayList<>();
        galleryImageDelete = new ArrayList<>();
        nursery_serviceList = new ArrayList<>();
        serviceIdsList = new ArrayList<>();
        selected_serviceModelList = new ArrayList<>();
        image_bg = view.findViewById(R.id.image_bg);
        image = view.findViewById(R.id.image);
        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);
        img5 = view.findViewById(R.id.img5);
        image_update_photo = view.findViewById(R.id.image_update_photo);
        img1_delete = view.findViewById(R.id.img1_delete);
        img2_delete = view.findViewById(R.id.img2_delete);
        img3_delete = view.findViewById(R.id.img3_delete);
        img4_delete = view.findViewById(R.id.img4_delete);
        img5_delete = view.findViewById(R.id.img5_delete);
        tv_name = view.findViewById(R.id.tv_name);
        tv_update_name = view.findViewById(R.id.tv_update_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_username = view.findViewById(R.id.tv_username);
        tv_update_phone = view.findViewById(R.id.tv_update_phone);
        tv_update_username = view.findViewById(R.id.tv_update_username);
        tv_update_password = view.findViewById(R.id.tv_update_password);
        ll_add_photo = view.findViewById(R.id.ll_add_photo);
        ll_add_services = view.findViewById(R.id.ll_add_services);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        UpdateUi(userModel);
        ll_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gallery.size()<5)
                {
                    whichSelected=2;
                    checkReadPermission();

                }else
                    {
                        Toast.makeText(homeActivity, R.string.max5, Toast.LENGTH_SHORT).show();

                    }
            }
        });

        image_update_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichSelected=1;
                checkReadPermission();
            }
        });

        ll_add_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServices();
            }
        });

        tv_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_full_name);
            }
        });

        tv_update_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_phone);

            }
        });

        tv_update_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_username);

            }
        });

        tv_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_password);

            }
        });

        img1_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageGallery(gallery.get(0).getId_photo());
            }
        });
        img2_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageGallery(gallery.get(1).getId_photo());
            }
        });

        img3_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageGallery(gallery.get(2).getId_photo());
            }
        });

        img4_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageGallery(gallery.get(3).getId_photo());
            }
        });

        img5_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageGallery(gallery.get(4).getId_photo());
            }
        });
    }
    private void UpdateUi(UserModel userModel)
    {
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+userModel.getUser_image())).into(image_bg);
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+userModel.getUser_image())).into(image);
        tv_name.setText(userModel.getUser_full_name());
        tv_phone.setText(userModel.getUser_phone());
        tv_username.setText(userModel.getUser_name());

        img1_delete.setVisibility(View.GONE);
        img2_delete.setVisibility(View.GONE);
        img3_delete.setVisibility(View.GONE);
        img4_delete.setVisibility(View.GONE);
        img5_delete.setVisibility(View.GONE);

        img1.setImageBitmap(null);
        img2.setImageBitmap(null);
        img3.setImageBitmap(null);
        img4.setImageBitmap(null);
        img5.setImageBitmap(null);


        img1.setImageResource(R.drawable.img_profile_bg);
        img2.setImageResource(R.drawable.img_profile_bg);
        img3.setImageResource(R.drawable.img_profile_bg);
        img4.setImageResource(R.drawable.img_profile_bg);
        img5.setImageResource(R.drawable.img_profile_bg);

        galleryList.clear();
        galleryList.add(img1);
        galleryList.add(img2);
        galleryList.add(img3);
        galleryList.add(img4);
        galleryList.add(img5);

        galleryImageDelete.clear();
        galleryImageDelete.add(img1_delete);
        galleryImageDelete.add(img2_delete);
        galleryImageDelete.add(img3_delete);
        galleryImageDelete.add(img4_delete);
        galleryImageDelete.add(img5_delete);

        nursery_serviceList.clear();
        nursery_serviceList.addAll(userModel.getKindergarten_service());
        adapter = new ServiceAdapter_Fragment_Nursery_Profile(getActivity(),nursery_serviceList,this);
        recView.setAdapter(adapter);

        gallery = userModel.getGallary();

        for (int i=0;i<gallery.size();i++)
        {
            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+gallery.get(i).getPhoto_name())).into(galleryList.get(i));
            galleryImageDelete.get(i).setVisibility(View.VISIBLE);
        }




    }
    private void getServices()
    {
        final ProgressDialog progressDialog =Common.createProgressDialog(getActivity(),getString(R.string.wait));
        progressDialog.show();
        Api.getService()
                .getNursery_UnChoosed_Services(userModel.getUser_id())
                .enqueue(new Callback<List<ServiceModel>>() {
                    @Override
                    public void onResponse(Call<List<ServiceModel>> call, Response<List<ServiceModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().size()>0)
                            {
                                createServiceDialog(response.body());
                            }else
                                {
                                    Toast.makeText(homeActivity, R.string.no_servs, Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ServiceModel>> call, Throwable t) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());

                        }catch (Exception e){}
                    }
                });
    }
    private void createServiceDialog(List<ServiceModel> serviceModelList)
    {
        serviceDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_service,null);
        TextView tv_title  = view.findViewById(R.id.tv_title);
        tv_title.setText(R.string.ch_serv);
        RecyclerView recView  = view.findViewById(R.id.recView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        RecyclerView.Adapter adapter = new ServiceAdapter_Nursery_Profile(getActivity(),this,serviceModelList);
        recView.setLayoutManager(manager);
        recView.setAdapter(adapter);
        LinearLayout ll_add_service = view.findViewById(R.id.ll_add_service);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceDialog.dismiss();
            }
        });

        ll_add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_serviceModelList.size()>0)
                {
                    serviceDialog.dismiss();
                    updateServices();
                }else
                    {
                        Toast.makeText(getActivity(), getString(R.string.ch_service), Toast.LENGTH_SHORT).show();
                    }

            }
        });

        serviceDialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog;
        serviceDialog.setView(view);
        serviceDialog.show();
        serviceDialog.setCanceledOnTouchOutside(false);




    }
    private void checkReadPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),readPerm)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] perm = {readPerm};
            ActivityCompat.requestPermissions(getActivity(),perm,read_req);
        }else {
            if (whichSelected==1)
            {
                SelectSingleImage();
            }else if (whichSelected==2)
            {
                SelectImage();

            }
        }
    }
    private void SelectImage()
    {

        Intent intent;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {

            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent,img_req);


    }
    private void SelectSingleImage() 
    {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==img_req&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (whichSelected==1)
            {
                Uri uri = data.getData();
                UpdateImageProfile(uri);
            }else if (whichSelected==2)
            {
                ClipData clipData = data.getClipData();

                if (clipData!=null)
                {
                    if ((gallery.size()+clipData.getItemCount())>5)
                    {
                        Toast.makeText(homeActivity, R.string.max5, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        for (int i=0;i<clipData.getItemCount();i++)
                        {
                            Uri uri = clipData.getItemAt(i).getUri();
                            uriList.add(uri);
                        }

                        UpdateGalleryImage(uriList);
                    }
                }else
                {
                    Uri uri = data.getData();
                    uriList.add(uri);
                    UpdateGalleryImage(uriList);

                }
            }
           

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {

                    if (whichSelected==1)
                    {
                        SelectSingleImage();
                    }else if (whichSelected==2)
                    {
                        SelectImage();

                    }
                }else
                {
                    Toast.makeText(getActivity(), R.string.acc_photo_denied, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void CreateAlertDialog_UpdateProfile(final String type)
    {
        updatedialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_profile,null);
        final TextView tv_title = view.findViewById(R.id.tv_title);
        final EditText edt_update = view.findViewById(R.id.edt_update);
        final EditText edt_newPassword = view.findViewById(R.id.edt_newPassword);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_close = view.findViewById(R.id.btn_close);

        if (type.equals(Tags.update_full_name))
        {
            tv_title.setText(R.string.upd_name);
            edt_update.setInputType(InputType.TYPE_CLASS_TEXT);
            edt_newPassword.setVisibility(View.GONE);
            edt_update.setHint(R.string.name);
            if (userModel!=null)
            {
                edt_update.setText(userModel.getUser_full_name());


            }
        }else if (type.equals(Tags.update_phone))
        {
            tv_title.setText(R.string.upd_phone);
            edt_update.setInputType(InputType.TYPE_CLASS_PHONE);
            edt_newPassword.setVisibility(View.GONE);
            edt_update.setHint(R.string.phone);
            if (userModel!=null)
            {
                edt_update.setText(userModel.getUser_phone());


            }

        }else if (type.equals(Tags.update_username))
        {
            tv_title.setText(R.string.upd_username);
            edt_update.setInputType(InputType.TYPE_CLASS_TEXT);
            edt_newPassword.setVisibility(View.GONE);
            edt_update.setHint(R.string.username);
            if (userModel!=null)
            {
                edt_update.setText(userModel.getUser_name());


            }
        }
        else if (type.equals(Tags.update_password))
        {

            tv_title.setText(R.string.upd_password);
            edt_update.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edt_newPassword.setVisibility(View.VISIBLE);
            edt_update.setHint(R.string.old_pass);

        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals(Tags.update_full_name))
                {
                    String m_name = edt_update.getText().toString();
                    if (!TextUtils.isEmpty(m_name))
                    {
                        updatedialog.dismiss();
                        edt_update.setError(null);

                        Common.CloseKeyBoard(getActivity(),edt_update);

                        UpdateName(m_name);

                    }else
                    {
                        edt_update.setError(getString(R.string.name_req));
                    }

                }
                if (type.equals(Tags.update_username))
                {
                    String m_username = edt_update.getText().toString();
                    if (!TextUtils.isEmpty(m_username))
                    {
                        updatedialog.dismiss();

                        edt_update.setError(null);

                        Common.CloseKeyBoard(getActivity(),edt_update);

                        UpdateUsername(m_username);

                    }else
                    {
                        edt_update.setError(getString(R.string.username_req));
                    }

                }
                else if (type.equals(Tags.update_phone))
                {

                    updatedialog.dismiss();

                    String m_phone = edt_update.getText().toString();

                    if (TextUtils.isEmpty(m_phone))
                    {
                        edt_update.setError(getString(R.string.phone_req));


                    }else if (!Patterns.PHONE.matcher(m_phone).matches()||m_phone.length()<6||m_phone.length()>13)
                    {
                        edt_update.setError(getString(R.string.inv_phone));
                    }
                    else
                    {
                        updatedialog.dismiss();

                        Common.CloseKeyBoard(getActivity(),edt_update);
                        edt_update.setError(null);
                        UpdatePhone(m_phone);

                    }
                }else if (type.equals(Tags.update_password))
                {

                    Log.e("upd","password");
                    String m_oldPassword = edt_update.getText().toString();
                    String m_newPassword = edt_newPassword.getText().toString();

                    if (!TextUtils.isEmpty(m_oldPassword)&&!TextUtils.isEmpty(m_newPassword))
                    {
                        updatedialog.dismiss();

                        Common.CloseKeyBoard(getActivity(),edt_update);
                        edt_update.setError(null);
                        edt_newPassword.setError(null);

                        UpdatePassword(m_oldPassword,m_newPassword);

                    }else
                    {
                        if (TextUtils.isEmpty(m_oldPassword))
                        {
                            edt_update.setError(getString(R.string.password_req));

                        }else
                        {
                            edt_update.setError(null);

                        }

                        if (TextUtils.isEmpty(m_newPassword))
                        {
                            edt_newPassword.setError(getString(R.string.newpass_req));

                        }
                        else
                        {
                            edt_newPassword.setError(null);

                        }

                    }





                }
            }
        });
        updatedialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog;
        updatedialog.setView(view);
        updatedialog.show();




    }
    private void UpdateImageProfile(Uri uri)
    {
        MultipartBody.Part image_part = Common.getMultiPart(getActivity(),uri,"user_image");
        RequestBody name_part = Common.getRequestBodyText(userModel.getUser_full_name());
        RequestBody phone_part = Common.getRequestBodyText(userModel.getUser_phone());
        RequestBody responsible_name_part = Common.getRequestBodyText(userModel.getPerson_responsible_name());
        RequestBody responsible_phone_part = Common.getRequestBodyText(userModel.getPerson_responsible_phone());
        RequestBody address_part = Common.getRequestBodyText(userModel.getUser_address());
        RequestBody username_part = Common.getRequestBodyText(userModel.getUser_name());
        RequestBody from_part = Common.getRequestBodyText(userModel.getFrom_hour());
        RequestBody to_part = Common.getRequestBodyText(userModel.getTo_hour());
        RequestBody hour_part = Common.getRequestBodyText(userModel.getHour_cost());

        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdateNurseryProfileImage(userModel.getUser_id(),name_part,username_part,phone_part,responsible_phone_part,responsible_name_part,address_part,hour_part,from_part,to_part,image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(homeActivity,R.string.succ, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(homeActivity,R.string.un_exist, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdateGalleryImage(List<Uri> uriList)
    {

        List<MultipartBody.Part> galleryPart = getMultipartImage(uriList);
        RequestBody name_part = Common.getRequestBodyText(userModel.getUser_full_name());
        RequestBody phone_part = Common.getRequestBodyText(userModel.getUser_phone());
        RequestBody responsible_name_part = Common.getRequestBodyText(userModel.getPerson_responsible_name());
        RequestBody responsible_phone_part = Common.getRequestBodyText(userModel.getPerson_responsible_phone());
        RequestBody address_part = Common.getRequestBodyText(userModel.getUser_address());
        RequestBody username_part = Common.getRequestBodyText(userModel.getUser_name());
        RequestBody from_part = Common.getRequestBodyText(userModel.getFrom_hour());
        RequestBody to_part = Common.getRequestBodyText(userModel.getTo_hour());
        RequestBody hour_part = Common.getRequestBodyText(userModel.getHour_cost());

        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdateNurseryProfileGallery(userModel.getUser_id(),name_part,username_part,phone_part,responsible_phone_part,responsible_name_part,address_part,hour_part,from_part,to_part,galleryPart)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(homeActivity,R.string.succ, Toast.LENGTH_SHORT).show();
                            }else
                                {
                                    Toast.makeText(homeActivity,R.string.un_exist, Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });


    }
    private void updateServices()
    {

        for (ServiceModel serviceModel :selected_serviceModelList)
        {
            serviceIdsList.add(serviceModel.getId_service());
        }
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdateNurseryProfileServices(userModel.getUser_id(),userModel.getUser_full_name(),userModel.getUser_name(),userModel.getUser_phone(),userModel.getPerson_responsible_phone(),userModel.getPerson_responsible_name(),userModel.getUser_address(),userModel.getHour_cost(),userModel.getFrom_hour(),userModel.getTo_hour(),serviceIdsList)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(homeActivity,R.string.succ, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(homeActivity,R.string.un_exist, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdateName(String name)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdateNurseryProfileData(userModel.getUser_id(),name,userModel.getUser_name(),userModel.getUser_phone(),userModel.getPerson_responsible_phone(),userModel.getPerson_responsible_name(),userModel.getUser_address(),userModel.getHour_cost(),userModel.getFrom_hour(),userModel.getTo_hour())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(homeActivity,R.string.succ, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(homeActivity,R.string.un_exist, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdatePhone(String phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdateNurseryProfileData(userModel.getUser_id(),userModel.getUser_full_name(),userModel.getUser_name(),phone,userModel.getPerson_responsible_phone(),userModel.getPerson_responsible_name(),userModel.getUser_address(),userModel.getHour_cost(),userModel.getFrom_hour(),userModel.getTo_hour())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(homeActivity,R.string.succ, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(homeActivity,R.string.un_exist, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdateUsername(String username)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdateNurseryProfileData(userModel.getUser_id(),userModel.getUser_full_name(),username,userModel.getUser_phone(),userModel.getPerson_responsible_phone(),userModel.getPerson_responsible_name(),userModel.getUser_address(),userModel.getHour_cost(),userModel.getFrom_hour(),userModel.getTo_hour())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(homeActivity,R.string.succ, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(homeActivity,R.string.un_exist, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdatePassword(String m_oldPassword, String m_newPassword) {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        dialog.show();
        Api.getService().UpdatePassword(userModel.getUser_id(),m_oldPassword,m_newPassword)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            Log.e("dddd",response.body().getSuccess_update_pass()+"");
                            if (response.body().getSuccess_update_pass()==0)
                            {

                                Toast.makeText(getActivity(), R.string.wrong_oldpass, Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess_update_pass()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.succ, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private List<MultipartBody.Part> getMultipartImage(List<Uri> uriList)
    {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for(Uri uri:uriList)
        {
            MultipartBody.Part part = Common.getMultiPart(getActivity(),uri,"gallary[]");
            partList.add(part);
        }
        return partList;
    }
    public void deleteItem(UserModel.Nursery_Service serviceModel, final int pos)
    {
        if (nursery_serviceList.size()>1)
        {
            final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.deltng));
            dialog.show();
            Api.getService()
                    .deleteService(userModel.getUser_id(),"1",serviceModel.getId_service())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful())
                            {
                                dialog.dismiss();


                                if (response.body().getSuccess_delete()==1)
                                {
                                    UpdateUserData(response.body());
                                    Toast.makeText(getActivity(), R.string.succ, Toast.LENGTH_SHORT).show();
                                }else
                                    {
                                        Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();

                                    }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                            try {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                                Log.e("error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }else
            {
                Toast.makeText(getActivity(), R.string.cnt_del_serv, Toast.LENGTH_SHORT).show();
            }
    }
    private void  UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        this.userSingleTone.setUserModel(this.userModel);
        UpdateUi(userModel);
        homeActivity.UpdateUserData(userModel);

    }
    public void setItem(ServiceModel serviceModel)
    {

        selected_serviceModelList.add(serviceModel);

    }
    public void removeItem(ServiceModel serviceModel)
    {

        selected_serviceModelList.remove(serviceModel);

    }
    private void deleteImageGallery(String id_photo)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.deltng));
        dialog.show();
        Api.getService()
                .deleteGalleryImage(userModel.getUser_id(),"2",id_photo)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_delete()==1)
                            {
                                UpdateUserData(response.body());
                            }else
                                {
                                    Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
}
