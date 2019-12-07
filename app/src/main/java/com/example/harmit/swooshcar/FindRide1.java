package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class FindRide1 extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "FindRideActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE1 = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    AutoCompleteTextView mactvOrigin, mactvDestination;
    Button FindPath, Continue;
    String origin, destination, duration, distance;
    TextView mtvduration, mtvdistance;
    ProgressDialog progressDialog;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        mtvduration = (TextView) findViewById(R.id.tvDuration1);
        mtvdistance = (TextView) findViewById(R.id.tvDistance1);

        FindPath = (Button) findViewById(R.id.FindPath1);
        FindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest();
            }
        });

        Continue = (Button) findViewById(R.id.continuefindride);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest();
                if (mtvdistance.getText().toString().equals("0 km")) {
                    Toast.makeText(getApplicationContext(), "Please enter valid route.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(FindRide1.this, FindRide2Activity.class);
                    intent.putExtra("origin1", origin);
                    intent.putExtra("destination1", destination);
                    intent.putExtra("duration1", duration);
                    intent.putExtra("distance1", distance);
                    startActivity(intent);
                }
            }
        });

        mactvOrigin = (AutoCompleteTextView) findViewById(R.id.actvOrigin1);
        mactvOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(FindRide1.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE1);
                } catch (GooglePlayServicesRepairableException e) {
                    printToast("Google Play Service Repair");
                } catch (GooglePlayServicesNotAvailableException e) {
                    printToast("Google Play Service Not Available");
                }
            }
        });
        mactvOrigin.setThreshold(2);

        mactvDestination = (AutoCompleteTextView) findViewById(R.id.actvDestination1);
        mactvDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(FindRide1.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
                } catch (GooglePlayServicesRepairableException e) {
                    printToast("Google Play Service Repair");
                } catch (GooglePlayServicesNotAvailableException e) {
                    printToast("Google Play Service Not Available");
                }
            }
        });
        mactvDestination.setThreshold(2);

        mGoogleApiClient = new GoogleApiClient.Builder(FindRide1.this).addApi(Places.GEO_DATA_API).enableAutoManage(this, GOOGLE_API_CLIENT_ID, this).addConnectionCallbacks(this).build();

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);

    }

    private void sendRequest() {
        origin = mactvOrigin.getText().toString();
        destination = mactvDestination.getText().toString();
        duration = mtvduration.getText().toString();
        distance = mtvdistance.getText().toString();

        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(22.308155, 70.800705);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 14));
        originMarkers.add(mMap.addMarker(new MarkerOptions().title("Rajkot").position(hcmus)));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected point");
                mMap.addMarker(markerOptions);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (!place.getAddress().toString().contains(place.getName())) {
                    mactvOrigin.setText(place.getName() + ", " + place.getAddress());
                } else {
                    mactvOrigin.setText(place.getAddress());
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
                mMap.animateCamera(cameraUpdate);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                printToast("Error in retrieving place info");

            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (!place.getAddress().toString().contains(place.getName())) {
                    mactvDestination.setText(place.getName() + ", " + place.getAddress());
                } else {
                    mactvDestination.setText(place.getAddress());
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
                mMap.animateCamera(cameraUpdate);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                printToast("Error in retrieving place info");

            }
        }
    }


    private void printToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.", "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            mtvduration.setText(route.duration.text);
            mtvdistance.setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue)).title(route.startAddress).position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green)).title(route.endAddress).position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: " + connectionResult.getErrorCode());

        Toast.makeText(this, "Google Places API connection failed with error code:" + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();

    }

}
