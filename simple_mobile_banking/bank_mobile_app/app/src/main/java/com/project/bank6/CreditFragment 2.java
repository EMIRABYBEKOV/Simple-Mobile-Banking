package com.project.bank6;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView credit_rules;
    Button credit_button;
    EditText salary, pledge, price, first_payment;
    String salary_data, pledge_data, price_data, first_payment_data, salary_response;
    JSONObject json_response;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditFragment newInstance(String param1, String param2) {
        CreditFragment fragment = new CreditFragment();
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
        View view =  inflater.inflate(R.layout.fragment_credit, container, false);

        credit_rules = view.findViewById(R.id.credit_rules);

        credit_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreditRulesFragment creditRulesFragment = new CreditRulesFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_id, creditRulesFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
            });

        credit_button = view.findViewById(R.id.button_credit);

        salary = view.findViewById(R.id.salary_id);
        pledge = view.findViewById(R.id.pledge_id);
        first_payment = view.findViewById(R.id.paid_id);
        price = view.findViewById(R.id.price_id);

        credit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salary_data = salary.getText().toString();
                pledge_data = pledge.getText().toString();
                first_payment_data = first_payment.getText().toString();
                price_data = price.getText().toString();

                String url = getResources().getString(R.string.url_take_credit);
                OkHttpClient client = new OkHttpClient();

                String access_token = LoginActivity.access_token;

                RequestBody formBody = new FormBody.Builder()
                        .add("salary", salary_data)
                        .add("j_check", "false")
                        .add("pledge", pledge_data)
                        .add("price", price_data)
                        .add("paid", first_payment_data)
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
                            salary_response = json_response.getString("salary");
                            if(salary_response.equals("")){

                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast toast = Toast.makeText(getActivity(),"Incorrect data. Please check the rules",Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        salary.setText("");
                                        pledge.setText("");
                                        first_payment.setText("");
                                        price.setText("");
                                        Toast toast = Toast.makeText(getActivity(),"Successfully sent",Toast.LENGTH_LONG);
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