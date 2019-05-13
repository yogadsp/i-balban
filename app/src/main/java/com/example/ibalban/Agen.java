package com.example.ibalban;

public class Agen {
    private String nik;
    private String nama;
    private String alamat;
    private String kecamatan;
    private String kabkota;
    private String provinsi;

    private String key;

    Agen(){}

    Agen(String nik, String nama, String alamat, String kecamatan, String kabkota, String provinsi){
        this.nik = nik;
        this.nama = nama;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kabkota = kabkota;
        this.provinsi = provinsi;
    }

    public String getNIK(){ return nik; }
    public String getNama(){ return nama; }
    public String getAlamat(){ return alamat; }
    public String getKecamatan(){ return kecamatan; }
    public String getKabKota(){ return kabkota; }
    public String getProvinsi(){ return provinsi; }

    public void setNIK(String nik){ this.nik = nik; }
    public void setNama(String nama){ this.nama = nama; }
    public void setAlamat(String alamat){ this.alamat = alamat; }
    public void setKecamatan(String kecamatan){ this.kecamatan = kecamatan; }
    public void setKabKota(String kabkota){ this.kabkota = kabkota; }
    public void setProvinsi(String provinsi){ this.provinsi = provinsi; }

    @Override
    public String toString(){
        return " " + nik + "\n" +
                " " + nama + "\n" +
                " " + alamat + "\n" +
                " " + kecamatan + "\n" +
                " " + kabkota + "\n" +
                " " + provinsi;
    }


}
