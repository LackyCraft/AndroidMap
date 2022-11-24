package com.example.schedulebus;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.schedulebus.databinding.ActivityMapsBinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Map<String, String>> data = null;

        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.504831351, 37.58467363),zoomLevel));

        try {
            DBcontext contextOpen = new DBcontext();
            connect = contextOpen.connect();
            if (connect != null) {
                String query = "SELECT \n" +
                        "id_transport_stop id,\n" +
                        "title,\n" +
                        "ST_X (point) long,\n" +
                        "ST_Y (point) lat\n" +
                        "from public.transport_stop ts";
                System.out.println(query);
                ResultSet res = connect.createStatement().executeQuery(query);

                while (res.next()) {
                    //Map<String, String> dtname = new HashMap<String, String>();
                    //dtname.put("id", res.getString(1));
                    //dtname.put("firstname", res.getString(2));
                    //dtname.put("fullname",res.getString(3));
                    //data.add(dtname);
                    double latitude = Double.parseDouble(res.getString("lat"));
                    double longitude = Double.parseDouble(res.getString("long"));
                    LatLng location = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(location).title(res.getString("title")));



                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}