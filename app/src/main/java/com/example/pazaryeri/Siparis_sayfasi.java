package com.example.pazaryeri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pazaryeri.adapter.siparis_adapter;
import com.example.pazaryeri.helper.dict;
import com.example.pazaryeri.helper.siparis_helper;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class Siparis_sayfasi extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView totaltxt;
    Button siparis_tmm_btn;
    siparis_adapter adapter;
    int totalucret = 0;
    String gelen_sayfa;
    ArrayList<siparis_helper> arrhelper=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_sayfasi);
        arrhelper.clear();
        toolbar();
        totaltxt=findViewById(R.id.totalfiyat_txt);
        siparis_tmm_btn=findViewById(R.id.siparis_tmmla_btn);
       gelen_veri();
        recyclerView=findViewById(R.id.siparis_rec);

        vericek();


        siparis_tmm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siparis_adapter.siparis_tmm();
                Intent i=new Intent(Siparis_sayfasi.this,Anasayfa.class);
                startActivity(i);
            }
        });



    }

    ArrayList<ParseObject> arrobject=new ArrayList<>();
    ArrayList<dict> arrdict=new ArrayList<>();
    int sayac=0;
    private void vericek() {
    ParseQuery<ParseObject> query=ParseQuery.getQuery("siparis_sepeti");
    query.whereEqualTo("urun_sahibi",gelen_sayfa);
    query.whereEqualTo("kullanici_id",ParseUser.getCurrentUser().getObjectId());
    query.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if (e == null && objects.size() > 0) {
                sayac=objects.size();
                for (int i = 0; objects.size() > i; i++) {

                    sayac--;
                        onceki_data_get(objects.get(i).getRelation("siparis_ayrinti").getQuery(),sayac);
                }
            }



        }
    });








    }

    private void onceki_data_get(final ParseQuery<ParseObject> object, final int sayac) {
    object.whereEqualTo("durum",true);
        object.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> insideobject, ParseException e) {
                if (e == null && insideobject.size() > 0) {

                    for (int i = 0; i < insideobject.size(); i++) {
                        boolean girdimi=false;

                        for (int x=0 ; x < arrdict.size() ; x++){
                            int eski_adet=0;
                            if ( !girdimi &&arrdict.get(x).getKey().matches(insideobject.get(i).getString("urun_id"))){
                                Log.d("eski adet","urun id  "+insideobject.get(i).getString("urun_id")+"  "+insideobject.get(i).getString("adet")+"     "+arrdict.get(x).getValue());
                                eski_adet=Integer.parseInt(arrdict.get(x).getValue())+Integer.parseInt(insideobject.get(i).getString("adet"));
                                arrdict.get(x).setValue(String.valueOf(eski_adet));
                                girdimi=true;
                            }
                        }
                        if (!girdimi){
                        dict dict = new dict();
                        dict.setKey(insideobject.get(i).getString("urun_id"));
                        dict.setValue(insideobject.get(i).getString("adet"));
                        arrdict.add(dict);
                        }

                    }
                    if (sayac == 0)
                        urun_bilgi_cek(arrdict);
                }
            }
        });
    }

    private void urun_bilgi_cek(final ArrayList<dict> arrdict) {
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("urunler");
        for (int i = 0; arrdict.size()>i; i++){
            final  int index=i;
            query.whereEqualTo("objectId",arrdict.get(i).getKey());
            query.whereEqualTo("satici",gelen_sayfa);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size()>0 && e==null){
                    siparis_helper helper=new siparis_helper();
                    helper.setAdet(arrdict.get(index).getValue());
                    helper.setPara(String.valueOf(objects.get(0).get("fiyat")));
                    helper.setName(objects.get(0).getString("urun_ismi"));
                    helper.setAciklama(objects.get(0).getString("info"));
                    helper.setUrun_sahibi(gelen_sayfa);
                    helper.setUrun_id(arrdict.get(index).getKey());
                    totalucret += Integer.parseInt(helper.getAdet()) * Integer.parseInt(helper.getPara());
                    totaltxt.setText("€"+String.valueOf(totalucret));

                        //Resim CEk
                        ParseFile postImage = objects.get(0).getParseFile("img");
                        String imageUrl = postImage.getUrl() ;//live url
                        Uri imageUri = Uri.parse(imageUrl);
                    helper.setResim_url(imageUrl);
                    arrhelper.add(helper);
                }

                    recycler(arrhelper);
                }

            });
        }


    }


    private void toolbar() {

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });

    }

    private void recycler(ArrayList<siparis_helper> helper) {

        adapter = new siparis_adapter(this, siparis_helper.getdata(helper));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profil_menu, menu);
        toolbar.setTitle("Siparislerim");
        return  true;
    }



    private void gelen_veri() {
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {

            gelen_sayfa = (String) b.get("sayfa_name");

        }

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.memu_profil_kaydet).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.memu_profil_kaydet:
                Intent i =new Intent(Siparis_sayfasi.this,Eskisiparis.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}
