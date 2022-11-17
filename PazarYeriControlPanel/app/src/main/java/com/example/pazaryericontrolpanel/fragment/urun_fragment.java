package com.example.pazaryericontrolpanel.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryericontrolpanel.R;
import com.example.pazaryericontrolpanel.adapter.urun_adapter;
import com.example.pazaryericontrolpanel.helper.urun_helper;
import com.example.pazaryericontrolpanel.urun.ekle;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class urun_fragment extends Fragment {
    urun_adapter adapter;
    Button yeniurunbtn;
    ArrayList<urun_helper> helper = new ArrayList<>();
    RecyclerView recyclerView;
    String sayfaname;
    Dialog progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.urunler, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        iduplate();
        showdialog("Even Wachten");

        getdata();
        setHasOptionsMenu(true);

        yeniurunbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ekle.class);
                i.putExtra("satici_name", sayfaname);
                i.putExtra("gelen", "add");
                startActivity(i);
            }
        });






    }
    private void showdialog(String txt) {
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setContentView(R.layout.diolog);

        TextView textVie=(TextView)progressBar.findViewById(R.id.dialog_txt);
        textVie.setText(txt);
        progressBar.show();}
    private void getdata() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Sirketler");
        query.whereEqualTo("ids", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                try{
                    sayfaname=objects.get(0).getString("sirket_Adi");

                }
                catch (Exception exception) {
                    sayfaname = "";
                }
                try {


                    ParseQuery<ParseObject> queryrelation = objects.get(0).getRelation("urunler").getQuery();
                    queryrelation.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> insadeobject, ParseException e) {
                            if (e == null) {

                                for (ParseObject a : insadeobject) {
                                    urun_helper oneitem = new urun_helper();
                                    oneitem.setInfo(a.getString("info"));
                                    oneitem.setSirket_name(a.getString("satici"));

                                    //Resim CEk
                                    ParseFile postImage = a.getParseFile("img");
                                    String imageUrl = postImage.getUrl();//live url
                                    Uri imageUri = Uri.parse(imageUrl);
                                    oneitem.setCesit(a.getString("cesit"));
                                    oneitem.setObjectid(a.getObjectId());
                                    oneitem.setResim(imageUri.toString());
                                    oneitem.setFiyat(a.get("fiyat").toString());
                                    oneitem.setIsim(a.getString("urun_ismi"));
                                    helper.add(oneitem);
                                }

                                recycview(helper);
                            }
                        }
                    });
                    progressBar.cancel();
                }
                catch (Exception exception)
                {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void iduplate() {
        progressBar = new Dialog(context);

        yeniurunbtn = ((AppCompatActivity) context).findViewById(R.id.yeniurunbtn);
    }

    private void recycview(ArrayList<urun_helper> helper) {

        try {
            recyclerView = ((AppCompatActivity) context).findViewById(R.id.recyc_urun);
            adapter = new urun_adapter(context, urun_helper.getdata(helper));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        catch (Exception e)
        {
            Log.d("error","rec error");}

    }


    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private void alertfilter() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.cesit);
        CheckBox peynir, sut, et_tavuk, baharat, cukulata, icecek;
        Button filtre, iptal;
        iptal = dialog.findViewById(R.id.cesit_iptal_btn);
        filtre = dialog.findViewById(R.id.filtre_cesit_btn);
        peynir = dialog.findViewById(R.id.cesit_filter_peynir);
        sut = dialog.findViewById(R.id.cesit_filter_sut_kahvalti);
        et_tavuk = dialog.findViewById(R.id.cesit_filter_et_tavuk);
        baharat = dialog.findViewById(R.id.cesit_filter_baharatlar);
        cukulata = dialog.findViewById(R.id.cesit_filter_cikolata);
        icecek = dialog.findViewById(R.id.cesit_filter_icecekler);
        dialog.getWindow().setLayout(1000, 1500);
        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


    }


    SearchView searchView;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_menu, menu);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Urunler");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_menu:
                alertfilter();
                break;
        }
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.filter_menu);


        final SearchView searchView = (SearchView) menu.findItem(R.id.search_menu).getActionView();
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(context, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}
