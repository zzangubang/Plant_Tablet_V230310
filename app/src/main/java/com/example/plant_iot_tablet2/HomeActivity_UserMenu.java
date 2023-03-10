package com.example.plant_iot_tablet2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity_UserMenu extends AppCompatActivity {
    // Back 버튼.
    ImageView button_back;

    // 사용자 정보.
    TextView nameT, phoneT;
    ImageView button_edit; // 핸드폰 번호 변경.
    GetUser gUser;
    String getUserURL = "http://aj3dlab.dothome.co.kr/Test_getUser_Android.php";

    // 로그아웃 버튼.
    Button button_logout;
    SharedPreferences sharedPreferences; // 로그인 정보 삭제.
    SharedPreferences.Editor editor;
    SendNoti sNoti;
    String sendNotiURL = "http://aj3dlab.dothome.co.kr/Test_notiS_Android.php";

    // 메뉴.
    LinearLayout button_pass, button_alarm, button_help, button_secession;

    //로딩창
    ProgressDialog dialog;

    String id = "";
    String name = "", phone = "", pass = "";
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user_menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }
        mContext = this;
        Intent getIntent = getIntent();
        id = getIntent.getStringExtra("id");

        // 로딩창.
        dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("정보 조회 중입니다.\n잠시만 기다려주세요.");

        // Back 버튼.
        button_back = (ImageView) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 사용자 정보.
        nameT = (TextView) findViewById(R.id.name);
        phoneT = (TextView) findViewById(R.id.phone);
        dialog.show();
        gUser = new GetUser();
        gUser.execute(getUserURL, id);

        button_edit = (ImageView) findViewById(R.id.button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_PassCheck.class);
                intent.putExtra("purpose", "phone");
                intent.putExtra("id", id);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼.
        button_logout = (Button) findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity_UserMenu.this, R.style.DialogTheme);
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences = getSharedPreferences("PlantLogin", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("id", "");
                        editor.putString("pass", "");
                        editor.commit();

                        // 알림 X.
                        stopService(new Intent(getApplicationContext(), NotiService.class));
                        sNoti = new SendNoti();
                        sNoti.execute(sendNotiURL, "X");

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        ((HomeActivity) HomeActivity.mContext).HomeFinish();

                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // 메뉴.
        // 비밀번호 변경.
        button_pass = (LinearLayout) findViewById(R.id.button_pass);
        button_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentWork("pass");
            }
        });
        // 알림.
        button_alarm = (LinearLayout) findViewById(R.id.button_alarm);
        button_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentWork("alarm");
            }
        });
        // 고객센터.
        button_help = (LinearLayout) findViewById(R.id.button_help);
        button_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentWork("help");
            }
        });
        // 계정 탈퇴.
        button_secession = (LinearLayout) findViewById(R.id.button_secession);
        button_secession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_PassCheck.class);
                intent.putExtra("purpose", "secession");
                intent.putExtra("id", id);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });
    }

    // HomeActivity_Work 변환.
    public void IntentWork(String work) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity_Work.class);
        intent.putExtra("work", work);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("pass", pass);
        startActivity(intent);
    }

    // 사용자 정보 얻어오기.
    class GetUser extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String id = (String) params[1];

            String serverURL = (String) params[0];
            String postParameters = "id=" + id;

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

                    name = item.getString("name");
                    phone = item.getString("phone");
                    pass = item.getString("pass");
                }

                nameT.setText(name);
                phoneT.setText(phone);
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 알림 요청.
    class SendNoti extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String noti = (String) params[1];

            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&noti=" + noti;
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

    // UI 업데이트.
    public void Refresh() {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}