package com.example.pazaryeri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class Kisisel_Bilgi extends AppCompatActivity {


    TextView isim, soyisim, postcode, evnumber, kat, mail, tel, sehir, kisisel_adres;
    Button sifre_degisti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisisel__bilgi);
        idupdate();
        getdata();
        sifre_degisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Kisisel_Bilgi.this,Sifre_degistir.class);
                startActivity(i);
            }
        });
    }


    private void getdata() {
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("users");
        query.whereEqualTo("kisi_objectId",ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0){
                    ParseQuery<ParseObject> getdetail=objects.get(0).getRelation("User_details").getQuery();
                    ParseQuery<ParseObject> getadress=objects.get(0).getRelation("User_adress").getQuery();

                    userdetailget(getdetail);
                    useradressget(getadress);
                }
            }
        });

    }

    private void useradressget(ParseQuery<ParseObject> getadress) {
        getadress.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size()>0){
                    postcode.setText(objects.get(0).getString("Postcode"));
                    evnumber.setText(objects.get(0).getString("huis_number"));

                    kat.setText(objects.get(0).getString("kat"));
                    kisisel_adres.setText(objects.get(0).getString("adres"));
                    sehir.setText(objects.get(0).getString("Sehir"));
                }
            }
        });
    }

    private void userdetailget(ParseQuery<ParseObject> getdetail) {
        getdetail.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0){

                    isim.setText(objects.get(0).getString("isim"));
                    mail.setText(objects.get(0).getString("Mail"));
                    soyisim.setText(objects.get(0).getString("soyisim"));
                    tel.setText(objects.get(0).getString("Telefon_number"));
                }
            }
        });
    }

    Toolbar toolbar;

    private void idupdate() {
        sifre_degisti=findViewById(R.id.kisisel_bilgi_sifre);
        isim = findViewById(R.id.kisisel_bilgi_isim);
        kisisel_adres = findViewById(R.id.kisisel_adres);
        soyisim = findViewById(R.id.kisisel_bilgi_soyad);
        postcode = findViewById(R.id.kisisel_bilgi_postcode);
        kat = findViewById(R.id.kisisel_bilgi_kat);
        evnumber = findViewById(R.id.kisisel_bilgi_evnumarasi);
        mail = findViewById(R.id.kisisel_bilgi_mail);
        tel = findViewById(R.id.kisisel_bilgi_telno);
        sehir = findViewById(R.id.kisisel_sehir);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mail.setEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent i = new Intent(Kisisel_Bilgi.this, Anasayfa.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_menu, menu);
        toolbar.setTitle("Profilim");
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
                databasesave();
                Intent i = new Intent(Kisisel_Bilgi.this, Anasayfa.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void databasesave() {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("users");
        query.whereEqualTo("kisi_objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                adresschange(objects);
                detailchange(objects);
            }
        });


    }

    private void detailchange(List<ParseObject> objects) {
        ParseQuery<ParseObject> relation = objects.get(0).getRelation("User_details").getQuery();
        relation.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : objects) {
                        a.put("isim", isim.getText().toString());
                        a.put("soyisim", soyisim.getText().toString());
                        a.put("Telefon_number", tel.getText().toString());
                        a.saveInBackground();
                    }
                }
            }
        });
    }

    private void adresschange(List<ParseObject> objects) {
        ParseQuery<ParseObject> relation = objects.get(0).getRelation("User_adress").getQuery();
        relation.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : objects) {
                        a.put("Bolge","");
                        a.put("Postcode",postcode.getText().toString());
                        a.put("huis_number",evnumber.getText().toString());
                        a.put("kat",kat.getText().toString());
                        a.put("Sehir",sehir.getText().toString());
                        a.put("adress",kisisel_adres.getText().toString());
                        a.saveInBackground();
                    }
                }
            }
        });
    }
}