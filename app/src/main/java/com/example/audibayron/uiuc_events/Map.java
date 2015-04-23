package com.example.audibayron.uiuc_events;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class Map extends FragmentActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        GoogleMap.OnMapClickListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi (LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do nothing
    }

    @Override
    public void onLocationChanged(Location location) {
        //Do nothing
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Do nothing
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMarkersToMap();

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            //here where the camera animate and zoom to particular location.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 14));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    /**
     * Brunt of the code
     */
    public Events[] mEvents = new Events[] {
            new Events(0, "Event1", "This Street", new LatLng(40.1132,-88.2258), "blah", 27, 04, 2015),
            new Events(1, "Event2", "Other Street", new LatLng(40.10866905, -88.22718859), "blah2", 28, 04, 2015)};

    public Marker[] mMarkers = new Marker[mEvents.length];
    public HashMap<String, Integer> mMarkerContainer = new HashMap< >();

    private View mView = null;

    private void lowerDetailedPage() {
        View mapToChange = findViewById(R.id.map);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapToChange.getLayoutParams();

        params.weight = 1f;
        mapToChange.setLayoutParams(params);
    }

    private void addMarkersToMap() {
        for (int i = 0; i < mMarkers.length; i++) {
            String temp = mEvents[i].getDetails();
            if(mEvents[i].getDetails().length() > 50) {
                mEvents[i].setDetails(mEvents[i].getDetails().substring(0, 50) + "...");
            }
            mMarkers[i] = mMap.addMarker(new MarkerOptions()
                    .position(mEvents[i].getAddressCo())
                    .title(mEvents[i].getEventName())
                    .snippet(mEvents[i].getMonth() + "/" + mEvents[i].getDate() + "/" + mEvents[i].getYear() + "\n" +
                            mEvents[i].getDetails()));
            mEvents[i].setDetails(temp);
            mMarkerContainer.put(mMarkers[i].getId(), i);
        }
    }

    private class CustomInfoWindowAdapter implements
            InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (mView == null)
                mView = getLayoutInflater().inflate(R.layout.info_windows, null);

            TextView tv = (TextView)mView.findViewById(R.id.iEventName);
            tv.setText(marker.getTitle());

            tv = (TextView)mView.findViewById(R.id.iDetails);
            tv.setText(marker.getSnippet());
            return mView;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int id = mMarkerContainer.get(marker.getId());
        for (int i = 0; i < mMarkers.length; i++) {
            if (mEvents[i].getId() == id) {
                //DetailedPage
                TextView dEventName = (TextView)findViewById(R.id.dEventName);
                TextView dTimeCreated = (TextView)findViewById(R.id.dTimeCreated);
                TextView dAddress = (TextView)findViewById(R.id.dAddress);
                TextView dDetails = (TextView)findViewById(R.id.dDetails);
                dEventName.setText(mEvents[i].getEventName());
                dTimeCreated.setText(mEvents[i].getMonth() + "/" + mEvents[i].getDate() + "/" + mEvents[i].getYear());
                dAddress.setText(mEvents[i].getAddressName());
                dDetails.setText(mEvents[i].getDetails());
            }
        }
        //Makes the DetailedPage scrollable
        TextView detailsScroll = (TextView) findViewById(R.id.dDetails);
        detailsScroll.setMovementMethod(new ScrollingMovementMethod());

        View mapToChange = findViewById(R.id.map);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapToChange.getLayoutParams();
        if (params.weight == 0.4f)
            lowerDetailedPage();
        else {
            params.weight = 0.4f;
            mapToChange.setLayoutParams(params);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        lowerDetailedPage();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        lowerDetailedPage();
        return false;
    }
}
