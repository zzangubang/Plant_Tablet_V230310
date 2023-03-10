package com.example.plant_iot_tablet2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Timer_LedR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Timer_LedR extends Fragment {
    NumberPicker ledRWF, ledRWH, ledRWM, ledRSF, ledRSH, ledRSM;

    int ledRWH_i = 0, ledRWM_i = 0, ledRSH_i = 0, ledRSM_i = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Timer_LedR() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_Work_Timer_LedR.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Timer_LedR newInstance(String param1, String param2) {
        MainActivity_Work_Timer_LedR fragment = new MainActivity_Work_Timer_LedR();
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
        View v = inflater.inflate(R.layout.fragment_main_work_timer_ledr, container, false);



        ledRWH_i = ((MainActivity_Work_Timer) getParentFragment()).ledRWH;
        ledRWM_i = ((MainActivity_Work_Timer) getParentFragment()).ledRWM;
        ledRSH_i = ((MainActivity_Work_Timer) getParentFragment()).ledRSH;
        ledRSM_i = ((MainActivity_Work_Timer) getParentFragment()).ledRSM;

        ledRWF = (NumberPicker) v.findViewById(R.id.ledRWF);
        ledRWF.setMinValue(0);
        ledRWF.setMaxValue(1);
        ledRWF.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledRWF.setDisplayedValues(new String[]{
                "AM", "PM"
        });
        ledRWF.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(i1 == 0) {
                    if(ledRWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = ledRWH.getValue();
                    }
                }
                else {
                    if(ledRWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = ledRWH.getValue() + 12;
                    }
                }
                ValueSet();
            }
        });
        ledRWH = (NumberPicker) v.findViewById(R.id.ledRWH);
        ledRWH.setMinValue(1);
        ledRWH.setMaxValue(12);
        ledRWH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledRWH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i == 11 && i1 == 12) {
                    if (ledRWF.getValue() == 0) {
                        ledRWF.setValue(1);
                    } else {
                        ledRWF.setValue(0);
                    }
                }
                if (i == 12 && i1 == 11) {
                    if (ledRWF.getValue() == 0) {
                        ledRWF.setValue(1);
                    } else {
                        ledRWF.setValue(0);
                    }
                }

                if(ledRWF.getValue() == 0) {
                    if(ledRWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = i1;
                    }
                }
                else {
                    if(ledRWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRWH = i1 + 12;
                    }
                }

                ValueSet();
            }
        });
        ledRWM = (NumberPicker) v.findViewById(R.id.ledRWM);
        ledRWM.setMinValue(0);
        ledRWM.setMaxValue(5);
        ledRWM.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledRWM.setDisplayedValues(new String[]{
                "00", "10", "20", "30", "40", "50"
        });
        ledRWM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ((MainActivity_Work_Timer)getParentFragment()).ledRWM = i1;
                ValueSet();
            }
        });
        ledRSF = (NumberPicker) v.findViewById(R.id.ledRSF);
        ledRSF.setMinValue(0);
        ledRSF.setMaxValue(1);
        ledRSF.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledRSF.setDisplayedValues(new String[]{
                "AM", "PM"
        });
        ledRSF.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(i1 == 0) {
                    if(ledRSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = ledRSH.getValue();
                    }
                }
                else {
                    if(ledRSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = ledRSH.getValue() + 12;
                    }
                }
                ValueSet();
            }
        });
        ledRSH = (NumberPicker) v.findViewById(R.id.ledRSH);
        ledRSH.setMinValue(1);
        ledRSH.setMaxValue(12);
        ledRSH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledRSH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i == 11 && i1 == 12) {
                    if (ledRSF.getValue() == 0) {
                        ledRSF.setValue(1);
                    } else {
                        ledRSF.setValue(0);
                    }
                }
                if (i == 12 && i1 == 11) {
                    if (ledRSF.getValue() == 0) {
                        ledRSF.setValue(1);
                    } else {
                        ledRSF.setValue(0);
                    }
                }

                if(ledRSF.getValue() == 0) {
                    if(ledRSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = i1;
                    }
                }
                else {
                    if(ledRSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledRSH = i1 + 12;
                    }
                }
                ValueSet();
            }
        });
        ledRSM = (NumberPicker) v.findViewById(R.id.ledRSM);
        ledRSM.setMinValue(0);
        ledRSM.setMaxValue(5);
        ledRSM.setDisplayedValues(new String[]{
                "00", "10", "20", "30", "40", "50"
        });
        ledRSM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ((MainActivity_Work_Timer)getParentFragment()).ledRSM = i1;
                ValueSet();
            }
        });

        // 처음 세팅.
        if (0 <= ledRWH_i && ledRWH_i < 12) {
            ledRWF.setValue(0);
            if (ledRWH_i == 0) {
                ledRWH.setValue(12);
            } else {
                ledRWH.setValue(ledRWH_i);
            }
            ledRWM.setValue(ledRWM_i);
        } else {
            ledRWF.setValue(1);
            if (ledRWH_i == 12) {
                ledRWH.setValue(12);
            } else {
                ledRWH.setValue(ledRWH_i - 12);
            }
            ledRWM.setValue(ledRWM_i);
        }

        if (0 <= ledRSH_i && ledRSH_i < 12) {
            ledRSF.setValue(0);
            if (ledRSH_i == 0) {
                ledRSH.setValue(12);
            } else {
                ledRSH.setValue(ledRSH_i);
            }
            ledRSM.setValue(ledRSM_i);
        } else {
            ledRSF.setValue(1);
            if (ledRSH_i == 12) {
                ledRSH.setValue(12);
            } else {
                ledRSH.setValue(ledRSH_i - 12);
            }
            ledRSM.setValue(ledRSM_i);
        }


        return v;
    }

    public void ValueSet() {
        String ledRWFT = "", ledRWHT = "", ledRWMT = "";
        String ledRSFT = "", ledRSHT = "", ledRSMT = "";

        if ((ledRWF.getValue() == ledRSF.getValue()) && (ledRWH.getValue() == ledRSH.getValue()) && (ledRWM.getValue() == ledRSM.getValue())) {
            ((MainActivity_Work_Timer) getParentFragment()).value.setText("계속 가동");
        } else {

            if (ledRWF.getValue() == 0) {
                ledRWFT = "AM";
            } else {
                ledRWFT = "PM";
            }
            ledRWHT = String.valueOf(ledRWH.getValue());
            ledRWMT = String.valueOf(ledRWM.getValue());

            if (ledRSF.getValue() == 0) {
                ledRSFT = "AM";
            } else {
                ledRSFT = "PM";
            }
            ledRSHT = String.valueOf(ledRSH.getValue());
            ledRSMT = String.valueOf(ledRSM.getValue());

            ((MainActivity_Work_Timer) getParentFragment()).value.setText(ledRWFT + " " + ledRWHT + ":" + ledRWMT + "0 ~ " + ledRSFT + " " + ledRSHT + ":" + ledRSMT + "0");
        }
    }
}