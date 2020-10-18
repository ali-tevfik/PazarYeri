    package com.example.pazaryeri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

    public class animasyon_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animasyon_page);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.girisanim);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.text_animasyon);
        ImageView img=findViewById(R.id.img_animasyon);
        img.clearAnimation();
        tv.clearAnimation();
        tv.startAnimation(a);
        img.startAnimation(a);
        Thread background=new Thread(){
            @Override
            public void run() {
                try {

                    sleep(2*1000);
                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Anasayfa.class);
                    startActivity(i);

                    //Remove activity
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(animasyon_page.this, "Program coktu tekrar deneyin!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        background.start();

    }
}