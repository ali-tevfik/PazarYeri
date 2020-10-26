package com.example.pazaryericontrolpanel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pazaryericontrolpanel.R;
import com.example.pazaryericontrolpanel.helper.siparis_ayrinti_helper;
import com.example.pazaryericontrolpanel.helper.siparis_list_helper;

import java.util.ArrayList;

public class siparis_ayrinti_adapter extends RecyclerView.Adapter<siparis_ayrinti_adapter.Holder> {
    Context context;
    ArrayList<siparis_ayrinti_helper> mdatalist;
    LayoutInflater layoutInflater;


    public siparis_ayrinti_adapter(Context context, ArrayList<siparis_ayrinti_helper> getdata) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        this.mdatalist = getdata;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.siparis_ayrinti_tek, parent, false);
        siparis_ayrinti_adapter.Holder holder = new siparis_ayrinti_adapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        siparis_ayrinti_helper tiklananmanzara = mdatalist.get(position);
        holder.setdata(tiklananmanzara, position);
    }

    @Override
    public int getItemCount() {
        return mdatalist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView fiyat,urunisim,adet;

        public Holder(@NonNull View itemView) {
            super(itemView);
            fiyat=itemView.findViewById(R.id.siparis_ayrinti_fiyat);
            urunisim=itemView.findViewById(R.id.siparis_ayrinti_urun_name);
            adet=itemView.findViewById(R.id.siparis_ayrinti_adet);
        }

        public void setdata(siparis_ayrinti_helper tiklananmanzara, int position) {
            fiyat.setText(tiklananmanzara.getFiyat());
            urunisim.setText(tiklananmanzara.getUrunname());
            adet.setText(tiklananmanzara.getAdet());
        }
    }
}
