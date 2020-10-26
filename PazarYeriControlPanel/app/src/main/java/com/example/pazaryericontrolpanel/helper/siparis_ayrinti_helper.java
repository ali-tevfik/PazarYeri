package com.example.pazaryericontrolpanel.helper;

import java.util.ArrayList;

public class siparis_ayrinti_helper {
    private  String adet,urunname,fiyat;


    public String getAdet() {
        return adet;
    }

    public void setAdet(String adet) {
        this.adet = adet;
    }

    public String getUrunname() {
        return urunname;
    }

    public void setUrunname(String urunname) {
        this.urunname = urunname;
    }


    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }



    public static ArrayList<siparis_ayrinti_helper> getdata(ArrayList<siparis_ayrinti_helper>a){return a;}

}
