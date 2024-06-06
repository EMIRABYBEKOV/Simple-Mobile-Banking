package com.project.bank6;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    EditText nickname;
    EditText firstname;
    EditText lastname;
    EditText birth;
    EditText email;
    EditText phone;
    EditText address;
    CheckBox verification;
    JSONObject json_response;
    String nick;
    String nickname_;
    String firstname_;
    String lastname_;
    String birth_;
    String email_;
    String phone_;
    String address_;
    Boolean verification_ = false;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        nickname = view.findViewById(R.id.r_nickname);
        firstname = view.findViewById(R.id.r_user_name);
        lastname = view.findViewById(R.id.r_last_name);
        birth = view.findViewById(R.id.r_date_of_birth);
        email = view.findViewById(R.id.r_email);
        phone = view.findViewById(R.id.r_phone);
        address = view.findViewById(R.id.r_address);


        String url = (getResources().getString(R.string.url_get_account));

        OkHttpClient client = new OkHttpClient();

        String token = LoginActivity.access_token;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail Failure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String myresponse = response.body().string();
                myresponse = myresponse.substring( 1, myresponse.length() - 1 );
                System.out.println(myresponse);
                try {
                    json_response = new JSONObject(myresponse);
                } catch (JSONException e) {
                    System.out.println();
                    e.printStackTrace();
                }
                try {
                    nickname_ = json_response.getString("username");
                    firstname_ = json_response.getString("user_name");
                    lastname_ = json_response.getString("last_name");
                    birth_ = json_response.getString("date_of_birth");
                    email_ = json_response.getString("email");
                    phone_ = json_response.getString("phone");
                    address_ = json_response.getString("address");

                    nickname.setText(nickname_);
                    firstname.setText(firstname_);
                    lastname.setText(lastname_);
                    birth.setText(birth_);
                    email.setText(email_);
                    phone.setText(phone_);
                    address.setText(address_);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }
}