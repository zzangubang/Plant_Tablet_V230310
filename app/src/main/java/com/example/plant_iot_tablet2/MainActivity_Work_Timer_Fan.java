package com.example.plant_iot_tablet2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Timer_Fan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Timer_Fan extends Fragment {
    NumberPicker fanWF, fanWH, fanWM, fanSF, fanSH, fanSM;

    int fanWH_i = 0, fanWM_i = 0, fanSH_i = 0, fanSM_i = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Timer_Fan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Timer_Fan newInstance(String param1, String param2) {
        MainActivity_Work_Timer_Fan fragment = new MainActivity_Work_Timer_Fan();
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
        View v = inflater.inflate(R.layout.fragment_main_work_timer_fan, container, false);

        fanWH_i = ((MainActivity_Work_Timer) getParentFragment()).fanWH;
        fanWM_i = ((MainActivity_Work_Timer) getParentFragment()).fanWM;
        fanSH_i = ((MainActivity_Work_Timer) getParentFragment()).fanSH;
        fanSM_i = ((MainActivity_Work_Timer) getParentFragment()).fanSM;

        fanWF = (NumberPicker) v.findViewById(R.id.fanWF);
        fanWF.setMinValue(0);
        fanWF.setMaxValue(1);
        fanWF.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        fanWF.setDisplayedValues(new String[]{
                "AM", "PM"
        });
        fanWF.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(i1 == 0) {
                    if(fanWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = fanWH.getValue();
                    }
                }
                else {
                    if(fanWH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = fanWH.getValue() + 12;
                    }
                }
                ValueSet();
            }
        });
        fanWH = (NumberPicker) v.findViewById(R.id.fanWH);
        fanWH.setMinValue(1);
        fanWH.setMaxValue(12);
        fanWH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        fanWH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i == 11 && i1 == 12) {
                    if (fanWF.getValue() == 0) {
                        fanWF.setValue(1);
                    } else {
                        fanWF.setValue(0);
                    }
                }
                if (i == 12 && i1 == 11) {
                    if (fanWF.getValue() == 0) {
                        fanWF.setValue(1);
                    } else {
                        fanWF.setValue(0);
                    }
                }

                if(fanWF.getValue() == 0) {
                    if(i1 == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = i1;
                    }
                }
                else {
                    if(i1 == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanWH = i1 + 12;
                    }
                }

                ValueSet();
            }
        });
        fanWM = (NumberPicker) v.findViewById(R.id.fanWM);
        fanWM.setMinValue(0);
        fanWM.setMaxValue(5);
        fanWM.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        fanWM.setDisplayedValues(new String[]{
                "00", "10", "20", "30", "40", "50"
        });
        fanWM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ((MainActivity_Work_Timer)getParentFragment()).fanWM = i1;
                ValueSet();
            }
        });
        fanSF = (NumberPicker) v.findViewById(R.id.fanSF);
        fanSF.setMinValue(0);
        fanSF.setMaxValue(1);
        fanSF.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        fanSF.setDisplayedValues(new String[]{
                "AM", "PM"
        });
        fanSF.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(i1 == 0) {
                    if(fanSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = fanSH.getValue();
                    }
                }
                else {
                    if(fanSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = fanSH.getValue() + 12;
                    }
                }
                ValueSet();
            }
        });
        fanSH = (NumberPicker) v.findViewById(R.id.fanSH);
        fanSH.setMinValue(1);
        fanSH.setMaxValue(12);
        fanSH.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        fanSH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i == 11 && i1 == 12) {
                    if (fanSF.getValue() == 0) {
                        fanSF.setValue(1);
                    } else {
                        fanSF.setValue(0);
                    }
                }
                if (i == 12 && i1 == 11) {
                    if (fanSF.getValue() == 0) {
                        fanSF.setValue(1);
                    } else {
                        fanSF.setValue(0);
                    }
                }
                if(fanSF.getValue() == 0) {
                    if(fanSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = 0;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = i1;
                    }
                }
                else {
                    if(fanSH.getValue() == 12) {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = 12;
                    }
                    else {
                        ((MainActivity_Work_Timer)getParentFragment()).fanSH = i1 +12;
                    }
                }
                ValueSet();
            }
        });
        fanSM = (NumberPicker) v.findViewById(R.id.fanSM);
        fanSM.setMinValue(0);
        fanSM.setMaxValue(5);
        fanSM.setDisplayedValues(new String[]{
                "00", "10", "20", "30", "40", "50"
        });
        fanSM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ((MainActivity_Work_Timer)getParentFragment()).fanSM = i1;
                ValueSet();
            }
        });

        // 처음 세팅.
        if (0 <= fanWH_i && fanWH_i < 12) {
            fanWF.setValue(0);
            if (fanWH_i == 0) {
                fanWH.setValue(12);
            } else {
                fanWH.setValue(fanWH_i);
            }
            fanWM.setValue(fanWM_i);
        } else {
            fanWF.setValue(1);
            if (fanWH_i == 12) {
                fanWH.setValue(12);
            } else {
                fanWH.setValue(fanWH_i - 12);
            }
            fanWM.setValue(fanWM_i);
        }

        if (0 <= fanSH_i && fanSH_i < 12) {
            fanSF.setValue(0);
            if (fanSH_i == 0) {
                fanSH.setValue(12);
            } else {
                fanSH.setValue(fanSH_i);
            }
            fanSM.setValue(fanSM_i);
        } else {
            fanSF.setValue(1);
            if (fanSH_i == 12) {
                fanSH.setValue(12);
            } else {
                fanSH.setValue(fanSH_i - 12);
            }
            fanSM.setValue(fanSM_i);
        }

        return v;
    }

    public void ValueSet() {
        String fanWFT = "", fanWHT = "", fanWMT = "";
        String fanSFT = "", fanSHT = "", fanSMT = "";

        if ((fanWF.getValue() == fanSF.getValue()) && (fanWH.getValue() == fanSH.getValue()) && (fanWM.getValue() == fanSM.getValue())) {
            ((MainActivity_Work_Timer) getParentFragment()).value.setText("계속 가동");
        } else {
            if (fanWF.getValue() == 0) {
                fanWFT = "AM";
            } else {
                fanWFT = "PM";
            }
            fanWHT = String.valueOf(fanWH.getValue());
            fanWMT = String.valueOf(fanWM.getValue());

            if (fanSF.getValue() == 0) {
                fanSFT = "AM";
                if (fanSH.getValue() == 12) {
                    ((MainActivity_Work_Timer) getParentFragment()).fanSH = 0;
                } else {
                    ((MainActivity_Work_Timer) getParentFragment()).fanSH = fanSH.getValue();
                }
            } else {
                fanSFT = "PM";
                if (fanSH.getValue() == 12) {
                    ((MainActivity_Work_Timer) getParentFragment()).fanSH = 12;
                } else {
                    ((MainActivity_Work_Timer) getParentFragment()).fanSH = fanSH.getValue() + 12;
                }
            }
            fanSHT = String.valueOf(fanSH.getValue());
            fanSMT = String.valueOf(fanSM.getValue());

            ((MainActivity_Work_Timer) getParentFragment()).value.setText(fanWFT + " " + fanWHT + ":" + fanWMT + "0 ~ " + fanSFT + " " + fanSHT + ":" + fanSMT + "0");
        }
    }
}