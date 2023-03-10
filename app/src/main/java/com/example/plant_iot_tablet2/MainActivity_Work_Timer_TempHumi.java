package com.example.plant_iot_tablet2;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.google.android.material.slider.LabelFormatter.LABEL_GONE;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.RangeSlider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Timer_TempHumi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Timer_TempHumi extends Fragment {
    LinearLayout layout; // 전체 레이아웃.

    TextView tempMinPlus, tempMinMinus, tempMaxPlus, tempMaxMinus, humiMinPlus, humiMinMinus, humiMaxPlus, humiMaxMinus;
    TextView tempMinT, tempMaxT, humiMinT, humiMaxT;
    RangeSlider tempSlider, humiSlider;

    InputMethodManager imm;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Timer_TempHumi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_Work_Timer_TempHumi.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Timer_TempHumi newInstance(String param1, String param2) {
        MainActivity_Work_Timer_TempHumi fragment = new MainActivity_Work_Timer_TempHumi();
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
        View v = inflater.inflate(R.layout.fragment_main_work_timer__temphumi, container, false);

        // 키보드.
        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        // 전체 레이아웃.
        layout = (LinearLayout) v.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tempMinT.clearFocus();
                    tempMaxT.clearFocus();
                    humiMinT.clearFocus();
                    humiMaxT.clearFocus();

                    imm.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        // 온도.
        tempMinT = (TextView) v.findViewById(R.id.tempMinT); // 최소 온도.
        tempMinT.setText(String.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMin));
        tempMinPlus = (TextView) v.findViewById(R.id.tempMinPlus);
        tempMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = ((MainActivity_Work_Timer) getParentFragment()).tempMin + 1;
                if(temp <= ((MainActivity_Work_Timer) getParentFragment()).tempMax) { // 최댓값보다 작아야만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).tempMin = temp;
                    tempMinT.setText(String.valueOf(temp));
                    ValueSet();
                    tempSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMax));
                }
            }
        });
        tempMinMinus = (TextView) v.findViewById(R.id.tempMinMinus);
        tempMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = ((MainActivity_Work_Timer) getParentFragment()).tempMin - 1;
                if(temp >= 0) { // 0 이상이여야 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).tempMin = temp;
                    tempMinT.setText(String.valueOf(temp));
                    ValueSet();
                    tempSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMax));
                }
            }
        });
        tempMaxT = (TextView) v.findViewById(R.id.tempMaxT); //최대 온도.
        tempMaxT.setText(String.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMax));
        tempMaxPlus = (TextView) v.findViewById(R.id.tempMaxPlus);
        tempMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = ((MainActivity_Work_Timer) getParentFragment()).tempMax + 1;
                if(temp <= 60) { // 60보다 작아야만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).tempMax = temp;
                    tempMaxT.setText(String.valueOf(temp));
                    ValueSet();
                }
            }
        });
        tempMaxMinus = (TextView) v.findViewById(R.id.tempMaxMinus);
        tempMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = ((MainActivity_Work_Timer) getParentFragment()).tempMax - 1;
                if(temp >= ((MainActivity_Work_Timer) getParentFragment()).tempMin) { // 최솟값보다 커야지만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).tempMax = temp;
                    tempMaxT.setText(String.valueOf(temp));
                    ValueSet();
                }
            }
        });
        tempSlider = (RangeSlider) v.findViewById(R.id.tempSlider);
        tempSlider.setValueFrom(0);
        tempSlider.setValueTo(60);
        tempSlider.setStepSize(1);
        tempSlider.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#6A9562"))); // custom.
        tempSlider.setTrackActiveTintList(ColorStateList.valueOf(Color.parseColor("#6A9562")));
        tempSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));
        tempSlider.setTickVisible(false);
        tempSlider.setLabelBehavior(LABEL_GONE);
        tempSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMax));
        tempSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) { // 손 뗐을 때 함수.
                // slider.getValues() => [0.0, 5.0] 식.
                int minNum = Float.toString(tempSlider.getValues().get(0)).indexOf(".");
                int maxNum = Float.toString(tempSlider.getValues().get(1)).indexOf(".");
                String minVal = Float.toString(tempSlider.getValues().get(0)).substring(0, minNum); // 왼쪽 thumb 값.
                String maxVal = Float.toString(tempSlider.getValues().get(1)).substring(0, maxNum); // 오른쪽 thumb 값.

                ((MainActivity_Work_Timer) getParentFragment()).tempMin = Integer.valueOf(minVal);
                ((MainActivity_Work_Timer) getParentFragment()).tempMax = Integer.valueOf(maxVal);
                tempMinT.setText(minVal);
                tempMaxT.setText(maxVal);
                ValueSet();

            }
        });

        // 습도.
        humiMinT = (TextView) v.findViewById(R.id.humiMinT); // 최소 습도.
        humiMinT.setText(String.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMin));
        humiMinPlus = (TextView) v.findViewById(R.id.humiMinPlus);
        humiMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int humi = ((MainActivity_Work_Timer) getParentFragment()).humiMin + 1;
                if(humi <= ((MainActivity_Work_Timer) getParentFragment()).humiMax) { // 최댓값보다 작아야만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).humiMin = humi;
                    humiMinT.setText(String.valueOf(humi));
                    ValueSet();
                }
            }
        });
        humiMinMinus = (TextView) v.findViewById(R.id.humiMinMinus);
        humiMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int humi = ((MainActivity_Work_Timer) getParentFragment()).humiMin - 1;
                if(humi >= 0) { // 0보다 커야지만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).humiMin = humi;
                    humiMinT.setText(String.valueOf(humi));
                    ValueSet();
                }
            }
        });
        humiMaxT = (TextView) v.findViewById(R.id.humiMaxT); // 최대 습도.
        humiMaxT.setText(String.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMax));
        humiMaxPlus = (TextView) v.findViewById(R.id.humiMaxPlus);
        humiMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int humi = ((MainActivity_Work_Timer) getParentFragment()).humiMax + 1;
                if(humi <= 100) { // 100보다 작아야만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).humiMax = humi;
                    humiMaxT.setText(String.valueOf(humi));
                    ValueSet();
                    humiSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMax));
                }
            }
        });
        humiMaxMinus = (TextView) v.findViewById(R.id.humiMaxMinus);
        humiMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int humi = ((MainActivity_Work_Timer) getParentFragment()).humiMax - 1;
                if(humi >= ((MainActivity_Work_Timer) getParentFragment()).humiMin) { // humiMin보다 커야지만 동작.
                    ((MainActivity_Work_Timer) getParentFragment()).humiMax = humi;
                    humiMaxT.setText(String.valueOf(humi));
                    ValueSet();
                    humiSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMax));
                }
            }
        });
        humiSlider = (RangeSlider) v.findViewById(R.id.humiSlider);
        humiSlider.setValueFrom(0);
        humiSlider.setValueTo(100);
        humiSlider.setStepSize(1);
        humiSlider.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#6A9562"))); // custom.
        humiSlider.setTrackActiveTintList(ColorStateList.valueOf(Color.parseColor("#6A9562")));
        humiSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor("#DADADA")));
        humiSlider.setTickVisible(false);
        humiSlider.setLabelBehavior(LABEL_GONE);
        humiSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMax));
        humiSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) { // 손 뗐을 때.;
                // slider.getValues() => [0.0, 5.0] 식.
                int minNum = Float.toString(humiSlider.getValues().get(0)).indexOf(".");
                int maxNum = Float.toString(humiSlider.getValues().get(1)).indexOf(".");
                String minVal = Float.toString(humiSlider.getValues().get(0)).substring(0, minNum); // 왼쪽 thumb 값.
                String maxVal = Float.toString(humiSlider.getValues().get(1)).substring(0, maxNum); // 오른쪽 thumb 값.

                ((MainActivity_Work_Timer) getParentFragment()).humiMin = Integer.valueOf(minVal);
                ((MainActivity_Work_Timer) getParentFragment()).humiMax = Integer.valueOf(maxVal);
                humiMinT.setText(minVal);
                humiMaxT.setText(maxVal);
                ValueSet();
            }
        });

        return v;
    }

    // MainActivity_Work_Timer의 value 텍스트 변경.
    public void ValueSet() {
        tempSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMax));
        humiSlider.setValues(Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMin), Float.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMax));
        ((MainActivity_Work_Timer) getParentFragment()).value.setText(String.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMin) + " ~ " +
                String.valueOf(((MainActivity_Work_Timer) getParentFragment()).tempMax) + "°C   /   " +
                String.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMin) + " ~ " + String.valueOf(((MainActivity_Work_Timer) getParentFragment()).humiMax) + "%");
    }
}