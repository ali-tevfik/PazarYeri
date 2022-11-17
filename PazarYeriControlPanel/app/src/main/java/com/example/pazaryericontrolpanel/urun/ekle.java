package com.example.pazaryericontrolpanel.urun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pazaryericontrolpanel.Anasayfa;
import com.example.pazaryericontrolpanel.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ekle extends AppCompatActivity {

    EditText name, cesit, fiyat, info;
    ImageView img;
    byte[] image = null;
    boolean add_mi = false;
    Button kaydet, resimsec;
    ArrayList<ParseObject> arr = new ArrayList<>();
    String gelenObjectId;
    Dialog progressBar;
    boolean gelen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekle);
        idler();
        showdialog("Even Wachten");

        gelenyer();
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checktext()) {
                    progressBar.show();
                    ParseQuery<ParseObject> anaobject = new ParseQuery<ParseObject>("Sirketler");
                    anaobject.whereEqualTo("ids", ParseUser.getCurrentUser().getObjectId());
                    Log.d("asd", ParseUser.getCurrentUser().getObjectId());
                    anaobject.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(final List<ParseObject> objects, ParseException e) {
                            if (e == null && objects.size() > 0) {
                                if (kaydet.getText().toString().matches("Kaydet")){
                                    kaydet_data(objects);
                                }
                                else {
                                    update_data(objects);
                                }
                            }
                        }
                    });
                }
            }
        });


        resimsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);

            }
        });

    }

    private void update_data(List<ParseObject> objects) {
        ParseQuery<ParseObject>  parseQuery=objects.get(0).getRelation("urunler").getQuery();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> insdeobject, ParseException e) {
                for (ParseObject a:insdeobject){
                    if (a.getObjectId().matches(gelenObjectId)){
                        a.put("urun_ismi",name.getText().toString());
                        a.put("fiyat",Integer.parseInt(fiyat.getText().toString()));
                        a.put("info",info.getText().toString());
                        if (image != null)
                        a.put("img",add_image());
                        a.put("cesit",cesit.getText().toString());
                        a.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e== null)
                                    git("Urun Guncellendir!");
                                else
                                    Toast.makeText(ekle.this, "HAta olustu!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }

    private void kaydet_data(final List<ParseObject> objects) {

        Toast.makeText(this, "BURDAA", Toast.LENGTH_SHORT).show();
        final ParseRelation relation = objects.get(0).getRelation("urunler");
        final ParseQuery<ParseObject> relationquery = objects.get(0).getRelation("urunler").getQuery();
        relationquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> insdeobject, ParseException e) {
                if (e == null) {

                    ParseObject addnew = new ParseObject("urunler");
                    addnew.put("urun_ismi", name.getText().toString());
                    addnew.put("fiyat", Integer.parseInt(fiyat.getText().toString()));
                    addnew.put("satici", gelen_satici_name);
                    addnew.put("info", info.getText().toString());
                    addnew.put("puan", 0);
                    addnew.put("puan_kisi_Sayisi", 0);
                    addnew.put("img", add_image());
                    addnew.put("cesit", cesit.getText().toString());
                    addnew.saveInBackground();
                    relation.add(addnew);
                    arr.add(addnew);
                    objects.get(0).put("urun_array", arr);
                    objects.get(0).saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                git("Urun Kaydedildi!");
                            }
                        }
                    });

                }
            }

        });
    }

    private void git(String s) {
        progressBar.cancel();
        Toast.makeText(ekle.this, s, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ekle.this, Anasayfa.class);
        startActivity(i);
    }

    private boolean checktext() {
        if ((image!=null || gelen) && !name.getText().toString().isEmpty() && !info.getText().toString().isEmpty() && !fiyat.getText().toString().isEmpty() && !cesit.getText().toString().isEmpty())
            return true;


        else {
            Toast.makeText(this, "Bos Alan Birakmayiniz!!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private ParseFile add_image() {
        ParseFile file = new ParseFile("img", image);
        file.saveInBackground();
        return file;
    }

    String gelen_satici_name;

    private void gelenyer() {
        gelen_satici_name = getIntent().getStringExtra("satici_name");
        try {
            if (getIntent().getStringExtra("gelen").matches("add")) {
                kaydet.setText("Kaydet");
                add_mi = true;
                resimsec.setText("Resim sec");
                progressBar.cancel();
            } else {
                kaydet.setText("Guncelle");
                resimsec.setText("Resmi Guncelle");
                gelenObjectId=getIntent().getStringExtra("object_id");
                gelen=true;
                name.setText(getIntent().getStringExtra("isim"));
                fiyat.setText(getIntent().getStringExtra("fiyat").toString());
                info.setText(getIntent().getStringExtra("info"));
                cesit.setText(getIntent().getStringExtra("cesit"));
                Picasso.get().load(getIntent().getStringExtra("resimurl")).into(img);
                progressBar.cancel();
            }

        } catch (Exception e) {
            Toast.makeText(this, "hata olustu", Toast.LENGTH_SHORT).show();
        }


    }
    private void showdialog(String txt) {
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setContentView(R.layout.diolog);

        TextView textVie=(TextView)progressBar.findViewById(R.id.dialog_txt);
        textVie.setText(txt);
        progressBar.show();}


    private void idler() {
        progressBar = new Dialog(this);
        name = findViewById(R.id.urun_ekle_name);
        fiyat = findViewById(R.id.urun_ekle_fiyat);
        info = findViewById(R.id.urun_ekle_info);
        cesit = findViewById(R.id.urun_ekle_cesit);
        img = findViewById(R.id.imgurun);
        kaydet = findViewById(R.id.urun_kaydet);
        resimsec = findViewById(R.id.resimekle);
    }

    Uri uri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            uri = data.getData();
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                byte[] inputData = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set ImageView image as a Bitmap


        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        image = byteBuffer.toByteArray();
        return byteBuffer.toByteArray();
    }
}
