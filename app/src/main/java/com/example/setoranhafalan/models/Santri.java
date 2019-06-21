package com.example.setoranhafalan.models;

public class Santri {

    private String id;
    private String nama;
    private String kelas;
    private String absen;
    private String email;
    private String pass;

    public Santri(String id, String nama, String kelas, String absen, String email, String pass) {
        this.id = id;
        this.nama = nama;
        this.kelas = kelas;
        this.absen = absen;
        this.email = email;
        this.pass = pass;
    }

    public Santri(String nama, String kelas, String absen, String email, String pass) {
        this.nama = nama;
        this.kelas = kelas;
        this.absen = absen;
        this.email = email;
        this.pass = pass;
    }

    public Santri(String nama, String kelas, String absen) {
        this.nama = nama;
        this.kelas = kelas;
        this.absen = absen;
    }

    public Santri() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getKelas() {
        return kelas;
    }

    public String getAbsen() {
        return absen;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}
