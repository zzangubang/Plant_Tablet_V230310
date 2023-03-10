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
import android.widget.Toast;

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
 * Use the {@link FindActivity_Pass_Input#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindActivity_Pass_Input extends Fragment {
    // 키보드.
    InputMethodManager imm;
    // 애니메이션.
    Animation fadeIn_anim;

    // 제목.
    TextView title_id, title_name, title_phone;

    // 이름, 핸드폰 번호.
    EditText edit_id, edit_name, edit_phone;
    Button editNull_id, editNull_name, editNull_phone;

    // 아이디 찾기 버튼.
    MaterialButton btn;


    // 등록된 ID 체크.
    GetID gID;
    String getIDURL = "http://aj3dlab.dothome.co.kr/Test_checkUser_Android.php";

    Toast toast;
    String id = "", name = "", phone = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindActivity_Pass_Input() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindActivity_Pass_Input.
     */
    // TODO: Rename and change types and number of parameters
    public static FindActivity_Pass_Input newInstance(String param1, String param2) {
        FindActivity_Pass_Input fragment = new FindActivity_Pass_Input();
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
        View v = inflater.inflate(R.layout.fragment_find_pass_input, container, false);



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

        // 애니메이션.
        fadeIn_anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        // 제목.
        title_id = (TextView) v.findViewById(R.id.title_id);
        title_name = (TextView) v.findViewById(R.id.title_name);
        title_phone = (TextView) v.findViewById(R.id.title_phone);
        title_id.startAnimation(fadeIn_anim);
        title_name.startAnimation(fadeIn_anim);
        title_phone.startAnimation(fadeIn_anim);

        // 아이디 입력.
        edit_id = (EditText) v.findViewById(R.id.edit_id);
        edit_id.startAnimation(fadeIn_anim);
        editNull_id = (Button) v.findViewById(R.id.editNull_id);
        editNull_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_id.setText(null);
                edit_id.requestFocus();
                imm.showSoftInput(edit_id, 0);
            }
        });
        edit_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 아이디 입력란 X.
                    editNull_id.setBackgroundResource(0);
                } else { // 아이디 입력란 O.
                    editNull_id.setBackgroundResource(R.drawable.sign_edit_null);
                }
            }
        });
        edit_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (edit_id.getText().toString().equals("")) {
                            toastShow("아이디를 입력해주세요");
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

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
            }
        });
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
                            toastShow("이름을 입력해주세요");
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
                            toastShow("번호를 입력해주세요");
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
                if (edit_id.getText().toString().equals("")) { // 아이디 입력 X.
                    toastShow("아이디를 입력해주세요");
                    edit_id.requestFocus();
                    imm.showSoftInput(edit_id, 0);

                } else {
                    if (edit_name.getText().toString().equals("")) { // 이름 입력 X.
                        toastShow("이름을 입력해주세요");
                        edit_name.requestFocus();
                        imm.showSoftInput(edit_name, 0);
                    } else {
                        if (edit_phone.getText().toString().equals("")) { // 번호 입력 X.
                            toastShow("번호를 입력해주세요");
                            edit_phone.requestFocus();
                            imm.showSoftInput(edit_phone, 0);
                        } else { // 회원가입 검사.
                            id = edit_id.getText().toString();
                            name = edit_name.getText().toString();
                            phone = edit_phone.getText().toString();

                            gID = new GetID();
                            gID.execute(getIDURL, name, phone);
                        }
                    }
                }
            }
        });

        return v;
    }

    // 사용자 id 얻어오기.
    class GetID extends AsyncTask<String, Integer, String> {
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
            String idT = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    idT = item.getString("id");
                }

                if(!idT.equals(null)) { // 등록된 아이디 존재.
                    if(id.equals(idT)) { // 정보 일치 O -> 아이디 변경.
                        Bundle bundle = new Bundle(); // id값 넘기기.
                        bundle.putString("id", idT);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        FindActivity_Pass_Revise find_pass = new FindActivity_Pass_Revise();
                        find_pass.setArguments(bundle);
                        transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                        transaction.replace(R.id.findFrame, find_pass);
                        transaction.commit();
                    }
                    else { // 정보 일치 X.
                        toastShow("등록된 정보가 없습니다");
                    }
                }

            } catch (JSONException e) { // 등록된 아이디 존재X.
                e.printStackTrace();

                toastShow("등록된 정보가 없습니다");
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