package com.example.setoranhafalan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.setoranhafalan.R;
import com.example.setoranhafalan.helper.MySharedPref;
import com.example.setoranhafalan.models.Santri;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

//    firebase
    private DatabaseReference refAdmin;

//    views
    private EditText edtEmail;
    private EditText edtPass;
    private MaterialButton mbtLogin;
    private TextView tvGuest;
    private LinearLayout llParent;

//    objects
    private MySharedPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lgoin);

        initView();

        mbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();

                checkAdmin(email, pass);
            }
        });

        tvGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
            }
        });
    }

    private void checkAdmin(String email, final String pass) {
        Query queryAdmin = refAdmin.orderByChild("email").equalTo(email);
        queryAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {

                    for (DataSnapshot snapAdmin : dataSnapshot.getChildren()) {
                        Santri santri = snapAdmin.getValue(Santri.class);
                        santri.setId(snapAdmin.getKey());

                        if (santri.getPass().equals(pass)) {
                            updateSharedPref(santri);
                            updateUI();
                        } else {
                            showMessage("Password Tidak Benar");
                        }
                    }
                } else {
                    showMessage("Pengguna Tidak Dikenal");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Error: " + databaseError.getDetails());
            }
        });
    }

    private void showMessage(String message) {
        Snackbar.make(llParent, message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateSharedPref(Santri santri) {
        myPref.addUser(santri.getId(), santri.getNama(), santri.getKelas(), santri.getAbsen(), santri.getEmail(), santri.getPass());
    }

    private void updateUI() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void initView() {
        refAdmin = FirebaseDatabase.getInstance().getReference("admin");

        edtEmail = findViewById(R.id.edt_email_login);
        edtPass = findViewById(R.id.edt_pass_loginn);
        mbtLogin = findViewById(R.id.mbt_login);
        tvGuest = findViewById(R.id.tv_guest_login);
        llParent = findViewById(R.id.parent_view_login);

        myPref = new MySharedPref(this);

        String id = myPref.getId();

        if (id != null) {
            updateUI();
        }
    }
}
