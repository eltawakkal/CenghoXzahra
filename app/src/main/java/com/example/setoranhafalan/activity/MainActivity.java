package com.example.setoranhafalan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.setoranhafalan.R;
import com.example.setoranhafalan.fragments.FragAdmin;
import com.example.setoranhafalan.fragments.FragGuest;
import com.example.setoranhafalan.helper.MySharedPref;

public class MainActivity extends AppCompatActivity {

    //object
    private MySharedPref myPref;

    //itemMenu
    private MenuItem mnuLogout;
    private MenuItem mnuLogin;

//    fragments
    private FragAdmin fragAdmin;
    private FragGuest fragGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();

        if (myPref.getId() == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_main, fragGuest).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_main, fragAdmin).commit();
        }
    }

    private void initview() {
        fragAdmin = new FragAdmin();
        fragGuest = new FragGuest();

        Toolbar toolbar = findViewById(R.id.toolbar_list_santri);

        setSupportActionBar(toolbar);

        myPref = new MySharedPref(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mnuLogout = menu.findItem(R.id.action_logout);
        mnuLogin = menu.findItem(R.id.action_login);

        if (myPref.getId() == null) {
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
}
