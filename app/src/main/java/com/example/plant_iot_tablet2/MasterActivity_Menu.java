package com.example.plant_iot_tablet2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

public class MasterActivity_Menu extends Activity {
    // 메뉴 버튼.
    LinearLayout button_approve, button_user, button_device, button_logout;

    // 로그아웃 (로그인 정보 삭제).
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_master_menu);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 메뉴 버튼.
        button_approve = (LinearLayout) findViewById(R.id.button_approve); // 회원가입 승인.
        button_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu", "approve");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_user = (LinearLayout) findViewById(R.id.button_user); // 회원 정보.
        button_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu", "user");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_device = (LinearLayout) findViewById(R.id.button_device); // 기기 정보.
        button_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu", "device");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_logout = (LinearLayout) findViewById(R.id.button_logout); // 로그아웃.
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MasterActivity_Menu.this, R.style.DialogTheme);
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences = getSharedPreferences("PlantLogin", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("id", "");
                        editor.putString("pass", "");
                        editor.commit();
                        //stopService(new Intent(getApplicationContext(), MyService.class));

                        Intent intent = new Intent();
                        intent.putExtra("menu", "logout");
                        setResult(RESULT_OK, intent);
                        finish();
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
    }
}
