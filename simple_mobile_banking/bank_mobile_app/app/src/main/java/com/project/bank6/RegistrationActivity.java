package com.project.bank6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class RegistrationActivity extends AppCompatActivity {

    EditText nickname, first_name, last_name, date_of_birt, email, phone, address, password1, password2;
    JSONObject json_response;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void toLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void register__(View v) {
        nickname = findViewById(R.id.r_nickname);
        first_name = findViewById(R.id.r_user_name);
        last_name = findViewById(R.id.r_last_name);
        date_of_birt = findViewById(R.id.r_date_of_birth);
        email = findViewById(R.id.r_email);
        phone = findViewById(R.id.r_phone);
        address = findViewById(R.id.r_address);
        password1 = findViewById(R.id.r_password1);
        password2 = findViewById(R.id.r_password2);


        String url = getResources().getString(R.string.url_register);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", nickname.getText().toString())
                .add("user_name", first_name.getText().toString())
                .add("last_name", last_name.getText().toString())
                .add("date_of_birth", date_of_birt.getText().toString())
                .add("email", email.getText().toString())
                .add("phone", phone.getText().toString())
                .add("address", address.getText().toString())
                .add("password", password1.getText().toString())
                .add("c_password", password2.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                try {
                    System.out.println(myresponse);
                    json_response = new JSONObject(myresponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    nick = json_response.getString("username");
                } catch (JSONException e) {
                    nick = "";
                    e.printStackTrace();
                }
                RegistrationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nick.equals("")){
                            Toast.makeText(RegistrationActivity.this, json_response.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}