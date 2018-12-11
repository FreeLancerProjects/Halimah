package com.appzone.halimah.activities.nursery_details.fragment.fragment_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.halimah.R;
import com.appzone.halimah.activities.nursery_details.activity.NurseryDetailsActivity;
import com.appzone.halimah.adapters.ReserveAdapter;
import com.appzone.halimah.models.ReserveModel;
import com.appzone.halimah.models.Slider_Nursery_Model;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Reserve extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    private static final String TAG="DATA";
    private Slider_Nursery_Model.NurseryModel nurseryModel;
    private LinearLayout ll_from,ll_to;
    private TextView tv_from,tv_to,tv_choose_date;
    private Button btn_book,btn_add;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private long from_hour=0,to_hour=0,date=0;
    private String txt_date="";
    private RecyclerView recView;
    private RecyclerView.Adapter  adapter;
    private RecyclerView.LayoutManager manager;
    private List<ReserveModel> reserveModelList;
    private String type="";
    private boolean isFound=false;
    private NurseryDetailsActivity activity;
    private double total_hour=0.0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Reserve getInstance(Slider_Nursery_Model.NurseryModel nurseryModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,nurseryModel);
        Fragment_Reserve fragment_reserve = new Fragment_Reserve();
        fragment_reserve.setArguments(bundle);
        return fragment_reserve;
    }

    private void initView(View view) {

        activity = (NurseryDetailsActivity) getActivity();
        reserveModelList = new ArrayList<>();
        ll_from = view.findViewById(R.id.ll_from);
        ll_to = view.findViewById(R.id.ll_to);
        tv_choose_date = view.findViewById(R.id.tv_choose_date);
        tv_from = view.findViewById(R.id.tv_from);
        tv_to = view.findViewById(R.id.tv_to);
        btn_book = view.findViewById(R.id.btn_book);
        btn_add = view.findViewById(R.id.btn_add);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(manager);
        adapter = new ReserveAdapter(getActivity(),reserveModelList,this);
        recView.setAdapter(adapter);
        recView.setNestedScrollingEnabled(false);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            nurseryModel = (Slider_Nursery_Model.NurseryModel) bundle.getSerializable(TAG);
            UpdateUI(nurseryModel);
        }

        tv_choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDateDialog();
            }
        });

        ll_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="from";
                CreateTimeDialog(type);
            }
        });

        ll_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (from_hour==0.0)
                {
                    tv_from.setError(getString(R.string.field_req));
                }else {
                    type="to";
                    CreateTimeDialog(type);

                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDataToAdd();
            }
        });
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setDateToReserve(reserveModelList);
            }
        });

    }

    private void CheckDataToAdd() {
        if (date!=0&&to_hour!=0&&from_hour!=0)
        {
            tv_choose_date.setError(null);
            tv_to.setError(null);
            tv_from.setError(null);

            if (reserveModelList.size()>0)
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
                String d1 = dateFormat.format(new Date(date));
                for (int i=0 ;i<reserveModelList.size();i++)
                {
                    String d2 = dateFormat.format(new Date(reserveModelList.get(i).getReservation_date()));
                    if (d1.equals(d2))
                    {
                       isFound=true;
                       break;
                    }

                }
            }else
                {
                   isFound =false;
                }

                if (!isFound)
                {
                    double total_hour_cost = getTotal_Hour_cost(new Date(to_hour),new Date(from_hour));

                    ReserveModel reserveModel1 = new ReserveModel(date, from_hour, to_hour,(double) Math.round(total_hour_cost));
                    reserveModelList.add(reserveModel1);
                    adapter.notifyDataSetChanged();

                }else
                    {
                        Toast.makeText(getActivity(), R.string.date_sel, Toast.LENGTH_SHORT).show();
                    }

            isFound=false;
            date=0;
            to_hour=0;
            from_hour=0;
            tv_from.setText(null);
            tv_to.setText(null);
            tv_from.setHint("00:00");
            tv_to.setHint("00:00");
            tv_choose_date.setText(getString(R.string.choose_date));
            btn_book.setVisibility(View.VISIBLE);
        }else
            {
                if (date==0)
                {
                    tv_choose_date.setError(getString(R.string.field_req));
                }else
                    {
                        tv_choose_date.setError(null);

                    }

                if (to_hour==0)
                {
                    tv_to.setError(getString(R.string.field_req));
                }else
                {
                    tv_to.setError(null);

                }

                if (from_hour==0)
                {
                    tv_from.setError(getString(R.string.field_req));
                }else
                {
                    tv_from.setError(null);

                }
            }

    }

    private void UpdateUI(Slider_Nursery_Model.NurseryModel nurseryModel) {

    }

    private double getTotal_Hour_cost(Date date1,Date date2)
    {
        long time = date1.getTime()-date2.getTime();
        int hour = (int) (time /(1000*60*60));
        int min = (int) ((time/(1000*60))%60);

        double min_cost = Double.parseDouble(nurseryModel.getHour_cost())/60.0;
        double total_cost = (hour*Double.parseDouble(nurseryModel.getHour_cost()))+(min_cost*min);
        return total_cost;
    }

    private void CreateDateDialog()
    {


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,2);
        datePickerDialog = DatePickerDialog.newInstance(this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        datePickerDialog.setOkColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        datePickerDialog.setOkText(R.string.select);
        datePickerDialog.setCancelText(R.string.cancel);
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.show(getActivity().getFragmentManager(),"Time");

        //calendar.setTime(new Date());
    }

    private void CreateTimeDialog(String type)
    {
        Calendar calendar = Calendar.getInstance();

        if (type.equals("from"))
        {

          calendar.setTime(new Date(Long.parseLong(nurseryModel.getFrom_hour())));

        }else if (type.equals("to"))
        {
            calendar.setTime(new Date(from_hour));
            calendar.add(Calendar.HOUR_OF_DAY,1);
        }


        timePickerDialog = TimePickerDialog.newInstance(this,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        timePickerDialog.setCancelColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        timePickerDialog.setOkColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        timePickerDialog.setOkText(R.string.select);
        timePickerDialog.setCancelText(R.string.cancel);

        if (type.equals("from"))
        {

            timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
            calendar.add(Calendar.HOUR_OF_DAY,8);
            timePickerDialog.setMaxTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));

        }else if (type.equals("to"))
        {


            timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
            Calendar calendar1 =Calendar.getInstance();
            calendar1.setTime(new Date(Long.parseLong(nurseryModel.getTo_hour())));
            timePickerDialog.setMaxTime(calendar1.get(Calendar.HOUR_OF_DAY),calendar1.get(Calendar.MINUTE),calendar1.get(Calendar.SECOND));

        }

        timePickerDialog.show(getActivity().getFragmentManager(),"Time");

    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)
        {
            Toast.makeText(activity, R.string.day_not_aval, Toast.LENGTH_SHORT).show();
        }else
        {
            date = calendar.getTimeInMillis();
            Log.e("date",date+"___");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            txt_date = dateFormat.format(new Date(date));
            tv_choose_date.setText(txt_date);
        }





    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",Locale.getDefault());

        if (type.equals("from"))
        {



            from_hour = calendar.getTimeInMillis();
            String from =dateFormat.format(new Date(from_hour));
            tv_from.setText(from);

            tv_to.setHint("00:00");
            to_hour=0;

        }else if (type.equals("to"))
        {
            to_hour = calendar.getTimeInMillis();
            tv_from.setError(null);
            String to =dateFormat.format(new Date(to_hour));
            tv_to.setText(to);
        }





    }

    public void delete(int pos) {
        reserveModelList.remove(pos);
        adapter.notifyItemRemoved(pos);
        if (reserveModelList.size()==0)
        {
            btn_book.setVisibility(View.INVISIBLE);
        }
    }
}
