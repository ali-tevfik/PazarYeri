package com.example.pazaryericontrolpanel.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;

import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pazaryericontrolpanel.R;
import com.example.pazaryericontrolpanel.adapter.siparis_list_adaper;
import com.example.pazaryericontrolpanel.helper.siparis_list_helper;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

public class Liste_fragment extends Fragment {

    siparis_list_adaper adapter;
    ArrayList<siparis_list_helper> helper = new ArrayList<>();
    RecyclerView recyclerView;
    String fiyat, bolge, objectid;
    TextView satis_txt, kazanc_txt, musteri_txt;
    String totalsatis = "0", totalkazanc = "0", totalmusteri;

    // Button bolge_filter, fiyat_filter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_eskilist, container, false);
    }

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        idupdate();
        getdata();
        setHasOptionsMenu(true);


    }

    private void getdata() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Sirketler");
        query.whereEqualTo("ids", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    getsiparis(objects.get(0).getString("sirket_Adi"));
                }
            }
        });


    }


    private void getsiparis(final String sirket_adi) {
        SimpleDateFormat start = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat finish = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String today = finish.format(d);
        Toast.makeText(context, today, Toast.LENGTH_SHORT).show();


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis");
        query.whereEqualTo("sirket_name", sirket_adi);
        query.whereGreaterThanOrEqualTo("createdAt", today);
        query.whereLessThan("createdAt", today);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    totalmusteri = String.valueOf(objects.size());
                    musteri_txt.setText(totalmusteri);
                    for (ParseObject a : objects) {
                        a.getRelation("siparis_ayrinti").getQuery().findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> c, ParseException e) {
                                if (e == null)
                                    totalsatis = String.valueOf(c.size() + Integer.parseInt(totalsatis));
                                satis_txt.setText(totalsatis);
                            }
                        });
                        fiyat = a.getString("ucret");
                        objectid = a.getObjectId();
                        getuser(a.getString("kullanici_id"), fiyat, objectid);
                    }
                }
            }
        });
    }

    private void getuser(String kullanici_id, final String fiyat, final String objectid) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("users");
        query.whereEqualTo("kisi_objectId", kullanici_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    final ParseQuery<ParseObject> querydetails = objects.get(0).getRelation("User_details").getQuery();
                    ParseQuery<ParseObject> queryaddress = objects.get(0).getRelation("User_adress").getQuery();
                    queryaddress.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null && objects.size() > 0) {
                                for (ParseObject a : objects) {
                                    bolge = a.getString("Bolge");
                                    getuserdetails(querydetails, fiyat, objectid, bolge);
                                }
                            }
                        }
                    });

                }
            }
        });


    }

    private void getuserdetails(ParseQuery<ParseObject> querydetails, final String fiyat, final String objectid, final String bolge) {
        querydetails.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject a : objects) {
                        totalkazanc = String.valueOf(Integer.parseInt(totalkazanc) + Integer.parseInt(fiyat));
                        kazanc_txt.setText("€" + totalkazanc);
                        siparis_list_helper oneitem;
                        oneitem = new siparis_list_helper();
                        oneitem.setFiyat("€" + fiyat);
                        oneitem.setObjectid(objectid);
                        oneitem.setBolge(bolge);
                        oneitem.setIsim(a.getString("isim") + " " + a.getString("soyisim"));
                        oneitem.setTelno(a.getString("Telefon_number"));
                        helper.add(oneitem);
                        recycview(helper);
                    }


                }
            }
        });
    }

    private void idupdate() {
        recyclerView = ((AppCompatActivity) context).findViewById(R.id.eskilistrecyclerview);
        kazanc_txt = ((AppCompatActivity) context).findViewById(R.id.total_kazanc_Text);
        musteri_txt = ((AppCompatActivity) context).findViewById(R.id.rapor_musteri);
        satis_txt = ((AppCompatActivity) context).findViewById(R.id.satis);

    }

    private void recycview(ArrayList<siparis_list_helper> helper) {

        adapter = new siparis_list_adaper(context, siparis_list_helper.getdata(helper));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void alertfilter() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.filter);
        CheckBox fiyatazalan, fiyatartan, limburg, drenthe, noordholland, zeeland;
        Button filtre, iptal;
        iptal = dialog.findViewById(R.id.filter_iptal_btn);
        filtre = dialog.findViewById(R.id.filtre_filter_btn);
        fiyatartan = dialog.findViewById(R.id.artanfiyat_filter);
        fiyatazalan = dialog.findViewById(R.id.azalanfiyat_filter);
        limburg = dialog.findViewById(R.id.filter_limburg);
        drenthe = dialog.findViewById(R.id.drenthe_filter);
        noordholland = dialog.findViewById(R.id.filter_nordholland);
        zeeland = dialog.findViewById(R.id.zeeland_filter);
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
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.toolbar_menu, menu);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Siparisler");

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

