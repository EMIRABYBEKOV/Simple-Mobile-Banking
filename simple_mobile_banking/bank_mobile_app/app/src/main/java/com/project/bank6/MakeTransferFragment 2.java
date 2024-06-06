package com.project.bank6;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeTransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeTransferFragment extends Fragment {

    Button button_transfer;
    EditText receiver_id, sum, currency;
    String  sum_data, currency_data, status, message,receiver_data;
    JSONObject json_response;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MakeTransferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeTransferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeTransferFragment newInstance(String param1, String param2) {
        MakeTransferFragment fragment = new MakeTransferFragment();
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
        View view =  inflater.inflate(R.layout.fragment_make_transfer, container, false);

        button_transfer = view.findViewById(R.id.button_transfer);

        receiver_id = view.findViewById(R.id.receiver_id);
        sum = view.findViewById(R.id.sum);
        currency = view.findViewById(R.id.currency);

        button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                receiver_data = receiver_id.getText().toString();
                sum_data = sum.getText().toString();
                currency_data = currency.getText().toString();

                String url = getResources().getString(R.string.url_make_transfer);
                OkHttpClient client = new OkHttpClient();

                String access_token = LoginActivity.access_token;

                RequestBody formBody = new FormBody.Builder()
                        .add("receiver", receiver_data)
                        .add("sum", sum_data)
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
                            message = json_response.getString("message");
                            if(status.equals("Fail")){

                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        receiver_id.setText("");
                                        sum.setText("");
                                        currency.setText("");
                                        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_LONG);
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