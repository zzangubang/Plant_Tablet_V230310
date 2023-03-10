package com.example.plant_iot_tablet2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignActivity_Pass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignActivity_Pass extends Fragment {
    // 키보드.
    InputMethodManager imm;
    // 애니메이션.
    Animation fadeIn_anim;

    // 제목.
    TextView title_pass, title_pass2;

    // 비밀번호 입력 및 비밀번호 확인.
    EditText edit_pass, edit_pass2;
    Button editVisable_pass, editVisable_pass2;
    String pass = "invisable", pass2 = "invisable";
    Button editNull_pass, editNull_pass2;
    TextView editWrong_pass, editWrong_pass2;

    // 계속하기 버튼.
    MaterialButton btn;

    // SignUp_ID에서 받아온 ID값.
    String id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignActivity_Pass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp_Pass.
     */
    // TODO: Rename and change types and number of parameters
    public static SignActivity_Pass newInstance(String param1, String param2) {
        SignActivity_Pass fragment = new SignActivity_Pass();
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
        View v = inflater.inflate(R.layout.fragment_sign_pass, container, false);

        // 키보드.
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // ID 값.
        if(getArguments() != null) {
            id = getArguments().getString("id");
        }

        // 애니메이션.
        fadeIn_anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

        // 제목.
        title_pass = (TextView) v.findViewById(R.id.title_pass);
        title_pass2 = (TextView) v.findViewById(R.id.title_pass2);
        title_pass.startAnimation(fadeIn_anim);
        title_pass2.startAnimation(fadeIn_anim);

        // 비밀번호 입력.
        edit_pass = (EditText) v.findViewById(R.id.edit_pass);
        edit_pass.startAnimation(fadeIn_anim);
        editVisable_pass = (Button) v.findViewById(R.id.editVisable_pass);
        editVisable_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass.equals("invisable")) { // 안보임 -> 보임.
                    pass = "visable";
                    editVisable_pass.setBackgroundResource(R.drawable.sign_edit_visable);
                    edit_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edit_pass.setSelection(edit_pass.getText().length());
                }
                else { // 보임 -> 안보임.
                    pass = "invisable";
                    editVisable_pass.setBackgroundResource(R.drawable.sign_edit_invisable);
                    edit_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edit_pass.setSelection(edit_pass.getText().length());
                }
            }
        });
        editNull_pass = (Button) v.findViewById(R.id.editNull_pass);
        editNull_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_pass.setText(null);
                edit_pass.requestFocus();
                imm.showSoftInput(edit_pass, 0);

                edit_pass2.setText(null);
                editWrong_pass.setText(null);
                editWrong_pass2.setText(null);
            }
        });
        editWrong_pass = (TextView) v.findViewById(R.id.editWrong_pass);
        edit_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editWrong_pass.setText(null);
                if(editable.toString().equals("")) { // 비밀번호 입력란 X.
                    editVisable_pass.setBackgroundResource(0);
                    editNull_pass.setBackgroundResource(0);
                }
                else { // 비밀번호 입력란 O.
                    if(pass.equals("visable")) {
                        editVisable_pass.setBackgroundResource(R.drawable.sign_edit_visable);
                    }
                    else {
                        editVisable_pass.setBackgroundResource(R.drawable.sign_edit_invisable);
                    }
                    editNull_pass.setBackgroundResource(R.drawable.sign_edit_null);
                }
            }
        });
        edit_pass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if(edit_pass.getText().toString().equals("")) {
                            editWrong_pass.setText("비밀번호를 입력해주세요");
                            return true;
                        }
                        break;
                }

                return false;
            }
        });

        // 비밀번호 확인.
        edit_pass2 = (EditText) v.findViewById(R.id.edit_pass2);
        edit_pass2.startAnimation(fadeIn_anim);
        editVisable_pass2 = (Button) v.findViewById(R.id.editVisable_pass2);
        editVisable_pass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass2.equals("invisable")) { // 안보임 -> 보임.
                    pass2 = "visable";
                    editVisable_pass2.setBackgroundResource(R.drawable.sign_edit_visable);
                    edit_pass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edit_pass2.setSelection(edit_pass2.getText().length());
                }
                else { // 보임 -> 안보임.
                    pass2 = "invisable";
                    editVisable_pass2.setBackgroundResource(R.drawable.sign_edit_invisable);
                    edit_pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edit_pass2.setSelection(edit_pass2.getText().length());
                }
            }
        });
        editNull_pass2 = (Button) v.findViewById(R.id.editNull_pass2);
        editNull_pass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_pass2.setText(null);
                edit_pass2.requestFocus();
                imm.showSoftInput(edit_pass2, 0);

                editWrong_pass2.setText(null);
            }
        });
        editWrong_pass2 = (TextView) v.findViewById(R.id.editWrong_pass2);
        edit_pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")) { // 비밀번호 입력란 X.
                    editVisable_pass2.setBackgroundResource(0);
                    editNull_pass2.setBackgroundResource(0);

                    editWrong_pass2.setText(null);
                }
                else { // 비밀번호 입력란 O.
                    if(pass2.equals("visable")) {
                        editVisable_pass2.setBackgroundResource(R.drawable.sign_edit_visable);
                    }
                    else {
                        editVisable_pass2.setBackgroundResource(R.drawable.sign_edit_invisable);
                    }
                    editNull_pass2.setBackgroundResource(R.drawable.sign_edit_null);

                    // 비밀번호 일치 확인.
                    if(editable.toString().equals(edit_pass.getText().toString())) { // 비밀번호 일치O.
                        editWrong_pass2.setTextColor(Color.parseColor("#003C85"));
                        editWrong_pass2.setText("비밀번호가 일치합니다 :)");
                    }
                    else { // 비밀번호 일치X.
                        editWrong_pass2.setTextColor(Color.parseColor("#850000"));
                        editWrong_pass2.setText("비밀번호가 일치하지 않습니다");
                    }
                }
            }
        });
        edit_pass2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        if(edit_pass2.getText().toString().equals("")) {
                            editWrong_pass2.setTextColor(Color.parseColor("#850000"));
                            editWrong_pass2.setText("비밀번호를 입력해주세요");
                            return true;
                        }
                        break;
                }

                return false;
            }
        });

        // 계속하기 버튼.
        btn = (MaterialButton) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_pass.getText().toString().equals("")) { // 비밀번호 입력란 X.
                    editWrong_pass.setText("비밀번호를 입력해주세요");
                    edit_pass.requestFocus();
                    imm.showSoftInput(edit_pass, 0);
                }
                else { // 비밀번호 입력란 O.
                    if(edit_pass2.getText().toString().equals("")) { // 비밀번호 확인란 X.
                        editWrong_pass2.setTextColor(Color.parseColor("#850000"));
                        editWrong_pass2.setText("비밀번호를 입력해주세요");
                        edit_pass2.requestFocus();
                        imm.showSoftInput(edit_pass2, 0);
                    }
                    else { // 비밀번호 확인란 O.
                        if(edit_pass.getText().toString().equals(edit_pass2.getText().toString())) { // 비밀번호 일치.
                            Bundle bundle = new Bundle(); // id, pass 값 넘기기.
                            bundle.putString("id", id);
                            bundle.putString("pass", edit_pass2.getText().toString());
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            SignActivity_User signUp_user = new SignActivity_User();
                            signUp_user.setArguments(bundle);
                            transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                            transaction.replace(R.id.signFrame, signUp_user);
                            transaction.commit();

                        }
                        else { // 비밀번호 일치 X.
                            editWrong_pass2.setTextColor(Color.parseColor("#850000"));
                            editWrong_pass2.setText("비밀번호가 일치하지 않습니다");
                            edit_pass2.requestFocus();
                            imm.showSoftInput(edit_pass2, 0);
                        }
                    }
                }
            }
        });

        return v;
    }
}