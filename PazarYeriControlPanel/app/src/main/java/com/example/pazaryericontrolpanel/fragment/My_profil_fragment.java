package com.example.pazaryericontrolpanel.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryericontrolpanel.Giris;
import com.example.pazaryericontrolpanel.R;
import com.example.pazaryericontrolpanel.adapter.siparis_list_adaper;
import com.example.pazaryericontrolpanel.helper.siparis_list_helper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class My_profil_fragment extends Fragment {
    private FragmentActivity myContext;
    siparis_list_adaper adapter;
    ArrayList<siparis_list_helper> helper = new ArrayList<>();
    RecyclerView recyclerView;
    Button bolge_filter, fiyat_filter;
    EditText sirket_name,isim,telefon_no,bilgiler;
    ImageView logo;
    TextView urun_adet;
    Dialog progressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = new Dialog(context);

        showdialog("Even Wachten");

        getdata();
        setHasOptionsMenu(true);

    }

    private void showdialog(String txt) {
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setContentView(R.layout.diolog);

        TextView textVie=(TextView)progressBar.findViewById(R.id.dialog_txt);
        textVie.setText(txt);
        progressBar.show();}
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logo= (ImageView)view.findViewById(R.id.profil_logo);
        sirket_name=(EditText)view.findViewById(R.id.profil_sirket_name);
        isim=(EditText)view.findViewById(R.id.profil_sirket_sahibi);
        telefon_no=(EditText)view.findViewById(R.id.profil_sirket_telefonno);
        urun_adet=(TextView)view.findViewById(R.id.profil_urun_adet);
        bilgiler=(EditText)view.findViewById(R.id.profil_info);
    }

    private void getdata() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Sirketler");
        query.whereEqualTo("ids", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject a:objects){
                        urunadetbul(objects.get(0));
                        sirket_name.setText(a.getString("sirket_Adi"));
                        isim.setText(a.getString("sahibi"));

                        bilgiler.setText(a.getString("info"));
                        telefon_no.setText(a.getString("telefon_numarasi"));

                        //Resim CEk
                        ParseFile postImage = a.getParseFile("logo");
                        String imageUrl = postImage.getUrl();//live url
                        Uri imageUri = Uri.parse(imageUrl);
                        Picasso.get().load(imageUri.toString()).into(logo);
                    }
                }
                progressBar.cancel();
            }
        });
    }

    private void urunadetbul(ParseObject parseObject) {
        ParseQuery<ParseObject> query=parseObject.getRelation("urunler").getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
               if (e == null){
                   urun_adet.setText(String.valueOf(objects.size()));
               }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myprofil, container, false);
    }

    Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.profil_menu, menu);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profil");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit_menu:
                ParseUser.logOut();
                Intent i = new Intent(myContext, Giris.class);
                startActivity(i);
        }
        return true;
    }
}