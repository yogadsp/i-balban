package com.example.ibalban;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAgenActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private EditText inNIK, inNama, inAlamat, inKec, inKabKota, inProv;
    GoogleSignInClient mGoogleSignInClient;
    String Semail = null;
    String emaill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agen);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // mendapatkan email dari akun google untuk primary key
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(EditAgenActivity.this);
        if (acct != null) {
            Semail = acct.getEmail();
        }

        emaill = Semail.replace(".", ",");

        inNIK = findViewById(R.id.editNIK);
        inNama = findViewById(R.id.editNama);
        inAlamat = findViewById(R.id.editAlamat);
        inKec = findViewById(R.id.editKec);
        inKabKota = findViewById(R.id.editKabKota);
        inProv = findViewById(R.id.editProv);

        mDatabase.child("Agen").child(emaill).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Mengambil value dari salah satu child, dan akan memicu data baru setiap kali diubah
                Agen data_agen = dataSnapshot.getValue(Agen.class);
                inNIK.setText(data_agen.getNIK());
                inNama.setText(data_agen.getNama());
                inAlamat.setText(data_agen.getAlamat());
                inKec.setText(data_agen.getKecamatan());
                inKabKota.setText(data_agen.getKabKota());
                inProv.setText(data_agen.getProvinsi());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Digunakan untuk menangani kejadian Error
                Log.e("MyListData", "Error: ", databaseError.toException());
            }
        });


        findViewById(R.id.btnEditAgen).setOnClickListener(new View.OnClickListener() {
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

                editAgen(new Agen(
                        Snik, Snama, Salamat, Skec, Skabkota, Sprov
                ), emaill);


            }
        });
    }

    private void editAgen(Agen ag, String id){
        mDatabase.child("Agen")
                // membuat custom key dengan menghilangkan push
                .child(id)
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

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditAgenActivity.this);
                        builder1.setMessage("Berhasil mengubah info !")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert1 = builder1.create();
                        alert1.show();

                        Intent intent = new Intent(EditAgenActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
