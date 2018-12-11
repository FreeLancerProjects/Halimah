package com.appzone.halimah.activities.nursery_details.fragment.fragment_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.halimah.R;
import com.appzone.halimah.adapters.ServiceAdapter_Fragment_Service;
import com.appzone.halimah.models.Slider_Nursery_Model;

public class Fragment_Service extends Fragment {
    private static final String TAG="DATA";
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Slider_Nursery_Model.NurseryModel nurseryModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Service getInstance(Slider_Nursery_Model.NurseryModel nurseryModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,nurseryModel);
        Fragment_Service fragment_service = new Fragment_Service();
        fragment_service.setArguments(bundle);
        return fragment_service;
    }
    private void initView(View view) {
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(true);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            nurseryModel = (Slider_Nursery_Model.NurseryModel) bundle.getSerializable(TAG);
            UpdateUI(nurseryModel);
        }
    }

    private void UpdateUI(Slider_Nursery_Model.NurseryModel nurseryModel) {
        adapter = new ServiceAdapter_Fragment_Service(getActivity(),nurseryModel.getService());
        recView.setAdapter(adapter);
    }
}
