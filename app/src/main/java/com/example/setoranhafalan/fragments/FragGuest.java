package com.example.setoranhafalan.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setoranhafalan.R;
import com.example.setoranhafalan.activity.DetailSetoranActivity;
import com.example.setoranhafalan.adapter.SantriAdapter;
import com.example.setoranhafalan.helper.MySharedPref;
import com.example.setoranhafalan.models.Santri;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragGuest extends Fragment {

    //    firebase
    private DatabaseReference refSantri;

    //    Views
    private SearchView srcSantri;
    private MaterialButton mbtSearh;
    private LinearLayout llParent;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_main_guest, container, false);

        initView(v);

        srcSantri.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                findSantri(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mbtSearh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findSantri(srcSantri.getQuery().toString());
            }
        });

        return v;
    }

    private void findSantri(String s) {
        progressDialog.show();

        Query query = refSantri.orderByChild("absen").equalTo(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Santri santri = null;

                for (DataSnapshot snapSantri : dataSnapshot.getChildren()) {
                    santri = snapSantri.getValue(Santri.class);
                    santri.setId(snapSantri.getKey());
                }

                if (santri == null) {
                    showMessage("Santri Tidak Ditemukan");
                } else {
                    gotoDetailSantri(santri);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Terjadi Kesalahan");
                progressDialog.dismiss();
            }
        });
    }

    private void gotoDetailSantri(Santri santri) {
        Intent intent = new Intent(getActivity(), DetailSetoranActivity.class);
        intent.putExtra(MySharedPref.ID_KEY, santri.getId());
        intent.putExtra(MySharedPref.NAMA_KEY, santri.getNama());
        intent.putExtra(MySharedPref.KELAS_KEY, santri.getKelas());
        intent.putExtra(MySharedPref.ABSEN_KEY, santri.getAbsen());

        getActivity().startActivity(intent);
    }

    private void initView(View v) {
        refSantri = FirebaseDatabase.getInstance().getReference("santri");
        srcSantri = v.findViewById(R.id.search_main_guest);
        mbtSearh = v.findViewById(R.id.mbt_searh_guest);
        llParent = v.findViewById(R.id.ll_parent_guest);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Mencari Data...");
    }

    private void showMessage(String message) {
        Snackbar.make(llParent, message, Snackbar.LENGTH_SHORT).show();
    }
}
