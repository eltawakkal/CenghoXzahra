package com.example.setoranhafalan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setoranhafalan.R;
import com.example.setoranhafalan.models.Setoran;

import java.util.List;

public class SetoranAdapter extends RecyclerView.Adapter<SetoranAdapter.SetoranViewHolder> {

    private List<Setoran> listSetoran;
    private Context context;

    public SetoranAdapter(List<Setoran> listSetoran, Context context) {
        this.listSetoran = listSetoran;
        this.context = context;
    }

    @NonNull
    @Override
    public SetoranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewSetoran = LayoutInflater.from(context)
                .inflate(R.layout.row_setoran, parent, false);
        SetoranViewHolder setoranViewHolder = new SetoranViewHolder(viewSetoran);

        return setoranViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetoranViewHolder holder, int position) {

        holder.setItems(listSetoran.get(position));

        if (position == listSetoran.size() -1) {
            holder.vBtmLine.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listSetoran.size();
    }

    public class SetoranViewHolder extends RecyclerView.ViewHolder {

        TextView tvTgl;
        TextView tvSurah;
        TextView tvAyat;
        View vBtmLine;

        public SetoranViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTgl = itemView.findViewById(R.id.tv_tgl_row_setoran);
            tvSurah = itemView.findViewById(R.id.tv_surah_row_setoran);
            tvAyat = itemView.findViewById(R.id.tv_ayah_row_setoran);
            vBtmLine = itemView.findViewById(R.id.view_line_btm_setoran);

        }

        void setItems(Setoran setoran) {
            tvTgl.setText(setoran.getTgl());
            tvSurah.setText(setoran.getSurah());
            tvAyat.setText(setoran.getAyat());
        }

    }
}
