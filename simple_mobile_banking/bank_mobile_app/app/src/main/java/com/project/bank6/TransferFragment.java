package com.project.bank6;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;

    JSONObject json_response;

    List<String> receivers = new ArrayList<>();
    List<String> senders = new ArrayList<>();
    List<String> sums = new ArrayList<>();
    List<String> currencies = new ArrayList<>();



    List<HashMap<String, String>> hash = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance(String param1, String param2) {
        TransferFragment fragment = new TransferFragment();
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
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);

        listView = view.findViewById(R.id.transaction_list);



        String url = getResources().getString(R.string.url_get_transfers);
        OkHttpClient client = new OkHttpClient();

        String access_token = LoginActivity.access_token;


        Request request = new Request.Builder()
                .url(url + "&q=" + LoginActivity.identification)
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string().replace("[", "").replace("]", "");
                System.out.println(90);

                System.out.println(90);
                System.out.println(90);
                System.out.println(90);
                System.out.println(90);
                System.out.println(myresponse);
                String[] js = myresponse.split("\\},");
                StringBuilder str = new StringBuilder();
                hash = new ArrayList<>();
                try {
                    for (int i = 0; i < js.length - 1; i++) {
                        hash.add(json_to_map(js[i] + "}"));
                    }
                    hash.add(json_to_map(js[js.length - 1]));
                    System.out.println(hash);


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });


        receivers.clear();
        senders.clear();
        sums.clear();
        currencies.clear();


        for(HashMap<String, String> i: hash){
            receivers.add(i.get("receiver"));
            senders.add(i.get("sender"));
            sums.add(i.get("sum"));
            currencies.add(i.get("currency"));
        }

        final String ATTRIBUTE_RECEIVER_TEXT = "receiver";
        final String ATTRIBUTE_SENDER_TEXT = "sender";
        final String ATTRIBUTE_SUM_TEXT = "sum";
        final String ATTRIBUTE_CURRENCY_TEXT = "currency";


        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                receivers.size());
        Map<String, Object> m;
        for (int i = 0; i < receivers.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_RECEIVER_TEXT, receivers.get(i));
            m.put(ATTRIBUTE_SENDER_TEXT, senders.get(i));
            m.put(ATTRIBUTE_SUM_TEXT, sums.get(i));
            m.put(ATTRIBUTE_CURRENCY_TEXT, currencies.get(i));
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_RECEIVER_TEXT, ATTRIBUTE_SENDER_TEXT, ATTRIBUTE_SUM_TEXT, ATTRIBUTE_CURRENCY_TEXT};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.receiver_tr, R.id.sender_tr, R.id.sum_tr, R.id.currency_tr};

        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, R.layout.transaction_item,
                from, to);

        // определяем список и присваиваем ему адаптер
        listView = (ListView) view.findViewById(R.id.transaction_list);
        listView.setAdapter(sAdapter);



        return view;
    }

    public static HashMap<String, String> json_to_map(String t) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }
        return map;
    }
}