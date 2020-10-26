package com.example.pazaryericontrolpanel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryericontrolpanel.R;
import com.example.pazaryericontrolpanel.helper.siparis_list_helper;
import com.example.pazaryericontrolpanel.siparis_ayrinti;

import java.util.ArrayList;

public class siparis_list_adaper extends RecyclerView.Adapter<siparis_list_adaper.Holder> {

    Context context;
    ArrayList<siparis_list_helper> mdatalist;
    LayoutInflater layoutInflater;


    public siparis_list_adaper(Context context, ArrayList<siparis_list_helper> getdata) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.tek_liste, parent, false);
        siparis_list_adaper.Holder holder = new siparis_list_adaper.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        siparis_list_helper tıklananmanzara = mdatalist.get(position);
        holder.setdata(tıklananmanzara, position);
    }

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name, telno, bolge,  fiyat;
        siparis_list_helper getitem;
        LinearLayout liner;

        public Holder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tekkisitelno);
            telno = itemView.findViewById(R.id.tekkisiisim);
            bolge = itemView.findViewById(R.id.tekkisibolge);
            fiyat = itemView.findViewById(R.id.tekkisifiyat);


            liner = (LinearLayout) itemView.findViewById(R.id.tek_liste_linear);
        }


        public void setdata(final siparis_list_helper tiklananmanzara, int position) {
            name.setText(tiklananmanzara.getIsim());
            telno.setText(tiklananmanzara.getTelno());
            bolge.setText(tiklananmanzara.getBolge());
            fiyat.setText(tiklananmanzara.getFiyat());
            getitem=tiklananmanzara;


            liner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, siparis_ayrinti.class);
                    i.putExtra("objectid", getitem.getObjectid());
                    context.startActivity(i);
                }
            });

        }
    }

}



