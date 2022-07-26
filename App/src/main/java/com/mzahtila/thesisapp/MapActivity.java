package com.mzahtila.thesisapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.mzahtila.thesisapp.Data.GroupContract;
import com.mzahtila.thesisapp.Data.QuizDbHelper;
import com.mzahtila.thesisapp.Managers.PermissionUtils;

import static com.google.android.gms.maps.GoogleMap.*;

public class MapActivity extends AppCompatActivity
        implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMapReadyCallback,
        LocationListener,
        OnMyLocationButtonClickListener {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    /* ---------------------------------------------------------------------------------------------
     * Parameters for using GPS positioning
     */
    final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    final int LOCATION_REQUEST = 1340;
    LocationManager locationManager;
    private QuizDbHelper mDbHelper;
    private GoogleMap mMap;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    //@TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                     // get the reference of Toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));     // setting a navigation icon in Toolbar
        toolbar.setTitle(R.string.map_main_title);        // set Title for Toolbar

        // Get group name as Subtitle for Toolbar
        String groupName = getGroupName();
        if (groupName.equals("")) {
            groupName = "Nameless :/";
        }
        toolbar.setSubtitle(groupName);     // set name of the group as Subtitle for Toolbar

        setSupportActionBar(toolbar);       // Setting/replace toolbar as the ActionBar

        // Handling "Back" button functionality
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, GroupDetailsActivity.class); // returns to preceding activity
                startActivity(i);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /* ---------------------------------------------------------------------------------------------
     * Setting the toolbar
     */
    @Override // Activity's overrided method used to set the menu file
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_info:
                Intent i = new Intent(MapActivity.this, InfoActivity.class);
                i.putExtra("backToActivity", "2");
                startActivity(i);
                break;
            case R.id.action_initial_map:
                setInitialMapView();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* ---------------------------------------------------------------------------------------------
     * Handling buttons' actions
     */
    public void onClickStartNewActivity(View view) {
        Intent i = new Intent(MapActivity.this, QuizMainActivity.class);
        startActivity(i);
    }

    /* ---------------------------------------------------------------------------------------------
     * Get group name from the database
     */
    private String getGroupName() {
        // Create database helper
        mDbHelper = new QuizDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] columns = {
                GroupContract.GR_NAM};

        // Define conditions for selection: where column _ID equals 1
        String selection = GroupContract._ID + "=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(
                GroupContract.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToPosition(0);
        int grNameColumnIndex = cursor.getColumnIndex(GroupContract.GR_NAM);
        String groupName = cursor.getString(grNameColumnIndex);
        cursor.close();

        return groupName;
    }

    /* ---------------------------------------------------------------------------------------------
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_SATELLITE);
        setInitialMapView();    // Set initial map view
        drawCheckpoints(mMap);  // Display checkpoints
        drawRoute(mMap);        // Display the route

        // Check the permission to access FINE_LOCATION
        if (checkLocationPermissions()) {
            // If the app has access to FINE_LOCATION it enables GPS positioning;
            enableMyLocation();
        } else
            // If the app doesn't have access to FINE_LOCATION it asks for the permission;
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
    }

    private void setInitialMapView() {
        LatLng initalView = new LatLng(47.454032, 13.604423); // Set center of the initial map view
        updateCamera(initalView, 14);
    }

    private void updateCamera(LatLng position, int zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)   // Sets the center of the map to Mountain View
                .zoom(zoomLevel)    // Sets the zoom
                .build();           // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void drawCheckpoints(GoogleMap mMap) {
        /*
         * Add markers for lecture stations
         */
        // Position 1
        LatLng position1 = new LatLng(47.45980, 13.61507);
        mMap.addMarker(new MarkerOptions().position(position1).title("1. Elevation tints").icon(BitmapDescriptorFactory.defaultMarker(30)));

        // Position 2
        LatLng position2 = new LatLng(47.460572, 13.609923);
        mMap.addMarker(new MarkerOptions().position(position2).title("2. Hachuring").icon(BitmapDescriptorFactory.defaultMarker(170)));

        // Position 3
        LatLng position3 = new LatLng(47.4562489, 13.5976464);
        mMap.addMarker(new MarkerOptions().position(position3).title("3. Shading").icon(BitmapDescriptorFactory.defaultMarker(200)));

        // Position 4
        LatLng position4 = new LatLng(47.4501041, 13.6075383);
        mMap.addMarker(new MarkerOptions().position(position4).title("4. Contour lines and elevations").icon(BitmapDescriptorFactory.defaultMarker(330)));
    }

    private void drawRoute(GoogleMap googleMap) {
        // Add route to the map
        Polyline route = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(getRoute())
                .width(20)
                .color(getResources().getColor(R.color.colorAccentBright))
        );

        Polyline route_stroke = googleMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(getRoute())
                .width(14)
                .color(getResources().getColor(R.color.colorAccent))
        );
    }

    private void enableMyLocation() {
        mMap.setMyLocationEnabled(true); // Enables positioning
        mMap.getUiSettings().setMyLocationButtonEnabled(true); // Shows MyLocationButton
        mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, (LocationListener) this); // Updates current position every LOCATION_UPDATE_MIN_DISTANCE value
    }

    public LatLng[] getRoute() {
        LatLng[] route = new LatLng[]{new LatLng(47.45980, 13.61507), // Position 1 --> START
                new LatLng(47.45992, 13.61512),
                new LatLng(47.45999, 13.61516),
                new LatLng(47.46008, 13.61521),
                new LatLng(47.46017, 13.61526),
                new LatLng(47.46024, 13.61531),
                new LatLng(47.46031, 13.61535),
                new LatLng(47.46040, 13.61541),
                new LatLng(47.46050, 13.61548),
                new LatLng(47.46061, 13.61557),
                new LatLng(47.46063, 13.61558),
                new LatLng(47.46067, 13.61558),
                new LatLng(47.46078, 13.61559),
                new LatLng(47.46079, 13.61558),
                new LatLng(47.46079, 13.61556),
                new LatLng(47.46077, 13.61556),
                new LatLng(47.46074, 13.61554),
                new LatLng(47.46072, 13.61551),
                new LatLng(47.46071, 13.61549),
                new LatLng(47.46070, 13.61548),
                new LatLng(47.46070, 13.61546),
                new LatLng(47.46071, 13.61545),
                new LatLng(47.46072, 13.61544),
                new LatLng(47.46073, 13.61543),
                new LatLng(47.46081, 13.61545),
                new LatLng(47.46083, 13.61545),
                new LatLng(47.46085, 13.61544),
                new LatLng(47.46086, 13.61542),
                new LatLng(47.46086, 13.61540),
                new LatLng(47.46086, 13.61539),
                new LatLng(47.46085, 13.61537),
                new LatLng(47.46084, 13.61536),
                new LatLng(47.46082, 13.61533),
                new LatLng(47.46081, 13.61530),
                new LatLng(47.46078, 13.61526),
                new LatLng(47.46075, 13.61524),
                new LatLng(47.46072, 13.61521),
                new LatLng(47.46070, 13.61519),
                new LatLng(47.46069, 13.61516),
                new LatLng(47.46068, 13.61514),
                new LatLng(47.46068, 13.61512),
                new LatLng(47.46068, 13.61511),
                new LatLng(47.46069, 13.61508),
                new LatLng(47.46069, 13.61507),
                new LatLng(47.46069, 13.61506),
                new LatLng(47.46069, 13.61504),
                new LatLng(47.46069, 13.61501),
                new LatLng(47.46068, 13.61499),
                new LatLng(47.46067, 13.61497),
                new LatLng(47.46067, 13.61496),
                new LatLng(47.46067, 13.61495),
                new LatLng(47.46067, 13.61493),
                new LatLng(47.46068, 13.61493),
                new LatLng(47.46069, 13.61494),
                new LatLng(47.46070, 13.61496),
                new LatLng(47.46071, 13.61499),
                new LatLng(47.46072, 13.61502),
                new LatLng(47.46073, 13.61505),
                new LatLng(47.46074, 13.61506),
                new LatLng(47.46075, 13.61507),
                new LatLng(47.46076, 13.61507),
                new LatLng(47.46090, 13.61505),
                new LatLng(47.46093, 13.61503),
                new LatLng(47.46095, 13.61503),
                new LatLng(47.46096, 13.61503),
                new LatLng(47.46098, 13.61504),
                new LatLng(47.46100, 13.61506),
                new LatLng(47.46102, 13.61507),
                new LatLng(47.46104, 13.61507),
                new LatLng(47.46105, 13.61506),
                new LatLng(47.46106, 13.61505),
                new LatLng(47.46108, 13.61504),
                new LatLng(47.46111, 13.61503),
                new LatLng(47.46115, 13.61502),
                new LatLng(47.46121, 13.61503),
                new LatLng(47.46127, 13.61503),
                new LatLng(47.46130, 13.61503),
                new LatLng(47.46131, 13.61502),
                new LatLng(47.46133, 13.61501),
                new LatLng(47.4611517, 13.6147535),
                new LatLng(47.4611208, 13.6147186),
                new LatLng(47.4610882, 13.6146945),
                new LatLng(47.4610855, 13.6146703),
                new LatLng(47.4610909, 13.6146368),
                new LatLng(47.4610583, 13.6145389),
                new LatLng(47.4610039, 13.6144598),
                new LatLng(47.4609839, 13.6144236),
                new LatLng(47.4609993, 13.6144061),
                new LatLng(47.4612541, 13.6144584),
                new LatLng(47.4613947, 13.6145268),
                new LatLng(47.4610673, 13.613517),
                new LatLng(47.4610111, 13.6134687),
                new LatLng(47.4609984, 13.6132729),
                new LatLng(47.4609368, 13.6131549),
                new LatLng(47.4609730, 13.6129752),
                new LatLng(47.4609422, 13.6129081),
                new LatLng(47.4610619, 13.6125085),
                new LatLng(47.4609785, 13.6123475),
                new LatLng(47.4609023, 13.6120257),
                new LatLng(47.4608352, 13.6118889),
                new LatLng(47.4608497, 13.6116824),
                new LatLng(47.4608135, 13.6115161),
                new LatLng(47.4606829, 13.6113873),
                new LatLng(47.4605632, 13.6112478),
                new LatLng(47.4605632, 13.6111888),
                new LatLng(47.4606067, 13.6110869),
                new LatLng(47.4605959, 13.6110386),
                new LatLng(47.4605813, 13.6108187),
                new LatLng(47.4605813, 13.6107999),
                new LatLng(47.4605650, 13.6105236),
                new LatLng(47.4606013, 13.6100972),
                new LatLng(47.4605795, 13.6099496),
                new LatLng(47.4605720, 13.6099230), // Position 2
                new LatLng(47.4605233, 13.6098048),
                new LatLng(47.4605142, 13.6097673),
                new LatLng(47.4605342, 13.6097109),
                new LatLng(47.4605088, 13.6094749),
                new LatLng(47.4604018, 13.6092201),
                new LatLng(47.4603365, 13.6087990),
                new LatLng(47.4602604, 13.6084503),
                new LatLng(47.4574640, 13.6040086),
                new LatLng(47.4573479, 13.6036599),
                new LatLng(47.4573116, 13.6034775),
                new LatLng(47.4573116, 13.6029813),
                new LatLng(47.4573334, 13.6026675),
                new LatLng(47.4572427, 13.6020130),
                new LatLng(47.4570468, 13.6016536),
                new LatLng(47.4568836, 13.6011708),
                new LatLng(47.4567530, 13.6006451),
                new LatLng(47.4567422, 13.6003768),
                new LatLng(47.4568075, 13.6001408),
                new LatLng(47.4569018, 13.6000121),
                new LatLng(47.4569670, 13.5998619),
                new LatLng(47.4570251, 13.5995561),
                new LatLng(47.4570795, 13.5993201),
                new LatLng(47.4573189, 13.5990787),
                new LatLng(47.4576308, 13.5988963),
                new LatLng(47.4579065, 13.5985476),
                new LatLng(47.4580116, 13.5981023),
                new LatLng(47.4580153, 13.5977966),
                new LatLng(47.4579137, 13.5974586),
                new LatLng(47.4578521, 13.5970831),
                new LatLng(47.4577360, 13.5967022),
                new LatLng(47.4575184, 13.5961658),
                new LatLng(47.4569525, 13.5971904),
                new LatLng(47.4566841, 13.5974532),
                new LatLng(47.4562489, 13.5976464),
                new LatLng(47.4558463, 13.5976410),
                new LatLng(47.4557519, 13.5974854),
                new LatLng(47.4555996, 13.5966057),
                new LatLng(47.4554074, 13.5961926),
                new LatLng(47.4551426, 13.5958439),
                new LatLng(47.4548016, 13.5952216),
                new LatLng(47.4546021, 13.5948622),
                new LatLng(47.4543228, 13.5945672),
                new LatLng(47.4540363, 13.5944223),
                new LatLng(47.4538222, 13.5944384),
                new LatLng(47.4536155, 13.5946369),
                new LatLng(47.4535574, 13.5949641),
                new LatLng(47.4535973, 13.5954148),
                new LatLng(47.4535973, 13.5958654),
                new LatLng(47.4535321, 13.5963374),
                new LatLng(47.4535502, 13.5965306),
                new LatLng(47.4536626, 13.5965306),
                new LatLng(47.4539746, 13.5958600),
                new LatLng(47.4540798, 13.5957688),
                new LatLng(47.4541632, 13.5958117),
                new LatLng(47.4541777, 13.5959190),
                new LatLng(47.4540762, 13.5960692),
                new LatLng(47.4539782, 13.5963535),
                new LatLng(47.4539818, 13.5967022),
                new LatLng(47.4540616, 13.5968739),
                new LatLng(47.4541560, 13.5971689),
                new LatLng(47.4542031, 13.5979199),
                new LatLng(47.4541197, 13.5988480),
                new LatLng(47.4537751, 13.5996956),
                new LatLng(47.4537134, 13.6000443),
                new LatLng(47.4537388, 13.6005914),
                new LatLng(47.4538114, 13.6010098),
                new LatLng(47.4539202, 13.6013210),
                new LatLng(47.4539782, 13.6019593),
                new LatLng(47.4539020, 13.6027747),
                new LatLng(47.4534885, 13.6039174),
                new LatLng(47.4524656, 13.6067069),
                new LatLng(47.4522951, 13.6069107),
                new LatLng(47.4521428, 13.6067981),
                new LatLng(47.4520339, 13.6067230),
                new LatLng(47.4519469, 13.6067981),
                new LatLng(47.4516313, 13.6075330),
                new LatLng(47.4515914, 13.6078602),
                new LatLng(47.4516204, 13.6086649),
                new LatLng(47.4515587, 13.6089814),
                new LatLng(47.4513774, 13.6094910),
                new LatLng(47.4513084, 13.6095124),
                new LatLng(47.4511924, 13.6090833),
                new LatLng(47.4509638, 13.6085093),
                new LatLng(47.4508840, 13.6082250),
                new LatLng(47.4507534, 13.6080158),
                new LatLng(47.4506954, 13.6077315),
                new LatLng(47.4507244, 13.6071521),
                new LatLng(47.4506664, 13.6065620),
                new LatLng(47.4505394, 13.6061007),
                new LatLng(47.4503725, 13.6059666),
                new LatLng(47.4502274, 13.6060470),
                new LatLng(47.4499408, 13.6066478),
                new LatLng(47.4494983, 13.6072433),
                new LatLng(47.4492770, 13.6073399),
                new LatLng(47.4485914, 13.6073452),
                new LatLng(47.4487655, 13.6075276),
                new LatLng(47.4489360, 13.6075920),
                new LatLng(47.4492044, 13.6077690),
                new LatLng(47.4493568, 13.6077797),
                new LatLng(47.4496652, 13.6076349),
                new LatLng(47.4501041, 13.6075383)}; // Position 4 --> END

        return route;
    }

    @Override
    public void onLocationChanged(Location gpsLocation) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Location location = null;
        if (!isGPSEnabled)
            buildAlertMessageNoGps();
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return false;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /* ---------------------------------------------------------------------------------------------
     * Functionalities for using GPS positioning on the map
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocationPermissions() {
        boolean locationPermission = false;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) { // Case when the app DOESN'T HAVE access to FINE_LOCATION
            //requestPermissions(LOCATION_PERMS, LOCATION_REQUEST); // If the app doesn't have access to FINE_LOCATION it asks for the permission;
        } else if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) { // Case when the app already HAS access to FINE_LOCATION
            //enableMyLocation();
            locationPermission = true;
        }
        return locationPermission;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                } else {
                    Toast.makeText(this, "Location cannot be obtained due to " + "missing permission.",
                            Toast.LENGTH_LONG).show();
                    mPermissionDenied = true; // Display the missing permission error dialog when the fragments resume.
                }
                break;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void showMissingPermissionError() {

        /**
         * Displays a dialog with error message explaining that the location permission is missing.
         */
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
