package com.example.pazaryeri.helper;

import java.util.ArrayList;
import java.util.Date;

public class eskisiparis_helper {
    public Date Tarih;
    public String total_fiayt,sirket,objectId;
    public boolean durum;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSirket() {
        return sirket;
    }

    public void setSirket(String sirket) {
        this.sirket = sirket;
    }

    public Date getTarih() {
        return Tarih;
    }

    public void setTarih(Date tarih) {
        Tarih = tarih;
    }


    public String getTotal_fiayt() {
        return total_fiayt;
    }

    public void setTotal_fiayt(String total_fiayt) {
        this.total_fiayt = total_fiayt;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    public static ArrayList<eskisiparis_helper> getdata(ArrayList<eskisiparis_helper> a){
        return a;
    }

}
