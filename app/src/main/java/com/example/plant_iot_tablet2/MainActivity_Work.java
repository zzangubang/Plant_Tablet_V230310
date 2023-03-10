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

public class MainActivity_Work extends AppCompatActivity {
    // 상단 바.
    ImageView button_back, button_menu;
    TextView title;

    // 프래그먼트.
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment Data, Timer;

    String model = "";
    String titleT; // MainActivity에서 "work" 읽기.
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_work);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 상태바 색상 변경.
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }

        mContext = this;

        // "work" 얻기.
        Intent getIntent = getIntent();
        model = getIntent.getStringExtra("model");
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
                PopupMenu fanPop = new PopupMenu(MainActivity_Work.this, button_menu);
                getMenuInflater().inflate(R.menu.menu_main_work, fanPop.getMenu());
                fanPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.data:
                                title.setText("상세 데이터");

                                Bundle bundle = new Bundle();
                                bundle.putString("data", "temp");

                                Data = new MainActivity_Work_Data(); // 프래그먼트 Set.
                                Data.setArguments(bundle);
                                fragmentManager = getSupportFragmentManager();
                                transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.workFrame, Data).commitAllowingStateLoss();
                                break;
                            case R.id.timer:
                                title.setText("자동 모드 값 설정");

                                Timer = new MainActivity_Work_Timer(); // 프래그먼트 Set.
                                fragmentManager = getSupportFragmentManager();
                                transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.workFrame, Timer).commitAllowingStateLoss();
                                break;
                        }
                        return false;
                    }
                });
                fanPop.show();
            }
        });
        title = (TextView) findViewById(R.id.title); // 제목(MainActivity에 따라 바뀜).
        switch (titleT) {
            case "data_temp":
                title.setText("상세 데이터");

                Bundle bundleT = new Bundle();
                bundleT.putString("data", "temp");

                Data = new MainActivity_Work_Data(); // 프래그먼트 Set.
                Data.setArguments(bundleT);
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Data).commitAllowingStateLoss();
                break;

            case "data_humi":
                title.setText("상세 데이터");

                Bundle bundleH = new Bundle();
                bundleH.putString("data", "humi");

                Data = new MainActivity_Work_Data(); // 프래그먼트 Set.
                Data.setArguments(bundleH);
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Data).commitAllowingStateLoss();
                break;

            case "data_illu":
                title.setText("상세 데이터");

                Bundle bundleI = new Bundle();
                bundleI.putString("data", "illu");

                Data = new MainActivity_Work_Data(); // 프래그먼트 Set.
                Data.setArguments(bundleI);
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Data).commitAllowingStateLoss();
                break;

            case "timer":
                title.setText("자동 모드 값 설정");

                Timer = new MainActivity_Work_Timer(); // 프래그먼트 Set.
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.workFrame, Timer).commitAllowingStateLoss();
                break;
        }
    }

}
