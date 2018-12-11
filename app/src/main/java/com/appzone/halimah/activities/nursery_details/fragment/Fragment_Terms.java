package com.appzone.halimah.activities.nursery_details.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.nursery_details.activity.NurseryDetailsActivity;
import com.appzone.halimah.models.TermsModel;
import com.appzone.halimah.remote.Api;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Terms extends Fragment{
    private TextView tv_content;
    private SmoothProgressBar smoothProgress;
    private Button btn_accept;
    private NurseryDetailsActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Terms getInstance()
    {
        return new Fragment_Terms();
    }
    private void initView(View view) {
        activity = (NurseryDetailsActivity) getActivity();
        tv_content = view.findViewById(R.id.tv_content);
        smoothProgress = view.findViewById(R.id.smoothProgress);
        btn_accept = view.findViewById(R.id.btn_accept);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Reserve();
            }
        });
        getTerms();

    }

    private void getTerms() {
        Api.getService()
                .getTerms_Condition()
                .enqueue(new Callback<TermsModel>() {
                    @Override
                    public void onResponse(Call<TermsModel> call, Response<TermsModel> response) {
                        if (response.isSuccessful())
                        {
                            smoothProgress.setVisibility(View.GONE);
                            tv_content.setText(response.body().getContent());
                            btn_accept.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<TermsModel> call, Throwable t) {
                        smoothProgress.setVisibility(View.GONE);
                        try {
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });
    }
}
