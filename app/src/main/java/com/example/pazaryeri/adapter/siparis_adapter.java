package com.example.pazaryeri.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryeri.R;
import com.example.pazaryeri.helper.dict;
import com.example.pazaryeri.helper.katagori_helper;
import com.example.pazaryeri.helper.siparis_helper;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class siparis_adapter extends RecyclerView.Adapter<siparis_adapter.Holder> {

    Context context;
    ArrayList<siparis_helper> mdatalist;
    LayoutInflater layoutInflater;
    private static siparis_helper allworthgetitem;
    LinearLayout linearLayout;
    private static Context mcontext;
    public static ArrayList<dict> mlist = new ArrayList<>();

    @NonNull
    @Override
    public siparis_adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.siparis_tek, parent, false);
        siparis_adapter.Holder holder = new siparis_adapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull siparis_adapter.Holder holder, int position) {
        siparis_helper tıklananmanzara = mdatalist.get(position);
        holder.setdata(tıklananmanzara, position);
    }

    public siparis_adapter(Context context, ArrayList<siparis_helper> getdata) {

        mlist.clear();
        adetkontrol = 0;
        this.context = context;
        mcontext = context;
        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
    }

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        Button siparis_tmm_btn;
        TextView arttir, azalt, fiyat, urun_name, aciklama, adet_txt;
        LinearLayout delete_item;
        siparis_helper getitem;

        public Holder(@NonNull View itemView) {
            super(itemView);
            delete_item = itemView.findViewById(R.id.siparis_tek_delete);
            fiyat = itemView.findViewById(R.id.tek_urun_fiyat);
            urun_name = itemView.findViewById(R.id.tek_urun_name);
            aciklama = itemView.findViewById(R.id.tek_urun_aciklama);
            adet_txt = itemView.findViewById(R.id.tek_urun_adet);
            img = itemView.findViewById(R.id.tek_siparis_img);
            arttir = itemView.findViewById(R.id.tek_adet_arttir);
            azalt = itemView.findViewById(R.id.tek_adet_azalt);

            arttir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adet = Integer.parseInt(getitem.getAdet());
                    adet += 1;
                    if (adet == 1)
                        azalt.setEnabled(true);
                    adet_txt.setText(String.valueOf(adet));
                    getitem.setAdet(String.valueOf(adet));
                    update_mlist(getitem);
                }
            });


            azalt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adet = Integer.parseInt(getitem.getAdet());
                    adet -= 1;
                    if (adet == 0)
                        azalt.setEnabled(false);
                    adet_txt.setText(String.valueOf(adet));
                    getitem.setAdet(String.valueOf(adet));
                    update_mlist(getitem);
                }
            });


            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mdatalist.remove(getitem);
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mdatalist.size());
                    deleteItem(getitem);
                }
            });


        }

        public void setdata(siparis_helper tiklananmanzara, int position) {
            fiyat.setText("€" + tiklananmanzara.getPara());
            adet_txt.setText(tiklananmanzara.getAdet());
            aciklama.setText(tiklananmanzara.getAciklama());
            urun_name.setText(tiklananmanzara.getName());
            Picasso.get().load(tiklananmanzara.getResim_url()).into(img);
            getitem = tiklananmanzara;
            allworthgetitem = tiklananmanzara;
            setdata_list(tiklananmanzara);

        }
    }

    private void update_mlist(siparis_helper getitem) {
        for (dict a : mlist) {
            if (getitem.getUrun_id().matches(a.getKey())) {
                a.setValue(getitem.getAdet());
            }
        }
    }

    private void setdata_list(siparis_helper tiklananmanzara) {
        dict dict = new dict();
        dict.setValue(tiklananmanzara.getAdet());
        dict.setKey(tiklananmanzara.getUrun_id());
        mlist.add(dict);
    }

    private void deleteItem(final siparis_helper getitem) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("siparis_sepeti");
        query.whereEqualTo("urun_sahibi", getitem.getUrun_sahibi());
        query.whereEqualTo("kullanici_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                ParseQuery<ParseObject> relation = objects.get(0).getRelation("siparis_ayrinti").getQuery();
                relation.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> insedeobject, ParseException e) {
                        if (e == null && insedeobject.size() > 0) {
                            for (ParseObject object : insedeobject) {
                                if (object.getString("urun_id").matches(getitem.getUrun_id())) {
                                    object.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show();
                                                if (insedeobject.size() == 1)
                                                    objects.get(0).deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null)
                                                                Toast.makeText(context, "anasilindi", Toast.LENGTH_SHORT).show();
                                                            else
                                                                Toast.makeText(context, "ana error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            } else
                                                Toast.makeText(context, "hata", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        });


    }

    public static void siparis_tmm() {
        find_relationitem();
    }

    private static void find_relationitem() {
        ParseQuery<ParseObject> query = new ParseQuery("siparis_sepeti");
        query.whereEqualTo("urun_sahibi", allworthgetitem.getUrun_sahibi());
        query.whereEqualTo("kullanici_id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseQuery<ParseObject> relation = objects.get(0).getRelation("siparis_ayrinti").getQuery();
                    relation.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> insideobjects, ParseException e) {
                            if (insideobjects.size() > 0 && e == null) {

                                add_siparis(insideobjects);
                                for (final ParseObject a : insideobjects) {
                                    a.put("durum", false);
                                    for (dict dict : mlist) {
                                        if (dict.getKey().matches(a.getString("urun_id"))) {
                                            a.put("adet", dict.getValue());
                                        }
                                    }

                                    a.deleteInBackground();
                                    a.saveInBackground();
                                }

                                objects.get(0).deleteInBackground();
                            }
                        }
                    });
                }
            }
        });

    }

    private static int adetkontrol = 0;
    private static String objcet_id = null;

    private static void add_siparis(final List<ParseObject> insideobjects) {

            adetkontrol++;
            ParseObject object = new ParseObject("siparis");
            object.put("sirket_name", allworthgetitem.getUrun_sahibi());
            object.put("kullanici_id", ParseUser.getCurrentUser().getObjectId());

            object.put("siparis_array", siparis_ayrinti(object, insideobjects));
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(mcontext, "asd", Toast.LENGTH_SHORT).show();
                }
            });
            objcet_id = object.getObjectId();



    }



    private static Object siparis_ayrinti(ParseObject object, List<ParseObject> insideobjects) {
        ArrayList<ParseObject> siparis_ayrinti = new ArrayList<>();
        ParseRelation<ParseObject> relation = object.getRelation("siparis_ayrinti");
        for (ParseObject a:insideobjects){
        ParseObject relationobject = new ParseObject("siparis_ayrinti");
        relationobject.put("urun_id", a.getString("urun_id"));
        relationobject.put("kullanici_id", ParseUser.getCurrentUser().getObjectId());
        for (dict dict:mlist){
            if (dict.getKey().matches(a.getString("urun_id"))){

                relationobject.put("adet", dict.getValue());
            }
        }
        relationobject.put("durum", false);
        relationobject.saveInBackground();
        siparis_ayrinti.add(relationobject);
        relation.add(relationobject);
        }
        return siparis_ayrinti;
    }


}
