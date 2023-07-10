package com.example.plant_iot_tablet2;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

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
 * Use the {@link SignActivity_User#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignActivity_User extends Fragment {
    // 키보드.
    InputMethodManager imm;
    // 애니메이션.
    Animation fadeIn_anim;

    // 제목.
    TextView title_name, title_phone;

    // 이름, 생년월일, 핸드폰 번호.
    EditText edit_name, edit_phone;
    Button editNull_name, editNull_phone;
    TextView editWrong_name, editWrong_phone;

    // 회원가입 완료 버튼.
    MaterialButton btn;

    // 입력한 정보로 등록된 아이디가 있는지 확인.
    GetCheckUser gCheckUser;
    String getCheckUserURL = "http://hosting.ajplants.com/Plant_checkUser_Android.php";
    // 등록된 아이디가 없을 경우 승인 요청.
    SendSign sSign;
    String sendSignURL = "http://hosting.ajplants.com/Plant_sign_Android.php";

    // SignUp_Pass에서 받아온 ID, pass값.
    String id, pass;
    String name,phone;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignActivity_User() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp_User.
     */
    // TODO: Rename and change types and number of parameters
    public static SignActivity_User newInstance(String param1, String param2) {
        SignActivity_User fragment = new SignActivity_User();
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
        View v = inflater.inflate(R.layout.fragment_sign_user, container, false);

        // 키보드.
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 문자 수신 및 송신 허용.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS
                    },
                    1);
        }

        // ID, pass 값.
        if (getArguments() != null) {
            id = getArguments().getString("id");
            pass = getArguments().getString("pass");
        }

        // 애니메이션.
        fadeIn_anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        // 제목.
        title_name = (TextView) v.findViewById(R.id.title_name);
        title_phone = (TextView) v.findViewById(R.id.title_phone);
        title_name.startAnimation(fadeIn_anim);
        title_phone.startAnimation(fadeIn_anim);

        // 이름 입력.
        edit_name = (EditText) v.findViewById(R.id.edit_name);
        edit_name.startAnimation(fadeIn_anim);
        editNull_name = (Button) v.findViewById(R.id.editNull_name);
        editNull_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_name.setText(null);
                edit_name.requestFocus();
                imm.showSoftInput(edit_name, 0);

                editWrong_name.setText(null);
            }
        });
        editWrong_name = (TextView) v.findViewById(R.id.editWrong_name);
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 이름 입력란 X.
                    editNull_name.setBackgroundResource(0);
                } else { // 이름 입력란 O.
                    editNull_name.setBackgroundResource(R.drawable.sign_edit_null);
                }
            }
        });
        edit_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (edit_name.getText().toString().equals("")) {
                            editWrong_name.setText("이름을 입력해주세요");
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        // 핸드폰.
        edit_phone = (EditText) v.findViewById(R.id.edit_phone);
        edit_phone.startAnimation(fadeIn_anim);
        editNull_phone = (Button) v.findViewById(R.id.editNull_phone);
        editNull_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_phone.setText(null);
                edit_phone.requestFocus();
                imm.showSoftInput(edit_phone, 0);

                edit_phone.setText(null);
            }
        });
        editWrong_phone = (TextView) v.findViewById(R.id.editWrong_phone);
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 번호 입력란 X.
                    editNull_phone.setBackgroundResource(0);
                } else { // 번호 입력란 O.
                    editNull_phone.setBackgroundResource(R.drawable.sign_edit_null);
                }
            }
        });
        edit_phone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (edit_phone.getText().toString().equals("")) {
                            editWrong_phone.setText("번호를 입력해주세요");
                            return true;
                        } else {
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                }
                return false;
            }
        });

        // 회원가입 완료.
        btn = (MaterialButton) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_name.getText().toString().equals("")) { // 이름 입력 X.
                    editWrong_name.setText("이름을 입력해주세요");
                    edit_name.requestFocus();
                    imm.showSoftInput(edit_name, 0);
                } else {
                    if (edit_phone.getText().toString().equals("")) { // 번호 입력 X.
                        editWrong_phone.setText("번호를 입력해주세요");
                        edit_phone.requestFocus();
                        imm.showSoftInput(edit_phone, 0);
                    } else { // 회원가입 검사.
                        name = edit_name.getText().toString();
                        phone = edit_phone.getText().toString();

                        gCheckUser = new GetCheckUser();
                        gCheckUser.execute(getCheckUserURL, name, phone);
                    }
                }
            }
        });

        return v;
    }

    // 사용자 정보 얻어오기.
    class GetCheckUser extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String name = (String) params[1];
            String phone = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "phone=" + phone + "&name=" + name;

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
            String idExis = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    idExis = item.getString("id");
                }

                if(!idExis.equals(null)) { // 등록된 아이디 존재.
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    SignActivity_Already sign_already = new SignActivity_Already();
                    transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                    transaction.replace(R.id.signFrame, sign_already);
                    transaction.commit();
                }

            } catch (JSONException e) { // 등록된 아이디 존재X.
                e.printStackTrace();

                sSign = new SendSign(); // 회원가입 승인 요청.
                sSign.execute(sendSignURL, id, pass, name, phone);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                SignActivity_Fin signUp_fin = new SignActivity_Fin();
                transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                transaction.replace(R.id.signFrame, signUp_fin);
                transaction.commit();
            }
        }
    }

    // 회원가입 승인 요청.
    class SendSign extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String id = (String) params[1];
            String pass = (String) params[2];
            String name = (String) params[3];
            String phone = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&pass=" + pass + "&name=" + name + "&phone=" + phone;

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
}