package com.appzone.halimah.activities.home_activity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.models.ContactModel;
import com.appzone.halimah.models.ResponseModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ContactUs extends Fragment {
    private EditText edt_name,edt_phone,edt_message;
    private Button btn_send;
    private ContactModel contactModel;
    private ImageView image_twitter,image_whatsapp,image_facebook;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us,container,false);
        initView(view);
        return view;
    }

    public static Fragment_ContactUs getInstance()
    {
        return new Fragment_ContactUs();
    }
    private void initView(View view) {

        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_message = view.findViewById(R.id.edt_message);
        btn_send = view.findViewById(R.id.btn_send);
        image_twitter = view.findViewById(R.id.image_twitter);
        image_whatsapp = view.findViewById(R.id.image_whatsapp);
        image_facebook = view.findViewById(R.id.image_facebook);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        getSocialData();
        if (!isWhatsInstalled())
        {
            image_whatsapp.setVisibility(View.GONE);
        }

        image_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendViaWhatsApp(contactModel.getWhatsapp());
            }
        });

        image_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendViaLink(contactModel.getTwitter());
            }
        });
        image_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendViaLink(contactModel.getFacebook());
            }
        });

    }

    private void sendViaWhatsApp(String phone)
    {

        Log.e("phone",phone);
        if (phone.startsWith("+"))
        {
            phone = phone.replace("+","");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"السلام عليكم");
        intent.putExtra("jid",phone+"@s.whatsapp.net");
        intent.setPackage("com.whatsapp");

        if (intent.resolveActivity(getActivity().getPackageManager())==null)
        {
            return;
        }
        startActivity(intent);
    }

    private void sendViaLink(String link)
    {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        }catch (Exception e)
        {
            Toast.makeText(getActivity(), R.string.link_inc, Toast.LENGTH_SHORT).show();
        }

    }

    private void getSocialData() {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(), getString(R.string.wait));
        dialog.show();
        Api.getService()
                .getContactUs()
                .enqueue(new Callback<ContactModel>() {
                    @Override
                    public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body()!=null)
                            {
                                contactModel = response.body();
                                Log.e("phone",contactModel.getPhone()+"_");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ContactModel> call, Throwable t) {

                        try {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void CheckData() {
        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone.getText().toString();
        String m_msg = edt_message.getText().toString();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()>=7&&m_phone.length()<13&&
                !TextUtils.isEmpty(m_msg)
                )
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_message.setError(null);

            SendData(m_name,m_phone,m_msg);

        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.name_req));
                }else
                    {
                        edt_name.setError(null);

                    }

                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.phone_req));
                }else if (m_phone.length()<=6||m_phone.length()>=13)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
                else
                {
                    edt_phone.setError(null);

                }

                if (TextUtils.isEmpty(m_msg))
                {
                    edt_message.setError(getString(R.string.msg_req));
                }else
                {
                    edt_message.setError(null);

                }
            }
    }

    private void SendData(String m_name, String m_phone, String m_msg)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.sending));
        dialog.show();
        Api.getService()
                .sendContactUs(m_name,m_phone,m_msg)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_contact()==1)
                            {
                                dialog.dismiss();
                                edt_message.setText(null);
                                edt_name.setText(null);
                                edt_phone.setText(null);
                                Toast.makeText(getActivity(),R.string.succ, Toast.LENGTH_SHORT).show();

                            }else
                                {
                                    Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                        dialog.dismiss();
                        try {
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();

                        }catch (Exception e){}
                    }
                });
    }

    private boolean isWhatsInstalled()
    {
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager!=null)
        {
            try {
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}

