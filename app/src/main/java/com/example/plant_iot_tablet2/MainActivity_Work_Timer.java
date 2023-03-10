package com.example.plant_iot_tablet2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Timer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Timer extends Fragment {
    // 레이아웃.
    Button button_tempHumi, button_fan, button_ledL, button_ledR; // 메뉴 버튼.
    TextView caution; // 주의사항(?).
    TextView value; // 해당 메뉴에 따른 값.
    Button button_save; // 저장 버튼.

    // 프래그먼트.
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment TempHumi, Fan, LedL, LedR;

    public int tempMin = 0, tempMax = 60, humiMin = 0, humiMax = 100;
    public int fanWH = 24, fanWM = 0, fanSH = 24, fanSM = 0;
    public int ledLWH = 0, ledLWM = 0, ledLSH = 0, ledLSM = 0;
    public int ledRWH = 0, ledRWM = 0, ledRSH = 0, ledRSM = 0;
    GetAuto gAuto;
    String getAutoURL = "http://aj3dlab.dothome.co.kr/Test_autoG_Android.php";
    SendAuto sAuto;
    String sendAutoURL = "http://aj3dlab.dothome.co.kr/Test_autoS_Android.php";

    String model = "";
    ProgressDialog dialog;
    Toast toast;
    public Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Timer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_Work_Timer.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Timer newInstance(String param1, String param2) {
        MainActivity_Work_Timer fragment = new MainActivity_Work_Timer();
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
        View v = inflater.inflate(R.layout.fragment_main_work_timer, container, false);

        mContext = getContext();
        model = ((MainActivity_Work)getActivity()).model;

        // 로딩창.
        dialog = new ProgressDialog(getContext(), R.style.ProgressDialogTheme);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("정보를 읽어오는 중입니다.\n잠시만 기다려주세요.");

        // 메뉴 선택에 따른 값.
        caution = (TextView) v.findViewById(R.id.caution);
        value = (TextView) v.findViewById(R.id.value);

        // 메뉴.
        button_tempHumi = (Button) v.findViewById(R.id.button_tempHumi); // 온도 및 습도.
        button_tempHumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_tempHumi.setBackgroundResource(R.drawable.select_on_selector);
                button_tempHumi.setTextColor(Color.parseColor("#FFFFFF"));
                caution.setText("※ 0 ~ 60°C / 0 ~ 100% 내에서 설정 가능합니다 ※");
                value.setText(String.valueOf(tempMin) + " ~ " + String.valueOf(tempMax) + "°C   /   " + String.valueOf(humiMin) + " ~ " + String.valueOf(humiMax) + "%");

                button_fan.setBackgroundResource(R.drawable.select_off_selector);
                button_fan.setTextColor(Color.parseColor("#646464"));
                button_ledL.setBackgroundResource(R.drawable.select_off_selector);
                button_ledL.setTextColor(Color.parseColor("#646464"));
                button_ledR.setBackgroundResource(R.drawable.select_off_selector);
                button_ledR.setTextColor(Color.parseColor("#646464"));

                TempHumi = new MainActivity_Work_Timer_TempHumi(); // 온도 및 습도.
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.timerFrame, TempHumi).commitAllowingStateLoss();
            }
        });
        button_fan = (Button) v.findViewById(R.id.button_fan);
        button_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String work = "", stop = "";
                caution.setText("※ 계속 가동을 원할 경우, 같은 시간으로 설정하면 됩니다 ※");
                button_fan.setBackgroundResource(R.drawable.select_on_selector);
                button_fan.setTextColor(Color.parseColor("#FFFFFF"));
                if (fanWH == fanSH && fanWM == fanSM) {
                    value.setText("계속 가동");
                } else {
                    if (0 <= fanWH && fanWH < 12) { // 가동 시간.
                        if (fanWH == 0) {
                            work = "AM 12:" + String.valueOf(fanWM) + "0 ~ ";
                        } else {
                            work = "AM " + String.valueOf(fanWH) + ":" + String.valueOf(fanWM) + "0 ~ ";
                        }
                    } else {
                        if (fanWH == 12) {
                            work = "PM 12:" + String.valueOf(fanWM) + "0 ~ ";
                        } else {
                            work = "PM " + String.valueOf(fanWH - 12) + ":" + String.valueOf(fanWM) + "0 ~ ";
                        }
                    }
                    if (0 <= fanSH && fanSH < 12) { // 중단 시간.
                        if (fanSH == 0) {
                            stop = "AM 12:" + String.valueOf(fanSM) + "0";
                        } else {
                            stop = "AM " + String.valueOf(fanSH) + ":" + String.valueOf(fanSM) + "0";
                        }
                    } else {
                        if (fanSH == 12) {
                            stop = "PM 12:" + String.valueOf(fanSM) + "0";
                        } else {
                            stop = "PM " + String.valueOf(fanSH - 12) + ":" + String.valueOf(fanSM) + "0";
                        }
                    }

                    value.setText(work + stop);
                }

                button_tempHumi.setBackgroundResource(R.drawable.select_off_selector);
                button_tempHumi.setTextColor(Color.parseColor("#646464"));
                button_ledL.setBackgroundResource(R.drawable.select_off_selector);
                button_ledL.setTextColor(Color.parseColor("#646464"));
                button_ledR.setBackgroundResource(R.drawable.select_off_selector);
                button_ledR.setTextColor(Color.parseColor("#646464"));

                Fan = new MainActivity_Work_Timer_Fan(); // 팬.
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.timerFrame, Fan).commitAllowingStateLoss();
            }
        });
        button_ledL = (Button) v.findViewById(R.id.button_ledL);
        button_ledL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String work = "", stop = "";
                caution.setText("※ 계속 가동을 원할 경우, 같은 시간으로 설정하면 됩니다 ※");
                button_ledL.setBackgroundResource(R.drawable.select_on_selector);
                button_ledL.setTextColor(Color.parseColor("#FFFFFF"));
                if (ledLWH == ledLSH && ledLWM == ledLSM) {
                    value.setText("계속 가동");
                } else {
                    if (0 <= ledLWH && ledLWH < 12) { // 가동 시간.
                        if (ledLWH == 0) {
                            work = "AM 12:" + String.valueOf(ledLWM) + "0 ~ ";
                        } else {
                            work = "AM " + String.valueOf(ledLWH) + ":" + String.valueOf(ledLWM) + "0 ~ ";
                        }
                    } else {
                        if (ledLWH == 12) {
                            work = "PM 12:" + String.valueOf(ledLWM) + "0 ~ ";
                        } else {
                            work = "PM " + String.valueOf(ledLWH - 12) + ":" + String.valueOf(ledLWM) + "0 ~ ";
                        }
                    }
                    if (0 <= ledLSH && ledLSH < 12) { // 중단 시간.
                        if (ledLSH == 0) {
                            stop = "AM 12:" + String.valueOf(ledLSM) + "0";
                        } else {
                            stop = "AM " + String.valueOf(ledLSH) + ":" + String.valueOf(ledLSM) + "0";
                        }
                    } else {
                        if (ledLSH == 12) {
                            stop = "PM 12:" + String.valueOf(ledLSM) + "0";
                        } else {
                            stop = "PM " + String.valueOf(ledLSH - 12) + ":" + String.valueOf(ledLSM) + "0";
                        }
                    }

                    value.setText(work + stop);
                }

                button_tempHumi.setBackgroundResource(R.drawable.select_off_selector);
                button_tempHumi.setTextColor(Color.parseColor("#646464"));
                button_fan.setBackgroundResource(R.drawable.select_off_selector);
                button_fan.setTextColor(Color.parseColor("#646464"));
                button_ledR.setBackgroundResource(R.drawable.select_off_selector);
                button_ledR.setTextColor(Color.parseColor("#646464"));

                LedL = new MainActivity_Work_Timer_LedL(); // 전등(좌).
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.timerFrame, LedL).commitAllowingStateLoss();
            }
        });
        button_ledR = (Button) v.findViewById(R.id.button_ledR);
        button_ledR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String work = "", stop = "";
                caution.setText("※ 계속 가동을 원할 경우, 같은 시간으로 설정하면 됩니다 ※");
                button_ledR.setBackgroundResource(R.drawable.select_on_selector);
                button_ledR.setTextColor(Color.parseColor("#FFFFFF"));
                if(ledRWH == ledRSH && ledRWM == ledRSM) {
                    value.setText("계속 가동");
                }
                else {
                    if (0 <= ledRWH && ledRWH < 12) { // 가동 시간.
                        if (ledRWH == 0) {
                            work = "AM 12:" + String.valueOf(ledRWM) + "0 ~ ";
                        } else {
                            work = "AM " + String.valueOf(ledRWH) + ":" + String.valueOf(ledRWM) + "0 ~ ";
                        }
                    } else {
                        if (ledRWH == 12) {
                            work = "PM 12:" + String.valueOf(ledRWM) + "0 ~ ";
                        } else {
                            work = "PM " + String.valueOf(ledRWH - 12) + ":" + String.valueOf(ledRWM) + "0 ~ ";
                        }
                    }
                    if (0 <= ledRSH && ledRSH < 12) { // 중단 시간.
                        if (ledRSH == 0) {
                            stop = "AM 12:" + String.valueOf(ledRSM) + "0";
                        } else {
                            stop = "AM " + String.valueOf(ledRSH) + ":" + String.valueOf(ledRSM) + "0";
                        }
                    } else {
                        if (ledRSH == 12) {
                            stop = "PM 12:" + String.valueOf(ledRSM) + "0";
                        } else {
                            stop = "PM " + String.valueOf(ledRSH - 12) + ":" + String.valueOf(ledRSM) + "0";
                        }
                    }

                    value.setText(work + stop);
                }

                button_tempHumi.setBackgroundResource(R.drawable.select_off_selector);
                button_tempHumi.setTextColor(Color.parseColor("#646464"));
                button_fan.setBackgroundResource(R.drawable.select_off_selector);
                button_fan.setTextColor(Color.parseColor("#646464"));
                button_ledL.setBackgroundResource(R.drawable.select_off_selector);
                button_ledL.setTextColor(Color.parseColor("#646464"));

                LedR = new MainActivity_Work_Timer_LedR(); // 전등(우).
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.timerFrame, LedR).commitAllowingStateLoss();
            }
        });

        // 저장 버튼.
        button_save = (Button) v.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fanWH == fanSH && fanWM == fanSM) {
                    fanWH = 0;
                    fanWM = 0;
                    fanSH = 0;
                    fanSM = 0;
                }
                if (ledLWH == ledLSH && ledLWM == ledLSM) {
                    ledLWH = 0;
                    ledLWM = 0;
                    ledLSH = 0;
                    ledLSM = 0;
                }
                if (ledRWH == ledRSH && ledRWM == ledRSM) {
                    ledRWH = 0;
                    ledRWM = 0;
                    ledRSH = 0;
                    ledRSM = 0;
                }
                String fanW = String.valueOf(fanWH) + ":" + String.valueOf(fanWM) + "0";
                String fanS = String.valueOf(fanSH) + ":" + String.valueOf(fanSM) + "0";
                String ledLW = String.valueOf(ledLWH) + ":" + String.valueOf(ledLWM) + "0";
                String ledLS = String.valueOf(ledLSH) + ":" + String.valueOf(ledLSM) + "0";
                String ledRW = String.valueOf(ledRWH) + ":" + String.valueOf(ledRWM) + "0";
                String ledRS = String.valueOf(ledRSH) + ":" + String.valueOf(ledRSM) + "0";

                String command = String.valueOf(tempMin) + "." + String.valueOf(tempMax) + "." + String.valueOf(humiMin) + "." + String.valueOf(humiMax) + "." + fanW + "." + fanS + "." + ledLW + "." + ledLS + "." + ledRW + "." + ledRS;
                sAuto = new SendAuto();
                sAuto.execute(sendAutoURL, command);
                toastShow("저장되었습니다");
            }
        });

        // 값 읽어오기.
        dialog.show();
        gAuto = new GetAuto();
        gAuto.execute(getAutoURL);

        return v;
    }


    // 이전 자동 모드일 때의 사용자 값 가져오기.
    class GetAuto extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "model=" + model;

            try {
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(postParameters.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            String TAG_JSON = "aj3dlab";
            String tempMinT = "", tempMaxT = "", humiMinT = "", humiMaxT = "", fanWT = "", fanST = "", ledLWT = "", ledLST = "", ledRWT = "", ledRST = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    tempMinT = item.getString("tempMin");
                    tempMaxT = item.getString("tempMax");
                    humiMinT = item.getString("humiMin");
                    humiMaxT = item.getString("humiMax");
                    fanWT = item.getString("fanW");
                    fanST = item.getString("fanS");
                    ledLWT = item.getString("ledLW");
                    ledLST = item.getString("ledLS");
                    ledRWT = item.getString("ledRW");
                    ledRST = item.getString("ledRS");
                }
                String fanW[] = fanWT.split(":");
                String fanS[] = fanST.split(":");
                String ledLW[] = ledLWT.split(":");
                String ledLS[] = ledLST.split(":");
                String ledRW[] = ledRWT.split(":");
                String ledRS[] = ledRST.split(":");

                tempMin = Integer.valueOf(tempMinT);
                tempMax = Integer.valueOf(tempMaxT);
                humiMin = Integer.valueOf(humiMinT);
                humiMax = Integer.valueOf(humiMaxT);

                fanWH = Integer.valueOf(fanW[0]);
                fanWM = Integer.valueOf(fanW[1]) / 10;
                fanSH = Integer.valueOf(fanS[0]);
                fanSM = Integer.valueOf(fanS[1]) / 10;
                ledLWH = Integer.valueOf(ledLW[0]);
                ledLWM = Integer.valueOf(ledLW[1]) / 10;
                ledLSH = Integer.valueOf(ledLS[0]);
                ledLSM = Integer.valueOf(ledLS[1]) / 10;
                ledRWH = Integer.valueOf(ledRW[0]);
                ledRWM = Integer.valueOf(ledRW[1]) / 10;
                ledRSH = Integer.valueOf(ledRS[0]);
                ledRSM = Integer.valueOf(ledRS[1]) / 10;

                value.setText(String.valueOf(tempMin) + " ~ " + String.valueOf(tempMax) + "°C   /   " + String.valueOf(humiMin) + " ~ " + String.valueOf(humiMax) + "%");
                dialog.dismiss();

                TempHumi = new MainActivity_Work_Timer_TempHumi(); // 프래그먼트 Set.
                fragmentManager = getChildFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.timerFrame, TempHumi).commitAllowingStateLoss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 동작 제어.
    class SendAuto extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "model=" + model + "&command=" + (String) params[1];

            try {
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(postParameters.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            String TAG_JSON = "aj3dlab";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // Toast 메시지 띄우기.
    public void toastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}