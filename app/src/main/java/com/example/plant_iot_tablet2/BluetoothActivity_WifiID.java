package com.example.plant_iot_tablet2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BluetoothActivity_WifiID extends Activity {
    // 뒤로가기 버튼.
    ImageView button_back;
    // 검색 버튼.
    ImageView button_search;

    // ListView.
    ListView list;
    BluetoothActivity_Wifi_ListAdapter listAdapter;
    ArrayList<BluetoothActivity_Wifi_ListItem> listItems;

    // 와이파이.
    WifiManager wifiManager;

    ProgressDialog dialog;
    String purpose = "", id = "", bleName = "", bleAddress = "", name = "";
    Toast toast;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bluetooth_wifi_id);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContext = this;

        // 로딩창.
        dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("주변 와이파이 스캔 중입니다.\n잠시만 기다려주세요.");

        Intent getIntent = getIntent();
        purpose = getIntent.getStringExtra("purpose");
        id = getIntent.getStringExtra("id");
        bleName = getIntent.getStringExtra("bleName");
        bleAddress = getIntent.getStringExtra("bleAddress");

        // 뒤로가기 버튼.
        button_back = (ImageView) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                intent.putExtra("purpose", purpose);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();

                // 와이파이 스캔 종료.
                try {
                    unregisterReceiver(receiver);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });

        // 검색 버튼.
        button_search = (ImageView) findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.clear();
                listAdapter.notifyDataSetChanged();

                dialog.show();
                WifiScan();
            }
        });

        // ListView.
        list = (ListView) findViewById(R.id.listWifi);
        listItems = new ArrayList<BluetoothActivity_Wifi_ListItem>();
        listAdapter = new BluetoothActivity_Wifi_ListAdapter(this, listItems);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new listItemClickListener());

        // 와이파이.
        dialog.show();
        WifiScan();
    }

    // ListView 아이템 클릭 시 이벤트.
    public class listItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
            name = listItems.get(position).getName(); // get name
            boolean flag = true;

            Intent intent = new Intent(getApplicationContext(), BluetoothActivity_WifiPass.class);
            intent.putExtra("purpose", purpose);
            intent.putExtra("id", id);
            intent.putExtra("bleName", bleName);
            intent.putExtra("bleAddress", bleAddress);
            intent.putExtra("wifiName", name);
            startActivity(intent);
            finish();

            // 와이파이 스캔 종료.
            try {
                unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }


        }
    }

    // 와이파이.
    // 와이파이 스캔.
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    };

    public void wifiScan() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }
    }

    // ANR 현상 발생하지 않도록 Thread 이용.
    void WifiScan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifiScan();
            }
        }).start();
    }

    // 스캔 성공 시.
    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        for (ScanResult r : results) {
            String wifiName = r.SSID;
            if (wifiName.equals("")) {

            } else {
                dialog.dismiss();
                listItems.add(new BluetoothActivity_Wifi_ListItem(wifiName));
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    // 스캔 실패 시.
    private void scanFailure() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                toastShow("와이파이 스캔에 실패하였습니다.\n잠시 후 다시 시도해주세요.");
                dialog.dismiss();
            }
        }, 0);
    }

    // 토스트 메세지 띄우기.
    public void toastShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
