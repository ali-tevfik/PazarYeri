package com.example.pazaryericontrolpanel.helper;

import java.util.ArrayList;

public class urun_helper {

    String sirket_name,isim,info,gr,fiyat,resim,objectid,cesit;

    public String getCesit() {
        return cesit;
    }

    public void setCesit(String cesit) {
        this.cesit = cesit;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getSirket_name() {
        return sirket_name;
    }

    public void setSirket_name(String sirket_name) {
        this.sirket_name = sirket_name;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getGr() {
        return gr;
    }

    public void setGr(String gr) {
        this.gr = gr;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public static ArrayList<urun_helper> getdata(ArrayList<urun_helper> a){return a;}
}
