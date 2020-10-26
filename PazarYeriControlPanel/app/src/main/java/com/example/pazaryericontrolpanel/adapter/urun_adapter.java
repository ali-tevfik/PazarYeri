package com.example.pazaryericontrolpanel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryericontrolpanel.R;
import com.example.pazaryericontrolpanel.helper.urun_helper;
import com.example.pazaryericontrolpanel.siparis_ayrinti;
import com.example.pazaryericontrolpanel.urun.ekle;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class urun_adapter extends RecyclerView.Adapter<urun_adapter.Holder> {
    Context context;
    ArrayList<urun_helper> mdatalist;
    LayoutInflater layoutInflater;

    public urun_adapter(Context context, ArrayList<urun_helper> getdata) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.urun_tek, parent, false);
        urun_adapter.Holder holder = new urun_adapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        urun_helper tiklananmanzara = mdatalist.get(position);
        holder.setdata(tiklananmanzara, position);

    }

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView isim, satici, gram, aciklama, fiyat;
        urun_helper getitem;
        ImageView resim, delete, edit;
        LinearLayout liner;

        public Holder(@NonNull View itemView) {
            super(itemView);
            isim = (TextView) itemView.findViewById(R.id.tek_urun_name);
            satici = (TextView) itemView.findViewById(R.id.tek_satici_name);
            //  gram=(TextView) itemView.findViewById(R.id.tek_urun_gram);
            aciklama = (TextView) itemView.findViewById(R.id.tek_urun_aciklama);
            fiyat = (TextView) itemView.findViewById(R.id.tek_urun_fiyat);
            resim = (ImageView) itemView.findViewById(R.id.tek_urun_img);
            delete = (ImageView) itemView.findViewById(R.id.urun_tek_delete);
            edit = (ImageView) itemView.findViewById(R.id.urun_tek_edit);


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ekle.class);
                    i.putExtra("gelen", "update");
                    i.putExtra("isim", getitem.getIsim());
                    i.putExtra("info", getitem.getInfo());
                    i.putExtra("fiyat", getitem.getFiyat());
                    i.putExtra("resimurl", getitem.getResim());
                    i.putExtra("cesit", getitem.getCesit());
                    i.putExtra("satici",getitem.getSirket_name());
                    i.putExtra("object_id", getitem.getObjectid());
                    context.startActivity(i);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                            // set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Do you want to Delete")

                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //your deleting code
                                    mdatalist.remove(getitem);
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), mdatalist.size());
                                    deletedata(getitem);
                                    dialog.dismiss();
                                }

                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            })
                            .create();
                    AlertDialog diaBox = myQuittingDialogBox;
                    diaBox.show();
                }
            });


        }

        public void setdata(final urun_helper tiklananmanzara, int position) {
            isim.setText(tiklananmanzara.getIsim());
            satici.setText(tiklananmanzara.getSirket_name());
            aciklama.setText(tiklananmanzara.getInfo());
            // gram.setText(tiklananmanzara.getGr());
            fiyat.setText(tiklananmanzara.getFiyat());
            Picasso.get().load(tiklananmanzara.getResim()).into(resim);
            getitem = tiklananmanzara;

        }
    }
    private void deletedata(final urun_helper getitem) {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Sirketler");
        query.whereEqualTo("ids", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null & objects.size() > 0) {
                    ParseQuery<ParseObject> getrelation = objects.get(0).getRelation("urunler").getQuery();
                    getrelation.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null && objects.size() > 0) {
                                for (ParseObject a : objects) {
                                    if (a.getObjectId().matches(getitem.getObjectid())) {
                                        a.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(context, "silme basarili!", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(context, "Silme ERRORR!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
