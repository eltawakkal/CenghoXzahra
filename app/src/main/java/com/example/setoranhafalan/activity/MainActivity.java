package com.example.setoranhafalan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

public class MainActivity extends AppCompatActivity {

//    firebase
    private DatabaseReference refSantri;

//    Views
    private Toolbar toolbar;
    private RecyclerView recSantri;
    private FloatingActionButton fabAddUser;
    private RelativeLayout rlMain;
    private AlertDialog.Builder alertDialog;
    private Dialog dialog;

//    object
    private SantriAdapter adapter;
    private List<Santri> listSantri;
    private MySharedPref myPref;

//    itemMenu
    private MenuItem mnuLogout;
    private MenuItem mnuLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddSantri();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mnuLogout = menu.findItem(R.id.action_logout);
        mnuLogin = menu.findItem(R.id.action_login);

        if (myPref.getId() == null) {
            fabAddUser.setVisibility(View.INVISIBLE);
            mnuLogout.setVisible(false);
        } else {
            mnuLogin.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                myPref.removeAdmin();
                gotoLoginActivity();
                break;

            case R.id.action_login:
                gotoLoginActivity();
                break;
        }

        return true;
    }

    private void gotoLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

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
        adapter = new SantriAdapter(listSantri, this);

        recSantri.setLayoutManager(new LinearLayoutManager(this));
        recSantri.setAdapter(adapter);
    }

    private void initView() {
        refSantri = FirebaseDatabase.getInstance().getReference("santri");

        toolbar = findViewById(R.id.toolbar_list_santri);
        recSantri = findViewById(R.id.rec_list_santri);
        fabAddUser = findViewById(R.id.fab_add_user);
        rlMain = findViewById(R.id.rl_nain);

        listSantri = new ArrayList<>();
        myPref = new MySharedPref(this);

        setSupportActionBar(toolbar);
    }

    private void showMessage(String message) {
        Snackbar.make(rlMain, message, Snackbar.LENGTH_SHORT).show();
    }

    void showDialogAddSantri() {

        View viewAddSantri = LayoutInflater.from(this)
                .inflate(R.layout.view_add_santri, null);

        final EditText edtNama = viewAddSantri.findViewById(R.id.edt_nama_view_santri);
        final EditText edtKelas = viewAddSantri.findViewById(R.id.edt_kelas_view_santri);
        final EditText edtAbsen = viewAddSantri.findViewById(R.id.edt_absen_view_santri);
        MaterialButton mbtAddSantri = viewAddSantri.findViewById(R.id.mbt_add_santri);

        alertDialog = new AlertDialog.Builder(this);
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
