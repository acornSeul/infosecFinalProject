package com.example.team5_final.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.team5_final.InvoiceScanActivity;
import com.example.team5_final.MainActivity;
import com.example.team5_final.R;
import com.example.team5_final.VocActivity;
import com.example.team5_final.util.SaveSharedPreference;

import org.jetbrains.annotations.NotNull;

public class Fragment_home extends Fragment{
    Button btn_scan;
    String uniqueId;
    TextView txt_vocGo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home1, container, false);

        btn_scan = (Button)view.findViewById(R.id.btn_scan);
        txt_vocGo = (TextView)view.findViewById(R.id.txt_vocGo);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_home1);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        //from AfterLoginActivity
        Bundle extra = this.getArguments();
        if (extra != null){
            extra = getArguments();
            uniqueId = extra.getString("uniqueId");
        }
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("frag_home unique", uniqueId);
                Intent intent = new Intent(getContext(), InvoiceScanActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                startActivity(intent);
            }
        });

        txt_vocGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vocIntent = new Intent(getContext(), VocActivity.class);
                startActivity(vocIntent);
            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                SaveSharedPreference.clearUserName(getContext());
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}