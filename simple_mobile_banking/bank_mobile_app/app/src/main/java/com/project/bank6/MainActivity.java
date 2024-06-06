package com.project.bank6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageButton transfer_b, wallet_b, profile_b;
    TextView username_field;
    ImageButton bbb;

    String wallet, currency, wallet_activity, identification;

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MainFragment mainFragment = new MainFragment();
        TransferFragment transferFragment = new TransferFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        CreditListFragment creditListFragment = new CreditListFragment();

        transfer_b = findViewById(R.id.transaction_button);
        wallet_b = findViewById(R.id.home_button);
        profile_b = findViewById(R.id.profile_button);

        bbb = findViewById(R.id.credit_button);


        setNewFragment(mainFragment);

        bbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewFragment(creditListFragment);
            }
        });

        transfer_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewFragment(transferFragment);
            }
        });

        wallet_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewFragment(mainFragment);
            }
        });

        profile_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewFragment(profileFragment);
            }
        });

        username_field = findViewById(R.id.username);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        username_field.setText(username);


        identification = intent.getStringExtra("identification");





    }


    public void setNewFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.main_frame_id, fragment);
        ft.addToBackStack(null); //chtob ne zasorat pamat
        ft.commit();
    }
}


