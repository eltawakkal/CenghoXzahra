package com.example.setoranhafalan.models;

public class Setoran {

    private String id;
    private String idSantri;
    private String ayat;
    private String juz;
    private String surah;
    private String tgl;

    public Setoran(String id, String idSantri, String ayat, String juz, String surah, String tgl) {
        this.id = id;
        this.idSantri = idSantri;
        this.ayat = ayat;
        this.juz = juz;
        this.surah = surah;
        this.tgl = tgl;
    }

    public Setoran(String idSantri, String ayat, String juz, String surah, String tgl) {
        this.idSantri = idSantri;
        this.ayat = ayat;
        this.juz = juz;
        this.surah = surah;
        this.tgl = tgl;
    }

    public Setoran(String ayat, String juz, String surah, String tgl) {
        this.ayat = ayat;
        this.juz = juz;
        this.surah = surah;
        this.tgl = tgl;
    }

    public Setoran() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getIdSantri() {
        return idSantri;
    }

    public String getAyat() {
        return ayat;
    }

    public String getJuz() {
        return juz;
    }

    public String getSurah() {
        return surah;
    }

    public String getTgl() {
        return tgl;
    }
}
