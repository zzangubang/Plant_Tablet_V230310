package com.example.plant_iot_tablet2;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivity_Work_Data_Illu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivity_Work_Data_Illu extends Fragment {
    ImageView button_calendar, button_last;
    TextView dateT;

    WebView webView;
    WebSettings settings;
    TableLayout table;

    GetDate gDate;
    String getDateURL = "http://hosting.ajplants.com/Plant_valueDate_Android.php";
    GetValue gValue;
    String getValueURL = "http://hosting.ajplants.com/Plant_illuTable_Android.php";

    ProgressDialog dialog;
    Toast toast;
    String model = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivity_Work_Data_Illu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivity_Work_Data_Illu.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivity_Work_Data_Illu newInstance(String param1, String param2) {
        MainActivity_Work_Data_Illu fragment = new MainActivity_Work_Data_Illu();
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
        View v = inflater.inflate(R.layout.fragment_main_work_data_illu, container, false);

        model = ((MainActivity_Work_Data) getParentFragment()).model;
        // 로딩창.
        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 조회중입니다.\n잠시만 기다려주세요.");

        gDate = new GetDate();
        gDate.execute(getDateURL);
        dialog.show();

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthS = String.valueOf(month+1);
                String dayOfMonthS = String.valueOf(dayOfMonth);
                if(month+1 < 10)
                    monthS = "0" + String.valueOf(month+1);
                if(dayOfMonth < 10)
                    dayOfMonthS = "0" +String.valueOf(dayOfMonth);
                String selectDate = year + "-" + monthS + "-" + dayOfMonthS;

                dateT.setText(selectDate);
                webView.loadUrl("http://hosting.ajplants.com/Plant_illuGraph.php?date="+selectDate+"&model="+model);
                dialog.show();
                gValue = new GetValue();
                gValue.execute(getValueURL, selectDate);
            }
        }, mYear, mMonth, mDay);
        button_calendar = (ImageView) v.findViewById(R.id.button_calendar); // 달력 버튼.
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        button_last = (ImageView) v.findViewById(R.id.button_last); // 마지막 날짜 버튼.
        button_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gDate = new GetDate();
                gDate.execute(getDateURL);
                dialog.show();
            }
        });
        dateT = (TextView) v.findViewById(R.id.date);

        table = (TableLayout) v.findViewById(R.id.table); // 테이블.

        webView = (WebView) v.findViewById(R.id.graph); // 웹뷰(그래프).
        webView.setWebViewClient(new WebViewClient());
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(settings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);

        return v;
    }

    // 마지막 날짜 읽어오기.
    class GetDate extends AsyncTask<String, Integer, String> {
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
            String date = "";

            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    date = item.getString("date");
                }

                dateT.setText(date);
                webView.loadUrl("http://hosting.ajplants.com/Plant_illuGraph.php?date="+date+"&model="+model);
                gValue = new GetValue();
                gValue.execute(getValueURL, date);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 값 읽어오기.
    class GetValue extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String date = (String) params[1];

            String serverURL = (String) params[0];
            String postParameters = "date=" + date + "&model=" + model;

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        protected void onPostExecute(String str) {
            String TAG_JSON = "aj3dlab";
            String time = "", data = "";
            table.removeViews(1, table.getChildCount()-1); // 테이블 비우기.

            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                Typeface typeface = getResources().getFont(R.font.main_font);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    time = item.getString("time");
                    data = item.getString("data");

                    TableRow tableRow = new TableRow(getContext());
                    tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    // 시간 값 넣기.
                    TextView TtextView = new TextView(getContext());
                    TtextView.setText(time);
                    TtextView.setTypeface(typeface);
                    TtextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                    TtextView.setPadding(2, 2, 2, 2);
                    TtextView.setGravity(Gravity.CENTER);
                    tableRow.addView(TtextView);

                    // 센서 값 넣기.
                    TextView V1textView = new TextView(getContext());
                    V1textView.setText(String.format("%.2f", Float.valueOf(data)));
                    V1textView.setTypeface(typeface);
                    V1textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                    V1textView.setPadding(2, 2, 2, 2);
                    V1textView.setGravity(Gravity.CENTER);
                    tableRow.addView(V1textView);

                    table.addView(tableRow);
                }
                dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                toastShow("해당 날짜의 데이터가 없거나 데이터 조회에 실패하였습니다. 잠시후 다시 시도해주세요.");
            }
        }
    }

    public void toastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}