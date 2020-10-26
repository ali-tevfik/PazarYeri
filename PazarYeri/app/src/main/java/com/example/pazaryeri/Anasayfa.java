package com.example.pazaryeri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.pazaryeri.adapter.katagoriadapter;
import com.example.pazaryeri.drawer.MenuListFragment;
import com.example.pazaryeri.helper.dict;
import com.example.pazaryeri.helper.katagori_helper;


import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleMusicDicesDrawable;
import com.jpardogo.android.googleprogressbar.library.NexusRotationCrossDrawable;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Anasayfa extends AppCompatActivity {

    private static final int FOLDING_CIRCLES = 0;
    private static final int MUSIC_DICES = 1;
    private static final int NEXUS_CROSS_ROTATION = 2;
    private static final int CHROME_FLOATING_CIRCLES = 3;
    katagoriadapter adapter;
    Toolbar toolbar;
    ArrayList<katagori_helper> helper = new ArrayList<>();
    RecyclerView recyclerView;
    FlowingDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        idupdate();

        recyclerView = findViewById(R.id.katagorirecyc);
        databasevericek();
        menu();
        setupmenu();

    }



    private void setupmenu() {
            FragmentManager fm = getSupportFragmentManager();
            MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
            if (mMenuFragment == null) {
                mMenuFragment = new MenuListFragment();
                fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();

    }}

    private void menu() {
        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }


    private void databasevericek() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );


        // Configure Query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sirketler");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    // Adding objects into the Array
                       tekvericek(objects);


            }

        }});

    }
    ArrayList<dict> arrdict=new ArrayList<>();

    private void tekvericek(List<ParseObject> objects) {
        for (int i =0;objects.size()>i;i++){
            //Resim CEk
            ParseFile postImage = objects.get(i).getParseFile("logo");
            String imageUrl = postImage.getUrl() ;//live url
            Uri imageUri = Uri.parse(imageUrl);

                dict dict=new dict();
                dict.setValue(String.valueOf(i));
                dict.setKey(objects.get(i).get("sirket_Adi").toString().toLowerCase());
                arrdict.add(dict);
                katagori_helper oneitem = new katagori_helper();
                oneitem.setKatogariname(objects.get(i).get("sirket_Adi").toString());
                oneitem.setKatagoricesitler("");
                oneitem.setKatogoriimgurl(imageUrl.toString());
                oneitem.setPuan(Integer.parseInt(objects.get(i).get("puan").toString()));
                helper.add(oneitem);


        }
        datayedekle();
        recycview(helper);







    }



    private void idupdate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }



    private void recycview(ArrayList<katagori_helper> helper) {

        adapter = new katagoriadapter(Anasayfa.this, katagori_helper.getdata(helper));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(Anasayfa.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        toolbar.setTitle("Saticilar");


        //searh action
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {

                // This is your adapter that will be filtered
                helper.clear();
                for (int i=0;yedek.size()>i;i++){
                    if (yedek.get(i).getKatogariname().toLowerCase().contains(newText.toLowerCase())){

                        katagori_helper helper1=new katagori_helper();
                        helper1.setPuan(yedek.get(i).getPuan());
                        helper1.setKatagoricesitler(yedek.get(i).getKatagoricesitler());
                        helper1.setKatogariname(yedek.get(i).getKatogariname());
                        helper1.setKatogoriimgurl(yedek.get(i).getKatogoriimgurl());
                        helper.add(helper1);

                    }
                    else {

                    }
                }
                        recycview(helper);



                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                 return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);






        return super.onCreateOptionsMenu(menu);
    }

    ArrayList<katagori_helper> yedek=new ArrayList<>();
    private void datayedekle() {
        for (int i=0; helper.size()>i;i++){
            katagori_helper helper1=new katagori_helper();
            helper1.setPuan(helper.get(i).getPuan());
            helper1.setKatagoricesitler(helper.get(i).getKatagoricesitler());
            helper1.setKatogariname(helper.get(i).getKatogariname());
            helper1.setKatogoriimgurl(helper.get(i).getKatogoriimgurl());
            yedek.add(helper1);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.sepet_menu).setVisible(false);
        menu.findItem(R.id.filter_menu).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id. filter_menu:
                alertfilter();
               break;




        }

        return true;
    }

    private void alertfilter() {
        final Dialog dialog = new Dialog(Anasayfa.this);
        dialog.setContentView(R.layout.filter_cesit);
        CheckBox peynir,sut,et_tavuk,baharat,cukulata,icecek;
        Button filtre,iptal;
        iptal=dialog.findViewById(R.id.cesit_iptal_btn);
        filtre=dialog.findViewById(R.id.filtre_cesit_btn);
        dialog.getWindow().setLayout(1000,1500);
        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }


}