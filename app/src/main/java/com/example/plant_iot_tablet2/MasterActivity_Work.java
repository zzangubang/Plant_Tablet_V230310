package com.example.plant_iot_tablet2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MasterActivity_Work extends AppCompatActivity {
    // 상단 바.
    ImageView button_back, button_menu;
    TextView title;

    // 프래그먼트.
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment Approve, User, Device;

    String titleT; // MasterActivity_ListManage에서 "work" 읽기.
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_work);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 상태바 색상 변경.
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }

        mContext = this;

        // "work" 얻기.
        Intent getIntent = getIntent();
        titleT = getIntent.getStringExtra("work");

        // 상단 바.
        button_back = (ImageView) findViewById(R.id.button_back); // Back 버튼.
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_menu = (ImageView) findViewById(R.id.button_menu); // 메뉴 버튼.
        button_menu.setOnClickListener(new View.OnClickListener() { // 팝업 메뉴 띄우기.
            @Override
            public void onClick(View view) {
                PopupMenu fanPop = new PopupMenu(MasterActivity_Work.this, button_menu);
                getMenuInflater().inflate(R.menu.menu_master_work, fanPop.getMenu());
                fanPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.approve:
                                title.setText("회원가입 승인");

                                Approve = new MasterActivity_Work_Approve(); // 프래그먼트 Set.
                                fragmentManager = getSupportFragmentManager();
                                transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.workFrame, Approve).commitAllowingStateLoss();
                                break;
                            case R.id.user:
                                title.setText("회원 정보");

                                User = new MasterActivity_Work_User(); // 프래그먼트 Set.
                                fragmentManager = getSupportFragmentManager();
                                transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.workFrame, User).commitAllowingStateLoss();
                                break;
                            case R.id.device:
                                title.setText("기기 정보");

                                Device = new MasterActivity_Work_Device(); // 프래그먼트 Set.
                                fragmentManager = getSupportFragmentManager();
                                transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.workFrame, Device).commitAllowingStateLoss();
                                break;
                        }
                        return false;
                    }
                });
                fanPop.show();
            }
        });
        title = (TextView) findViewById(R.id.title); // 제목(MasterActivity_ListManage에 따라 바뀜).
        switch (titleT) {
            case "approve":
                title.setText("회원가입 승인");

                Approve = new MasterActivity_Work_Approve(); // 프래그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Approve).commitAllowingStateLoss();
                break;
            case "user":
                title.setText("회원 정보");

                User = new MasterActivity_Work_User(); // 프래그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, User).commitAllowingStateLoss();
                break;
            case "device":
                title.setText("기기 정보");

                Device = new MasterActivity_Work_Device(); // 프래그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Device).commitAllowingStateLoss();
                break;
        }
    }
}