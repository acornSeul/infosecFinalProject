package com.example.team5_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.team5_final.fragment.Fragment_apply;
import com.example.team5_final.fragment.Fragment_home;
import com.example.team5_final.fragment.Fragment_list;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class AfterLoginActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String uniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afrerlogin);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        Log.d("unique test", "seul unique : " + uniqueId);

        bottomNavigationView = findViewById(R.id.bottom_view);


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
                //fragment로 파라미터 넘기기
                Bundle bundle = new Bundle();
                bundle.putString("uniqueId", uniqueId);

                fragment.setArguments(bundle);
            } else{
                fragment = new Fragment_list();
            }

            transaction.add(R.id.main_frame, fragment, tag);
        } else {
            transaction.show(fragment);
        }

        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();

    }

}
