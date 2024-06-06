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
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username_field, password_filed;
    JSONObject json_response, json_response2;
    public static String access_token, identification;
    String check_iden;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_field = findViewById(R.id.username_field1);
    }

    public void login_view(View v) {
        username_field = findViewById(R.id.username_field1);
        password_filed = findViewById(R.id.password_field1);


        String url = getResources().getString(R.string.url_login);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", username_field.getText().toString())
                .add("password", password_filed.getText().toString())
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
                    json_response = new JSONObject(myresponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    access_token = json_response.getString("access");

                } catch (JSONException e) {
                    access_token = "";
                    e.printStackTrace();
                }
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (access_token.equals("")) {
                            username_field.setText("");
                            password_filed.setText("");
                            Toast.makeText(LoginActivity.this, "Username or password isn't correct", Toast.LENGTH_SHORT).show();
                        } else {
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
                                        check_iden = json_response.getString("identification");
                                        identification = json_response.getString("identification");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("username", username_field.getText().toString());
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        String url4 = getResources().getString(R.string.url_create_wallet);
                                        OkHttpClient client2 = new OkHttpClient();


                                        Request request2 = new Request.Builder()
                                                .url(url4)
                                                .addHeader("Authorization", "Bearer " + access_token)
                                                .build();

                                        client2.newCall(request2).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String myresponse2 = response.body().string();
                                                System.out.println(myresponse2);
                                                try {
                                                    json_response2 = new JSONObject(myresponse2);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                String wallet_;
                                                String currency_;
                                                try {
                                                    System.out.println(2);
                                                    System.out.println(2);
                                                    System.out.println(2);

                                                    identification = json_response2.getString("user_number");
                                                    System.out.println(1);
                                                    System.out.println(1);                                            System.out.println(1);

                                                    System.out.println(identification);
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("username", username_field.getText().toString());
                                                    startActivity(intent);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    public void toRegistration(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }}
