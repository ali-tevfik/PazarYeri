package com.example.pazaryeri.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryeri.R;
import com.example.pazaryeri.eski_siparis_Ayrinti;
import com.example.pazaryeri.helper.eskisiparis_helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class eskisiparis_adapter extends RecyclerView.Adapter<eskisiparis_adapter.Holder> {


    Context context;
    ArrayList<eskisiparis_helper> mdatalist;
    LayoutInflater layoutInflater;
    LinearLayout linearLayout;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.eski_siparis_tek, parent, false);
        eskisiparis_adapter.Holder holder = new eskisiparis_adapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        eskisiparis_helper tiklananmanzara = mdatalist.get(position);
        holder.setdata(tiklananmanzara, position);
    }


    public eskisiparis_adapter(Context context, ArrayList<eskisiparis_helper> getdata) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
    }

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tarih, sirketname, yuvarlakicon, fiyat;

        CardView linearLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tarih = itemView.findViewById(R.id.tek_eski_siparis_tarih);
            fiyat = itemView.findViewById(R.id.tek_eski_siparis_fiyat);
            sirketname = itemView.findViewById(R.id.tek_eski_siparis_sirket);
            yuvarlakicon = itemView.findViewById(R.id.tek_eski_siparis_icon);
            linearLayout = itemView.findViewById(R.id.tek_eski_siparis_linear);


        }

        public void setdata(final eskisiparis_helper tiklananmanzara, int position) {


            tarih.setText(String.valueOf(tiklananmanzara.getTarih().getDate())+"-"+String.valueOf(tiklananmanzara.getTarih().getMonth())+"-"+String.valueOf(tiklananmanzara.getTarih().getYear()));
            fiyat.setText("€"+tiklananmanzara.getTotal_fiayt());
            sirketname.setText(tiklananmanzara.getSirket());
            if (tiklananmanzara.isDurum()) {
                yuvarlakicon.setBackgroundColor(Color.parseColor("#6CBA29"));
                linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                yuvarlakicon.setBackgroundColor(Color.parseColor("#F4A72C"));
                linearLayout.setBackgroundColor(Color.parseColor("#BACAFF"));
            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, eski_siparis_Ayrinti.class);
                    i.putExtra("sirket_name",tiklananmanzara.getSirket());
                    i.putExtra("tarih",String.valueOf(tiklananmanzara.getTarih().getDate())+"-"+String.valueOf(tiklananmanzara.getTarih().getMonth())+"-"+String.valueOf(tiklananmanzara.getTarih().getYear()));
                    i.putExtra("objectId",tiklananmanzara.getObjectId());
                    context.startActivity(i);
                }
            });
        }
    }
}
