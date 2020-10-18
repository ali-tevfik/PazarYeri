package com.example.pazaryeri.drawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazaryeri.Eskisiparis;
import com.example.pazaryeri.Giris;
import com.example.pazaryeri.Kisisel_Bilgi;
import com.example.pazaryeri.R;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class MenuListFragment  extends Fragment {
    private ImageView ivMenuUserProfilePhoto;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        View navHeader = vNavigation.getHeaderView(0);
        ivMenuUserProfilePhoto = (ImageView) navHeader.findViewById(R.id.drawerimg);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.eskisiparis_menu:
                        Intent i=new Intent(context, Eskisiparis.class);
                        startActivity(i);
                        break;
                    case R.id.Profil_menu_drawer_Layout:
                        Intent ii=new Intent(context, Kisisel_Bilgi.class);
                        startActivity(ii);
                        break;
                    case  R.id.Cikis_menu_drawer_Layout:
                        ParseUser.logOut();
                        Intent iii=new Intent(context, Giris.class);
                        startActivity(iii);

                }
                return false;
            }
        }) ;
        setupHeader();
        return  view ;
    }
    private void setupHeader() {

        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
        String profilePhoto = "deneme";
        Picasso.get()
                .load(profilePhoto)
                .placeholder(R.drawable.logo)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .into(ivMenuUserProfilePhoto);
    }
Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
