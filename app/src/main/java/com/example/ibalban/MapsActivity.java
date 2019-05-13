package com.example.ibalban;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

//    private static final String TAG = "MapsActivity";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    GoogleSignInClient mGoogleSignInClient;

    String Semail = null;
    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    Marker marker;
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.btnRef).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.g_map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        // Check location permission is granted - if it is, start
//        // the service, otherwise request the permission
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST);
//        }
        TamBanList = new ArrayList<>();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted

        } else {
            finish();
        }
    }


//    // Setting a custom info window adapter for the google map
//    mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
//
//        // Use default InfoWindow frame
//        @Override
//        public View getInfoWindow(Marker arg0) {
//            return null;
//        }
//
//        // Defines the contents of the InfoWindow
//        @Override
//        public View getInfoContents(Marker arg0) {
//
//            // Getting view from the layout file info_window_layout
//            View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
//
//            // Getting the position from the marker
//            LatLng latLng = arg0.getPosition();
//
//            // Getting reference to the TextView to set latitude
//            TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
//
//            // Getting reference to the TextView to set longitude
//            TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
//
//            // Setting the latitude
//            tvLat.setText("Latitude:" + latLng.latitude);
//
//            // Setting the longitude
//            tvLng.setText("Longitude:"+ latLng.longitude);
//
//            // Returning the view containing InfoWindow contents
//            return v;
//
//        }
//    });

    Marker myMarker;
//    ArrayList<TambalBan> TamBanList;
//    Marker marker;
    List<TambalBan> TamBanList;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().isMapToolbarEnabled();

        mDatabase2.child("Tambal_Ban").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    TambalBan data_tb = s.getValue(TambalBan.class);

                    TamBanList.add(data_tb);
                    for (int i = 0; i < TamBanList.size(); i++)
                    {
                        double latitude = Double.parseDouble(data_tb.lat);
                        double longitude = Double.parseDouble(data_tb.longi);
                        LatLng location = new LatLng(latitude, longitude);
                        if (mMap != null) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(data_tb.nama_tambal)
                                    .snippet("Jenis Ban : " + data_tb.getJenisBan() + "\n"
                                                + "Jenis Kendaraan : " + data_tb.getJenisKen() + "\n"
                                                + "No HP : " + data_tb.getNoHP() + "\n"));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Digunakan untuk menangani kejadian Error
                Log.e("MyListData", "Error: ", databaseError.toException());
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng pos) {
                // First check if myMarker is null
                if (myMarker == null) {
                    // mendapatkan email dari akun google untuk primary key
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MapsActivity.this);
                    if (acct != null) {
                        Semail = acct.getEmail();
                    }

                    mDatabase.child("Agen").addListenerForSingleValueEvent(new ValueEventListener() {
                        String emaill = Semail.replace(".", ",");

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(emaill).exists()) {
                                    // Marker was not set yet. Add marker:
                                    myMarker = googleMap.addMarker(new MarkerOptions()
                                            .position(pos)
//                            .title("Your marker title")
//                            .snippet("Your marker snippet")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                                    builder.setMessage("Tap 2 kali pada marker untuk menambahkan info tambahan!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    Toast.makeText(MapsActivity.this
                                            , "Anda bukan Agen"
                                            ,Toast.LENGTH_SHORT).show();
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    // Marker already exists, just update it's position
                    myMarker.setPosition(pos);
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            boolean doubleBackToExitPressedOnce = false;
            @Override
            public boolean onMarkerClick(final Marker marker) {
                // fungsi untuk mengklik 2 kali pada marker
                // agar bisa berpindah activity
                if (doubleBackToExitPressedOnce) {
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MapsActivity.this);
                    if (acct != null) {
                        Semail = acct.getEmail();
                    }

                    mDatabase.child("Agen").addListenerForSingleValueEvent(new ValueEventListener() {
                        String emaill = Semail.replace(".", ",");

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(emaill).exists()) {
                                    Intent intent = new Intent(MapsActivity.this, TambahMarkerActivity.class);

                                    String Slatt = String.valueOf(marker.getPosition().latitude);
                                    String Slongg = String.valueOf(marker.getPosition().longitude);
                                    // mengirim data ke activity tujuan
                                    // puExtra(key, value)
                                    intent.putExtra("lat",Slatt);
                                    intent.putExtra("long",Slongg);
                                    startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
                                    builder1.setMessage("Anda bukan Agen!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                }
                                            });
                                    AlertDialog alert1 = builder1.create();
                                    alert1.show();
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    this.doubleBackToExitPressedOnce = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 500);
                    marker.showInfoWindow();
                    Toast.makeText(MapsActivity.this
                            , "Klik kotak dialog untuk melihat lebih detail"
                            ,Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
        builder1.setMessage(marker.getTitle() + "\n" + marker.getSnippet())
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    // ---------------------------------------------------------------------------------------- //

    // pengaturan menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // mendapatkan email dari akun google untuk primary key
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MapsActivity.this);
        if (acct != null) {
            Semail = acct.getEmail();
        }
        if (item.getItemId() == R.id.daftar_agen) {

            mDatabase.child("Agen").addListenerForSingleValueEvent(new ValueEventListener() {
                String emaill = Semail.replace(".", ",");

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(emaill).exists()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                            builder.setMessage("Anda sudah terdaftar menjadi Agen!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            startActivity(new Intent(MapsActivity.this, DaftarAgenActivity.class));
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (item.getItemId() == R.id.logout) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MapsActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
        } else if (item.getItemId() == R.id.info_akun) {
            mDatabase.child("Agen").addListenerForSingleValueEvent(new ValueEventListener() {
                String emaill = Semail.replace(".", ",");

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(emaill).exists()) {
                        startActivity(new Intent(MapsActivity.this, InfoAgenActivity.class));
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setMessage("Hanya Agen yang bisa melihat info akun!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return true;
    }


}
