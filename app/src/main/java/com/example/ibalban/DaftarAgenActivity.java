package com.example.ibalban;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class DaftarAgenActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText inNIK, inNama, inAlamat, inKec, inKabKota, inProv;
    GoogleSignInClient mGoogleSignInClient;
    String Semail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_agen);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        inNIK = findViewById(R.id.editNIK);
        inNama = findViewById(R.id.editNama);
        inAlamat = findViewById(R.id.editAlamat);
        inKec = findViewById(R.id.editKec);
        inKabKota = findViewById(R.id.editKabKota);
        inProv = findViewById(R.id.editProv);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // mendapatkan email dari akun google untuk primary key
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DaftarAgenActivity.this);
        if (acct != null) {
            Semail = acct.getEmail();
        }

        findViewById(R.id.btnDaftarAgen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Snik = inNIK.getText().toString();
                String Snama = inNama.getText().toString();
                String Salamat = inAlamat.getText().toString();
                String Skec = inKec.getText().toString();
                String Skabkota = inKabKota.getText().toString();
                String Sprov = inProv.getText().toString();

                if(Snik.equals("")){
                    inNIK.setError("NIK harus diisi");

                }
                if(Snama.equals("")){
                    inNama.setError("Nama harus diisi");

                }
                if(Salamat.equals("")){
                    inAlamat.setError("Alamat harus diisi");

                }
                if(Skec.equals("")){
                    inKec.setError("Kecamatan harus diisi");

                }
                if(Skabkota.equals("")){
                    inKabKota.setError("Kabupaten / Kota harus diisi");

                }
                if(Sprov.equals("")){
                    inProv.setError("Provinsi harus diisi");

                }

                    submitAgen(new Agen(
                            Snik, Snama, Salamat, Skec, Skabkota, Sprov
                    ));


            }
        });
    }

    // fungsi untuk memgubah titik menjadi koma
    // karena dalam firebase tidak diizinkan titik
    static String encodeEmail(String email) {
        return email.replace(".", ",");
    }

    static String decodeEmail(String email) {
        return email.replace(",", ".");
    }

    private void submitAgen(Agen ag){
        mDatabase.child("Agen")
                // membuat custom key dengan menghilangkan push
                 .child(encodeEmail(Semail))
//                 .push()
                 .setValue(ag)
                 .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         inNIK.setText("");
                         inNama.setText("");
                         inAlamat.setText("");
                         inKec.setText("");
                         inKabKota.setText("");
                         inProv.setText("");

                         AlertDialog.Builder builder1 = new AlertDialog.Builder(DaftarAgenActivity.this);
                         builder1.setMessage("Berhasil menjadi Agen !")
                                 .setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         //do things
                                     }
                                 });
                         AlertDialog alert1 = builder1.create();
                         alert1.show();

                         AlertDialog.Builder builder = new AlertDialog.Builder(DaftarAgenActivity.this);
                         builder.setMessage("Agen dapat menambahkan lokasi tambal ban dengan cara : \n " +
                                 "Menahan beberapa detik lokasi yang akan ditambahkan pada Map")
                                 .setCancelable(false)
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         //do things
                                     }
                                 });
                         AlertDialog alert = builder.create();
                         alert.show();

                         Intent intent = new Intent(DaftarAgenActivity.this, MapsActivity.class);
                         startActivity(intent);
                     }
                 });

    }

}
