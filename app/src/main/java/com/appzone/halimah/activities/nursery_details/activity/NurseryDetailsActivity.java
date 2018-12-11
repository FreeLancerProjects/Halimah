package com.appzone.halimah.activities.nursery_details.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.nursery_details.fragment.Fragment_Terms;
import com.appzone.halimah.activities.nursery_details.fragment.fragment_details.fragment_details.Fragment_Details;
import com.appzone.halimah.models.ReserveModel;
import com.appzone.halimah.models.ResponseModel;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.remote.Api;
import com.appzone.halimah.share.Common;
import com.appzone.halimah.singletone.UserSingleTone;
import com.appzone.halimah.tags.Tags;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NurseryDetailsActivity extends AppCompatActivity {
    private Slider_Nursery_Model.NurseryModel nurseryModel;
    private ImageView image_back;
    private TextView tv_title;
    private FragmentManager fragmentManager;
    private Fragment_Details fragment_details;
    private Fragment_Terms fragment_terms;
    private List<ReserveModel> reserveModelList;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursery_details);
        initView();
        getDataFromIntent();
    }

    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        fragmentManager = getSupportFragmentManager();
        image_back  = findViewById(R.id.image_back);
        tv_title = findViewById(R.id.tv_title);
        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_back.setRotation(180f);
        }
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                back();
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            nurseryModel = (Slider_Nursery_Model.NurseryModel) intent.getSerializableExtra("data");
            updateUI(nurseryModel);
        }
    }

    private void updateUI(Slider_Nursery_Model.NurseryModel nurseryModel) {
        UpdateTitle(nurseryModel.getUser_full_name());
        Display_Fragment_Details(nurseryModel);
    }

    public void setDateToReserve(List<ReserveModel> reserveModelList)
    {
        this.reserveModelList=reserveModelList;
        Display_Fragment_Terms_Conditions();

    }
    public void Reserve()
    {
        double total_cost = 0.0;
        for (ReserveModel reserveModel:reserveModelList)
        {
            total_cost+=reserveModel.getTotal_hour_cost();
        }

        if (userModel==null)
        {
            Common.CreateUserNotSignInAlertDialog(this,getString(R.string.si_su));
        }else
            {
                if (userModel.getUser_type().equals(Tags.NURSERY_TYPE))
                {
                    Common.CreateUserNotSignInAlertDialog(this,getString(R.string.serve_avail));
                }else
                    {
                        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.booking));
                        dialog.show();
                        Api.getService()
                                .reserve(userModel.getUser_id(),nurseryModel.getUser_id(),String.valueOf(Math.round(total_cost)),reserveModelList)
                                .enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful())
                                        {
                                            dialog.dismiss();

                                            if (response.body().getSuccess_resevation()==1)
                                            {
                                                fragmentManager.popBackStack("fragment_details",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                Toast.makeText(NurseryDetailsActivity.this, R.string.succ, Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else
                                                {
                                                    Toast.makeText(NurseryDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        try {
                                            dialog.dismiss();

                                            Log.e("Error",t.getMessage());
                                            Toast.makeText(NurseryDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();

                                        }catch (Exception e)
                                        {
                                        }
                                    }
                                });
                    }
            }
    }

    public void Display_Fragment_Terms_Conditions()
    {
        if (fragment_details!=null&&fragment_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_details).commit();
        }

        if (fragment_terms==null)
        {
            fragment_terms = Fragment_Terms.getInstance();
        }

        if (fragment_terms.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_terms).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_nursery_details_container,fragment_terms,"fragment_terms").addToBackStack("fragment_terms").commit();
            }
    }

    public void Display_Fragment_Details(Slider_Nursery_Model.NurseryModel nurseryModel)
    {
        if (fragment_terms!=null&&fragment_terms.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_terms).commit();
        }
        if (fragment_details==null)
        {
            fragment_details = Fragment_Details.getInstance(nurseryModel);
        }

        if (fragment_details.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_details).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_nursery_details_container,fragment_details,"fragment_details").addToBackStack("fragment_details").commit();
            }
    }

    public void UpdateTitle(String title)
    {
        tv_title.setText(title);
    }

    public void back()
    {
        if (fragment_details!=null&&fragment_details.isVisible())
        {
            fragmentManager.popBackStack("fragment_details",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            finish();
        }else
            {
                Display_Fragment_Details(nurseryModel);
            }
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
