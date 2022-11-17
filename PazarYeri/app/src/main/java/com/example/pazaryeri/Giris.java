package com.example.pazaryeri;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pazaryeri.tanitim.first;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Giris extends AppCompatActivity {

TextView yenikayit,mail,sifre;
Button girisbtn;
Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris);
        tanitim();
        idupdate();


        yenikayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Giris.this,Kayit_ol.class);
                startActivity(i);
            }
        });



        girisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(Giris.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.diolog);

                TextView textVie=(TextView)dialog.findViewById(R.id.dialog_txt);
                textVie.setText("Bilgileriniz kontrol ediliyor..");
                dialog.show();
                ParseUser.logInInBackground(mail.getText().toString(),sifre.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (parseUser != null) {
                            if (parseUser.getBoolean("emailVerified") && !parseUser.getBoolean("satici")){
                                dialog.cancel();
                            Intent i =new Intent(Giris.this,Anasayfa.class);
                            startActivity(i);
                            }
                            else {
                                dialog.cancel();
                                Toast.makeText(Giris.this, "Please verify your Email!!", Toast.LENGTH_SHORT).show();
                                ParseUser.logOut();
                            }
                        } else {
                            dialog.cancel();
                            ParseUser.logOut();
                            Toast.makeText(Giris.this, "Hatali Giris!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });



    }

    private void tanitim() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        if(prefManager.isFirstTimeLaunch()){
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(Giris.this, first.class));
            finish();
        }
    }


    private void idupdate() {
        yenikayit=findViewById(R.id.giris_kaydol);
        mail=findViewById(R.id.kuladigiris);
        sifre=findViewById(R.id.sifregiris);
        girisbtn=findViewById(R.id.grsbtn);


    }
}
