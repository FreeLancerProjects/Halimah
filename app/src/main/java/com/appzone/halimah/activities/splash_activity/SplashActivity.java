package com.appzone.halimah.activities.splash_activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.home_activity.activity.HomeActivity;
import com.appzone.halimah.activities.sign_in_activity.SignInActivity;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.preferences.Preferences;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progBar;
    private Animation animation;
    private FrameLayout fl;
    private Preferences preferences;
    private String session="";
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = Preferences.getInstance();
        session = preferences.getSession(this);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        fl = findViewById(R.id.fl);
        animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        fl.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("animation","end");
                if (session.equals(Tags.LOGIN_STATE))
                {
                    userModel = preferences.getUserModel(SplashActivity.this);
                    userSingleTone = UserSingleTone.getInstance();
                    userSingleTone.setUserModel(userModel);
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }else
                    {
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        animation.cancel();
        super.onDestroy();
    }
}
