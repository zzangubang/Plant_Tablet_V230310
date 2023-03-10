package com.example.plant_iot_tablet2;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Use the {@link SignActivity_ID#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignActivity_ID extends Fragment {
    // 키보드.
    InputMethodManager imm;
    // 애니메이션.
    Animation fadeIn_anim;

    // 제목.
    TextView title_id;

    // 아이디 입력.
    EditText edit_id;
    Button editNull_id;
    TextView editWrong_id; // 오류 발생 시 문구.

    // 아이디 중복 체크 및 계속하기 버튼.
    MaterialButton btn;
    String btnState = "아이디 중복 확인";
    GetCheckID gCheckID;
    String getCheckIDURL = "http://aj3dlab.dothome.co.kr/Test_checkID_Android.php";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignActivity_ID() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp_ID.
     */
    // TODO: Rename and change types and number of parameters
    public static SignActivity_ID newInstance(String param1, String param2) {
        SignActivity_ID fragment = new SignActivity_ID();
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
        View v = inflater.inflate(R.layout.fragment_sign_id, container, false);

        // 키보드.
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 애니메이션.
        fadeIn_anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        title_id = (TextView) v.findViewById(R.id.title_id);
        title_id.startAnimation(fadeIn_anim);

        // 아이디 입력.
        edit_id = (EditText) v.findViewById(R.id.edit_id);
        editNull_id = (Button) v.findViewById(R.id.editNull_id);
        editNull_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_id.setText(null);
            }
        });
        editWrong_id = (TextView) v.findViewById(R.id.editWrong_id);
        edit_id.startAnimation(fadeIn_anim);
        edit_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // x 아이콘 생성 여부.
                if(editable.toString().equals("")) {
                    editNull_id.setBackgroundResource(0);
                }
                else {
                    editNull_id.setBackgroundResource(R.drawable.sign_edit_null);
                }

                // btn 상태 변화.
                if(btnState.equals("계속하기")) {
                    btnState = "아이디 중복 확인";
                    btn.setText(btnState);
                }
            }
        });


        // 아이디 중복 체크 및 계속하기 버튼.
        btn = (MaterialButton) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edit_id.getText().toString();
                if(id.equals("")) { // 아이디 입력X.
                    editWrong_id.setTextColor(Color.parseColor("#850000"));
                    editWrong_id.setText("아이디를 입력해주세요");

                    edit_id.requestFocus();
                    imm.showSoftInput(edit_id, 0);
                }
                else { // 아이디 입력O.
                    if(btnState.equals("아이디 중복 확인")) { // 아직 중복 체크를 하지 않았거나 중복된 경우.
                        gCheckID = new GetCheckID();
                        gCheckID.execute(getCheckIDURL, id);
                    }
                    else { // 중복 체크가 완료된 경우. (String = "계속하기")
                        Bundle bundle = new Bundle(); // id값 넘기기.
                        bundle.putString("id", id);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        SignActivity_Pass signUp_pass = new SignActivity_Pass();
                        signUp_pass.setArguments(bundle);
                        transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                        transaction.replace(R.id.signFrame, signUp_pass);
                        transaction.commit();
                    }
                }
            }
        });

        return v;
    }

    // 로그인 정보 얻어오기.
    class GetCheckID extends AsyncTask<String, Integer, String> {
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
            String check = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    check = item.getString("check");
                }

                if (check.equals("OK")) {
                    editWrong_id.setTextColor(Color.parseColor("#850000"));
                    editWrong_id.setText("사용할 수 없는 아이디입니다");
                    btnState = "아이디 중복 확인";
                    btn.setText(btnState);

                    edit_id.requestFocus();
                    imm.showSoftInput(edit_id, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                editWrong_id.setTextColor(Color.parseColor("#003C85"));
                editWrong_id.setText("사용 가능한 아이디입니다 :)");

                btnState = "계속하기";
                btn.setText(btnState);
            }
        }
    }
}