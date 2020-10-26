package com.example.pazaryericontrolpanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Giris extends AppCompatActivity {

    EditText kullanici_adi, sifre;
    Button giris_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris);
        id();
        database();
        giris_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(kullanici_adi.getText().toString(), sifre.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            if (user.getBoolean("satici")) {
                                Intent i =new Intent(Giris.this,Anasayfa.class);
                                startActivity(i);
                            } else {
                                ParseUser.logOut();
                                Toast.makeText(Giris.this, "Satici Kimliginiz Yok!", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(Giris.this, "Sifre veya Kullanici adi Yanlis!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void id() {
        giris_btn = findViewById(R.id.grsbtn);
        kullanici_adi = findViewById(R.id.kuladigiris);
        sifre = findViewById(R.id.sifregiris);
    }

    private void database() {


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ParseUser.getCurrentUser() != null){
            Intent i =new Intent(Giris.this,Anasayfa.class);
            startActivity(i);
        }
    }
}
