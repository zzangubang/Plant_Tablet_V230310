package com.example.plant_iot_tablet2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // 상단 바.
    ImageView button_back, button_menu;
    TextView nameT;

    // 환경 값.
    TextView lastUpdate;
    RelativeLayout tempLayout, humiLayout, illuLayout;
    TextView tempValue, humiValue, illuValue, levelValue;
    TextView errTempT, errHumiT;

    // 제어.
    String fan = "", fanE = "", ledL = "", ledR = "", water = "", pump = "", mode = "";
    ToggleButton button_mode;
    RelativeLayout button_fan, button_fanE, button_ledL, button_ledR, button_water, button_pump;
    ImageView fanI, fanEI, ledLI, ledRI, waterI, pumpI;
    TextView fanStepT;

    // 그래프.
    PagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    GetValue gValue;
    String getValueURL = "http://aj3dlab.dothome.co.kr/Plant_value_Android.php";
    SendActive sActive;
    String sendCommandURL = "http://aj3dlab.dothome.co.kr/Plant_command_Android.php";
    GetBle gBle;
    String getBleURL = "http://aj3dlab.dothome.co.kr/Test_bleG_Android.php";

    Toast toast;
    Timer timer;
    String id = "", name = "", model = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        id = getIntent.getStringExtra("id");
        name = getIntent.getStringExtra("name");
        model = getIntent.getStringExtra("model");

        // 상단 바.
        button_back = (ImageView) findViewById(R.id.button_back); // 뒤로가기 버튼.
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_menu = (ImageView) findViewById(R.id.button_menu); // 메뉴 버튼.
        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_Menu.class);
                startActivityForResult(intent, 1);
            }
        });
        nameT = (TextView) findViewById(R.id.name); // 모델 이름 (별칭).
        nameT.setText(name);

        // 환경 값.
        lastUpdate = (TextView) findViewById(R.id.lastUpdate); // 마지막 업데이트 날짜.

        tempLayout = (RelativeLayout) findViewById(R.id.tempLayout); // 온도. (99.99 == 에러)
        tempValue = (TextView) findViewById(R.id.tempValue);
        errTempT = (TextView) findViewById(R.id.errTemp);
        tempLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_Work.class);
                intent.putExtra("model", model);
                intent.putExtra("work", "data_temp");
                startActivity(intent);
            }
        });

        humiLayout = (RelativeLayout) findViewById(R.id.humiLayout); // 습도. (0.00 == 에러)
        humiValue = (TextView) findViewById(R.id.humiValue);
        errHumiT = (TextView) findViewById(R.id.errHumi);
        humiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_Work.class);
                intent.putExtra("model", model);
                intent.putExtra("work", "data_humi");
                startActivity(intent);
            }
        });

        illuLayout = (RelativeLayout) findViewById(R.id.illuLayout); // 조도.
        illuValue = (TextView) findViewById(R.id.illuValue);
        illuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_Work.class);
                intent.putExtra("model", model);
                intent.putExtra("work", "data_illu");
                startActivity(intent);
            }
        });

        levelValue = (TextView) findViewById(R.id.levelValue); // 수위.

        // 제어.
        button_mode = (ToggleButton) findViewById(R.id.button_mode); // 자동/수동.
        button_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mode) {
                    case "auto":
                        mode = "manual";
                        button_mode.setChecked(false);

                        button_fan.setEnabled(true);
                        button_fanE.setEnabled(true);
                        button_ledL.setEnabled(true);
                        button_ledR.setEnabled(true);
                        button_water.setEnabled(true);
                        button_pump.setEnabled(true);
                        break;
                    case "manual":
                        mode = "auto";
                        button_mode.setChecked(true);

                        button_fan.setEnabled(false);
                        button_fanE.setEnabled(false);
                        button_ledL.setEnabled(false);
                        button_ledR.setEnabled(false);
                        button_water.setEnabled(false);
                        button_pump.setEnabled(false);
                        break;
                    /*default:
                        if (button_mode.isChecked()) {
                            mode = "auto";
                            button_mode.setChecked(true);

                            button_fan.setEnabled(false);
                            button_fanE.setEnabled(false);
                            button_ledL.setEnabled(false);
                            button_ledR.setEnabled(false);
                            button_water.setEnabled(false);
                        } else {
                            mode = "manual";
                            button_mode.setChecked(false);
                            button_mode.setChecked(false);

                            button_fan.setEnabled(true);
                            button_fanE.setEnabled(true);
                            button_ledL.setEnabled(true);
                            button_ledR.setEnabled(true);
                            button_water.setEnabled(true);
                            button_pump.setEnabled(true);
                        }
                        break;*/
                }

                SendCommand();
            }
        });

        button_fan = (RelativeLayout) findViewById(R.id.button_fan); // 팬(내부).
        fanI = (ImageView) findViewById(R.id.fanI);
        fanStepT = (TextView) findViewById(R.id.fanStepT);
        button_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fan.equals("off")) {
                    PopupMenu fanPop = new PopupMenu(MainActivity.this, button_fan);
                    getMenuInflater().inflate(R.menu.menu_fan, fanPop.getMenu());
                    fanPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            fanI.setImageResource(R.drawable.main_fan_on_icon);
                            switch (menuItem.getItemId()) {
                                case R.id.fan1:
                                    fan = "on1";
                                    fanStepT.setText("①");
                                    break;
                                case R.id.fan2:
                                    fan = "on2";
                                    fanStepT.setText("②");
                                    break;
                                case R.id.fan3:
                                    fan = "on3";
                                    fanStepT.setText("③");
                                    break;
                            }
                            SendCommand();
                            return false;
                        }
                    });
                    fanPop.show();
                } else {
                    fan = "off";
                    fanI.setImageResource(R.drawable.main_fan_off_icon);
                    fanStepT.setText("");
                }
                SendCommand();
            }
        });
        button_fan.setOnLongClickListener(new View.OnLongClickListener() { // 팬(내부) 세기 조절.
            @Override
            public boolean onLongClick(View view) {
                if (fan.equals("on1") || fan.equals("on2") || fan.equals("on3")) {
                    PopupMenu fanPop = new PopupMenu(MainActivity.this, button_fan);
                    getMenuInflater().inflate(R.menu.menu_fan, fanPop.getMenu());
                    fanPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.fan1:
                                    fan = "on1";
                                    fanStepT.setText("①");
                                    break;
                                case R.id.fan2:
                                    fan = "on2";
                                    fanStepT.setText("②");
                                    break;
                                case R.id.fan3:
                                    fan = "on3";
                                    fanStepT.setText("③");
                                    break;
                            }
                            SendCommand();
                            return false;
                        }
                    });
                    fanPop.show();
                }

                return true;
            }
        });

        button_fanE = (RelativeLayout) findViewById(R.id.button_fanE); // 팬(외부).
        fanEI = (ImageView) findViewById(R.id.fanEI);
        button_fanE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fanE.equals("off")) {
                    fanE = "on";
                    fanEI.setImageResource(R.drawable.main_fan_on_icon);
                } else {
                    fanE = "off";
                    fanEI.setImageResource(R.drawable.main_fan_off_icon);
                }
                SendCommand();
            }
        });

        button_ledL = (RelativeLayout) findViewById(R.id.button_ledL); // 전등(좌).
        ledLI = (ImageView) findViewById(R.id.ledLI);
        button_ledL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ledL.equals("off")) {
                    ledL = "on";
                    ledLI.setImageResource(R.drawable.main_led_on_icon);
                } else {
                    ledL = "off";
                    ledLI.setImageResource(R.drawable.main_led_off_icon);
                }
                SendCommand();
            }
        });

        button_ledR = (RelativeLayout) findViewById(R.id.button_ledR); // 전등(우).
        ledRI = (ImageView) findViewById(R.id.ledRI);
        button_ledR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ledR.equals("off")) {
                    ledR = "on";
                    ledRI.setImageResource(R.drawable.main_led_on_icon);
                } else {
                    ledR = "off";
                    ledRI.setImageResource(R.drawable.main_led_off_icon);
                }
                SendCommand();
            }
        });

        button_water = (RelativeLayout) findViewById(R.id.button_water); // 연무기.
        waterI = (ImageView) findViewById(R.id.waterI);
        button_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (water.equals("off")) {
                    water = "on";
                    waterI.setImageResource(R.drawable.main_water_on_icon);
                } else {
                    water = "off";
                    waterI.setImageResource(R.drawable.main_water_off_icon);
                }
                SendCommand();
            }
        });

        button_pump = (RelativeLayout) findViewById(R.id.button_pump); // 펌프.
        pumpI = (ImageView) findViewById(R.id.pumpI);
        button_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pump.equals("off")) {
                    pump = "on";
                    pumpI.setImageResource(R.drawable.main_pump_on_icon);
                } else {
                    pump = "off";
                    pumpI.setImageResource(R.drawable.main_pump_off_icon);
                }
                SendCommand();
            }
        });

        // 그래프.
        viewPager = (ViewPager) findViewById(R.id.main_pager);
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        adapter = new MainActivity_PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        gValue = new GetValue();
        gValue.execute(getValueURL);
        timeTask();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) { // 메뉴 선택.
            if(resultCode == RESULT_OK) {
                String menu = data.getStringExtra("menu");
                switch (menu) {
                    case "data":
                        Intent intentD = new Intent(getApplicationContext(), MainActivity_Work.class);
                        intentD.putExtra("model", model);
                        intentD.putExtra("work", "data_temp");
                        startActivity(intentD);
                        break;
                    case "timer":
                        Intent intentT = new Intent(getApplicationContext(), MainActivity_Work.class);
                        intentT.putExtra("model", model);
                        intentT.putExtra("work", "timer");
                        startActivity(intentT);
                        break;
                    case "wifi":
                        gBle = new GetBle();
                        gBle.execute(getBleURL);
                        break;
                }
            }
        }
    }

    // 25 -> 10초마다 값 갱신하도록.
    public void timeTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gValue = new GetValue();
                gValue.execute(getValueURL);
            }
        };
        timer = new Timer();
        timer.schedule(task, 10000, 10000); //10초마다 실행
    }

    // 현재 상태 읽어오기.
    class GetValue extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "model=" + model;

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
            String date = "", time = "";
            String tempD = "", humiD = "", illuD = "", levelD = "";
            String fanD = "", fanED = "", ledLD = "", ledRD = "", waterD = "", pumpD = "", modeD = "";
            String errTemp = "", errHumi = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    date = item.getString("date");
                    time = item.getString("time");

                    tempD = item.getString("temp");
                    humiD = item.getString("humi");
                    illuD = item.getString("illu");
                    levelD = item.getString("level");

                    fanD = item.getString("fan");
                    fanED = item.getString("fanE");
                    ledLD = item.getString("ledL");
                    ledRD = item.getString("ledR");
                    waterD = item.getString("water");
                    pumpD = item.getString("pump");
                    modeD = item.getString("mode").trim();

                    errTemp = item.getString("errTemp");
                    errHumi = item.getString("errHumi");
                }

                lastUpdate.setText(date + " " + time);
                errTempT.setText(errTemp);
                errHumiT.setText(errHumi);

                // 환경 값.
                tempValue.setText(String.format("%.2f", Float.valueOf(tempD)));
                humiValue.setText(String.format("%.2f", Float.valueOf(humiD)));
                illuValue.setText(illuD);
                if (levelD.equals("FULL")) {
                    levelValue.setText("적절");
                } else if (levelD.equals("EMPTY")) {
                    levelValue.setText("부족");
                } else {
                    levelValue.setText("에러");
                }

                // 내부팬.
                if (fanD.equals("FANOFF")) {
                    fan = "off";
                    fanI.setImageResource(R.drawable.main_fan_off_icon);
                    fanStepT.setText("");
                } else {
                    fanI.setImageResource(R.drawable.main_fan_on_icon);
                    switch (fanD) {
                        case "FANON1":
                            fan = "on1";
                            fanStepT.setText("①");
                            break;
                        case "FANON2":
                            fan = "on2";
                            fanStepT.setText("②");
                            break;
                        case "FANON3":
                            fan = "on3";
                            fanStepT.setText("③");
                            break;
                        default:
                            break;
                    }
                }

                // 외부팬.
                if (fanED.equals("EXTFOFF")) {
                    fanE = "off";
                    fanEI.setImageResource(R.drawable.main_fan_off_icon);
                } else {
                    fanE = "on";
                    fanEI.setImageResource(R.drawable.main_fan_on_icon);
                }
                // 전등(좌).
                if (ledLD.equals("LLAMPOFF")) {
                    ledL = "off";
                    ledLI.setImageResource(R.drawable.main_led_off_icon);
                } else {
                    ledL = "on";
                    ledLI.setImageResource(R.drawable.main_led_on_icon);
                }
                //전등(우).
                if (ledRD.equals("RLAMPOFF")) {
                    ledR = "off";
                    ledRI.setImageResource(R.drawable.main_led_off_icon);
                } else {
                    ledR = "on";
                    ledRI.setImageResource(R.drawable.main_led_on_icon);
                }
                // 연무기.
                if (waterD.equals("WATEROFF")) {
                    water = "off";
                    waterI.setImageResource(R.drawable.main_water_off_icon);
                } else {
                    water = "on";
                    waterI.setImageResource(R.drawable.main_water_on_icon);
                }
                // 펌프.
                if (pumpD.equals("PUMPOFF")) {
                    pump = "off";
                    pumpI.setImageResource(R.drawable.main_pump_off_icon);
                } else {
                    pump = "on";
                    pumpI.setImageResource(R.drawable.main_pump_on_icon);
                }

                // 자동/수동.
                if (modeD.equals("AUTO")) {
                    mode = "auto";
                    button_mode.setChecked(true);

                    button_fan.setEnabled(false);
                    button_fanE.setEnabled(false);
                    button_ledL.setEnabled(false);
                    button_ledR.setEnabled(false);
                    button_water.setEnabled(false);
                    button_pump.setEnabled(false);
                } else if (modeD.equals("MANUAL")) {
                    mode = "manual";
                    button_mode.setChecked(false);

                    button_fan.setEnabled(true);
                    button_fanE.setEnabled(true);
                    button_ledL.setEnabled(true);
                    button_ledR.setEnabled(true);
                    button_water.setEnabled(true);
                    button_pump.setEnabled(true);
                } else {
                    button_fan.setEnabled(false);
                    button_fanE.setEnabled(false);
                    button_ledL.setEnabled(false);
                    button_ledR.setEnabled(false);
                    button_water.setEnabled(false);
                    button_pump.setEnabled(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void SendCommand() {
        String command = fan + ":" + fanE + ":" + ledL + ":" + ledR + ":" + water + ":" + pump + ":" + mode;
        sActive = new SendActive();
        sActive.execute(sendCommandURL, command);
        timer.cancel();
        timeTask();
    }

    // 동작 제어.
    class SendActive extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "model=" + model + "&command=" + (String) params[1];

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

    // 블루투스 정보 얻어오기.
    class GetBle extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&model=" + model;

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
            String bleName = "", bleAddress = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    bleName = item.getString("bleName");
                    bleAddress = item.getString("bleAddress");
                }

                Intent intent = new Intent(getApplicationContext(), BluetoothActivity_WifiID.class);
                intent.putExtra("id", id);
                intent.putExtra("purpose", "wifi");
                intent.putExtra("bleName", bleName);
                intent.putExtra("bleAddress", bleAddress);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void toastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    // 뒤로가기.
    @Override
    public void onBackPressed() {
        finish();
    }
}