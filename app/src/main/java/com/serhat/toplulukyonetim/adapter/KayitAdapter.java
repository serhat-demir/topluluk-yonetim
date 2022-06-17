package com.serhat.toplulukyonetim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.model.Kayit;

import java.util.List;

public class KayitAdapter extends RecyclerView.Adapter<KayitAdapter.KayitHolder> {
    private Context context;
    private List<Kayit> kayitlar;

    public KayitAdapter(Context context, List<Kayit> kayitlar) {
        this.context = context;
        this.kayitlar = kayitlar;
    }

    public class KayitHolder extends RecyclerView.ViewHolder {
        public TextView cardKayitTxtIcerik, cardKayitTxtTarih;

        public KayitHolder(@NonNull View itemView) {
            super(itemView);

            cardKayitTxtIcerik = itemView.findViewById(R.id.cardKayitTxtIcerik);
            cardKayitTxtTarih = itemView.findViewById(R.id.cardKayitTxtTarih);
        }
    }

    @NonNull
    @Override
    public KayitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KayitHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_kayit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull KayitHolder holder, int position) {
        Kayit kayit = kayitlar.get(position);

        holder.cardKayitTxtIcerik.setText(kayit.getKayitIcerik());
        holder.cardKayitTxtTarih.setText(kayit.getKayitTarih().substring(0, 16));
    }

    @Override
    public int getItemCount() {
        return kayitlar.size();
    }
}
