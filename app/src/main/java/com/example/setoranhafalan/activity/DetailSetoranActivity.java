package com.example.setoranhafalan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.setoranhafalan.R;
import com.example.setoranhafalan.adapter.SetoranAdapter;
import com.example.setoranhafalan.helper.MySharedPref;
import com.example.setoranhafalan.models.Santri;
import com.example.setoranhafalan.models.Setoran;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailSetoranActivity extends AppCompatActivity {

//    firebase
    private DatabaseReference refSetoran;
    private DatabaseReference refSantri;

//    Views
    private RecyclerView recSetoran;
    private FloatingActionButton fabAddSetoran;
    private CoordinatorLayout clMain;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    private EditText edtNama;
    private EditText edtKelas;
    private EditText edtAbsen;

//    Objects
    private SetoranAdapter adapter;
    private List<Setoran> listSetoran;
    private AlertDialog.Builder alertDialog;
    private Dialog dialog;
    private MySharedPref myPref;

//    Variables
    private String id;
    private String nama;
    private String kelas;
    private String absen;
    private String textForShare = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setoran);

        initView();

        fabAddSetoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddSetoran();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setoran, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionn_share_setorann:
                shareDataSetoran();
                break;
            case R.id.action_udpdate_santri:
                showDialogEditSantri(nama, kelas, absen);
                break;
            case R.id.actionn_delete_santri:
                showDeleteDialog(nama);
        }

        return true;
    }

    private void shareDataSetoran() {
        if (listSetoran.size() == 0) {
            showMessage("Belum ada data setoran");
        } else {
            textForShare = "";
            textForShare += "List Setoran Hafalan Akhi " + nama + "\n\n";

            for (Setoran setoran : listSetoran) {
                textForShare += "[" + setoran.getTgl() + "]\n" + "Surah : " + setoran.getSurah() + "\nAyat : " + setoran.getAyat() + "\n\n";
            }

            textForShare += "Di share melalui aplikasi *Setoran Hafalan Cengho*";

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Rekap Laporan Akhi " + nama);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, textForShare);
            startActivity(Intent.createChooser(sharingIntent, "Bagikan Rekap Setoran"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = refSetoran.orderByChild("idSantri").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSetoran.clear();

                for (DataSnapshot snapSetoran : dataSnapshot.getChildren()) {
                    Setoran setoran = snapSetoran.getValue(Setoran.class);
                    setoran.setId(snapSetoran.getKey());

                    listSetoran.add(setoran);
                }

                setupRecyclerItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRecyclerItems() {
        adapter = new SetoranAdapter(listSetoran, this);

        recSetoran.setLayoutManager(new LinearLayoutManager(this));
        recSetoran.setAdapter(adapter);
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        refSetoran = FirebaseDatabase.getInstance().getReference("setoran");
        refSantri = FirebaseDatabase.getInstance().getReference("santri");

        id = getIntent().getStringExtra(MySharedPref.ID_KEY);
        nama = getIntent().getStringExtra(MySharedPref.NAMA_KEY);
        kelas = getIntent().getStringExtra(MySharedPref.KELAS_KEY);
        absen = getIntent().getStringExtra(MySharedPref.ABSEN_KEY);

        recSetoran = findViewById(R.id.rec_setoran);
        fabAddSetoran = findViewById(R.id.fab_add_setoran);
        clMain = findViewById(R.id.setoran_view);
        toolbar = findViewById(R.id.toolbar_setoran);
        collapsingToolbarLayout = findViewById(R.id.collaps_setoran);

        setSupportActionBar(toolbar);
        setTitle(nama);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listSetoran = new ArrayList<>();
        myPref = new MySharedPref(this);

        if (myPref.getId() == null) {
            fabAddSetoran.setVisibility(View.GONE);
        } else {
            fabAddSetoran.setVisibility(View.VISIBLE);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(clMain, message, Snackbar.LENGTH_SHORT).show();
    }

    void showDialogAddSetoran() {

        //TODO initialize all the obejects

        View viewAddSetoran = LayoutInflater.from(this)
                .inflate(R.layout.view_add_setorani, null);

        final Spinner edtSurah = viewAddSetoran.findViewById(R.id.edt_surah_view_surah);
        final Spinner edtJuz = viewAddSetoran.findViewById(R.id.edt_juz_view_surah);
        final EditText edtAyat = viewAddSetoran.findViewById(R.id.edt_ayat_view_surah);
        MaterialButton mbtAddSetoran = viewAddSetoran.findViewById(R.id.mbt_add_setoran);

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(viewAddSetoran);

        dialog = alertDialog.create();
        dialog.show();

        mbtAddSetoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM yyyy");

                String currentDate = dateFormat.format(date);
                String setoranId = refSetoran.push().getKey();

                String surah = edtSurah.getSelectedItem().toString();
                String juz = edtJuz.getSelectedItem().toString();
                String ayat = edtAyat.getText().toString();

                Setoran setoran = new Setoran(id, ayat, juz, surah, currentDate);

                refSetoran.child(setoranId).setValue(setoran)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("Setoran Berhasil Ditamn");
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

    void showDeleteDialog(String name) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog
                .setTitle("Hapus Data")
                .setMessage("Apakah data ananda " + name + " Akan Dihapus?")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refSantri.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailSetoranActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", null)
                .create().show();
    }

    void showDialogEditSantri(final String name, String kelas, String absen) {

        View viewAddSantri = LayoutInflater.from(this)
                .inflate(R.layout.view_add_santri, null);

        edtNama = viewAddSantri.findViewById(R.id.edt_nama_view_santri);
        edtKelas = viewAddSantri.findViewById(R.id.edt_kelas_view_santri);
        edtAbsen = viewAddSantri.findViewById(R.id.edt_absen_view_santri);

        edtNama.setText(name);
        edtKelas.setText(kelas);
        edtAbsen.setText(absen);

        MaterialButton mbtAddSantri = viewAddSantri.findViewById(R.id.mbt_add_santri);
        mbtAddSantri.setText("Udpate Data Santri");

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(viewAddSantri);

        dialog = alertDialog.create();
        dialog.show();

        mbtAddSantri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nama =  edtNama.getText().toString();
                String kelas = edtKelas.getText().toString();
                String absen = edtAbsen.getText().toString();

                Santri santri = new Santri(nama, kelas, absen, null, null);

                refSantri.child(id).setValue(santri)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("Data Santri Diupdate");
                                collapsingToolbarLayout.setTitle(nama);
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
