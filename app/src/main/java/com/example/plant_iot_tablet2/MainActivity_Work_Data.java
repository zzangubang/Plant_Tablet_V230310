package com.example.plant_iot_tablet2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Data#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Data extends Fragment {
    // 레이아웃.
    Button button_temp, button_humi, button_illu; // 버튼(온도, 습도, 조도).

    // 프래그먼트.
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment Temp, Humi, Illu;

    String data = "", model = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Data() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_Work_Data.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Data newInstance(String param1, String param2) {
        MainActivity_Work_Data fragment = new MainActivity_Work_Data();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_work_data, container, false);

        data = this.getArguments().getString("data");
        model = ((MainActivity_Work) getActivity()).model;

        button_temp = (Button) v.findViewById(R.id.button_temp);
        button_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_temp.setBackgroundResource(R.drawable.select_on_selector);
                button_temp.setTextColor(Color.parseColor("#FFFFFF"));

                button_humi.setBackgroundResource(R.drawable.select_off_selector);
                button_humi.setTextColor(Color.parseColor("#646464"));
                button_illu.setBackgroundResource(R.drawable.select_off_selector);
                button_illu.setTextColor(Color.parseColor("#646464"));

                Temp = new MainActivity_Work_Data_Temp(); // 온도.
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.dataFrame, Temp).commitAllowingStateLoss();
            }
        });

        button_humi = (Button) v.findViewById(R.id.button_humi);
        button_humi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_humi.setBackgroundResource(R.drawable.select_on_selector);
                button_humi.setTextColor(Color.parseColor("#FFFFFF"));

                button_temp.setBackgroundResource(R.drawable.select_off_selector);
                button_temp.setTextColor(Color.parseColor("#646464"));
                button_illu.setBackgroundResource(R.drawable.select_off_selector);
                button_illu.setTextColor(Color.parseColor("#646464"));

                Humi = new MainActivity_Work_Data_Humi(); // 온도.
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.dataFrame, Humi).commitAllowingStateLoss();
            }
        });

        button_illu = (Button) v.findViewById(R.id.button_illu);
        button_illu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_illu.setBackgroundResource(R.drawable.select_on_selector);
                button_illu.setTextColor(Color.parseColor("#FFFFFF"));

                button_temp.setBackgroundResource(R.drawable.select_off_selector);
                button_temp.setTextColor(Color.parseColor("#646464"));
                button_humi.setBackgroundResource(R.drawable.select_off_selector);
                button_humi.setTextColor(Color.parseColor("#646464"));

                Illu = new MainActivity_Work_Data_Illu(); // 온도.
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.dataFrame, Illu).commitAllowingStateLoss();
            }
        });

        // 온도 데이터.
        if(data.equals("temp")) {
            button_temp.setBackgroundResource(R.drawable.select_on_selector);
            button_temp.setTextColor(Color.parseColor("#FFFFFF"));

            button_humi.setBackgroundResource(R.drawable.select_off_selector);
            button_humi.setTextColor(Color.parseColor("#646464"));
            button_illu.setBackgroundResource(R.drawable.select_off_selector);
            button_illu.setTextColor(Color.parseColor("#646464"));

            Temp = new MainActivity_Work_Data_Temp(); // 온도.
            fragmentManager = getChildFragmentManager();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dataFrame, Temp).commitAllowingStateLoss();
        }

        // 습도 데이터.
        else if(data.equals("humi")) {
            button_humi.setBackgroundResource(R.drawable.select_on_selector);
            button_humi.setTextColor(Color.parseColor("#FFFFFF"));

            button_temp.setBackgroundResource(R.drawable.select_off_selector);
            button_temp.setTextColor(Color.parseColor("#646464"));
            button_illu.setBackgroundResource(R.drawable.select_off_selector);
            button_illu.setTextColor(Color.parseColor("#646464"));

            Humi = new MainActivity_Work_Data_Humi(); // 온도.
            fragmentManager = getChildFragmentManager();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dataFrame, Humi).commitAllowingStateLoss();
        }

        // 조도 데이터.
        else if(data.equals("illu")) {
            button_illu.setBackgroundResource(R.drawable.select_on_selector);
            button_illu.setTextColor(Color.parseColor("#FFFFFF"));

            button_temp.setBackgroundResource(R.drawable.select_off_selector);
            button_temp.setTextColor(Color.parseColor("#646464"));
            button_humi.setBackgroundResource(R.drawable.select_off_selector);
            button_humi.setTextColor(Color.parseColor("#646464"));

            Illu = new MainActivity_Work_Data_Illu(); // 온도.
            fragmentManager = getChildFragmentManager();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dataFrame, Illu).commitAllowingStateLoss();
        }

        return v;
    }

}