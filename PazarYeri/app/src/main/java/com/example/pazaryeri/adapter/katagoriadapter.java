package com.example.pazaryeri.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryeri.R;
import com.example.pazaryeri.Siparis_sayfasi;
import com.example.pazaryeri.Urunler;
import com.example.pazaryeri.helper.katagori_helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class katagoriadapter extends RecyclerView.Adapter<katagoriadapter.Holder> {
    Context context;
    ArrayList<katagori_helper> mdatalist;
    int getAdapterPosition=0;
    boolean find=false;
    LayoutInflater layoutInflater;
    LinearLayout linearLayout;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.katogari_tek, parent, false);
        Holder holder = new Holder(view);
        getAdapterPosition=holder.getAdapterPosition();
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        katagori_helper tıklananmanzara=mdatalist.get(position);

        holder.setdata(tıklananmanzara,position);
    }



    public katagoriadapter(Context context,ArrayList<katagori_helper> getdata  ){
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
        this.mdatalist=getdata;

    }


    @Override
    public int getItemCount() {
        return mdatalist.size();
    }



    public class Holder extends RecyclerView.ViewHolder {
        ImageView resim;
        TextView name,cesit;
        RatingBar ratingBar;

        public Holder(@NonNull View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.tek_katagori_liner);
            ratingBar=(RatingBar) itemView.findViewById(R.id.rating);
            resim = (ImageView) itemView.findViewById(R.id.katagoritekimg);
            name = (TextView) itemView.findViewById(R.id.katagoritekisim);
            cesit = (TextView) itemView.findViewById(R.id.katagortekcesit);

        }


        public void setdata(final katagori_helper tiklananmanzara, int position) {
            name.setText(tiklananmanzara.getKatogariname());
            cesit.setText(tiklananmanzara.getKatagoricesitler());
            Picasso.get().load(tiklananmanzara.getKatogoriimgurl()).into(resim);
            ratingBar.setRating(tiklananmanzara.getPuan());
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(context, Urunler.class);
                    i.putExtra("satici_name",tiklananmanzara.getKatogariname());
                    i.putExtra("satici_logo",tiklananmanzara.getKatogoriimgurl());
                    context.startActivity(i);

                }
            });
        }


    }
}
