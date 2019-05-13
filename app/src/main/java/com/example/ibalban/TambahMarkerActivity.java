package com.example.ibalban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahMarkerActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText inNamaTambal, inJenisBan, inJenisKen, inNoHP;
    GoogleSignInClient mGoogleSignInClient;
    String Semail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_marker);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        inNamaTambal = findViewById(R.id.editNamaTambal);
        inJenisBan = findViewById(R.id.editJenisBan);
        inJenisKen = findViewById(R.id.editJenisKen);
        inNoHP = findViewById(R.id.editNoHP);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // mendapatkan email dari akun google untuk primary key
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(TambahMarkerActivity.this);
        if (acct != null) {
            Semail = acct.getEmail();
        }

        findViewById(R.id.btnTambahMarker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Snama_tambal = inNamaTambal.getText().toString();
                String Sjenis_ban = inJenisBan.getText().toString();
                String Sjenis_ken = inJenisKen.getText().toString();
                String Sno_hp = inNoHP.getText().toString();
                String SSemail = Semail.replace(".", ",");
                String Slat= getIntent().getStringExtra("lat");
                String Slong= getIntent().getStringExtra("long");

                if(Snama_tambal.equals("")){
                    inNamaTambal.setError("Nama Tambal Ban harus diisi");

                }
                if(Sjenis_ban.equals("")){
                    inJenisBan.setError("Jenis Ban harus diisi");

                }
                if(Sjenis_ken.equals("")){
                    inJenisKen.setError("Jenis Kendaraan harus diisi");

                }
                if(Sno_hp.equals("")){
                    inNoHP.setError("No Handphone harus diisi");

                }
                if(Slat.equals("")){
                    Toast.makeText(TambahMarkerActivity.this
                            , "Latitude Kosong"
                            ,Toast.LENGTH_SHORT).show();
                }
                if(Slong.equals("")){
                    Toast.makeText(TambahMarkerActivity.this
                            , "Longitude"
                            ,Toast.LENGTH_SHORT).show();

                }

                if(Snama_tambal != null && Sjenis_ban != null
                        && Sjenis_ken != null && Sno_hp != null && SSemail != null
                        && SSemail != null && Slat != null && Slong != null) {
                    submitMarker(new TambalBan(
                            Snama_tambal, Sjenis_ban, Sjenis_ken, Sno_hp, SSemail, Slat, Slong
                    ));
                }


            }
        });
    }

    private void submitMarker(TambalBan ta){
        mDatabase.child("Tambal_Ban")
                .push()
                .setValue(ta)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(TambahMarkerActivity.this
                                , "Berhasil Menambah Informasi"
                                ,Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(TambahMarkerActivity.this, MapsActivity.class));

                    }
                });

    }
}
