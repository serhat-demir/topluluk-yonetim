package com.serhat.toplulukyonetim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serhat.toplulukyonetim.R;
import com.serhat.toplulukyonetim.model.Bildirim;
import com.serhat.toplulukyonetim.utils.Utils;

import java.util.List;

public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.BildirimHolder> {
    private Context context;
    private List<Bildirim> bildirimler;

    public BildirimAdapter(Context context, List<Bildirim> bildirimler) {
        this.context = context;
        this.bildirimler = bildirimler;
    }

    public class BildirimHolder extends RecyclerView.ViewHolder {
        private View cardBildirimViewBildirimOkundu;
        private TextView cardBildirimTxtBildirimIcerik;

        public BildirimHolder(@NonNull View itemView) {
            super(itemView);

            cardBildirimViewBildirimOkundu = itemView.findViewById(R.id.cardBildirimViewBildirimOkundu);
            cardBildirimTxtBildirimIcerik = itemView.findViewById(R.id.cardBildirimTxtBildirimIcerik);
        }
    }

    @NonNull
    @Override
    public BildirimHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BildirimHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bildirim, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BildirimHolder holder, int position) {
        Bildirim bildirim = bildirimler.get(position);

        holder.cardBildirimTxtBildirimIcerik.setText(bildirim.getBildirimIcerik());

        if (bildirim.getBildirimOkundu().equals(Utils.BILDIRIM_OKUNMADI)) {
            holder.cardBildirimViewBildirimOkundu.setBackgroundColor(context.getResources().getColor(R.color.light_teal));
        }
    }

    @Override
    public int getItemCount() {
        return bildirimler.size();
    }
}
