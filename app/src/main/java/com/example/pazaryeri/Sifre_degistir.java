package com.example.pazaryeri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class Sifre_degistir extends AppCompatActivity {
EditText eski,yeni1,yeni2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);
        id();

    }
    Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.memu_profil_kaydet:
                kaydet();

        }
        return  true;
    }

    private void kaydet() {
        ParseUser.requestPasswordResetInBackground(ParseUser.getCurrentUser().getEmail(), new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    if (yeni1.getText().toString().matches(yeni2.getText().toString())){
                        if (yeni1.getText().length()>7){
                        ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), eski.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null){
                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    currentUser.setPassword(yeni2.getText().toString());
                                    currentUser.saveInBackground();
                                    Toast.makeText(Sifre_degistir.this, "Sifre Basarili Bir Sekilde Degistirildi!", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(Sifre_degistir.this,Anasayfa.class);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(Sifre_degistir.this, "Eski Sifre Yanlis", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                        else
                            Toast.makeText(Sifre_degistir.this, "Sifrenizin uzunlugu az!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(Sifre_degistir.this, "Sifreler uyusmuyor!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Sifre_degistir.this, "Sifre Degistirme Basarisiz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_menu, menu);
        toolbar.setTitle("Sifre Degistir");

        return true;
    }

    private void id() {

         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        eski=findViewById(R.id.sifredegistir_eskisifre);
        yeni1=findViewById(R.id.sifredegistir_yenisifre1);
        yeni2=findViewById(R.id.sifredegistir_yenisifre2);
    }
}
