package com.example.pazaryeri.helper;

import java.util.ArrayList;

public class katagori_helper {
    private int puan;

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    String katogoriimgurl,katogariname,katagoricesitler;

    public String getKatagoricesitler() {
        return katagoricesitler;
    }

    public void setKatagoricesitler(String katagoricesitler) {
        this.katagoricesitler = katagoricesitler;
    }

    public String getKatogoriimgurl() {
        return katogoriimgurl;
    }

    public void setKatogoriimgurl(String katogoriimgurl) {
        this.katogoriimgurl = katogoriimgurl;
    }

    public String getKatogariname() {
        return katogariname;
    }

    public void setKatogariname(String katogariname) {
        this.katogariname = katogariname;
    }


    public static ArrayList<katagori_helper> getdata(ArrayList<katagori_helper> a){
        return a;
    }
}

