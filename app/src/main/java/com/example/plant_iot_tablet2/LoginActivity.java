package com.example.plant_iot_tablet2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    // 키보드.
    InputMethodManager imm;
    // 로딩창.
    ProgressDialog dialog;

    // 전체 레이아웃.
    LinearLayout loginLayout;

    // 아이디, 비밀번호 입력.
    EditText edit_id, edit_pass;
    ImageView editNull_id, editNull_pass;

    // 로그인.
    TextView wrongT;
    Button button_login;

    // 아이디/비밀번호 찾기 및 회원가입.
    TextView button_id, button_pass, button_sign;

    // 로그인 기능.
    GetLogin gLogin;
    String getLoginURL = "http://hosting.ajplants.com/Plant_login_Android.php";
    // 자동 로그인.
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 블루투스 및 와이파이 권한 허용.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_NETWORK_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);
        }

        // 키보드.
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // 로딩창.
        dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로그인 중입니다.\n잠시만 기다려주세요.");

        // 전체 레이아웃.
        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NullPointer 관련 오류가 떠서 임시로 try/catch문 이용.
                try {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        // 자동 로그인.
        sharedPreferences = getSharedPreferences("PlantLogin", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String auto_id = sharedPreferences.getString("id", "");
        String auto_pass = sharedPreferences.getString("pass", "");
        if(!auto_id.equals("") && !auto_pass.equals("")) { // 로그인 정보가 있는 경우(자동 로그인).
            login(auto_id, auto_pass);
        }

        // 아이디, 비밀번호 입력.
        // 아이디.
        edit_id = (EditText) findViewById(R.id.edit_id);
        editNull_id = (ImageView) findViewById(R.id.editNull_id);
        editNull_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_id.setText(null);
                edit_id.requestFocus();
            }
        });
        edit_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) { // 입력 여부에 따라 'x 아이콘' 적용.
                if(editable.toString().equals("")) { editNull_id.setBackgroundResource(0); }
                else { editNull_id.setBackgroundResource(R.drawable.edit_null); }
            }
        });

        // 비밀번호.
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        editNull_pass = (ImageView) findViewById(R.id.editNull_pass);
        editNull_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_pass.setText(null);
                edit_pass.requestFocus();
            }
        });
        edit_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) { // 입력 여부에 따라 'x 아이콘' 적용.
                if(editable.toString().equals("")) { editNull_pass.setBackgroundResource(0); }
                else { editNull_pass.setBackgroundResource(R.drawable.edit_null); }
            }
        });
        edit_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch(i) {
                    case EditorInfo.IME_ACTION_DONE:
                        if(edit_id.getText().toString().equals("")) { // 아이디 입력 X.
                            wrongT.setText("아이디를 입력해주세요.");
                            edit_pass.clearFocus();
                            edit_id.requestFocus();
                            imm.showSoftInput(edit_id, 0);
                        }
                        else {
                            if(edit_pass.getText().toString().equals(""))  { // 비밀번호 입력 X.
                                wrongT.setText("비밀번호를 입력해주세요.");
                            }
                            else { // 모두 입력 완료.
                                login(edit_id.getText().toString(), edit_pass.getText().toString());
                                return false;
                            }
                        }

                        break;
                }
                return true;
            }
        });

        // 로그인
        wrongT = (TextView) findViewById(R.id.wrongT);
        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_id.getText().toString().equals("")) { // 아이디 입력 X.
                    wrongT.setText("아이디를 입력해주세요.");
                    edit_id.requestFocus();
                    imm.showSoftInput(edit_id, 0);
                }
                else {
                    if(edit_pass.getText().toString().equals("")) { // 비밀번호 입력 X.
                        wrongT.setText("비밀번호를 입력해주세요.");
                        edit_pass.requestFocus();
                        imm.showSoftInput(edit_pass, 0);
                    }
                    else { // 모두 입력 완료.
                        login(edit_id.getText().toString(), edit_pass.getText().toString());
                    }
                }
            }
        });

        // 아이디/비밀번호 찾기 및 회원가입.
        // 아이디 찾기.
        button_id = (TextView) findViewById(R.id.button_id);
        button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindActivity_ID.class);
                startActivity(intent);
            }
        });
        // 비밀번호 찾기.
        button_pass = (TextView) findViewById(R.id.button_pass);
        button_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindActivity_Pass.class);
                startActivity(intent);
            }
        });
        //회원가입.
        button_sign = (TextView) findViewById(R.id.button_sign);
        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
            }
        });
    }

    // 로그인 함수.
    void login(String id, String pass) {
        dialog.show();

        gLogin = new GetLogin();
        gLogin.execute(getLoginURL, id, pass);
    }

    // 로그인 정보 얻어오기.
    class GetLogin extends AsyncTask<String, Integer, String> {
        String id, pass;
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            id = (String) params[1];
            pass = (String) params[2];
            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&pass=" + pass;

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
            String type = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    type = item.getString("type");
                }

                if (!type.equals("X")) { // 로그인 승인
                    // 로그인 정보 저장 (for 자동로그인).
                    editor.putString("id", id);
                    editor.putString("pass", pass);
                    editor.commit();

                    // 로그인 성공 시, 화면 이동.
                    if(type.equals("O")) { // 관리자 승인 O.
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                    }
                    else { // 관리자.
                        Intent intent = new Intent(getApplicationContext(), MasterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else { // 관리자 승인 X.
                    wrongT.setText("관리자 승인 대기중입니다.\n관리자의 승인은 최소 1일 ~ 3일(주말 제외)이 소요됩니다.");
                }
            } catch (JSONException e) { // 사용자 정보 X.
                e.printStackTrace();
                wrongT.setText("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
            }
            dialog.dismiss();
        }
    }
}