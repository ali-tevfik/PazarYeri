package com.example.pazaryeri.helper;

import java.util.ArrayList;

public class eskisiparisayrinti_helper {
    private  String urun_id,adet,urunismi;
    private  float star;
    boolean durum;

    public String getUrun_id() {
        return urun_id;
    }

    public void setUrun_id(String urun_id) {
        this.urun_id = urun_id;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }


    public String getAdet() {
        return adet;
    }

    public void setAdet(String adet) {
        this.adet = adet;
    }

    public String getUrunismi() {
        return urunismi;
    }

    public void setUrunismi(String urunismi) {
        this.urunismi = urunismi;
    }

    public static ArrayList<eskisiparisayrinti_helper> getdata(ArrayList<eskisiparisayrinti_helper>a){return a;}
}
