package com.example.pokrz.inotes2.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokrz.inotes2.NoteViewModel;
import com.example.pokrz.inotes2.R;
import com.example.pokrz.inotes2.entities.Note;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private NoteViewModel noteViewModel;
    private ArrayList<LatLng> locations;
    private ArrayList<String> titles;
    ArrayList<MarkerOptions> markers;

    public static final float COORDINATE_OFFSET = 0.0005f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            mGoogleMap.clear();
            locations = new ArrayList<>();
            titles = new ArrayList<>();
            markers = new ArrayList<>();

            for (Note note: notes) {
                if(note.getLocation() != null) {
                    LatLng noteLocation = new LatLng(note.getLocation().getLatitude(), note.getLocation().getLongitude());
                    checkLocations(noteLocation, note.getTitle());
                }
            }
            drawMarkers();

        });
        mGoogleMap.setOnInfoWindowClickListener(this);
    }

    private void checkLocations(LatLng noteLocation, String title) {
        double lat = noteLocation.latitude;
        double lon = noteLocation.longitude;
        if (locations != null && locations.size() > 0) {
            while (locations.contains(noteLocation)) {
                lon += COORDINATE_OFFSET;
                noteLocation = new LatLng(lat, lon);
            }
        }
        locations.add(new LatLng(lat, lon));
        titles.add(title);
    }

    private void drawMarkers() {
        for (int i = 0; i < locations.size(); i++) {
            markers.add(new MarkerOptions().position(locations.get(i)).title(titles.get(i)));
        }

        for (int i = 0; i < markers.size(); i++) {
            mGoogleMap.addMarker(markers.get(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mMapView.getMapAsync(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
