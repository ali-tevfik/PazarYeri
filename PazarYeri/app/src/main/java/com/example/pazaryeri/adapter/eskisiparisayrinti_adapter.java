package com.example.pazaryeri.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryeri.R;
import com.example.pazaryeri.helper.eskisiparisayrinti_helper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class eskisiparisayrinti_adapter extends RecyclerView.Adapter<eskisiparisayrinti_adapter.Holder> {
   private static Context context;
    ArrayList<eskisiparisayrinti_helper> mdatalist;
    LayoutInflater layoutInflater;

    public eskisiparisayrinti_adapter(Context context, ArrayList<eskisiparisayrinti_helper> getdata) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.eski_saipris_ayrinti_tek, parent, false);
        eskisiparisayrinti_adapter.Holder holder = new eskisiparisayrinti_adapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        eskisiparisayrinti_helper tiklananmanzara = mdatalist.get(position);
        holder.setdata(tiklananmanzara, position);
    }

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView adet, isim;
        RatingBar star;

        public Holder(@NonNull View itemView) {
            super(itemView);
            adet = itemView.findViewById(R.id.siparis_ayrinti_adet);
            isim = itemView.findViewById(R.id.siparis_ayrinti_urun_name);

            star = itemView.findViewById(R.id.rating_siparisayrinti_tek);


        }

        public void setdata(final eskisiparisayrinti_helper tiklananmanzara, int position) {
            adet.setText(tiklananmanzara.getAdet());
            isim.setText(tiklananmanzara.getUrunismi());
            star.setRating(tiklananmanzara.getStar());
            add_data(tiklananmanzara);
            if (!tiklananmanzara.isDurum()) {

                star.setIsIndicator(true);
            }
            star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    mlist_update(tiklananmanzara, rating);

                }
            });
        }
    }

    private void add_data(eskisiparisayrinti_helper tiklananmanzara) {
        eskisiparisayrinti_helper helper = new eskisiparisayrinti_helper();
        helper.setUrunismi(tiklananmanzara.getUrunismi());
        helper.setAdet(tiklananmanzara.getAdet());
        helper.setStar(tiklananmanzara.getStar());
        helper.setUrun_id(tiklananmanzara.getUrun_id());
        helper.setDurum(tiklananmanzara.isDurum());
        arrlist.add(helper);
    }

    private static ArrayList<eskisiparisayrinti_helper> arrlist = new ArrayList<>();

    private void mlist_update(eskisiparisayrinti_helper tiklananmanzara, float rating) {
        for (eskisiparisayrinti_helper a : arrlist) {
            if (a.getUrunismi().matches(tiklananmanzara.getUrunismi())) {
                a.setStar(rating);
            }
        }

    }

    public static void kaydet() {
        for (final eskisiparisayrinti_helper a : arrlist) {
            if (a.getStar() != 0) {
                final ParseQuery<ParseObject> object = new ParseQuery<ParseObject>("urunler");
                object.whereEqualTo("objectId", a.getUrun_id());
                object.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (ParseObject insideobj : objects) {
                                int kisi_sayi = 0;
                                int puan = 0;
                                int totalpuan = 0;
                                try {
                                    puan = insideobj.getInt("puan");
                                    kisi_sayi = insideobj.getInt("puan_kisi_Sayisi");
                                } catch (Exception a) {
                                }

                                totalpuan = kisi_sayi * puan;
                                totalpuan += a.getStar();
                                totalpuan = totalpuan / (kisi_sayi + 1);
                                insideobj.put("puan",totalpuan);
                                insideobj.put("puan_kisi_Sayisi",kisi_sayi+1);
                                insideobj.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e ==null)
                                            Toast.makeText(context, "basarili", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, "nee", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }
                    }
                });
            }
        }
    }
}
