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

import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.ToplulukActivity;
import com.serhat.toplulukyonetim.TopluluklarActivity;
import com.serhat.toplulukyonetim.model.Topluluk;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

public class ToplulukAdapter extends RecyclerView.Adapter<ToplulukAdapter.ToplulukHolder> {
    private Context context;
    private List<Topluluk> topluluklar;

    public ToplulukAdapter(Context context, List<Topluluk> topluluklar) {
        this.context = context;
        this.topluluklar = topluluklar;
    }

    public class ToplulukHolder extends RecyclerView.ViewHolder {
        public CardView cardTopluluk;
        public TextView cardToplulukTxtToplulukAd, cardToplulukTxtToplulukAciklama, cardToplulukTxtToplulukUye, cardToplulukTxtToplulukGizlilik;
        public ImageView cardToplulukImgToplulukGizlilik;
        public View divider;

        public ToplulukHolder(@NonNull View itemView) {
            super(itemView);

            cardTopluluk = itemView.findViewById(R.id.cardKonuDetay);
            cardToplulukTxtToplulukAd = itemView.findViewById(R.id.cardKonuDetayTxtYazar);
            cardToplulukTxtToplulukAciklama = itemView.findViewById(R.id.cardKonuDetayTxtIcerik);
            cardToplulukTxtToplulukUye = itemView.findViewById(R.id.cardKonuDetayBtnSabitle);
            cardToplulukTxtToplulukGizlilik = itemView.findViewById(R.id.cardKonuDetayBtnDuzenle);
            cardToplulukImgToplulukGizlilik = itemView.findViewById(R.id.cardToplulukImgToplulukGizlilik);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    @NonNull
    @Override
    public ToplulukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToplulukHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_topluluk, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ToplulukHolder holder, int position) {
        Topluluk topluluk = topluluklar.get(position);

        holder.cardToplulukTxtToplulukAd.setText(Utils.stringToTitleCase(topluluk.getToplulukAd()));
        holder.cardToplulukTxtToplulukAciklama.setText(topluluk.getToplulukAciklama());
        holder.cardToplulukTxtToplulukUye.setText(topluluk.getToplulukUyeSayisi() + " Üye");

        if (topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_ACIK)) {
            holder.cardToplulukTxtToplulukGizlilik.setText(context.getResources().getString(R.string.toplulukAcik));
            holder.cardToplulukImgToplulukGizlilik.setImageResource(R.drawable.ic_unlock);
        } else if (topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_KAPALI)) {
            holder.cardToplulukTxtToplulukGizlilik.setText(context.getResources().getString(R.string.toplulukKapali));
            holder.cardToplulukImgToplulukGizlilik.setImageResource(R.drawable.ic_lock);
        } else if (topluluk.getToplulukGizlilik().equals(Utils.TOPLULUK_GIZLI)) {
            holder.cardToplulukTxtToplulukGizlilik.setText(context.getResources().getString(R.string.toplulukGizli));
            holder.cardToplulukImgToplulukGizlilik.setImageResource(R.drawable.ic_lock);
        }

        holder.cardTopluluk.setOnClickListener(view -> {
            if (Utils.InternetKontrol(context)) {
                Intent intent = new Intent(context, ToplulukActivity.class);
                intent.putExtra(Utils.INTENT_STRING_EXTRA_TOPLULUK_ID, topluluk.getToplulukId());
                context.startActivity(intent);
                ((TopluluklarActivity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return topluluklar.size();
    }
}
