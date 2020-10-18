package com.example.pazaryeri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class Kayit_ol extends AppCompatActivity {
    Button kytbtn;
    TextView mail, sifre, isim, soyisim, postcode, evnumber, kat, sehir, tel, againsifre, kisisel_adres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        idupdate();

        kytbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser user = new ParseUser();
// Set the user's username and password, which can be obtained by a forms
                user.setUsername(mail.getText().toString());
                user.setEmail(mail.getText().toString());
                if (sifre.getText().toString().matches(againsifre.getText().toString()))
                    user.setPassword(againsifre.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseObject users = new ParseObject("User");

                            Toast.makeText(Kayit_ol.this, "basarili", Toast.LENGTH_SHORT).show();
                            ParseObject a = new ParseObject("users");
                            a.put("kisi_objectId", ParseUser.getCurrentUser().getObjectId());
                            a.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    kisiselbilgi(user.getObjectId());
                                }
                            });
                        } else {
                            Log.d("giris basarisiz", e.getMessage());
                            ParseUser.logOut();
                            Toast.makeText(Kayit_ol.this, "basarisiz", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }


        });
    }

    private void kisiselbilgi(final String objectId) {
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("users");
        query.whereEqualTo("kisi_objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    final ArrayList<ParseObject> array = new ArrayList<>();
                    final ParseRelation<ParseObject> relation = objects.get(0).getRelation("User_details");
                    final ParseQuery<ParseObject> queryrelation = objects.get(0).getRelation("User_details").getQuery();
                    queryrelation.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> insedeobject, ParseException e) {
                            if (e == null) {
                                ParseObject reminderList = new ParseObject("User_details");
                                reminderList.put("isim", isim.getText().toString());
                                reminderList.put("soyisim", soyisim.getText().toString());
                                reminderList.put("Telefon_number", tel.getText().toString());
                                reminderList.put("Mail", mail.getText().toString());
                                reminderList.saveInBackground();
                                relation.add(reminderList);
                                array.add(reminderList);
                                objects.get(0).put("arr_details", array);
                                objects.get(0).saveInBackground();
                                adreskaydet(objectId);


                            }

                        }
                    });
                }
            }
        });


    }

    private void adreskaydet(String objectId) {

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("users");
        query.whereEqualTo("kisi_objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {

                    final ArrayList<ParseObject> array = new ArrayList<>();
                    final ParseRelation<ParseObject> relation = objects.get(0).getRelation("User_adress");
                    final ParseQuery<ParseObject> queryrelation = objects.get(0).getRelation("User_adress").getQuery();
                    queryrelation.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> insadeobject, ParseException e) {
                            if (e == null) {
                                ParseObject reminderList = new ParseObject("User_adress");
                                reminderList.put("Mail", mail.getText().toString());
                                reminderList.put("Postcode", postcode.getText().toString());
                                reminderList.put("huis_number", evnumber.getText().toString());
                                reminderList.put("kat", kat.getText().toString());
                                reminderList.put("Sehir", sehir.getText().toString());
                                reminderList.put("adres", kisisel_adres.getText().toString());
                                reminderList.saveInBackground();
                                relation.add(reminderList);
                                array.add(reminderList);
                                objects.get(0).put("arr_adress", array);
                                objects.get(0).saveInBackground();
                                Intent i = new Intent(Kayit_ol.this, Anasayfa.class);
                                startActivity(i);

                            }

                        }
                    });
                }
            }
        });

    }

    private void idupdate() {
        kisisel_adres = findViewById(R.id.kisisel_adres);
        isim = findViewById(R.id.kisisel_bilgi_isim);
        mail = findViewById(R.id.kisisel_bilgi_mail);
        sifre = findViewById(R.id.kayitol_sifre);
        againsifre = findViewById(R.id.kayitol_againsifre);
        soyisim = findViewById(R.id.kisisel_bilgi_soyad);
        postcode = findViewById(R.id.kisisel_bilgi_postcode);
        evnumber = findViewById(R.id.kisisel_bilgi_evnumarasi);
        kat = findViewById(R.id.kisisel_bilgi_kat);
        sehir = findViewById(R.id.kisisel_sehir);
        tel = findViewById(R.id.kisisel_bilgi_telno);
        kytbtn = findViewById(R.id.yenikyt_btn);
    }
}
