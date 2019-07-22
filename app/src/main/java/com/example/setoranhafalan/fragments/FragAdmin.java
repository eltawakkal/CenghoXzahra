package com.example.setoranhafalan.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setoranhafalan.R;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragAdmin extends Fragment {

    //    firebase
    private DatabaseReference refSantri;

    //    Views
    private Toolbar toolbar;
    private RecyclerView recSantri;
    private RelativeLayout rlMain;
    private AlertDialog.Builder alertDialog;
    private Dialog dialog;
    private SearchView srcSantri;
    private FloatingActionButton fabAddUser;

    private EditText edtNama;
    private EditText edtKelas;
    private EditText edtAbsen;

    //    object
    private SantriAdapter adapter;
    private List<Santri> listSantri;
    private MySharedPref myPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_main_admin, container, false);

        initView(v);

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddSantri();
            }
        });

        srcSantri.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchSantri(s);

                return false;
            }
        });

        srcSantri.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getAllSantri();
                return false;
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getAllSantri();
    }

    void searchSantri(String name) {

        List<Santri> newListSantri = new ArrayList<>();

        for (Santri santri : listSantri) {
            if (santri.getNama().toLowerCase().contains(name)) {
                newListSantri.add(santri);
            }
        }

        adapter.updateData(newListSantri);
    }

    void getAllSantri() {
        refSantri.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSantri.clear();

                for (DataSnapshot snapSantri : dataSnapshot.getChildren()) {
                    Santri santri = snapSantri.getValue(Santri.class);
                    santri.setId(snapSantri.getKey());

                    listSantri.add(santri);
                }

                setupRecycleritems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Error: " + databaseError.getDetails());
            }
        });
    }

    private void setupRecycleritems() {
        adapter = new SantriAdapter(listSantri, getActivity());

        recSantri.setLayoutManager(new LinearLayoutManager(getContext()));
        recSantri.setAdapter(adapter);
    }

    private void initView(View v) {
        refSantri = FirebaseDatabase.getInstance().getReference("santri");

        toolbar = v.findViewById(R.id.toolbar_list_santri);
        recSantri = v.findViewById(R.id.rec_list_santri);
        fabAddUser = v.findViewById(R.id.fab_add_user);
        rlMain = v.findViewById(R.id.rl_nain);
        srcSantri = v.findViewById(R.id.search_main);

        listSantri = new ArrayList<>();
        myPref = new MySharedPref(getActivity());
    }

    private void showMessage(String message) {
        Snackbar.make(rlMain, message, Snackbar.LENGTH_SHORT).show();
    }

    void showDialogAddSantri() {

        View viewAddSantri = LayoutInflater.from(getContext())
                .inflate(R.layout.view_add_santri, null);

        edtNama = viewAddSantri.findViewById(R.id.edt_nama_view_santri);
        edtKelas = viewAddSantri.findViewById(R.id.edt_kelas_view_santri);
        edtAbsen = viewAddSantri.findViewById(R.id.edt_absen_view_santri);
        MaterialButton mbtAddSantri = viewAddSantri.findViewById(R.id.mbt_add_santri);

        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setView(viewAddSantri);

        dialog = alertDialog.create();
        dialog.show();

        mbtAddSantri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = refSantri.push().getKey();

                String nama =  edtNama.getText().toString();
                String kelas = edtKelas.getText().toString();
                String absen = edtAbsen.getText().toString();

                Santri santri = new Santri(nama, kelas, absen, null, null);

                refSantri.child(id).setValue(santri)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("Data Santri Ditambahkan");
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage("Error: " + e.toString());
                            }
                        });
            }
        });
    }
}
