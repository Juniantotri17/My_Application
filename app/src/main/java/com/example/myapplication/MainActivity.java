package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

//        ImageView appbarSearch = findViewById(R.id.appbar_search);
//        ImageView appbarAccount = findViewById(R.id.appbar_account);
////        appbarAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"Account Klik", Toast.LENGTH_LONG).show();
//            }
//        });
//        appbarSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"Search Klik", Toast.LENGTH_LONG).show();
//            }
//        });
//
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_jadwal:
                    fragment = new JadwalFragment();
                    break;
                case R.id.navigation_keuangan:
                    fragment = new KeuanganFragment();
                    break;
                case R.id.navigation_admin:
                    fragment = new AdminFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    fragment).commit();
            return true;
        }
    };
}
