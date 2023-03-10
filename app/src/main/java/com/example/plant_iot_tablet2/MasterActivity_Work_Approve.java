package com.example.plant_iot_tablet2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
 * Use the {@link MasterActivity_Work_Approve#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterActivity_Work_Approve extends Fragment {
    //ListView.
    ListView list;
    MasterActivity_Work_Approve_ListAdapter listAdapter;
    ArrayList<MasterActivity_Work_Approve_ListItem> listItems;

    GetList gList;
    String getListURL = "http://aj3dlab.dothome.co.kr/Test_signListG_Android.php";

    public static Context mContext;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MasterActivity_Work_Approve() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterActivity_Approve.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterActivity_Work_Approve newInstance(String param1, String param2) {
        MasterActivity_Work_Approve fragment = new MasterActivity_Work_Approve();
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
        View v = inflater.inflate(R.layout.fragment_master_work_approve, container, false);

        mContext = getContext();

        // ListView.
        list = (ListView) v.findViewById(R.id.listApprove);
        listItems = new ArrayList<MasterActivity_Work_Approve_ListItem>();
        listAdapter = new MasterActivity_Work_Approve_ListAdapter(getContext(), listItems);
        list.setAdapter(listAdapter);
        gList = new GetList();
        gList.execute(getListURL);

        return v;
    }

    // 리스트 수정.
    public void ModifyList(int position, String name, String phone, String date, String approve, int approveC, int button_approve, int button_delete, boolean enable) {
        listItems.set(position, new MasterActivity_Work_Approve_ListItem(name, phone, date, approve, approveC, button_approve, button_delete,enable));
        listAdapter.notifyDataSetChanged();
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
            String name = "", phone = "", date = "";
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    name = item.getString("name");
                    phone = item.getString("phone");
                    date = item.getString("date");

                    listItems.add(new MasterActivity_Work_Approve_ListItem(name, phone, date, "", 0, R.drawable.approve_icon, R.drawable.approve_not_icon, true));
                    listAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    // ListAdapter 같은 클래스 내에 존재하도록.
    public class MasterActivity_Work_Approve_ListAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater = null;
        MasterActivity_Work_Approve_ListAdapter listAdapter;
        ArrayList<MasterActivity_Work_Approve_ListItem> items;

        TextView name, phone, signDate, text_approve;
        ImageView button_approve; // 승인 버튼.
        ImageView button_delete; // 거절 버튼.

        // 가입 승인 or 거절.
        SignApprove sApprove;
        String signApproveURL = "http://aj3dlab.dothome.co.kr/Test_signApprove_Android.php";

        public MasterActivity_Work_Approve_ListAdapter(Context context, ArrayList<MasterActivity_Work_Approve_ListItem> data) {
            mContext = context;
            items = data;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = mLayoutInflater.inflate(R.layout.list_item_master_approve, viewGroup, false);

            String nameT = items.get(i).getName();
            String phoneT = items.get(i).getPhone();
            String dateT = items.get(i).getDate();
            String approveT =items.get(i).getApprove();
            int approveTC = items.get(i).getApproveC();
            int approveB = items.get(i).getApproveB();
            int deleteB = items.get(i).getDeleteB();
            boolean enable = items.get(i).getEnable();

            name = (TextView) view.findViewById(R.id.name);
            name.setText(nameT);
            phone = (TextView) view.findViewById(R.id.phone);
            phone.setText(phoneT);
            signDate = (TextView) view.findViewById(R.id.signDate);
            signDate.setText(dateT);
            text_approve = (TextView)view.findViewById(R.id.text_approve);
            text_approve.setText(approveT);
            text_approve.setTextColor(approveTC);

            button_approve = (ImageView) view.findViewById(R.id.button_approve);
            button_approve.setImageResource(approveB);
            button_approve.setEnabled(enable);
            button_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModifyList(i, nameT, phoneT, dateT, "승인 완료", Color.parseColor("#646464"), 0, 0, false);

                    sApprove = new SignApprove();
                    sApprove.execute(signApproveURL, nameT, phoneT, "approve");
                }
            });
            button_delete = (ImageView) view.findViewById(R.id.button_delete);
            button_delete.setImageResource(deleteB);
            button_delete.setClickable(enable);
            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
                    builder.setMessage(nameT + " 님의 가입 요청을 거절하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            ModifyList(i, nameT, phoneT, dateT, "승인 거절", Color.parseColor("#850000"), 0, 0, false);

                            sApprove = new SignApprove();
                            sApprove.execute(signApproveURL, nameT, phoneT, "delete");
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
            });

            return view;
        }

        // 가입 승인 or 거절.
        class SignApprove extends AsyncTask<String, Integer, String> {
            String name, phone, work;
            @Override
            protected String doInBackground(String... params) {
                StringBuilder jsonHtml = new StringBuilder();
                name = (String) params[1];
                phone = (String) params[2];
                work = (String) params[3];

                String serverURL = (String) params[0];
                String postParameters = "name=" + name + "&phone=" + phone + "&work=" + work;

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
}