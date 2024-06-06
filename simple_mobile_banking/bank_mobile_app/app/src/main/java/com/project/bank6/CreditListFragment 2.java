package com.project.bank6;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView listView;
    List<HashMap<String, String>> hash = new ArrayList<>();
    JSONObject json_response;

    List<String> pledge = new ArrayList<>();
    List<String> final_amount = new ArrayList<>();
    List<String> paid = new ArrayList<>();
    List<String> credit_long = new ArrayList<>();
    List<String> date_taking = new ArrayList<>();
    List<String> date_full_payment = new ArrayList<>();
    List<String> every_month = new ArrayList<>();
    List<String> notices = new ArrayList<>();
    List<String> approval = new ArrayList<>();


    public CreditListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreditListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditListFragment newInstance(String param1, String param2) {
        CreditListFragment fragment = new CreditListFragment();
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
         View view = inflater.inflate(R.layout.fragment_credit_list, container, false);

        System.out.println(99);
        System.out.println(99);;
        System.out.println(LoginActivity.identification);


        listView = view.findViewById(R.id.credit_list);



        String url = getResources().getString(R.string.url_take_credit_list);
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

                System.out.println(myresponse);



                String[] js = myresponse.split("\\},");
                System.out.println(1);
                System.out.println(1);System.out.println(1);System.out.println(1);
                System.out.println(js[0]);
                System.out.println(js.length);

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
        System.out.println(hash.toString());

        pledge.clear();
        final_amount.clear();
        paid.clear();
        credit_long.clear();
        date_taking.clear();
        date_full_payment.clear();
        every_month.clear();
        notices.clear();
        approval.clear();

        for(HashMap<String, String> i: hash){
            pledge.add(i.get("pledge"));
            final_amount.add(i.get("final_amount"));
            paid.add(i.get("paid"));
            credit_long.add(i.get("credit_long"));
            date_taking.add(i.get("date_taking"));
            date_full_payment.add(i.get("date_payment"));
            every_month.add(i.get("every_month"));
            notices.add(i.get("notices"));
            approval.add(i.get("approval"));

        }

        final String ATTRIBUTE_PLEDGE = "pledge";
        final String ATTRIBUTE_FINAL_AMOUNT = "final_amount";
        final String ATTRIBUTE_PAID = "paid";
        final String ATTRIBUTE_CREDIT_LONG = "credit_long";
        final String ATTRIBUTE_DATE_TAKING = "date_taking";
        final String ATTRIBUTE_DATE_FULL_PAYMENT = "date_payment";
        final String ATTRIBUTE_EVERY_MONTH = "every_month";
        final String ATTRIBUTE_NOTICES = "notices";
        final String ATTRIBUTE_APPROVAL = "approval";



        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                pledge.size());
        Map<String, Object> m;
        for (int i = 0; i < pledge.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_PLEDGE, pledge.get(i));
            m.put(ATTRIBUTE_FINAL_AMOUNT, final_amount.get(i));
            m.put(ATTRIBUTE_PAID, paid.get(i));
            m.put(ATTRIBUTE_CREDIT_LONG, credit_long.get(i));
            m.put(ATTRIBUTE_DATE_TAKING, date_taking.get(i));
            m.put(ATTRIBUTE_DATE_FULL_PAYMENT, date_full_payment.get(i));
            m.put(ATTRIBUTE_EVERY_MONTH, every_month.get(i));
            m.put(ATTRIBUTE_NOTICES, notices.get(i));
            m.put(ATTRIBUTE_APPROVAL, approval.get(i));
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = { ATTRIBUTE_PLEDGE, ATTRIBUTE_FINAL_AMOUNT, ATTRIBUTE_PAID, ATTRIBUTE_CREDIT_LONG, ATTRIBUTE_DATE_TAKING,
                ATTRIBUTE_DATE_FULL_PAYMENT, ATTRIBUTE_EVERY_MONTH, ATTRIBUTE_NOTICES, ATTRIBUTE_APPROVAL};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.pledge_tr, R.id.final_amount_tr, R.id.paid_tr, R.id.credit_long_tr, R.id.date_taking_tr, R.id.date_of_full_payment_tr,
        R.id.every_month_tr, R.id.notices_tr, R.id.approval_tr};

        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, R.layout.credit_item,
                from, to);


        // определяем список и присваиваем ему адаптер
        listView = (ListView) view.findViewById(R.id.credit_list);
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