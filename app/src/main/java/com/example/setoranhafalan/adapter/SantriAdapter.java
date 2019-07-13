package com.example.setoranhafalan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.setoranhafalan.R;
import com.example.setoranhafalan.activity.DetailSetoranActivity;
import com.example.setoranhafalan.helper.MySharedPref;
import com.example.setoranhafalan.models.Santri;
import java.util.List;

public class SantriAdapter extends RecyclerView.Adapter<SantriAdapter.SantriViewHolder> {

    private List<Santri> listSantri;
    private Activity context;

    public SantriAdapter(List<Santri> listSantri, Activity context) {
        this.listSantri = listSantri;
        this.context = context;
    }

    @NonNull
    @Override
    public SantriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewSantri = LayoutInflater.from(context)
                .inflate(R.layout.row_santri, parent, false);
        SantriViewHolder santriViewHolder = new SantriViewHolder(viewSantri);

        return santriViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SantriViewHolder holder, final int position) {
        holder.setItems(listSantri.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailSetoranActivity.class);
                intent.putExtra(MySharedPref.ID_KEY, listSantri.get(position).getId());
                intent.putExtra(MySharedPref.NAMA_KEY, listSantri.get(position).getNama());
                intent.putExtra(MySharedPref.KELAS_KEY, listSantri.get(position).getKelas());
                intent.putExtra(MySharedPref.ABSEN_KEY, listSantri.get(position).getAbsen());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSantri.size();
    }

    public class SantriViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama;
        TextView tvNoAbsen;
        TextView tvKelas;

        public SantriViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_nama_row_user);
            tvNoAbsen = itemView.findViewById(R.id.tv_absen_row_user);
            tvKelas = itemView.findViewById(R.id.tv_kelas_row_user);
        }

        void setItems(Santri santri) {
            tvNoAbsen.setText(santri.getAbsen());
            tvKelas.setText(santri.getKelas());
            tvNama.setText(santri.getNama());
        }
    }

    public void updateData(List<Santri> newListSantri) {
        this.listSantri = newListSantri;

        notifyDataSetChanged();
    }
}
