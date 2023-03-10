package com.example.plant_iot_tablet2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Timer_LedL#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Timer_LedL extends Fragment {
    NumberPicker ledLWF, ledLWH, ledLWM, ledLSF, ledLSH, ledLSM;

    int ledLWH_i = 0, ledLWM_i = 0, ledLSH_i = 0, ledLSM_i = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Timer_LedL() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_Work_Timer_LedL.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Timer_LedL newInstance(String param1, String param2) {
        MainActivity_Work_Timer_LedL fragment = new MainActivity_Work_Timer_LedL();
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
        View v = inflater.inflate(R.layout.fragment_main_work_timer_ledl, container, false);

        ledLWH_i = ((MainActivity_Work_Timer) getParentFragment()).ledLWH;
        ledLWM_i = ((MainActivity_Work_Timer) getParentFragment()).ledLWM;
        ledLSH_i = ((MainActivity_Work_Timer) getParentFragment()).ledLSH;
        ledLSM_i = ((MainActivity_Work_Timer) getParentFragment()).ledLSM;


        ledLWF = (NumberPicker) v.findViewById(R.id.ledLWF);
        ledLWF.setMinValue(0);
        ledLWF.setMaxValue(1);
        ledLWF.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledLWF.setDisplayedValues(new String[]{
                "AM", "PM"
        });
        ledLWF.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(i1 == 0) {
                    if(ledLWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = ledLWH.getValue();
                    }
                }
                else {
                    if(ledLWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = ledLWH.getValue() + 12;
                    }
                }
                ValueSet();
            }
        });
        ledLWH = (NumberPicker) v.findViewById(R.id.ledLWH);
        ledLWH.setMinValue(1);
        ledLWH.setMaxValue(12);
        ledLWH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledLWH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i == 11 && i1 == 12) {
                    if (ledLWF.getValue() == 0) {
                        ledLWF.setValue(1);
                    } else {
                        ledLWF.setValue(0);
                    }
                }
                if (i == 12 && i1 == 11) {
                    if (ledLWF.getValue() == 0) {
                        ledLWF.setValue(1);
                    } else {
                        ledLWF.setValue(0);
                    }
                }

                if(ledLWF.getValue() == 0) {
                    if(ledLWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = i1;
                    }
                }
                else {
                    if(ledLWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLWH = i1 + 12;
                    }
                }

                ValueSet();
            }
        });
        ledLWM = (NumberPicker) v.findViewById(R.id.ledLWM);
        ledLWM.setMinValue(0);
        ledLWM.setMaxValue(5);
        ledLWM.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledLWM.setDisplayedValues(new String[] {
                "00", "10", "20", "30", "40", "50"
        });
        ledLWM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ((MainActivity_Work_Timer)getParentFragment()).ledLWM = i1;
                ValueSet();
            }
        });
        ledLSF = (NumberPicker) v.findViewById(R.id.ledLSF);
        ledLSF.setMinValue(0);
        ledLSF.setMaxValue(1);
        ledLSF.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledLSF.setDisplayedValues(new String[] {
                "AM", "PM"
        });
        ledLSF.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(i1 == 0) {
                    if(ledLSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = ledLSH.getValue();
                    }
                }
                else {
                    if(ledLSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = ledLSH.getValue() + 12;
                    }
                }
                ValueSet();
            }
        });
        ledLSH = (NumberPicker) v.findViewById(R.id.ledLSH);
        ledLSH.setMinValue(1);
        ledLSH.setMaxValue(12);
        ledLSH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        ledLSH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i == 11 && i1 == 12) {
                    if (ledLSF.getValue() == 0) {
                        ledLSF.setValue(1);
                    } else {
                        ledLSF.setValue(0);
                    }
                }
                if (i == 12 && i1 == 11) {
                    if (ledLSF.getValue() == 0) {
                        ledLSF.setValue(1);
                    } else {
                        ledLSF.setValue(0);
                    }
                }

                if(ledLSF.getValue() == 0) {
                    if(ledLSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = i1;
                    }
                }
                else {
                    if(ledLSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).ledLSH = i1 + 12;
                    }
                }
                ValueSet();
            }
        });
        ledLSM = (NumberPicker) v.findViewById(R.id.ledLSM);
        ledLSM.setMinValue(0);
        ledLSM.setMaxValue(5);
        ledLSM.setDisplayedValues(new String[] {
                "00", "10", "20", "30", "40", "50"
        });
        ledLSM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ((MainActivity_Work_Timer)getParentFragment()).ledLSM = i1;
                ValueSet();
            }
        });

        // 처음 세팅.
        if(0 <= ledLWH_i && ledLWH_i < 12) {
            ledLWF.setValue(0);
            if(ledLWH_i == 0) {
                ledLWH.setValue(12);
            }
            else {
                ledLWH.setValue(ledLWH_i);
            }
            ledLWM.setValue(ledLWM_i);
        }
        else {
            ledLWF.setValue(1);
            if(ledLWH_i == 12) {
                ledLWH.setValue(12);
            }
            else {
                ledLWH.setValue(ledLWH_i - 12);
            }
            ledLWM.setValue(ledLWM_i);
        }

        if(0 <= ledLSH_i && ledLSH_i < 12) {
            ledLSF.setValue(0);
            if(ledLSH_i == 0) {
                ledLSH.setValue(12);
            }
            else {
                ledLSH.setValue(ledLSH_i);
            }
            ledLSM.setValue(ledLSM_i);
        }
        else {
            ledLSF.setValue(1);
            if(ledLSH_i == 12) {
                ledLSH.setValue(12);
            }
            else {
                ledLSH.setValue(ledLSH_i - 12);
            }
            ledLSM.setValue(ledLSM_i);
        }



        return v;
    }

    public void ValueSet() {
        String ledLWFT = "", ledLWHT = "", ledLWMT = "";
        String ledLSFT = "", ledLSHT = "", ledLSMT = "";

        if ((ledLWF.getValue() == ledLSF.getValue()) && (ledLWH.getValue() == ledLSH.getValue()) && (ledLWM.getValue() == ledLSM.getValue())) {
            ((MainActivity_Work_Timer) getParentFragment()).value.setText("계속 가동");
        } else {

            if (ledLWF.getValue() == 0) {
                ledLWFT = "AM";
            } else {
                ledLWFT = "PM";
            }
            ledLWHT = String.valueOf(ledLWH.getValue());
            ledLWMT = String.valueOf(ledLWM.getValue());

            if (ledLSF.getValue() == 0) {
                ledLSFT = "AM";
            } else {
                ledLSFT = "PM";
            }
            ledLSHT = String.valueOf(ledLSH.getValue());
            ledLSMT = String.valueOf(ledLSM.getValue());

            ((MainActivity_Work_Timer) getParentFragment()).value.setText(ledLWFT + " " + ledLWHT + ":" + ledLWMT + "0 ~ " + ledLSFT + " " + ledLSHT + ":" + ledLSMT + "0");
        }
    }
}