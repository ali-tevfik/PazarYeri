    package com.example.pazaryeri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

    public class animasyon_page extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_animasyon_page);
            database();
            Animation a = AnimationUtils.loadAnimation(this, R.anim.girisanim);
            a.reset();
            TextView tv = (TextView) findViewById(R.id.text_animasyon);
            ImageView img = findViewById(R.id.img_animasyon);
            img.clearAnimation();
            tv.clearAnimation();
            tv.startAnimation(a);
            img.startAnimation(a);
            Thread background = new Thread() {
                @Override
                public void run() {
                    try {

                        sleep(2 * 1000);

                        if (ParseUser.getCurrentUser() != null) {
                            // After 5 seconds redirect to another intent
                            Intent i = new Intent(getBaseContext(), Anasayfa.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getBaseContext(), Giris.class);
                            startActivity(i);
                        }
                        //Remove activity
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(animasyon_page.this, "Program coktu tekrar deneyin!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            background.start();

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
    }