package com.example.plant_iot_tablet2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Activity_PassCheck extends Activity {
    EditText edit;
    ImageView editNull;
    TextView button_cancel, button_apply;

    InputMethodManager imm;
    Toast toast;

    Secession secession;
    String secessionURL = "http://hosting.ajplants.com/Plant_secession_Android.php";
    SharedPreferences sharedPreferences; // 로그인 정보 삭제.
    SharedPreferences.Editor editor;

    String purpose = "", id = "", pass = "";
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pass_check);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent getIntent = getIntent();
        purpose = getIntent.getStringExtra("purpose");
        id = getIntent.getStringExtra("id");
        pass = getIntent.getStringExtra("pass");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드.

        edit = (EditText) findViewById(R.id.edit);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 비밀번호 입력란 X.
                    editNull.setImageResource(0);
                } else { // 비밀번호 입력란 O.
                    editNull.setImageResource(R.drawable.edit_null);
                }
            }
        });

        editNull = (ImageView) findViewById(R.id.editNull);
        editNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setText(null);
                edit.requestFocus();
                imm.showSoftInput(edit, 0);
            }
        });

        button_cancel = (TextView) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_apply = (TextView) findViewById(R.id.button_apply);
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals("")) {
                    toastShow("비밀번호를 입력해주세요.");
                    edit.requestFocus();
                    imm.showSoftInput(edit, 0);
                } else {
                    if (edit.getText().toString().equals(pass)) { // 비밀번호 알맞게 입력한 경우.
                        try {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기.
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        switch (purpose) {
                            case "phone": // 전화번호 변경.
                                    Intent intentP = new Intent(getApplicationContext(), Activity_PhoneRevise.class);
                                    intentP.putExtra("id", id);
                                    startActivity(intentP);
                                    finish();
                                break;
                            case "secession": // 계정 탈퇴.
                                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_PassCheck.this, R.style.DialogTheme);
                                builder.setMessage("정말로 탈퇴하시겠습니까? :(");
                                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        secession = new Secession();
                                        secession.execute(secessionURL);
                                        sharedPreferences = getSharedPreferences("PlantLogin", MODE_PRIVATE);
                                        editor = sharedPreferences.edit();
                                        editor.putString("id", "");
                                        editor.putString("pass", "");
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        ((HomeActivity)HomeActivity.mContext).HomeFinish();
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                break;
                            default:
                                break;
                        }
                    }
                    else { // 비밀번호가 틀린 경우.
                        toastShow("비밀번호를 잘못 입력하셨습니다.");
                    }
                }
            }
        });
    }

    // 계정 탈퇴.
    class Secession extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

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
                }
            } catch (JSONException e) {
            }
        }
    }

    public void toastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
