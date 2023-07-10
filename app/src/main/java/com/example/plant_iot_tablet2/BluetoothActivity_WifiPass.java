package com.example.plant_iot_tablet2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BluetoothActivity_WifiPass extends Activity {
    // 레이아웃.
    ImageView button_back; // 뒤로가기 버튼.
    TextView wifiNameT; // 와이파이 이름.
    EditText edit; // EditText.
    ImageView editVisible, editNull; // EditText 빈 칸 및 보이기/안보이기.
    String visible = "invisible";
    TextView button_cancel, button_apply; // 취소 및 적용 버튼.

    // 블루투스.
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket = null;
    UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothActivity_ConnectedThread connectedThread;
    boolean state = false;
    int connected_checkNum = 0;

    SendModel sModel;
    String sendModelURL = "http://hosting.ajplants.com/Plant_modelAdd_Android.php";
    SendBle sBle;
    String sendBleURL = "http://hosting.ajplants.com/Plant_bleS_Android.php";
    SendAuto sAuto;
    String sendAutoURL = "http://hosting.ajplants.com/Plant_autoS_Android.php";

    // 알림.
    SendNoti sNoti;
    String sendNotiURL = "http://hosting.ajplants.com/Plant_notiS_Android.php";

    InputMethodManager imm; // 키보드.
    ProgressDialog connectDialog, changeDialog;

    String purpose = "", id = "", bleName = "", bleAddress = "", wifiName = "";
    String wifiPass = "";
    Toast toast;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bluetooth_wifi_pass);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mContext = this;

        // 키보드.
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Intent getIntent = getIntent();
        purpose = getIntent.getStringExtra("purpose");
        id = getIntent.getStringExtra("id");
        bleName = getIntent.getStringExtra("bleName");
        bleAddress = getIntent.getStringExtra("bleAddress");
        wifiName = getIntent.getStringExtra("wifiName");

        // 레이아웃.
        button_back = (ImageView) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기.
                } catch (NullPointerException e) {
                e.printStackTrace();
            }
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity_WifiID.class); // 와이파이 선택 창으로 돌아가기.
                intent.putExtra("purpose", purpose);
                intent.putExtra("id", id);
                intent.putExtra("bleName", bleName);
                intent.putExtra("bleAddress", bleAddress);
                startActivity(intent);
                finish();
            }
        });
        wifiNameT = (TextView) findViewById(R.id.wifiName);
        wifiNameT.setText(wifiName);
        editVisible = (ImageView) findViewById(R.id.editVisible);
        editVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible.equals("invisible")) { // 안보임 -> 보임.
                    visible = "visible";
                    editVisible.setImageResource(R.drawable.edit_visible);
                    edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edit.setSelection(edit.getText().length());
                } else { // 보임 -> 안보임.
                    visible = "invisible";
                    editVisible.setImageResource(R.drawable.edit_invisible);
                    edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edit.setSelection(edit.getText().length());
                }
            }
        });
        editNull = (ImageView) findViewById(R.id.editNull);
        editNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setText(null);
                edit.requestFocus();
                imm.showSoftInput(edit, 0);
            }
        });
        edit = (EditText) findViewById(R.id.edit);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) { // 비밀번호 입력란 X.
                    editVisible.setImageResource(0);
                    editNull.setImageResource(0);
                } else { // 비밀번호 입력란 O.
                    if (editVisible.equals("visible")) {
                        editVisible.setImageResource(R.drawable.edit_visible);
                    } else {
                        editVisible.setImageResource(R.drawable.edit_invisible);
                    }
                    editNull.setImageResource(R.drawable.edit_null);
                }
            }
        });

        button_cancel = (TextView) findViewById(R.id.button_cancel); // 취소 버튼.
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_apply = (TextView) findViewById(R.id.button_apply); // 적용 버튼.
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity_WifiPass.this, R.style.DialogTheme);
                    builder.setMessage("와이파이 비밀번호가 없습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            wifiPass = "";
                            try {
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기.
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            connectDialog.show();
                            CreateSocket();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            toastShow("비밀번호를 입력해주세요.");
                            edit.requestFocus();
                            imm.showSoftInput(edit, 0);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    wifiPass = edit.getText().toString();
                    try {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // 키보드 내리기.
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    connectDialog.show();
                    CreateSocket();
                }
            }
        });

        // 로딩창.
        connectDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        connectDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        connectDialog.setMessage("기기에 연결 시도 중입니다.\n잠시만 기다려주세요.");
        connectDialog.setCancelable(false);

        changeDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        changeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        changeDialog.setMessage("입력하신 와이파이에 연결 시도 중입니다.\n잠시만 기다려주세요.");
        changeDialog.setCancelable(false);

    }

    // ANR 현상 발생하지 않도록 Thread 이용.
    void CreateSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = true;
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bleAddress);
                try {
                    bluetoothSocket = createBluetoothSocket(device); // 소켓 생성.
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    bluetoothSocket.connect();
                } catch (IOException e) { // 연결 실패.
                    flag = false;

                    connectDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class); // 다시 블루투스 선택 창으로 돌아가기.
                    intent.putExtra("id", id);
                    intent.putExtra("purpose", purpose);
                    startActivity(intent);

                    BluetoothActivity_WifiPass.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(BluetoothActivity_WifiPass.this, "기기와의 연결에 실패했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                    finish();
                }
                if (flag) {
                    connectedThread = new BluetoothActivity_ConnectedThread(bluetoothSocket);
                    connectedThread.start();
                }
                else {
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {

                        @Override

                        public void run() {

                            toastShow("flag - false");

                        }

                    });
                }
            }
        }).start();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            try {
                BluetoothSocket createSocket = (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
                return createSocket;
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
            }
        } catch (Exception e) {
            Log.e("BluetoothActivity", "Could not create Insecure RFComm Connection", e);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
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


    // ConnectedThread.
    public class BluetoothActivity_ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;
        OutputStream tmpOut = null;

        boolean flag = true;
        Timer timer;

        public BluetoothActivity_ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            state = true;

            connected_checkNum = 0;
            timeTask(); // WIFI:SET 송신.
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            while (flag) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);

                        final String text = new String(buffer, "US-ASCII");
                        postToastMessage(text);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Log.e("WIFI:OK", "ArrayIndex 오류");
                }
            }
        }


        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
                flag = false;
            } catch (IOException e) {
            }
        }

        // 프로토콜 수신.
        public void postToastMessage(final String message) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    String[] value = message.split(":");

                    if (value[0].equals("WIFI")) {
                        if (value[1].contains("OK")) { // OK: 받을 준비 ( WIFI:OK )
                            timer.cancel();

                            connectDialog.dismiss();
                            changeDialog.show();
                            connectedThread.write("WIFI:INFO:" + bleName + ":" + wifiName + ":" + wifiPass + "\n"); // WIFI:INFO:모델명:와이파이ID:와이파이PASS

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(changeDialog.isShowing()) {
                                        changeDialog.dismiss();

                                        toastShow("기기와의 연결에 실패했습니다. 잠시 후 다시 시도해주세요.");
                                        Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                                        intent.putExtra("id", id);
                                        intent.putExtra("purpose", purpose);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            },15000);	 // 15초 뒤에 실행.
                        }
                        if (value[1].trim().equals("CHANGE")) { // CHANGE: 설정 완료 ( WIFI:CHANGE )
                            changeDialog.dismiss();

                            if(purpose.equals("add")) { // 모델 추가. ("purpose" == "add")
                                sModel = new SendModel();
                                sModel.execute(sendModelURL, bleName, bleAddress, id);

                                String command = "0.60.0.100.0:00.0:00.0:00.0:00.0:00.0:00";
                                sAuto = new SendAuto();
                                sAuto.execute(sendAutoURL, bleName, command);

                                if (id.equals("ahjoo")) { // 관리자.
                                    ((MasterActivity) MasterActivity.mContext).Refresh();
                                    sNoti = new SendNoti();
                                    sNoti.execute(sendNotiURL);
                                    stopService(new Intent(getApplicationContext(), NotiService.class));
                                } else { // 관리자 X.
                                    ((HomeActivity) HomeActivity.mContext).Refresh();

                                }
                                finish();
                            }
                            else { // 와이파이 변경. ("purpose" == "wifi")
                                sBle = new SendBle();
                                sBle.execute(sendBleURL, bleName, bleAddress, id);

                                toastShow("변경되었습니다");
                                finish();
                            }
                        }
                    }
                }
            });
        }

        // WIFI:OK를 받을 때까지 2초마다 송신.
        public void timeTask() {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (connected_checkNum >= 3) {
                        timer.cancel();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "기기와의 연결에 실패했습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }, 0);

                        connectDialog.dismiss();

                        if(purpose.equals("add")) {
                            Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("purpose", purpose);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            finish();
                        }
                    }
                    else {
                        connectedThread.write("WIFI:SET\r\n");
                        connected_checkNum++;
                    }
                }
            };
            timer = new Timer();
            timer.schedule(task, 2000, 2000); // 2초마다 실행.
        }
    }

    // DB에 모델 추가.
    class SendModel extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String bleN = (String) params[1];
            String bleP = (String) params[2];
            String id = (String) params[3];

            String serverURL = (String) params[0];
            String postParameters = "bleN=" + bleN + "&bleP=" + bleP + "&id=" + id;

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

    // 블루투스 정보 변경.
    class SendBle extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String bleN = (String) params[1];
            String bleP = (String) params[2];
            String id = (String) params[3];

            String serverURL = (String) params[0];
            String postParameters = "bleN=" + bleN + "&bleP=" + bleP + "&id=" + id + "&model=" + bleN;

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

    // 자동모드 값 초기화.
    class SendAuto extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            String model = (String) params[1];
            String command = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "model=" + model + "&command=" + command;

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

    // 알림 요청.
    class SendNoti extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&noti=" + "X";
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

    // Activity가 종료되었을 때 시점.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (state) { // ConnectedThread가 실행 중이라면 (WIFI:SET 송신 중)
            connectedThread.write("WIFI:FIN\r\n"); // WIFI:FIN

            connectedThread.cancel();
        }
    }
}
