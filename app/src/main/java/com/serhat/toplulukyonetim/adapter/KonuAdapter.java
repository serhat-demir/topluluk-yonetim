package com.serhat.toplulukyonetim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.serhat.toplulukyonetim.KonuDetayActivity;
import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.ToplulukActivity;
import com.serhat.toplulukyonetim.model.Konu;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

public class KonuAdapter extends RecyclerView.Adapter<KonuAdapter.KonuHolder> {
    private Context context;
    private List<Konu> konular;

    public KonuAdapter(Context context, List<Konu> konular) {
        this.context = context;
        this.konular = konular;
    }

    public class KonuHolder extends RecyclerView.ViewHolder {
        public CardView cardKonu;
        public TextView cardKonuTxtBaslik, cardKonuTxtYorum, cardKonuTxtSabit, cardKonuTxtYazar, cardKonuTxtTarih;
        public ImageView cardKonuImgSabit;

        public KonuHolder(@NonNull View itemView) {
            super(itemView);

            cardKonu = itemView.findViewById(R.id.cardKonu);
            cardKonuTxtBaslik = itemView.findViewById(R.id.cardKonuTxtBaslik);
            cardKonuTxtYorum = itemView.findViewById(R.id.cardKonuTxtYorum);
            cardKonuTxtSabit = itemView.findViewById(R.id.cardKonuTxtSabit);
            cardKonuTxtYazar = itemView.findViewById(R.id.cardKonuTxtYazar);
            cardKonuTxtTarih = itemView.findViewById(R.id.cardKonuTxtTarih);
            cardKonuImgSabit = itemView.findViewById(R.id.cardKonuImgSabit);
        }
    }

    @NonNull
    @Override
    public KonuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KonuHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_konu, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KonuHolder holder, int position) {
        Konu konu = konular.get(position);

        holder.cardKonu.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                Intent intent = new Intent(context, KonuDetayActivity.class);
                intent.putExtra(Utils.INTENT_STRING_EXTRA_KONU_ID, String.valueOf(konu.getKonuId()));
                context.startActivity(intent);
                ((ToplulukActivity) context).finish();
            }
        });

        holder.cardKonuTxtBaslik.setText(Utils.stringToTitleCase(konu.getKonuBaslik()));
        holder.cardKonuTxtYorum.setText(konu.getKonuYorumSayisi() + " Yorum");
        holder.cardKonuTxtYazar.setText(Utils.stringToTitleCase(konu.getKonuYazarAd()) + ",");
        holder.cardKonuTxtTarih.setText(konu.getKonuTarih().substring(0, 16));

        if (konu.getKonuSabit() == Utils.KONU_SABIT) {
            holder.cardKonuImgSabit.setVisibility(View.VISIBLE);
            holder.cardKonuTxtSabit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return konular.size();
    }
}
