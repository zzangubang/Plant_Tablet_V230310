package com.example.plant_iot_tablet2;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
 * Use the {@link HomeActivity_Work_Pass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeActivity_Work_Pass extends Fragment {
    EditText edit, editChange, editChange2;
    ImageView editNull, editNullChange, editNullChange2;
    ImageView editVisibleChange, editVisibleChange2;
    String visibleChange = "invisible", visibleChange2 = "invisible";
    TextView wrongT;

    Button button_change;

    SendPass sPass;
    String sendPassURL = "http://hosting.ajplants.com/Plant_sendPass_Android.php";

    InputMethodManager imm;
    Toast toast;
    String id = "", pass = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeActivity_Work_Pass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeActivity_Work_Pass.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeActivity_Work_Pass newInstance(String param1, String param2) {
        HomeActivity_Work_Pass fragment = new HomeActivity_Work_Pass();
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
        View v = inflater.inflate(R.layout.fragment_home_work_pass, container, false);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드.

        id = ((HomeActivity_Work)getActivity()).id;
        pass = ((HomeActivity_Work)getActivity()).pass;

        // 현재 비밀번호.
        edit = (EditText) v.findViewById(R.id.edit);
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
        editNull = (ImageView) v.findViewById(R.id.editNull);
        editNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setText(null);
                edit.requestFocus();
                imm.showSoftInput(edit, 0);
            }
        });

        // 변경할 비밀번호.
        editChange = (EditText) v.findViewById(R.id.editChange);
        editChange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 비밀번호 입력란 X.
                    editNullChange.setImageResource(0);
                    editVisibleChange.setImageResource(0);
                } else { // 비밀번호 입력란 O.
                    editNullChange.setImageResource(R.drawable.edit_null);
                    if(visibleChange.equals("visible")) {
                        editVisibleChange.setImageResource(R.drawable.edit_visible);
                    }
                    else {
                        editVisibleChange.setImageResource(R.drawable.edit_invisible);
                    }
                }
            }
        });
        editVisibleChange = (ImageView) v.findViewById(R.id.editVisibleChange);
        editVisibleChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibleChange.equals("visible")) { // 보임 -> 안보임.
                    visibleChange = "invisible";
                    editVisibleChange.setImageResource(R.drawable.edit_invisible);
                    editChange.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editChange.setSelection(editChange.getText().length());
                }
                else { // 안보임 -> 보임.
                    visibleChange = "visible";
                    editVisibleChange.setImageResource(R.drawable.edit_visible);
                    editChange.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editChange.setSelection(editChange.getText().length());
                }
            }
        });
        editNullChange = (ImageView) v.findViewById(R.id.editNullChange);
        editNullChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editChange.setText(null);
                editChange.requestFocus();
                imm.showSoftInput(editChange, 0);

                editChange2.setText(null);
                wrongT.setText(null);
            }
        });

        // 변경할 비밀번호 확인.
        editChange2 = (EditText) v.findViewById(R.id.editChange2);
        editChange2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 비밀번호 입력란 X.
                    editNullChange2.setImageResource(0);
                    editVisibleChange2.setImageResource(0);
                } else { // 비밀번호 입력란 O.
                    editNullChange2.setImageResource(R.drawable.edit_null);

                    // Visible or Invisible.
                    if(visibleChange2.equals("visible")) {
                        editVisibleChange2.setImageResource(R.drawable.edit_visible);
                    }
                    else {
                        editVisibleChange2.setImageResource(R.drawable.edit_invisible);
                    }

                    // 위에 비밀번호와 일치하는지.
                    if(editable.toString().equals(editChange.getText().toString())) { // 일치 O.
                        wrongT.setTextColor(Color.parseColor("#003C85"));
                        wrongT.setText("비밀번호가 일치합니다 :)");
                    }
                    else { // 일치 X.
                        wrongT.setTextColor(Color.parseColor("#850000"));
                        wrongT.setText("비밀번호가 일치하지 않습니다");
                    }
                }
            }
        });
        editVisibleChange2 = (ImageView) v.findViewById(R.id.editVisibleChange2);
        editVisibleChange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibleChange2.equals("visible")) { // 보임 -> 안보임.
                    visibleChange2 = "invisible";
                    editVisibleChange2.setImageResource(R.drawable.edit_invisible);
                    editChange2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editChange2.setSelection(editChange2.getText().length());
                }
                else { // 안보임 -> 보임.
                    visibleChange2 = "visible";
                    editVisibleChange2.setImageResource(R.drawable.edit_visible);
                    editChange2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editChange2.setSelection(editChange.getText().length());
                }
            }
        });
        editNullChange2 = (ImageView) v.findViewById(R.id.editNullChange2);
        editNullChange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editChange2.setText(null);
                editChange2.requestFocus();
                imm.showSoftInput(editChange2, 0);
            }
        });
        wrongT = (TextView) v.findViewById(R.id.wrongT);

        // 변경 버튼.
        button_change = (Button) v.findViewById(R.id.button_change);
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit.getText().toString().equals("") || editChange.getText().toString().equals("") || editChange2.getText().toString().equals("")) { // 빈 칸 존재.
                    toastShow("비밀번호를 입력해주세요");
                    if(edit.getText().toString().equals("")) {
                        edit.requestFocus();
                        imm.showSoftInput(edit, 0);
                    }
                    else if (editChange.getText().toString().equals("")) {
                        editChange.requestFocus();
                        imm.showSoftInput(editChange, 0);
                    }
                    else {
                        editChange2.requestFocus();
                        imm.showSoftInput(editChange2, 0);
                    }
                }
                else { // 빈 칸 존재X.
                    if(!edit.getText().toString().equals(pass)) { // 현재 비밀번호 일치 X.
                        edit.requestFocus();
                        imm.showSoftInput(edit, 0);
                        edit.setSelection(edit.getText().length());
                        toastShow("비밀번호를 잘못 입력하셨습니다.");
                    }
                    else { // 현재 비밀번호 일치 O.
                        if(editChange.getText().toString().equals(editChange2.getText().toString())) { // 변경할 비밀번호 == 비밀번호 확인.
                            try {
                                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기.
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            sPass = new SendPass(); // 비밀번호 변경하기.
                            sPass.execute(sendPassURL, editChange2.getText().toString());
                            toastShow("변경하였습니다");
                            ((HomeActivity_Work)getActivity()).Finish();
                        }
                        else { // 변경할 비밀번호 != 비밀번호 확인.
                            editChange2.requestFocus();
                            imm.showSoftInput(editChange2, 0);
                            editChange2.setSelection(editChange2.getText().length());
                            toastShow("비밀번호가 일치하지 않습니다.");
                        }
                    }
                }
            }
        });

        return v;
    }

    // 비밀번호 변경.
    class SendPass extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String pass = (String) params[1];

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
            toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}