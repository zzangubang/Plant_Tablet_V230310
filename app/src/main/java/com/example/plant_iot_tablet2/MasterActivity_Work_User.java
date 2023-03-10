package com.example.plant_iot_tablet2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MasterActivity_Work_User#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterActivity_Work_User extends Fragment {
    ExpandableListView listView;
    MasterActivity_Work_User_ListAdapter adapter;
    ArrayList<MasterActivity_Work_User_Parent_ListItem> parentList = new ArrayList<>();
    ArrayList<ArrayList<MasterActivity_Work_User_Child_ListItem>> childList = new ArrayList<>();

    GetUser gUser;
    String getUserURL = "http://aj3dlab.dothome.co.kr/Test_user_userG_Android.php";
    GetDevice gDevice;
    String getDeviceURL = "http://aj3dlab.dothome.co.kr/Test_user_deviceG_Android.php";

    ProgressDialog dialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MasterActivity_Work_User() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterActivity_Device.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterActivity_Work_User newInstance(String param1, String param2) {
        MasterActivity_Work_User fragment = new MasterActivity_Work_User();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_master_work_user, container, false);

        // 로딩창.
        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("정보 조회중입니다.\n잠시만 기다려주세요.");

        listView = (ExpandableListView) v.findViewById(R.id.listDevice);
        adapter = new MasterActivity_Work_User_ListAdapter();
        adapter.parentItems = parentList;
        adapter.childItems = childList;
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);

        dialog.show();
        gUser = new GetUser();
        gUser.execute(getUserURL);

        return v;
    }

    // 회원 정보 얻어오기.
    class GetUser extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();

            String serverURL = (String) params[0];
            String postParameters = "";

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
            String id = "", name = "", phone = "", date = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    id = item.getString("id");
                    name = item.getString("name");
                    phone = item.getString("phone");
                    date = item.getString("date");

                    parentList.add(new MasterActivity_Work_User_Parent_ListItem(name, phone, date));
                    childList.add(new ArrayList<MasterActivity_Work_User_Child_ListItem>());
                    gDevice = new GetDevice();
                    gDevice.execute(getDeviceURL, String.valueOf(i), id);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        }
    }

    // 기기 정보 얻어오기.
    class GetDevice extends AsyncTask<String, Integer, String> {
        String position = "";
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            position = (String) params[1];
            String id = (String) params[2];

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
            String model = "", date = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    model = item.getString("model");
                    date = item.getString("date");

                    MasterActivity_Work_User_Child_ListItem childItem = new MasterActivity_Work_User_Child_ListItem(model, date);
                    childList.get(Integer.valueOf(position)).add(childItem);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        }
    }
}