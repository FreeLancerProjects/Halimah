package com.appzone.halimah.activities.forget_password;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.models.ResponseModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView image_back;
    private EditText edt_email,edt_username;
    private Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        image_back = findViewById(R.id.image_back);
        edt_email = findViewById(R.id.edt_email);
        edt_username = findViewById(R.id.edt_username);

        btn_send = findViewById(R.id.btn_send);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_back.setRotation(180f);
        }

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }

    private void CheckData() {
        String m_email = edt_email.getText().toString();
        String m_username = edt_username.getText().toString();
        String regex = "((([a-zA-Z]+(_)?([0-9]+)?)+)?([0-9]+(_)?([a-zA-Z]+)?)?)+";

        if (!TextUtils.isEmpty(m_email)&&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_email)
                && m_username.matches(regex)
                )
        {
            edt_email.setError(null);
            edt_username.setError(null);

            Common.CloseKeyBoard(this,edt_email);

            ResetPassword(m_email,m_username);
        }else
            {
                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.email_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));

                }else
                {
                    edt_email.setError(null);

                }

                if (TextUtils.isEmpty(m_username))
                {
                    edt_username.setError(getString(R.string.username_req));
                }else if (!m_username.matches(regex))
                {
                    edt_username.setError(getString(R.string.inv_username));

                }else
                {
                    edt_username.setError(null);

                }

            }


    }

    private void ResetPassword(String m_email, String m_username)
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.sending));
        dialog.show();
        Api.getService()
                .resetPassword(m_email,m_username)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            if (response.body().getSuccess_rest()==1)
                            {
                                CreateAlertDialog();
                            }else {
                                Toast.makeText(ForgetPasswordActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error",t.getMessage());

                            Toast.makeText(ForgetPasswordActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                        }catch (NullPointerException e){}

                    }
                });
    }

    private void CreateAlertDialog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        Button done_btn = view.findViewById(R.id.doneBtn);
        tv_msg.setText(getString(R.string.recov_msg));
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog;
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(view);
        alertDialog.show();
    }
}
