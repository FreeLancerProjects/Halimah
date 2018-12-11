package com.appzone.halimah.activities.sign_in_activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.forget_password.ForgetPasswordActivity;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.activities.sign_up_activity.activity.SignUpActivity;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.preferences.Preferences;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button btn_client_signup,btn_nursery_signup,btn_signin;
    private ImageView image_back;
    private TextView tv_password,tv_skip;
    private EditText edt_username,edt_password;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {
        image_back  = findViewById(R.id.image_back);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_back.setRotation(180f);
        }

        edt_username  = findViewById(R.id.edt_username);
        edt_password  = findViewById(R.id.edt_password);
        tv_skip  = findViewById(R.id.tv_skip);

        btn_client_signup  = findViewById(R.id.btn_client_signup);
        btn_nursery_signup  = findViewById(R.id.btn_nursery_signup);
        tv_password  = findViewById(R.id.tv_password);
        btn_signin  = findViewById(R.id.btn_signin);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_client_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUpActivity("member");
            }
        });
        btn_nursery_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignUpActivity("nursery");
            }
        });
        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });



    }

    private void CheckData() {
        String m_username = edt_username.getText().toString();
        String m_password = edt_password.getText().toString();

        if (!TextUtils.isEmpty(m_username)&&
                !TextUtils.isEmpty(m_password))
        {
            edt_username.setError(null);
            edt_password.setError(null);
            SignIn(m_username,m_password);
        }else
        {
            if (TextUtils.isEmpty(m_username))
            {
                edt_username.setError(getString(R.string.username_req));
            }else
            {
                edt_username.setError(null);

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

    private void SignIn(String m_username, String m_password)
    {
        Common.CloseKeyBoard(this,edt_username);
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.signing_in));
        dialog.show();
        Api.getService()
                .sign_in(m_username,m_password)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_login()==1)
                            {
                                preferences = Preferences.getInstance();
                                userSingleTone = UserSingleTone.getInstance();
                                UserModel userModel = response.body();
                                preferences.Create_Update_UserModel(SignInActivity.this,userModel);
                                userSingleTone.setUserModel(userModel);
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else
                            {
                                Toast.makeText(SignInActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error",t.getMessage());
                            Toast.makeText(SignInActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });

    }

    private void navigateToSignUpActivity(String type)
    {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        finish();
    }
}

