package net.movilbox.dcsperu.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.movilbox.dcsperu.R;


public class ActMapSuper extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private double latitud = 0;
    private double longitud = 0;
    private double latitudActual = 0;
    private double longitudActual = 0;
    private RequestQueue rq;
    public static final String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_super);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            latitud = bundle.getDouble("latitud_a");
            latitudActual = bundle.getDouble("latitud_d");
            longitud = bundle.getDouble("longitud_a");
            longitudActual = bundle.getDouble("longitud_d");

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Tiene Permisos de ubicacion", Toast.LENGTH_LONG).show();
            return;
        } else {
            //mMap.setMyLocationEnabled(true);

            LatLng pdvlocation1 = new LatLng(latitud, longitud);
            mMap.addMarker(new MarkerOptions()
                    .position(pdvlocation1)
                    .title("Anterior")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location2)));

            LatLng pdvlocation2 = new LatLng(latitudActual, longitudActual);
            mMap.addMarker(new MarkerOptions()
                    .position(pdvlocation2)
                    .title("Actual")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location1)));

            LatLng myCoordinates = new LatLng(latitud, longitud);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                    .zoom(11)                   // Sets the zoom
                    //.bearing(50)              // Sets the orientation of the camera to east
                    .tilt(45)                 // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

    }

}
