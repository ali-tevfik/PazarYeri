package com.example.pazaryeri;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris);

        database();
        UserControl();
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
                ParseUser.logInInBackground(mail.getText().toString(),sifre.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (parseUser != null) {

                            Intent i =new Intent(Giris.this,Anasayfa.class);
                            startActivity(i);
                        } else {
                            ParseUser.logOut();
                            Toast.makeText(Giris.this, "Hatali Giris!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });



    }

    private void UserControl() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i=new Intent(this,animasyon_page.class);
            startActivity(i);
            // do stuff with the user
        } else {
            // show the signup or login screen
        }
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

    private void idupdate() {
        yenikayit=findViewById(R.id.giris_kaydol);
        mail=findViewById(R.id.kuladigiris);
        sifre=findViewById(R.id.sifregiris);
        girisbtn=findViewById(R.id.grsbtn);


    }
}
