package com.example.plant_iot_tablet2;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
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
 * Use the {@link MasterActivity_ListDelete#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterActivity_ListDelete extends Fragment {
    // Back 버튼.
    ImageView button_back;

    // 리스트 삭제 버튼.
    ImageView button_delete;
    ModelDelete dModel;
    String modelDeleteURL = "http://aj3dlab.dothome.co.kr/Test_modelDelete_Android.php";

    // ListView.
    ListView list;
    Model_Delete_ListAdapter listAdapter;
    ArrayList<Model_Delete_ListItem> listItems;
    GetList gList;
    String getListURL = "http://aj3dlab.dothome.co.kr/Test_modelListG_Android.php";

    String id = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MasterActivity_ListDelete() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterActivity_ListDelete.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterActivity_ListDelete newInstance(String param1, String param2) {
        MasterActivity_ListDelete fragment = new MasterActivity_ListDelete();
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
        View v = inflater.inflate(R.layout.fragment_master_list_delete, container, false);

        id = "ahjoo";

        // Back 버튼.
        button_back = (ImageView) v.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                MasterActivity_ListManage listManage = new MasterActivity_ListManage();
                transaction.replace(R.id.listFrame, listManage);
                transaction.commit();
            }
        });

        // 리스트 삭제 버튼.
        button_delete = (ImageView) v.findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = listAdapter.getCount();
                int checkCount = 0;
                ArrayList<Integer> arrayList = new ArrayList<Integer>();
                for(int i=0; i<count; i++) { // 삭제할 리스트 갯수 파악.
                    if(listItems.get(i).getCheck().equals("O")) {
                        checkCount++;
                        arrayList.add(i); // 몇 번째 리스트를 삭제할건지 파악.
                    }
                }

                if(checkCount != 0) { // 0개가 아닐 때만 AlertDialog 출력.
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
                    builder.setMessage(String.valueOf(checkCount) + "개의 리스트를 삭제하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for(int j=0; j<arrayList.size(); j++) {
                                dModel = new ModelDelete(); // 모델 삭제.
                                dModel.execute(modelDeleteURL, listItems.get(arrayList.get(j)).getModel(), listItems.get(arrayList.get(j)).getId());
                            }

                            // 다시 화면 전환.
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            MasterActivity_ListManage listManage = new MasterActivity_ListManage();
                            transaction.replace(R.id.listFrame, listManage);
                            transaction.commit();
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        // ListView.
        list = (ListView) v.findViewById(R.id.plantList);
        listItems = new ArrayList<Model_Delete_ListItem>();
        listAdapter = new Model_Delete_ListAdapter(getContext(), listItems);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new listItemClickListener());

        gList = new GetList();
        gList.execute(getListURL);

        return v;
    }

    // ListView 아이템 클릭 시 이벤트.
    public class listItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
            String name = listItems.get(position).getName();
            String model = listItems.get(position).getModel();
            String id = listItems.get(position).getId();

            String check = listItems.get(position).getCheck();
            if(check.equals("O")) {
                check = "X";
            }
            else {
                check = "O";
            }

            ModifyList(position, name, model, id, check);
        }
    }
    // 리스트 수정.
    public void ModifyList(int position, String name, String model, String id, String check) {
        listItems.set(position, new Model_Delete_ListItem(name, model, id, check));
        listAdapter.notifyDataSetChanged();
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
            String model = "", name = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    model = item.getString("model");
                    name = item.getString("name");

                    listItems.add(new Model_Delete_ListItem(name, model, id, "X"));
                    listAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 모델 삭제.
    class ModelDelete extends AsyncTask<String, Integer, String> {
        String model, id;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            model = (String) params[1];
            id = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "model=" + model + "&id=" + id;

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
            }
        }
    }
}