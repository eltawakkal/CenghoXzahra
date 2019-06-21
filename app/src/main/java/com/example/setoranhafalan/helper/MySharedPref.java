package com.example.setoranhafalan.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    public static final String ID_KEY = "ID";
    public static final String NAMA_KEY = "NAMA";
    public static final String KELAS_KEY = "KELAS";
    public static final String ABSEN_KEY = "ABSEN";
    public static final String EMAIL_KEY = "EMAIL";
    public static final String PASS_KEY = "PASS";
    private static final String PREF_NAME = "CengPref";

    private Context context;

    private SharedPreferences myPref;
    private SharedPreferences.Editor myEditor;

    public MySharedPref(Context context) {
        this.context = context;

        myPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void addUser(String id, String nama, String kelas, String absen, String email, String pass) {
        myEditor = myPref.edit();

        myEditor.putString(ID_KEY, id);
        myEditor.putString(NAMA_KEY, nama);
        myEditor.putString(NAMA_KEY, kelas);
        myEditor.putString(ABSEN_KEY, absen);
        myEditor.putString(EMAIL_KEY, email);
        myEditor.putString(PASS_KEY, pass);

        myEditor.commit();
    }

    public void removeAdmin() {
        myEditor = myPref.edit();
        myEditor.clear();
        myEditor.commit();
    }

    public String getId() {
        return myPref.getString(ID_KEY, null);
    }

    public String getNamaKey() {
        return myPref.getString(NAMA_KEY, null);
    }

    public String getKelasKey() {
        return myPref.getString(KELAS_KEY, null);
    }

    public String getAbsenKey() {
        return myPref.getString(ABSEN_KEY, null);
    }

    public String getEmailKey() {
        return myPref.getString(EMAIL_KEY, null);
    }

    public String getPassKey() {
        return myPref.getString(PASS_KEY, null);
    }
}
