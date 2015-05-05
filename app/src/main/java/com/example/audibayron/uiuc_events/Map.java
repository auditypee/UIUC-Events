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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
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

    /**
     * Map startup basically
     */
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

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

        openDb();

        Cursor crs = db.getAllRows();
        db.deleteAll();
        db.insertRow("Mulch Lovers Club", "1005 W Gregory Dr\nUrbana, IL 61801", 10, 05, 2015,
                "A place where all Mulch Lovers are welcome", 40.1036614, -88.2210804);
        db.insertRow("Party on the Plots", "Morrow Plots\n1102 S Goodwin Ave\nUrbana, IL 61801", 05, 05, 2015,
                "Self-explanatory", 40.10461, -88.226188);
        db.insertRow("Pin'd Announcement", "UIUC\n1304 Springfield Ave\nUrbana, IL 61801", 05, 05, 2015,
                "Announcement", 40.1131944, -88.225971);
        db.insertRow("Midnight Ignore Finals Frisbee Game", "Main Quadrangle\nUrbana, IL 61801", 14, 05, 2015,
                "Sick and tired of studying for finals? Are you thinking, \"I'll never make it to grad school\"?" +
                        " Well think no more! Throw all your worries away by participating in the annual \"Midnight" +
                        " Ignore Finals Frisbee Games\" where students just like you don't give a shit anymore!",
                40.1075389, -88.2273339);
        db.insertRow("Hacker Space", "201 North Goodwin Avenue\nUrbana, IL 61801", 10, 05, 2015,
                "This week's Hacker Space will be in SC 3405 as usual.", 40.113992, -88.2244112);
        mEvents = db.getEvents(crs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDb();
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
        //Adds the markers
        addMarkersToMap();

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        //Sets the camera on the current location at startup
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
    /**
     * Database
     */
    private DBAdapter db;

    private void openDb() {
        db = new DBAdapter(this);
        db.open();
    }
    private void closeDb() {
        db.close();
    }

    /**
     * Brunt of the code
     */
    //Dummy markers
    public ArrayList<Events> mEvents = new ArrayList<>();
    public ArrayList<Marker> mMarkers = new ArrayList<>();
    public HashMap<Marker, LatLng> mMarkerContainer = new HashMap<>();

    private View mView = null;

    private AutoResizeTextView resize;

    //Self-explanatory
    private void lowerDetailedPage() {
        View mapToChange = findViewById(R.id.mapLayout);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapToChange.getLayoutParams();

        params.weight = 1f;
        mapToChange.setLayoutParams(params);
    }

    //Self-explanatory
    private void addMarkersToMap() {
        //Goes through the EventsList and sets it as marker
        for (int i = 0; i < mEvents.size(); i++) {
            String temp = mEvents.get(i).getDetails();
            if(mEvents.get(i).getDetails().length() > 50) {
                mEvents.get(i).setDetails(mEvents.get(i).getDetails().substring(0, 50) + "...");
            }
            mMarkers.add(mMap.addMarker(new MarkerOptions()
                    .position(mEvents.get(i).getAddressCo())
                    .alpha(.7f)
                    .infoWindowAnchor(4.8f, 1.4f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin4))
                    .title(mEvents.get(i).getEventName())
                    .snippet(mEvents.get(i).getMonth() + "/" + mEvents.get(i).getDate() + "/" + mEvents.get(i).getYear() + "\n" +
                            mEvents.get(i).getDetails())));
            mEvents.get(i).setDetails(temp);
            mMarkerContainer.put(mMarkers.get(i), mMarkers.get(i).getPosition());
        }
    }

    //The InfoWindow, currently set to only the contents of the window is shown -- window itself isn't changed
    private class CustomInfoWindowAdapter implements
            InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            if (mView == null)
                mView = getLayoutInflater().inflate(R.layout.info_windows, null);

            //Each InfoWindow content is changed depending on the marker
            TextView tv = (TextView)mView.findViewById(R.id.iEventName);
            tv.setText(marker.getTitle());

            tv = (TextView)mView.findViewById(R.id.iDetails);
            tv.setText(marker.getSnippet());
            return mView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    //What happens when the Info Window is clicked
    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng id = mMarkerContainer.get(marker);
        //DetailedPage is changed to the marker
        for (int i = 0; i < mEvents.size(); i++) {
            if (id.equals(mEvents.get(i).getAddressCo())) {
                //DetailedPage
                /**
                 * TextView dEventName = (TextView) findViewById(R.id.dEventName);
                 * TextView dTimeCreated = (TextView) findViewById(R.id.dTimeCreated);
                 * TextView dAddress = (TextView) findViewById(R.id.dAddress);
                 * TextView dDetails = (TextView) findViewById(R.id.dDetails);
                 */
                AutoResizeTextView dEventName = (AutoResizeTextView) findViewById(R.id.dEventName);
                AutoResizeTextView dTimeCreated = (AutoResizeTextView) findViewById(R.id.dTimeCreated);
                AutoResizeTextView dAddress = (AutoResizeTextView) findViewById(R.id.dAddress);
                TextView dDetails = (TextView) findViewById(R.id.dDetails);
                dEventName.setText(mEvents.get(i).getEventName());
                dTimeCreated.setText(mEvents.get(i).getMonth() + "/" + mEvents.get(i).getDate() + "/" + mEvents.get(i).getYear());       dTimeCreated.resizeText();
                dAddress.setText(mEvents.get(i).getAddressName());
                dDetails.setText(mEvents.get(i).getDetails());
            }
        }
        //Makes the DetailedPage scrollable
        TextView detailsScroll = (TextView) findViewById(R.id.dDetails);
        detailsScroll.setMovementMethod(new ScrollingMovementMethod());

        //Shows the DetailedPage
        View mapToChange = findViewById(R.id.mapLayout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapToChange.getLayoutParams();
        if (params.weight == 0.4f)
            lowerDetailedPage();
        else {
            params.weight = 0.4f;
            mapToChange.setLayoutParams(params);
        }
    }

    //What happens when the map is clicked
    @Override
    public void onMapClick(LatLng latLng) {
        lowerDetailedPage();
    }

    //What happens when the marker is clicked
    @Override
    public boolean onMarkerClick(Marker marker) {
        lowerDetailedPage();
        // Calculate required horizontal shift for current screen density
        final int dX = getResources().getDimensionPixelSize(R.dimen.map_dx);
        // Calculate required vertical shift for current screen density
        final int dY = getResources().getDimensionPixelSize(R.dimen.map_dy);
        final Projection projection = mMap.getProjection();
        final Point markerPoint = projection.toScreenLocation(
                marker.getPosition()
        );
        // Shift the point we will use to center the map
        markerPoint.offset(dX, dY);
        final LatLng newLatLng = projection.fromScreenLocation(markerPoint);
        // Buttery smooth camera swoop :)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
        // Show the info window (as the overloaded method would)
        marker.showInfoWindow();
        return true; // Consume the event since it was dealt with
    }

    //What happens when the MyLocation is clicked
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
