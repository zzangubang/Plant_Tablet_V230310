package com.example.plant_iot_tablet2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Activity_ListRevise extends Activity {
    TextView listName;
    EditText edit;
    ImageView editNull;
    TextView button_cancel, button_apply;

    ModelRevise rModel;
    String modelReviseURL = "http://hosting.ajplants.com/Plant_modelRevise_Android.php";

    InputMethodManager imm; // 키보드.
    Toast toast;
    String name = "", model = "", id = "";
    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list_revise);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContext = this;

        Intent getIntent = getIntent();
        name = getIntent.getStringExtra("name");
        model = getIntent.getStringExtra("model");
        id = getIntent.getStringExtra("id");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드.

        // 레이아웃.
        listName = (TextView) findViewById(R.id.listName);
        listName.setText(model);

        editNull = (ImageView) findViewById(R.id.editNull);
        editNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setText(null);
                edit.requestFocus();
                imm.showSoftInput(edit, 0);
            }
        });
        edit = (EditText) findViewById(R.id.edit);
        edit.setText(name);
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

        button_cancel = (TextView) findViewById(R.id.button_cancel); // 취소 버튼.
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_apply = (TextView) findViewById(R.id.button_apply); // 적용 버튼.
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals("")) {
                    toastShow("변경하실 이름을 입력해주세요.");
                    edit.requestFocus();
                    imm.showSoftInput(edit, 0);
                } else {
                    name = edit.getText().toString();
                    try {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기.
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    rModel = new ModelRevise();
                    rModel.execute(modelReviseURL, name, model, id);

                    if (id.equals("ahjoo")) { // 관리자.
                        ((MasterActivity) MasterActivity.mContext).Refresh();
                    } else { // 관리자 X.
                        ((HomeActivity) HomeActivity.mContext).Refresh();
                    }

                    finish();
                }
            }
        });
    }

    // 이름 변경.
    class ModelRevise extends AsyncTask<String, Integer, String> {
        String name, model, id;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            name = (String) params[1];
            model = (String) params[2];
            id = (String) params[3];

            String serverURL = (String) params[0];
            String postParameters = "name=" + name + "&model=" + model + "&id=" + id;

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

    // 토스트 메세지 띄우기.
    public void toastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
