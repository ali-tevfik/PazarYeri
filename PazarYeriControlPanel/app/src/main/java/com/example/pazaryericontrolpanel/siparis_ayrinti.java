package com.example.pazaryericontrolpanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pazaryericontrolpanel.adapter.siparis_ayrinti_adapter;
import com.example.pazaryericontrolpanel.adapter.urun_adapter;
import com.example.pazaryericontrolpanel.helper.siparis_ayrinti_helper;
import com.example.pazaryericontrolpanel.helper.urun_helper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class siparis_ayrinti extends AppCompatActivity {
    ArrayList<siparis_ayrinti_helper> helper = new ArrayList<>();
    RecyclerView recyclerView;
    siparis_ayrinti_adapter adapter;
    String getobjectid, adet;
    Button order_complete;
    TextView name, land, telefon, adress;
    Boolean order;
    Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siparis_ayrinti);
        idupdate();
        showdialog("Even Wachten");

        gelendata();
        getdata();
        order_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis");
                query.whereEqualTo("objectId", getobjectid);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0 )
                        {
                            for (ParseObject a:objects){
                                a.put("durum", true);
                                a.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null)
                                            Toast.makeText(siparis_ayrinti.this, "Order completed", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(siparis_ayrinti.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            
                        }
                    }
                });
                Intent i = new Intent(siparis_ayrinti.this, Anasayfa.class);
                startActivity(i);
            }
        });

        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?z=10&q=" + adress.getText());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        telefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:"+telefon.getText().toString()));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(siparis_ayrinti.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(siparis_ayrinti.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                }else {     //have got permission
                    try{
                        startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void getdata() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis");
        query.whereEqualTo("objectId", getobjectid);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject a : objects) {
                        order = false;
                        if (a.getBoolean("durum") == true) {
                            order_complete.setVisibility(View.GONE);
                            order = a.getBoolean("durum");
                        }
                        getuserdetails(a.getString("kullanici_id"));
                        getsiparisayrinti(a);

                    }
                }
            }
        });
    }
int kontrol_adet=0;
    boolean naber=false;
    private void getsiparisayrinti(ParseObject a) {
        ParseQuery<ParseObject> getralation = a.getRelation("siparis_ayrinti").getQuery();
        getralation.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject a : objects) {
                        kontrol_adet++;
                        if (kontrol_adet==objects.size())
                            naber=true;
                        geturunbilgi(a.getString("urun_id"), a.getString("adet"),naber);
                    }



                }

            }
        });
    }

    private void geturunbilgi(String urun_id, final String adet, final boolean naber) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("urunler");
        query.whereEqualTo("objectId", urun_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject a : objects) {
                        siparis_ayrinti_helper oneitem = new siparis_ayrinti_helper();
                        oneitem.setAdet(adet);
                        oneitem.setOrder_status(order);
                        oneitem.setFiyat(String.valueOf(Integer.parseInt(a.get("fiyat").toString()) *Integer.parseInt(adet)));
                        oneitem.setUrunname(a.getString("urun_ismi"));
                        helper.add(oneitem);
                        if (naber)
                            recycview(helper);
                    }
                }
                progressBar.cancel();
            }
        });
    }

    private void getuserdetails(String a) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("users");
        query.whereEqualTo("kisi_objectId", a);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject a : objects) {
                        ParseQuery<ParseObject> getralation = a.getRelation("User_details").getQuery();
                        getralation.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> insadeobjects, ParseException e) {
                                if (e == null && insadeobjects.size() > 0) {
                                    for (ParseObject aa : insadeobjects) {
                                        name.setText(aa.getString("isim") + " " + aa.getString("soyisim"));
                                        telefon.setText(aa.getString("Telefon_number"));
                                    }
                                }
                            }
                        });

                        ParseQuery<ParseObject> getrelation2 = a.getRelation("User_adress").getQuery();
                        getrelation2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null && objects.size() > 0) {
                                    for (ParseObject aa : objects) {
                                        adress.setText(aa.getString("adress") + " " + aa.getString("huis_number") + " " + aa.getString("kat") + "  " + aa.getString("Postcode"));
                                        land.setText(aa.getString("Sehir"));
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });

    }

    private void gelendata() {
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {

            getobjectid = (String) b.get("objectid");

        }
    }

    private void idupdate() {
        order = false;
        progressBar = new Dialog(this);
        order_complete = findViewById(R.id.eskilist_ayrinti_ok_btn);
        recyclerView = findViewById(R.id.siparis_ayrinti_recyc);
        name = findViewById(R.id.eskilist_ayrinti_isim);
        adress = findViewById(R.id.eskilist_ayrinti_adress);
        land = findViewById(R.id.eskilist_ayrinti_land);
        telefon = findViewById(R.id.eskilist_ayrinti_telefon);

    }
    private void showdialog(String txt) {
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setContentView(R.layout.diolog);

        TextView textVie=(TextView)progressBar.findViewById(R.id.dialog_txt);
        textVie.setText(txt);
        progressBar.show();}

    private void recycview(ArrayList<siparis_ayrinti_helper> helper) {
        adapter = new siparis_ayrinti_adapter(this, siparis_ayrinti_helper.getdata(helper));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
