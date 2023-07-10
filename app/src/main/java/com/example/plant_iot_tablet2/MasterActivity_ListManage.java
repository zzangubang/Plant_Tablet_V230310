package com.example.plant_iot_tablet2;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
 * Use the {@link MasterActivity_ListManage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterActivity_ListManage extends Fragment {
    // 메뉴 버튼
    ImageView button_menu;
    // 모델 삭제.
    ImageView button_delete;
    // 모델 추가 버튼.
    LinearLayout button_add;

    // ListView.
    ListView list;
    Model_ListAdapter listAdapter;
    ArrayList<Model_ListItem> listItems;
    GetList gList;
    String getListURL = "http://hosting.ajplants.com/Plant_modelListG_Android.php";

    String id = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MasterActivity_ListManage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterActivity_ListManage.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterActivity_ListManage newInstance(String param1, String param2) {
        MasterActivity_ListManage fragment = new MasterActivity_ListManage();
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
        View v = inflater.inflate(R.layout.fragment_master_list_manage, container, false);

        id = "ahjoo";

        // 모델 추가 버튼.
        button_add = (LinearLayout) v.findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BluetoothActivity.class);
                intent.putExtra("purpose", "add");
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        // 모델 삭제.
        button_delete = (ImageView) v.findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                MasterActivity_ListDelete listDelete = new MasterActivity_ListDelete();
                transaction.replace(R.id.listFrame, listDelete);
                transaction.commit();
            }
        });

        // 메뉴 버튼.
        button_menu = (ImageView) v.findViewById(R.id.button_menu);
        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(), MasterActivity_Menu.class);
                startActivityForResult(intent, 1);
            }
        });

        // ListView.
        list = (ListView) v.findViewById(R.id.plantList);
        listItems = new ArrayList<Model_ListItem>();
        listAdapter = new Model_ListAdapter(getContext(), listItems);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new listItemClickListener());

        gList = new GetList();
        gList.execute(getListURL);


        return v;
    }

    // ListView 아이템 클릭 시 이벤트.
    public class listItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("id", listItems.get(position).getId());
            intent.putExtra("name", listItems.get(position).getName());
            intent.putExtra("model", listItems.get(position).getModel());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) { // 메뉴 선택.
            if(resultCode == RESULT_OK) {
                String menu = data.getStringExtra("menu");
                switch (menu) {
                    case "approve": // 회원가입 승인.
                        Intent intentA = new Intent(getContext(), MasterActivity_Work.class);
                        intentA.putExtra("work", "approve");
                        startActivity(intentA);
                        break;
                    case "user": // 회원 정보.
                        Intent intentU = new Intent(getContext(), MasterActivity_Work.class);
                        intentU.putExtra("work", "user");
                        startActivity(intentU);
                        break;
                    case "device": // 기기 정보.
                        Intent intentD = new Intent(getContext(), MasterActivity_Work.class);
                        intentD.putExtra("work", "device");
                        startActivity(intentD);
                        break;
                    case "logout": // 로그아웃.
                        Intent intentL = new Intent(getContext(), LoginActivity.class);
                        startActivity(intentL);
                        ((MasterActivity) MasterActivity.mContext).MasterFinish();
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
            String postParameters = "id=" + id; // 관리자 아이디.


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
            String model = "", name = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    model = item.getString("model");
                    name = item.getString("name");

                    listItems.add(new Model_ListItem(name, model, id));
                    listAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}