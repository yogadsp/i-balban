package com.example.ibalban;

public class TambalBan {
    public String nama_tambal;
    public String jenis_ban;
    public String jenis_ken;
    public String no_hp;
    public String email;

    // latitude dan longitude untuk mendapatkan koordainat pada map
    public String lat;
    public String longi;

    public String key;

    TambalBan(){}

    TambalBan(String nama_tambal, String jenis_ban, String jenis_ken, String no_hp, String email, String lat, String longi){
        this.nama_tambal = nama_tambal;
        this.jenis_ban = jenis_ban;
        this.jenis_ken = jenis_ken;
        this.no_hp = no_hp;
        this.email = email;
        this.lat = lat;
        this.longi = longi;
    }

    public String getNamaTambal(){ return nama_tambal; }
    public String getJenisBan(){ return jenis_ban; }
    public String getJenisKen(){ return jenis_ken; }
    public String getNoHP(){ return no_hp; }
    public String getEmail(){ return email; }
    public String getLat(){ return lat; }
    public String getLongi(){ return longi; }
    public String getKey(){ return key; }

    public void setNamaTambal(){ this.nama_tambal = nama_tambal; }
    public void setJenisBan(){ this.jenis_ban = jenis_ban; }
    public void setJenisKen(){ this.jenis_ken = jenis_ken; }
    public void setNoHP(){ this.no_hp = no_hp; }
    public void setEmail(){ this.email = email; }
    public void setLat(){ this.lat = lat; }
    public void setLongi(){ this.longi = longi; }
    public void setKey(String key){ this.key = key; }

    @Override
    public String toString(){
        return " " + nama_tambal + "\n" +
                " " + jenis_ban + "\n" +
                " " + jenis_ken + "\n" +
                " " + no_hp + "\n" +
                " " + email + "\n" +
                " " + lat + "\n" +
                " " + longi;
    }
}
