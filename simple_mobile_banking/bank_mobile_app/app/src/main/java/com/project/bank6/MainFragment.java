package com.project.bank6;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainFragment extends Fragment {

    TextView account_balance, currency, identification;
    ImageButton make_transfer_b, currency_button, credit_button, credit_list_button;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    JSONObject json_response;
    public static String  wallet_, currency_, identification_, wallet_activity_;
    Boolean wallet_activity;

    private String mParam1;
    private String mParam2;

    public MainFragment() {
  }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        account_balance = view.findViewById(R.id.account_balance);
        currency = view.findViewById(R.id.account_currency);
        identification = view.findViewById(R.id.account_identification);



        String url2 = getResources().getString(R.string.url_get_wallet);
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url2)
                .addHeader("Authorization", "Bearer " + LoginActivity.access_token)
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

                    wallet_ = json_response.getString("wallet");
                    currency_ = json_response.getString("wallet_currency");
                    identification_ = json_response.getString("identification");

                } catch (JSONException e) {
                    wallet_ = "";
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(wallet_.equals("")){

                        }else{
                            account_balance.setText(wallet_);
                            currency.setText(currency_);
                            identification.setText(identification_);

                        }
                    }
                });
            }
        });


        make_transfer_b = view.findViewById(R.id.make_transfer_button);

        make_transfer_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeTransferFragment maketransferFragment = new MakeTransferFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_id, maketransferFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        currency_button = view.findViewById(R.id.currency_button);
        currency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCurrencyFragment changeCurrencyFragment = new ChangeCurrencyFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_id, changeCurrencyFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        credit_button = view.findViewById(R.id.take_credit_button);
        credit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreditFragment creditFragment = new CreditFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_id, creditFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });






        return view;

    }

}