package com.example.pazaryericontrolpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.example.pazaryericontrolpanel.fragment.Liste_fragment;
import com.example.pazaryericontrolpanel.fragment.My_profil_fragment;
import com.example.pazaryericontrolpanel.fragment.urun_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.Parse;
import com.parse.ParseUser;

public class Anasayfa extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        bottomNavigationView = findViewById(R.id.navigation);
         idupdate();
        database();
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Liste_fragment()).commit();



    }
    Toolbar toolbar;

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

        // Find the toolbar view inside the activity layout
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        toolbar.setTitle("Siparisler");
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.siparis_liste_menu:
                    selectedFragment = new Liste_fragment();
                    break;
                case R.id.urunler_menu:
                    selectedFragment = new urun_fragment();
                    break;
                case  R.id.profil_menu:
                    selectedFragment = new My_profil_fragment();
                    break;

            }

            if(selectedFragment != null)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (ParseUser.getCurrentUser()==null){
            ParseUser.logOut();
            Intent i =new Intent(Anasayfa.this,Giris.class);
            startActivity(i);
        }
    }
}
