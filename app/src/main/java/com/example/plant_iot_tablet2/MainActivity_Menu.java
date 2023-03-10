package com.example.plant_iot_tablet2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.Objects;

public class MainActivity_Menu extends Activity {
    // 메뉴 버튼.
    LinearLayout button_data, button_timer, button_wifi, button_info;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_main_menu);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 메뉴.
        button_data = (LinearLayout) findViewById(R.id.button_data); // 상세 데이터 버튼.
        button_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu", "data");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_timer = (LinearLayout) findViewById(R.id.button_timer); // 자동 모드 값 설정 버튼.
        button_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu", "timer");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_wifi = (LinearLayout) findViewById(R.id.button_wifi); // 와이파이 관리 버튼.
        button_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu", "wifi");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
