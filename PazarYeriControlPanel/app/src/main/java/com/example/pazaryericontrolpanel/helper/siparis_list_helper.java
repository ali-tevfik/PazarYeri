package com.example.pazaryericontrolpanel.helper;

import java.util.ArrayList;

public class siparis_list_helper {

    private String siparisteslim,siparisveristarih,isim,telno,bolge,fiyat,objectid;
    private  boolean orderStatus;

    public  void setorderStatus(boolean orderStatus){this.orderStatus = orderStatus;}
    public  boolean getorderStatus(){ return  this.orderStatus;}
    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getSiparisteslim() {
        return siparisteslim;
    }

    public void setSiparisteslim(String siparisteslim) {
        this.siparisteslim = siparisteslim;
    }

    public String getSiparisveristarih() {
        return siparisveristarih;
    }

    public void setSiparisveristarih(String siparisveristarih) {
        this.siparisveristarih = siparisveristarih;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getBolge() {
        return bolge;
    }

    public void setBolge(String bolge) {
        this.bolge = bolge;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

public static ArrayList<siparis_list_helper> getdata(ArrayList<siparis_list_helper>a){return a;}


}
