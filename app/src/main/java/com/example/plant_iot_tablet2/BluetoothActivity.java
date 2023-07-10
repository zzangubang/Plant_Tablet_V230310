package com.example.plant_iot_tablet2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/* checkSelfPermission의 if/else 문 다 실행되도록 작성함. */
public class BluetoothActivity extends Activity {
    //ListView.
    ListView list;
    BluetoothActivity_ListAdapter listAdapter;
    ArrayList<BluetoothActivity_ListItem> listItems;

    // 블루투스.
    ImageView button_search; // 새로고침(찾기) 버튼.
    ArrayList<String> deviceList;
    BluetoothAdapter bluetoothAdapter;
    String name = "", address = "";

    // 등록된 모델인지 체크.
    GetCheckModel gModel;
    String getCheckModelURL = "http://hosting.ajplants.com/Plant_checkModel_Android.php";

    String id = "", purpose = "";
    ProgressDialog dialog;
    Toast toast;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bluetooth);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContext = this;

        Intent getIntent = getIntent();
        id = getIntent.getStringExtra("id");
        purpose = getIntent.getStringExtra("purpose");

        // 로딩창.
        dialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("잠시만 기다려주세요.");

        // ListView.
        list = (ListView) findViewById(R.id.listBle);
        listItems = new ArrayList<BluetoothActivity_ListItem>();
        listAdapter = new BluetoothActivity_ListAdapter(this, listItems);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new listItemClickListener());

        // 블루투스.
        deviceList = new ArrayList<String>();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) { // 버전에 따라 BluetoothAdapter 설정.
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        if (bluetoothAdapter == null) { // 기기가 블루투스 지원을 하지 않는 경우.
            toastShow("블루투스 지원을 하지 않는 기기입니다");
            try {
                unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            finish();
        } else { // 기기가 블루투스 지원을 하는 경우.
            BluetoothScan();
        }

        button_search = (ImageView) findViewById(R.id.button_search); // 새로고침(찾기) 버튼.
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    } else {
                        BluetoothScan();
                    }
                }
                else {
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    } else {
                        BluetoothScan();
                    }
                }
            }
        });
    }

    // 블루투스 스캔.
    public void BluetoothScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!bluetoothAdapter.isEnabled()) { // 블루투스 비활성.
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // 블루투스 연결 요청.
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                    startActivity(intent);
                }
            } else {  // 블루투스 활성.
                bluetoothAdapter.startDiscovery();
                listItems.clear();
                deviceList.clear();
                listAdapter.notifyDataSetChanged();
                try {
                    unregisterReceiver(receiver);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
            }
        }
    }

    // ACTION_FOUND를 위한 BroadcastReceiver.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String deviceName = "", deviceHardwareAddress = "";

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    deviceName = device.getName();
                    deviceHardwareAddress = device.getAddress();
                }
                else {
                    deviceName = device.getName();
                    deviceHardwareAddress = device.getAddress();
                }

                // 중복 체크.
                int cnt = 0;
                if (!"".equals(deviceName) && deviceName != null) { // deviceName이 공백이 아닐 때만 적용 (공백이면 패스).
                    for (int i = 0; i < deviceList.size(); i++) {
                        if (deviceName.equals(deviceList.get(i))) { // 중복 값 존재.
                            cnt++;
                        }
                    }
                    if (cnt == 0) { // 중복 값이 없을 때만 List에 추가.
                        listItems.add(new BluetoothActivity_ListItem(deviceName, deviceHardwareAddress)); // 검색된 기기 목록 추가.
                        listAdapter.notifyDataSetChanged();
                        deviceList.add(deviceName);
                    }
                }
            }
        }
    };

    // ListView 아이템 클릭 시 이벤트.
    public class listItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            name = listItems.get(position).getName(); // get name
            address = listItems.get(position).getAddress(); // get address
            boolean flag = true;

            // 블루투스 스캔 종료.
            try {
                unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            // 등록된 모델인지 확인.
            dialog.show();
            gModel = new GetCheckModel();
            gModel.execute(getCheckModelURL, name, address);
        }
    }

    // 등록된 모델인지 확인.
    class GetCheckModel extends AsyncTask<String, Integer, String> {
        String name = "", address = "";

        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            name = (String) params[1];
            address = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "name=" + name;

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
            String idT = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    idT = item.getString("id");
                }
                if (!idT.equals("")) { // 이미 등록된 모델.
                    toastShow("등록된 모델입니다");
                }
                dialog.dismiss();
            } catch (JSONException e) { // 등록되지 않은 모델.
                e.printStackTrace();

                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity_WifiID.class);
                intent.putExtra("purpose", purpose);
                intent.putExtra("id", id);
                intent.putExtra("bleName", name);
                intent.putExtra("bleAddress", address);
                startActivity(intent);
                finish();
            }
        }
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
