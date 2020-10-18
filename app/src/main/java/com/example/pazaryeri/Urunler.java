package com.example.pazaryeri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pazaryeri.adapter.urun_adapter;
import com.example.pazaryeri.helper.dict;
import com.example.pazaryeri.helper.urun_helper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class Urunler extends AppCompatActivity implements adet_interface {


    String adet1 = "0";
    urun_adapter adapter;
    Toolbar toolbar;
    ArrayList<urun_helper> helper = new ArrayList<>();
    RecyclerView recyclerView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String gelen_sayfa_name, gelen_sayfa_logo;
    TextView satici_adi, satici_telno, satici_info;
    ImageView satici_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunler);
        idupdate();
        helper.clear();
        toolbar();
        collops();
        gelenveriler();
        setdata();


    }
    private void setdata() {
        //get data for database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sirketler");
        query.whereEqualTo("sirket_Adi", gelen_sayfa_name);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                satici_telno.setText(objects.get(0).get("telefon_numarasi").toString());
                satici_info.setText(objects.get(0).get("info").toString());
            }
        });


        Picasso.get().load(gelen_sayfa_logo).into(satici_logo);
        satici_adi.setText(gelen_sayfa_name);

    }


    private void databasevericek() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );


        // Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("urunler");
        query.whereEqualTo("satici", gelen_sayfa_name);


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // Adding objects into the Array
                    tekvericek(objects);


                }

            }
        });

    }

    private void tekvericek(final List<ParseObject> objects) {
        helper.clear();
        for (int i = 0; objects.size() > i; i++) {
            final urun_helper oneitem = new urun_helper();
            oneitem.setAdet(0);
            if (eskisiparis_arr.size()>0){
                for (dict a:eskisiparis_arr){
                    Log.d("idDDDDDDD",objects.get(i).getObjectId());
                    if (a.getKey().matches(objects.get(i).getObjectId())){

                        oneitem.setAdet(Integer.parseInt(a.getValue()));
                    }
                }
            }
            //Resim CEk
            ParseFile postImage = objects.get(i).getParseFile("img");
            String imageUrl = postImage.getUrl();//live url
            Uri imageUri = Uri.parse(imageUrl);


            oneitem.setAciklama(objects.get(i).get("info").toString());
            oneitem.setSatici(gelen_sayfa_name);
            oneitem.setResim_url(imageUri.toString());
            oneitem.setFiyat(objects.get(i).get("fiyat").toString());
            oneitem.setCesit(objects.get(i).getString("cesit"));
            oneitem.setIsim(objects.get(i).get("urun_ismi").toString());
            oneitem.setUrun_id(objects.get(i).getObjectId());

            helper.add(oneitem);


        }
        datayedekle();

        recycview(helper);


    }



    ArrayList<urun_helper> yedek=new ArrayList<>();

    private void datayedekle() {
            for (int i=0; helper.size()>i;i++){
                urun_helper helper1= new urun_helper();
                helper1.setAdet(helper.get(i).getAdet());
                helper1.setUrun_id(helper.get(i).getUrun_id());
                helper1.setAciklama(helper.get(i).getAciklama());
                helper1.setCesit(helper.get(i).getCesit());
                helper1.setResim_url(helper.get(i).getResim_url());
                helper1.setFiyat(helper.get(i).getFiyat());
                helper1.setGram(helper.get(i).getGram());
                helper1.setIsim(helper.get(i).getIsim());
                helper1.setSatici(helper.get(i).getSatici());
                yedek.add(helper1);
            }

    }


    private void gelenveriler() {
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {

            gelen_sayfa_name = (String) b.get("satici_name");
            gelen_sayfa_logo = (String) b.get("satici_logo");

        }


    }

    private void idupdate() {
        recyclerView = findViewById(R.id.urunlerrecyclerview);

        satici_adi = findViewById(R.id.sirket_bilgi_adi);
        satici_info = findViewById(R.id.sirket_bilgi_aciklama);
        satici_logo = findViewById(R.id.sirket_bilgi_logo);
        satici_telno = findViewById(R.id.sirket_no);
    }

    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(getResources().getColor(R.color.beyaz));

        setSupportActionBar(toolbar);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void collops() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
        collapsingToolbarLayout.setScrimAnimationDuration(750);
        collapsingToolbarLayout.setTitle("dnemesirket");
    }


    private void recycview(ArrayList<urun_helper> helper) {

        adapter = new urun_adapter(this, urun_helper.getdata(helper), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        toolbar.setTitle("Urunler");

        //searh action
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // This is your adapter that will be filtered

                helper.clear();
                for (int i=0;yedek.size()>i;i++){
                    if (yedek.get(i).getIsim().toLowerCase().contains(newText.toLowerCase())){

                        urun_helper helper1=new urun_helper();
                        helper1.setAdet(yedek.get(i).getAdet());
                        helper1.setUrun_id(yedek.get(i).getUrun_id());
                        helper1.setAciklama(yedek.get(i).getAciklama());
                        helper1.setCesit(yedek.get(i).getCesit());
                        helper1.setResim_url(yedek.get(i).getResim_url());
                        helper1.setFiyat(yedek.get(i).getFiyat());
                        helper1.setGram(yedek.get(i).getGram());
                        helper1.setIsim(yedek.get(i).getIsim());
                        helper1.setSatici(yedek.get(i).getSatici());
                        helper.add(helper1);

                    }
                    else {

                    }
                }
                recycview(helper);

                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                // **Here you can get the value "query" which is entered in the search box.**
                Toast.makeText(Urunler.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);


        //sepetustu

        last_siparis_varmi();
        RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.sepet_menu).getActionView();
        tv = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);

        tv.setText(adet1);
        //yaziyi gizler
        tv.setVisibility(View.GONE);
        //yaziyi goster
        tv.setVisibility(View.VISIBLE);

        menu.findItem(R.id.sepet_menu).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Urunler.this, Siparis_sayfasi.class);
                i.putExtra("sayfa_name",gelen_sayfa_name);
                startActivity(i);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    //yukarda adet yazmak icin sayi cekiyorum
    private void last_siparis_varmi() {
        final int[] adet = {0};
        ParseUser user = ParseUser.getCurrentUser();
        // Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("siparis_sepeti");
        query.whereEqualTo("urun_sahibi", gelen_sayfa_name);
        query.whereEqualTo("kullanici_id", user.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0) {
                    for (int adet_sayi = 0; adet_sayi < objects.size(); adet_sayi++) {
                        try {
                            eski_siparis_cek(objects.get(adet_sayi).getRelation("siparis_ayrinti").getQuery());
                            adet[0] += Integer.parseInt(objects.get(adet_sayi).get("adet").toString());

                        } catch (Exception e1) {

                            adet1 = "0";
                            tv.setText(adet1);
                        }

                        adet1 = String.valueOf(adet[0]);
                        tv.setText(adet1);
                    }
                }
                else if (objects.size()==0)
                    databasevericek();
            }
        });



    }

    ArrayList<dict> eskisiparis_arr=new ArrayList<>();

    private void eski_siparis_cek(ParseQuery<ParseObject> siparis_ayrinti) {
        siparis_ayrinti.whereEqualTo("durum",true);
            siparis_ayrinti.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size()>0){
                        for (int i=0;i<objects.size();i++){
                            dict dict=new dict();
                            dict.setKey(objects.get(i).getString("urun_id"));
                            dict.setValue(objects.get(i).getString("adet"));
                            eskisiparis_arr.add(dict);
                        }


                    } databasevericek();
                }
            });
    }


    TextView tv;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter_menu:
                alertfilter();
                break;

        }
        return true;
    }

    private void alertfilter() {

            final Dialog dialog = new Dialog(Urunler.this);
            dialog.setContentView(R.layout.filter_cesit);
            Button filtre, iptal;
            LinearLayout linearLayout=dialog.findViewById(R.id.checkbox);
        getcesitler(linearLayout);

        iptal = dialog.findViewById(R.id.cesit_iptal_btn);
        filtre = dialog.findViewById(R.id.filtre_cesit_btn);
        dialog.getWindow().setLayout(1000, 1500);
        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    helper.clear();
                    for (int x=0;arrdict.size()>x;x++){
                    for (int i=0;yedek.size()>i;i++){
                        if (yedek.get(i).getCesit().matches(arrdict.get(x).getValue())){

                            urun_helper helper1=new urun_helper();
                            helper1.setCesit(yedek.get(i).getCesit());
                            helper1.setAdet(yedek.get(i).getAdet());
                            helper1.setUrun_id(yedek.get(i).getUrun_id());
                            helper1.setAciklama(yedek.get(i).getAciklama());
                            helper1.setResim_url(yedek.get(i).getResim_url());
                            helper1.setFiyat(yedek.get(i).getFiyat());
                            helper1.setGram(yedek.get(i).getGram());
                            helper1.setIsim(yedek.get(i).getIsim());
                            helper1.setSatici(yedek.get(i).getSatici());
                            helper.add(helper1);

                        }
                        else {

                        }
                    }
                    }
                    recycview(helper);
                    dialog.cancel();
                }


        });

        dialog.show();

    }

    private void getcesitler(final LinearLayout linearLayout) {
    ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Sirketler");
    query.whereEqualTo("sirket_Adi",gelen_sayfa_name);
    query.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            tekcesitcek(objects.get(0).getRelation("cesit").getQuery(),linearLayout);
        }
    });


    }


    ArrayList<dict> arrdict =new ArrayList<>();
    private void tekcesitcek(final ParseQuery<ParseObject> cesit, final LinearLayout linearLayout) {
    cesit.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0){
                    for (int i=0;objects.size()>i;i++){
                            CheckBox checkBox = new CheckBox(Urunler.this);
                            checkBox.setText(objects.get(i).getString("cesit_ismi"));
                            if(arrdict.size()>0){
                                for (int x=0; arrdict.size()> x; x++){
                                    if (checkBox.getText().toString().matches(arrdict.get(x).getValue())){
                                        checkBox.setChecked(true);
                                    }
                                }
                            }
                            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked){
                                        dict dict=new dict();
                                        dict.setValue(buttonView.getText().toString());
                                        dict.setKey("1");
                                        arrdict.add(dict);
                                    }
                                    else{
                                        for (int i=0;i<arrdict.size();i++){
                                            if (arrdict.get(i).getValue().matches(buttonView.getText().toString())){
                                                arrdict.remove(i);
                                            }
                                        }
                                    }
                                }
                            });

                        linearLayout.addView(checkBox);



                    }

                }
        }
    });
    }


    @Override
    protected void onStop() {
        super.onStop();
        urun_adapter.kaydet();

    }


    //sepettekki sayi artis yeri
    @Override
    public void adet(int adet) {
        adet += Integer.parseInt(adet1);
        tv.setText(String.valueOf(adet));


    }
}