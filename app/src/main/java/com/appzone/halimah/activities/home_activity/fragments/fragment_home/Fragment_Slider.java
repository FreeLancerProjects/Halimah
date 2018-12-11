package com.appzone.halimah.activities.home_activity.fragments.fragment_home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.halimah.R;
import com.appzone.halimah.tags.Tags;
import com.squareup.picasso.Picasso;

public class Fragment_Slider extends Fragment {
    private static final String TAG="DATA";
    private ImageView image;

    public static Fragment_Slider getInstance(String image_url)
    {
        Bundle bundle = new Bundle();
        bundle.putString(TAG,image_url);
        Fragment_Slider fragment_slider =new Fragment_Slider();
        fragment_slider.setArguments(bundle);
        return fragment_slider;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        image = view.findViewById(R.id.image);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            String url = bundle.getString(TAG);
            UpdateUI(url);
        }
    }

    private void UpdateUI(String url) {
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_PATH+url)).into(image);
    }
}
