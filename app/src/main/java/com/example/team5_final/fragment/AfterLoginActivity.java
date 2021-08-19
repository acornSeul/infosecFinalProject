package com.example.team5_final.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.team5_final.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AfterLoginActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String uniqueId;
    private long backKeyClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afrerlogin);

        //from MainActivity
        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");
        Log.d("afterlogin unique", uniqueId);

        bottomNavigationView = findViewById(R.id.bottom_view);

        Fragment_home home = new Fragment_home();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,home).commit();
        Bundle bundle = new Bundle();
        bundle.putString("uniqueId", uniqueId);
        home.setArguments(bundle);

        Menu menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                BottomNavigate(item.getItemId());
                return true;
            }
        });


    }
    private void BottomNavigate(int id){
        String tag = String.valueOf(id);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        Fragment currentFragment = manager.getPrimaryNavigationFragment();
        if (currentFragment != null){
            transaction.hide(currentFragment);
        }

        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null){
            if (id == R.id.bottom_home){
                fragment = new Fragment_home();
            } else if (id == R.id.bottom_apply){
                fragment = new Fragment_apply();
            } else{
                fragment = new Fragment_list();
            }
            Bundle bundle = new Bundle();
            bundle.putString("uniqueId", uniqueId);
            fragment.setArguments(bundle);

            transaction.add(R.id.main_frame, fragment, tag);
        } else {
            transaction.show(fragment);
        }
        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyClickTime + 2000){
            backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(AfterLoginActivity.this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000){
            ActivityCompat.finishAffinity(this);
        }
    }
}
