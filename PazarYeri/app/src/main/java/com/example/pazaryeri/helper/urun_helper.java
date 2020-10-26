package com.example.pazaryeri.helper;

import java.util.ArrayList;

public class urun_helper {

    String isim;
    String satici;
    String aciklama;
    String fiyat;
    String gram;
    String cesit;
    String urun_id;

    public String getCesit() {
        return cesit;
    }

    public void setCesit(String cesit) {
        this.cesit = cesit;
    }

    public String getUrun_id() {
        return urun_id;
    }

    public void setUrun_id(String urun_id) {
        this.urun_id = urun_id;
    }

    String resim_url;

    public String getResim_url() {
        return resim_url;
    }

    public void setResim_url(String resim_url) {
        this.resim_url = resim_url;
    }

    int adet;

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSatici() {
        return satici;
    }

    public void setSatici(String satici) {
        this.satici = satici;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getGram() {
        return gram;
    }

    public void setGram(String gram) {
        this.gram = gram;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public static ArrayList<urun_helper> getdata(ArrayList<urun_helper> a){
        return a;
    }
}
