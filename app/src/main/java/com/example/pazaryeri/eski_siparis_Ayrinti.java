package com.example.pazaryeri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pazaryeri.adapter.eskisiparis_adapter;
import com.example.pazaryeri.adapter.eskisiparisayrinti_adapter;
import com.example.pazaryeri.helper.eskisiparis_helper;
import com.example.pazaryeri.helper.eskisiparisayrinti_helper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class eski_siparis_Ayrinti extends AppCompatActivity {


    RecyclerView recyclerView;
    eskisiparisayrinti_adapter adapter;
    String objectId, sirket_name, get_tarih;
    ArrayList<eskisiparisayrinti_helper> arrhelper = new ArrayList<>();
    TextView satici,tarih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eski_siparis__ayrinti);
        idupdate();
        gelenveri();

        recyclerView = findViewById(R.id.siparis_ayrinti_recyc);

        getdata();

    }

    private void gelenveri() {
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            get_tarih = (String) b.get("tarih");
            objectId = (String) b.get("objectId");
            sirket_name = (String) b.get("sirket_name");
            tarih.setText(get_tarih);
            satici.setText(sirket_name);

        }

    }

    private void getdata() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis");
        query.whereEqualTo("objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (final ParseObject a : objects) {
                        ucret_txt.setText("€" + a.getString("ucret"));
                        if (a.getBoolean("durum"))
                            icon.setBackgroundColor((Color.parseColor("#6CBA29")));
                        else
                            icon.setBackgroundColor((Color.parseColor("#F4A72C")));
                        ParseQuery<ParseObject> relation = a.getRelation("siparis_ayrinti").getQuery();
                        relation.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> insadeobjects, ParseException e) {
                                if (e == null && insadeobjects.size() > 0) {

                                    for (ParseObject aa : insadeobjects) {
                                        geturunismi(aa, insadeobjects.size(), a);


                                    }

                                }

                            }
                        });
                    }
                }
            }
        });


    }

    int control1 = 0;

    private void geturunismi(final ParseObject aa, final int control, final ParseObject a) {
        ParseQuery<ParseObject> query = new ParseQuery("urunler");
        query.whereEqualTo("objectId", aa.getString("urun_id"));
        query.whereEqualTo("satici", sirket_name);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                control1++;
                eskisiparisayrinti_helper helper = new eskisiparisayrinti_helper();
                helper.setStar(0);
                helper.setUrun_id(aa.getString("urun_id"));
                helper.setDurum(a.getBoolean("durum"));
                helper.setAdet(aa.getString("adet"));

                helper.setUrunismi(objects.get(0).getString("urun_ismi"));
                arrhelper.add(helper);
                if (control == control1)
                    recycview(arrhelper);
            }

        });

    }

    private void recycview(ArrayList<eskisiparisayrinti_helper> helper) {

        adapter = new eskisiparisayrinti_adapter(this, eskisiparisayrinti_helper.getdata(helper));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    Toolbar toolbar;
    RelativeLayout relativeLayout;
    TextView ucret_txt, icon;

    private void idupdate() {
        toolbar = findViewById(R.id.toolbar);
        satici=findViewById(R.id.satici_bilgi_isim);
        tarih=findViewById(R.id.satici_bilgi_tarih);
        ucret_txt = findViewById(R.id.eskisiparis_ayrinti_totalpara);
        icon = findViewById(R.id.eskisiparis_ayrinti_icon);
        relativeLayout = findViewById(R.id.eskisiparis_ayrinti_relayout);
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
        toolbar.setTitle("Eski Siparisler");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.memu_profil_kaydet).setTitle("Kaydet");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.memu_profil_kaydet:
                eskisiparisayrinti_adapter.kaydet();
                Intent i = new Intent(eski_siparis_Ayrinti.this, Anasayfa.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}