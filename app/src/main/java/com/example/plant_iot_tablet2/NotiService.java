package com.example.plant_iot_tablet2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NotiService extends Service {
    NotiThread notiThread;
    NotiHandler notiHandler;

    String id = "";
    ArrayList<String> modelList, checkNoti;

    GetList gList;
    String getListURL = "http://hosting.ajplants.com/Plant_modelListG_Android.php";
    GetValue gValue;
    String getValueURL = "http://hosting.ajplants.com/Plant_valueU_Android.php";

    String CHANNEL_ID = "AJPLANTs";
    String CHANNEL_NOTI_ID = "AJPLANTs - level";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        notiHandler = new NotiHandler();
        notiThread = new NotiThread(notiHandler);
    }

    public void stop() {
        notiThread.interrupt();
    }

    @Override
    public void onDestroy() {
        Log.d("NotiService", "onDestroy");
        notiThread.interrupt();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = getSharedPreferences("PlantLogin", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        id = sharedPreferences.getString("id", ""); // id 받기.

        // 포그라운드 서비스.
        createNotificationChannel(CHANNEL_ID, "ForegroundService", NotificationManager.IMPORTANCE_LOW);
        createNotificationChannel(CHANNEL_NOTI_ID, "levelNoti", NotificationManager.IMPORTANCE_HIGH);
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("AJPLANTs")
                .setContentText("포그라운드 서비스 실행 중..")
                .setSmallIcon(R.mipmap.app_icon_round)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2300, notification);

        // 수위 알림 기능 시작.
        modelList = new ArrayList<String>();
        checkNoti = new ArrayList<String>();
        gList = new GetList();
        gList.execute(getListURL);

        notiThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    // Handler. (실행)
    public class NotiHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            for(int i=0; i<modelList.size(); i++) {
                gValue = new GetValue();
                gValue.execute(getValueURL, modelList.get(i), String.valueOf(i));
            }
        }
    }

    // Thread. (몇 초 주기로 할건지)
    public class NotiThread extends Thread {
        Handler handler;
        boolean isRun = true;

        public NotiThread(Handler handler) {
            this.handler = handler;
        }

        public void run() {
            while (isRun) {
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(60000); // 1분 마다 반복 수행.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    // 리스트 얻어오기.
    class GetList extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "id=" + id;

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
            String model = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    model = item.getString("model");

                    modelList.add(model);
                    checkNoti.add("X");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 현재 상태 읽어오기.
    class GetValue extends AsyncTask<String, Integer, String> {
        int position;
        String model = "";

        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            position = Integer.valueOf((String) params[2]);
            model = (String) params[1];

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
            String levelD = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    levelD = item.getString("level");
                }

                /* 수위 부족 - checkNoti.get(i) => X면 O로 바꾼뒤 noti, O면 그냥 냅두기.
                *  수위 적절 - checkNoti.get(i) => X면 냅두고, O면 X로 바꾸기. */
                if(levelD.equals("EMPTY")) { // 수위 부족.
                    if(checkNoti.get(position).equals("X")) {
                        checkNoti.set(position, "O");
                        createNotification(CHANNEL_NOTI_ID, model, position+1106);
                    }
                }
                else { // 수위 적절.
                    if(checkNoti.get(position).equals("O")) {
                        checkNoti.set(position, "X");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void createNotificationChannel(String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, String model, int position) {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentText(model + " 수위가 부족합니다 :(")
                .setDefaults(Notification.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(position, builder.build());
    }
}