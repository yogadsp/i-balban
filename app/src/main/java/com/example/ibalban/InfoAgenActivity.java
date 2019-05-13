package com.example.ibalban;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class InfoAgenActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;
    TextView nikTV;
    TextView namaTV;
    TextView alamatTV;
    TextView kecTV;
    TextView kabkotaTV;
    TextView provTV;

    String Semail = null;
    String emaill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_agen);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // mendapatkan email dari akun google untuk primary key
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(InfoAgenActivity.this);
        if (acct != null) {
            Semail = acct.getEmail();
        }

        emaill = Semail.replace(".", ",");

        nikTV = findViewById(R.id.editNIK);
        namaTV = findViewById(R.id.editNama);
        alamatTV = findViewById(R.id.editAlamat);
        kecTV = findViewById(R.id.editKec);
        kabkotaTV = findViewById(R.id.editKabKota);
        provTV = findViewById(R.id.editProv);

        mDatabase.child("Agen").child(emaill).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Mengambil value dari salah satu child, dan akan memicu data baru setiap kali diubah
                Agen data_agen = dataSnapshot.getValue(Agen.class);
                nikTV.setText(data_agen.getNIK());
                namaTV.setText(data_agen.getNama());
                alamatTV.setText(data_agen.getAlamat());
                kecTV.setText(data_agen.getKecamatan());
                kabkotaTV.setText(data_agen.getKabKota());
                provTV.setText(data_agen.getProvinsi());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Digunakan untuk menangani kejadian Error
                Log.e("MyListData", "Error: ", databaseError.toException());
            }
        });

        findViewById(R.id.btnUbahAgen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoAgenActivity.this, EditAgenActivity.class));
            }
        });
    }
}

