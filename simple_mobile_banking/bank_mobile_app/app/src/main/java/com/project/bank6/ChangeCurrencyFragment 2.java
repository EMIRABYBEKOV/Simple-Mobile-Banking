package com.project.bank6;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChangeCurrencyFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button buy_currency;
    EditText currency;
    String currency_data, status, message;
    JSONObject json_response;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeCurrencyFragment() {
        // Required empty public constructor
    }


    public static ChangeCurrencyFragment newInstance(String param1, String param2) {
        ChangeCurrencyFragment fragment = new ChangeCurrencyFragment();
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
        View view =  inflater.inflate(R.layout.fragment_change_currency, container, false);

        buy_currency = view.findViewById(R.id.buy_currency);
        currency = view.findViewById(R.id.currency);

        buy_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                currency_data = currency.getText().toString();

                String url = getResources().getString(R.string.url_buy_currency);
                OkHttpClient client = new OkHttpClient();

                String access_token = LoginActivity.access_token;

                RequestBody formBody = new FormBody.Builder()
                        .add("currency", currency_data)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .addHeader("Authorization", "Bearer " + access_token)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String myresponse = response.body().string();
                        System.out.println(myresponse);

                        try {
                            json_response = new JSONObject(myresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            status = json_response.getString("Status");
                            if(status.equals("Fail")){

                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast toast = Toast.makeText(getActivity(),"Something goes wrong. Try later(",Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        currency.setText("");
                                        Toast toast = Toast.makeText(getActivity(),"Successfully bought.",Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });

        return view;
    }
}