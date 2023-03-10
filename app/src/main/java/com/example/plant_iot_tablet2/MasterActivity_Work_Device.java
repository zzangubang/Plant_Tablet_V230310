package com.example.plant_iot_tablet2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
 * Use the {@link MasterActivity_Work_Device#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterActivity_Work_Device extends Fragment {
    //ListView.
    ListView list;
    MasterActivity_Work_Device_ListAdapter listAdapter;
    ArrayList<MasterActivity_Work_Device_ListItem> listItems;

    GetList gList;
    String getListURL = "http://aj3dlab.dothome.co.kr/Test_deviceG_Android.php";

    ProgressDialog dialog;
    public static Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MasterActivity_Work_Device() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterActivity_Work_Devicec.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterActivity_Work_Device newInstance(String param1, String param2) {
        MasterActivity_Work_Device fragment = new MasterActivity_Work_Device();
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
        View v = inflater.inflate(R.layout.fragment_master_work_device, container, false);

        mContext = getContext();
        // 로딩창.
        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("정보 조회중입니다.\n잠시만 기다려주세요.");


        // ListView.
        list = (ListView) v.findViewById(R.id.listDevice);
        listItems = new ArrayList<MasterActivity_Work_Device_ListItem>();
        listAdapter = new MasterActivity_Work_Device_ListAdapter(getContext(), listItems);
        list.setAdapter(listAdapter);

        dialog.show();
        gList = new GetList();
        gList.execute(getListURL);

        return v;
    }

    // 리스트 정보 얻어오기.
    class GetList extends AsyncTask<String, Integer, String> {
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
            String model = "", lastDate = "", id = "", name = "", addDate = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    model = item.getString("model");
                    lastDate = item.optString("lastDate");
                    id = item.getString("id");
                    name = item.getString("name");
                    addDate = item.getString("addDate");

                    listItems.add(new MasterActivity_Work_Device_ListItem(model, lastDate, id, name, addDate));
                    listAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        }
    }
}