package com.appzone.halimah.activities.sign_up_activity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.preferences.Preferences;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_SignUp extends Fragment{
    private EditText edt_name,edt_username,edt_phone,edt_password;
    private Button btn_signup;
    private double myLat = 0.0,myLng = 0.0;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_sign_up,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_SignUp getInstance()
    {
        return new Fragment_Client_SignUp();
    }
    private void initView(View view) {
        edt_name = view.findViewById(R.id.edt_name);
        edt_username = view.findViewById(R.id.edt_username);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_password = view.findViewById(R.id.edt_password);
        btn_signup = view.findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();

            }
        });

    }

    private void CheckData() {
        String m_name = edt_name.getText().toString();
        String m_username = edt_username.getText().toString();
        String m_phone = edt_phone.getText().toString();
        String m_password = edt_password.getText().toString();
        String regex = "((([a-zA-Z]+(_)?([0-9]+)?)+)?([0-9]+(_)?([a-zA-Z]+)?)?)+";

        if(!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_username)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()>=7&&m_phone.length()<13&&
                !TextUtils.isEmpty(m_password)
                && m_username.matches(regex)


                )
        {
            Common.CloseKeyBoard(getActivity(),edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_username.setError(null);
            edt_password.setError(null);

            SignUp(m_name,m_username,m_phone,m_password);
        }else
        {
            if (TextUtils.isEmpty(m_name))
            {
                edt_name.setError(getString(R.string.name_req));
            }else
            {
                edt_name.setError(null);
            }

            if (TextUtils.isEmpty(m_username))
            {
                edt_username.setError(getString(R.string.username_req));
            }else if (!m_username.matches(regex))
            {
                edt_username.setError(getString(R.string.inv_username));

            }
            else
            {
                edt_username.setError(null);
            }

            if (TextUtils.isEmpty(m_phone))
            {
                edt_phone.setError(getString(R.string.phone_req));
            }else if (m_phone.length()<=6||m_phone.length()>=13)
            {
                edt_phone.setError(getString(R.string.inv_phone));
            }else
            {
                edt_phone.setError(null);
            }

            if (TextUtils.isEmpty(m_password))
            {
                edt_password.setError(getString(R.string.password_req));
            }else
            {
                edt_password.setError(null);
            }
        }
    }

    private void SignUp(String m_name, String m_username, String m_phone, String m_password)
    {
        Common.CloseKeyBoard(getActivity(),edt_name);
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.sinng_up));
        dialog.show();
        Api.getService()
                .sign_up_client(m_name,m_username,m_phone,m_password, Tags.CLIENT_TYPE)
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
                            }else
                            {
                                Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();

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

    public void Location(double lat, double lng)
    {
        myLat = lat;
        myLng = lng;

        Log.e("lat",lat+"_");
        Log.e("lng",lng+"_");

    }
}
