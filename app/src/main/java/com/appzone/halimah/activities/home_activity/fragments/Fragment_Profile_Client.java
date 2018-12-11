package com.appzone.halimah.activities.home_activity.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile_Client extends Fragment {

    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private TextView tv_name,tv_phone,tv_username;
    private HomeActivity homeActivity;
    private AlertDialog updatedialog;
    private ImageView img_update_name,img_update_phone,img_update_username,img_update_password;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_client,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Profile_Client getInstance()
    {
        return new Fragment_Profile_Client();
    }
    private void initView(View view) {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        homeActivity = (HomeActivity) getActivity();
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_username = view.findViewById(R.id.tv_username);

        img_update_name = view.findViewById(R.id.img_update_name);
        img_update_phone = view.findViewById(R.id.img_update_phone);
        img_update_username = view.findViewById(R.id.img_update_username);
        img_update_password = view.findViewById(R.id.img_update_password);

        UpdateUi(this.userModel);




        img_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_full_name);
            }
        });

        img_update_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_phone);
            }
        });



        img_update_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_username);
            }
        });

        img_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog_UpdateProfile(Tags.update_password);
            }
        });
    }

    private void UpdateUi(UserModel userModel) {
        tv_name.setText(userModel.getUser_full_name());
        tv_phone.setText(userModel.getUser_phone());
        tv_username.setText(userModel.getUser_name());
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

                        update_name(m_name);

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

                        update_username(m_username);

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
                        update_phone(m_phone);

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

                        update_Password(m_oldPassword,m_newPassword);

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

    private void update_name(String newName)
    {


        final ProgressDialog progressDialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        progressDialog.show();

        Api.getService().UpdateProfileData(userModel.getUser_id(),userModel.getUser_phone(),newName,userModel.getUser_name())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.succ, Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getSuccess_update()==2)
                            {
                                Toast.makeText(getActivity(), R.string.un_exist, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                        try {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (NullPointerException e){}

                    }
                });
    }

    private void update_phone(String newPhone)
    {


        final ProgressDialog progressDialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        progressDialog.show();

        Api.getService().UpdateProfileData(userModel.getUser_id(),newPhone,userModel.getUser_full_name(),userModel.getUser_name())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {
                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.succ, Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getSuccess_update()==2)
                            {
                                Toast.makeText(getActivity(), R.string.un_exist, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (NullPointerException e){}
                    }
                });
    }
    private void update_username(String username)
    {


        final ProgressDialog progressDialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        progressDialog.show();

        Api.getService().UpdateProfileData(userModel.getUser_id(),userModel.getUser_phone(),userModel.getUser_full_name(),username)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_update()==1)
                            {

                                UpdateUserData(response.body());
                                Toast.makeText(getActivity(), R.string.succ, Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getSuccess_update()==2)
                            {
                                Toast.makeText(getActivity(),R.string.un_exist, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                        try {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());

                        }catch (NullPointerException e){}
                    }
                });
    }

    private void update_Password(String oldPass,String newPass)
    {
        final ProgressDialog progressDialog = Common.createProgressDialog(getActivity(),getString(R.string.updtng));
        progressDialog.show();

        Api.getService().UpdatePassword(userModel.getUser_id(),oldPass,newPass)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();

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
                            progressDialog.dismiss();
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();

                        }catch (NullPointerException e){}
                    }
                });
    }

    private void  UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        UpdateUi(userModel);
        homeActivity.UpdateUserData(userModel);

    }

}
