package com.example.pazaryeri.helper;

import java.util.ArrayList;

public class siparis_helper {

    private String name,adet,resim_url,para,aciklama,urun_sahibi,urun_id;

    public String getUrun_id() {
        return urun_id;
    }

    public void setUrun_id(String urun_id) {
        this.urun_id = urun_id;
    }

    public String getUrun_sahibi() {
        return urun_sahibi;
    }

    public void setUrun_sahibi(String urun_sahibi) {
        this.urun_sahibi = urun_sahibi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getAdet() {
        return adet;
    }

    public void setAdet(String adet) {
        this.adet = adet;
    }

    public String getResim_url() {
        return resim_url;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public void setResim_url(String resim_url) {
        this.resim_url = resim_url;
    }

    public static ArrayList<siparis_helper> getdata(ArrayList<siparis_helper> a){return  a;}
}
