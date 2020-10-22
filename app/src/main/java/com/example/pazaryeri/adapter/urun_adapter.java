package com.example.pazaryeri.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryeri.R;
import com.example.pazaryeri.Urunler;
import com.example.pazaryeri.adet_interface;
import com.example.pazaryeri.helper.siparis_sepet_helper;
import com.example.pazaryeri.helper.urun_helper;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class urun_adapter extends RecyclerView.Adapter<urun_adapter.Holder> {
    private static Context mcontext;
    private static urun_helper satici_bilgi;
    private static boolean oncedenverivarmi = false;
    private static int adet_total = 0;
    private static ArrayList<Integer> deleteindex;
    Context context;
    ArrayList<urun_helper> mdatalist;
    LayoutInflater layoutInflater;
    ArrayList<Integer> tiklanan_postiion = new ArrayList<>();

    private static ArrayList<siparis_sepet_helper> liste = new ArrayList<>();
    siparis_sepet_helper siparislistesi_ekleme;

    int postion;


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.urun_tek, parent, false);
        urun_adapter.Holder holder = new urun_adapter.Holder(view);
        return holder;
    }

    urun_helper tiklananmanzara;

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        tiklananmanzara = tiklananmanzara;
        urun_helper tiklananmanzara = mdatalist.get(position);
        position = postion;
        holder.setdata(tiklananmanzara, position);
    }

   private static adet_interface listener;

    public urun_adapter(Context context, ArrayList<urun_helper> getdata, adet_interface listener) {
        this.listener = listener;
        adet_total = 0;
        this.context = context;
        listener.durum(false);
        mcontext = context;
        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
        liste.clear();
        oncedenverivarmi=false;
    }

    int totaladet = 0;

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView isim, gram, fiyat, adet_arttir, adet_azalt, urun_adet, urun_cesit;
        ImageView resim;
        Toolbar toolbar;

        urun_helper getTiklananmanzara;
        CardView cardview;

        public Holder(@NonNull View itemView) {
            super(itemView);
            isim = (TextView) itemView.findViewById(R.id.tek_urun_name);
            urun_cesit = (TextView) itemView.findViewById(R.id.tek_urun_cesit);
            cardview = (CardView) itemView.findViewById(R.id.urun_tek_cardview);
            fiyat = (TextView) itemView.findViewById(R.id.tek_urun_fiyat);
            adet_arttir = (TextView) itemView.findViewById(R.id.tek_adet_arttir);
            adet_azalt = (TextView) itemView.findViewById(R.id.tek_adet_azalt);
            urun_adet = (TextView) itemView.findViewById(R.id.tek_urun_adet);
            resim = (ImageView) itemView.findViewById(R.id.tek_urun_img);

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertiextrainfo();

                }
            });

            adet_arttir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int a = getTiklananmanzara.getAdet();
                    a = a + 1;
                    getTiklananmanzara.setAdet(a);
                    totaladet = totaladet + 1;
                    urun_adet.setText(String.valueOf(a));

                    listener.adet(totaladet);


                    if (getTiklananmanzara.getAdet() == 1) {
                        //database eklemk icin array olusturdum
                        dataekle(getTiklananmanzara);

                    } else {
                        //database eklemk icin 1 den fazla adet olani ekleme
                        for (int i = 0; liste.size() > i; i++) {
                            if (liste.get(i).getUrun_adi().matches(getTiklananmanzara.getIsim())) {
                                liste.get(i).setUrun_adet(String.valueOf(getTiklananmanzara.getAdet()));
                            }
                        }
                    }

                }
            });


            adet_azalt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = getTiklananmanzara.getAdet();
                    if (a >= 1) {
                        a = a - 1;

                        getTiklananmanzara.setAdet(a);
                        if (getTiklananmanzara.getAdet() == 0)
                            adet_azalt.setEnabled(false);
                        totaladet = totaladet - 1;
                        urun_adet.setText(String.valueOf(a));
                        for (int i = 0; liste.size() > i; i++) {
                            if (liste.get(i).getUrun_adi().matches(getTiklananmanzara.getIsim())) {
                                liste.get(i).setUrun_adet(String.valueOf(getTiklananmanzara.getAdet()));


                            }

                        }


                        listener.adet(totaladet);
                    }

                }
            });

        }


        public void setdata(urun_helper tiklananmanzara, int position) {
            isim.setText(tiklananmanzara.getIsim());
            fiyat.setText(tiklananmanzara.getFiyat());
            urun_cesit.setText(tiklananmanzara.getCesit());
            urun_adet.setText(String.valueOf(tiklananmanzara.getAdet()));
            Picasso.get().load(tiklananmanzara.getResim_url()).into(resim);
            getTiklananmanzara = tiklananmanzara;
            satici_bilgi = getTiklananmanzara;
            if (tiklananmanzara.getAdet() > 0) {
                dataekle(tiklananmanzara);
                adet_total += tiklananmanzara.getAdet();
                oncedenverivarmi = true;
            }

        }

        public void alertiextrainfo() {

            View mView = layoutInflater.inflate(R.layout.dialog_login, null);
            //create
            final TextView urun_adi_alert = (TextView) mView.findViewById(R.id.urun_adi_alert);
            final RatingBar ratingBar = mView.findViewById(R.id.urun_yildizi);
            final TextView urun_info = (TextView) mView.findViewById(R.id.urun_info);
            final TextView fiyat = (TextView) mView.findViewById(R.id.urun_info);
            ImageView img = mView.findViewById(R.id.dialog_img);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            //set data
            ratingBar.setRating(Float.parseFloat("2.0"));
            urun_adi_alert.setText(getTiklananmanzara.getIsim());
            urun_info.setText(getTiklananmanzara.getAciklama());
            fiyat.setText(getTiklananmanzara.getFiyat());
            Picasso.get().load(getTiklananmanzara.getResim_url()).into(img);
            mBuilder.setView(mView);


            final AlertDialog dialog = mBuilder.create();
            dialog.show();

        }


    }

    private void dataekle(urun_helper getTiklananmanzara) {
        siparislistesi_ekleme = new siparis_sepet_helper();
        siparislistesi_ekleme.setKullanici_id(ParseUser.getCurrentUser().getObjectId());
        siparislistesi_ekleme.setUrun_adet(String.valueOf(getTiklananmanzara.getAdet()));
        siparislistesi_ekleme.setFiyat(getTiklananmanzara.getFiyat());
        siparislistesi_ekleme.setUrun_id(getTiklananmanzara.getUrun_id());
        siparislistesi_ekleme.setUrun_adi(getTiklananmanzara.getIsim());
        siparislistesi_ekleme.setUrun_sahibi(getTiklananmanzara.getSatici());
        liste.add(siparislistesi_ekleme);

    }
    public static void kaydet() {
        int adet_nedir = 0;

        try {

            if (liste.size() > 0 && !oncedenverivarmi) {

// stick the objects in an array
                ParseObject siparis_sepeti = new ParseObject("siparis_sepeti");
                siparis_sepeti.put("urun_sahibi", satici_bilgi.getSatici());
                siparis_sepeti.put("kullanici_id", ParseUser.getCurrentUser().getObjectId());
                ArrayList<ParseObject> siparis_ayrinti = new ArrayList<ParseObject>();

                ParseRelation<ParseObject> relation = siparis_sepeti.getRelation("siparis_ayrinti");

                for (int i = 0; liste.size() > i; i++) {

                    ParseObject list_item = new ParseObject("siparis_ayrinti");

                    list_item.put("adet", liste.get(i).getUrun_adet());
                    adet_nedir += Integer.parseInt(liste.get(i).getUrun_adet());
                    list_item.put("kullanici_id", ParseUser.getCurrentUser().getObjectId());
                    list_item.put("urun_id", liste.get(i).getUrun_id());
                    list_item.saveInBackground();
                    siparis_ayrinti.add(list_item);
                    relation.add(list_item);

                }

                siparis_sepeti.put("adet", String.valueOf(adet_nedir));
                siparis_sepeti.put("siparis_array", siparis_ayrinti);


                siparis_sepeti.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(mcontext, "basarili", Toast.LENGTH_SHORT).show();
                            listener.durum(true);

                        } else {
                            Log.d("a;slfjasf", e.getMessage());
                            Toast.makeText(mcontext, "a", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            } else if (totaladetbul() != adet_total) {
                update();
            }
            else
                listener.degisenvarmi(true);


        } catch (Exception a) {
            Toast.makeText(mcontext, "Bir hata olustu", Toast.LENGTH_SHORT).show();
        }

    }


    private static int totaladetbul() {
        int deger = 0;
        for (int i = 0; i < liste.size(); i++)
            deger += Integer.parseInt(liste.get(i).getUrun_adet());
        return deger;
    }

    private static void update() {
        try {


            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis_sepeti");
            query.whereEqualTo("kullanici_id", ParseUser.getCurrentUser().getObjectId());
            query.whereEqualTo("urun_sahibi", satici_bilgi.getSatici());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    ParseQuery<ParseObject> relation = null;
                    try {

                        relation = object.getRelation("siparis_ayrinti").getQuery();
                    } catch (Exception ex) {
                        Toast.makeText(mcontext, "error update" , Toast.LENGTH_SHORT).show();
                    }
                    if (relation != null) {
                        relation.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null && objects.size() > 0) {
                                    int adet_nedir = 0;
                                    for (int i = 0; liste.size() > i; i++) {

                                        boolean girdimi = false;
                                        for (int x = 0; objects.size() > x; x++) {

                                            if (objects.get(x).getString("kullanici_id").matches(ParseUser.getCurrentUser().getObjectId()) && objects.get(x).getString("urun_id").matches(liste.get(i).getUrun_id())) {
                                                girdimi = true;
                                                if (liste.get(i).getUrun_adet().matches("0"))
                                                    objects.get(x).deleteInBackground();
                                                else {
                                                    objects.get(x).put("adet", liste.get(i).getUrun_adet());
                                                    adet_nedir += Integer.parseInt(liste.get(i).getUrun_adet());
                                                    objects.get(x).saveInBackground();
                                                }
                                            }
                                        }
                                        if (!girdimi) {
                                            adet_nedir += Integer.parseInt(liste.get(i).getUrun_adet());
                                            ArrayList<ParseObject> siparis_ayrinti = new ArrayList<ParseObject>();
                                            ParseRelation<ParseObject> relation = object.getRelation("siparis_ayrinti");
                                            ParseObject object1 = new ParseObject("siparis_ayrinti");
                                            object1.put("adet", liste.get(i).getUrun_adet());
                                            object1.put("urun_id", liste.get(i).getUrun_id());
                                            object1.put("kullanici_id", ParseUser.getCurrentUser().getObjectId());
                                            object1.put("durum", true);
                                            relation.add(object1);
                                            siparis_ayrinti.add(object1);
                                            object.put("siparis_array", siparis_ayrinti);



                                        }

                                    }
                                    object.put("adet", String.valueOf(adet_nedir));
                                    if (adet_nedir <= 0) {
                                        object.deleteInBackground();
                                        oncedenverivarmi = false;
                                    } else {
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {

                                                    listener.durum(true);

                                                    Toast.makeText(mcontext, "oke", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(mcontext, "nee", Toast.LENGTH_SHORT).show();
                                                    Log.d("error", e.getMessage());
                                                }
                                                liste.clear();
                                            }
                                        });
                                    }
                                    liste.clear();

                                }
                            }
                        });


                    }
                }
            });

        } catch (Exception a) {
            Toast.makeText(mcontext, "Bir hata olustu", Toast.LENGTH_SHORT).show();
        }
    }


}