package com.example.pazaryeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.pazaryeri.adapter.eskisiparis_adapter;
import com.example.pazaryeri.adapter.siparis_adapter;
import com.example.pazaryeri.helper.eskisiparis_helper;
import com.example.pazaryeri.helper.siparis_helper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Eskisiparis extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    eskisiparis_adapter adapter;
    ArrayList<eskisiparis_helper> helper = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eskisiparis);
        idupdate();
        recyclerView = findViewById(R.id.recyclerview_eskisiparis);
        getdata();


        recycview(helper);
    }

    ArrayList<eskisiparis_helper> arrlist=new ArrayList<>();
    private void getdata() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis");
        query.whereEqualTo("kullanici_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject a:objects){
                        eskisiparis_helper helper=new eskisiparis_helper();
                        helper.setDurum(a.getBoolean("durum"));
                        helper.setTarih(a.getCreatedAt());
                        helper.setObjectId(a.getObjectId());
                        helper.setTotal_fiayt(a.getString("ucret"));
                        helper.setSirket(a.getString("sirket_name"));
                        arrlist.add(helper);
                    }
                    recycview(arrlist);
                }
            }
        });
    }


    private void recycview(ArrayList<eskisiparis_helper> helper) {

        adapter = new eskisiparis_adapter(this, eskisiparis_helper.getdata(helper));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void idupdate() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_menu, menu);
        toolbar.setTitle("Eski siparisler");
        return true;

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.memu_profil_kaydet).setVisible(false);
        return true;
    }


}