package com.example.plant_iot_tablet2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity_Work extends AppCompatActivity {
    ImageView button_back;
    TextView title;

    // 프래그먼트.
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment Pass, Alarm, Help;

    String work = "";
    String id = "", name = "", phone = "", pass = "";
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 상태바 색상 변경.
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }

        mContext = this;

        Intent getIntent = getIntent();
        work = getIntent.getStringExtra("work");
        id = getIntent.getStringExtra("id");
        name = getIntent.getStringExtra("name");
        phone = getIntent.getStringExtra("phone");
        pass = getIntent.getStringExtra("pass");

        button_back = (ImageView) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title = (TextView) findViewById(R.id.title);

        switch (work) {
            case "pass":
                title.setText("비밀번호 변경");
                Pass = new HomeActivity_Work_Pass(); // 프레그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Pass).commitAllowingStateLoss();
                break;
            case "alarm":
                title.setText("알림");
                Alarm = new HomeActivity_Work_Alarm(); // 프레그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Alarm).commitAllowingStateLoss();
                break;
            case "help":
                title.setText("고객 센터");
                Help = new HomeActivity_Work_Help(); // 프레그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Help).commitAllowingStateLoss();
                break;
        }
    }

    public void Finish() {
        ((HomeActivity_UserMenu)HomeActivity_UserMenu.mContext).Refresh();
        finish();
    }
}