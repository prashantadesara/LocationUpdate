package com.prashant.adesara.location.update;

import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class ServiceDemo extends Service implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	private double Ltid, lgid, lat, lng = 0;
	private static LocationRequest mLocationRequest = null;
    private static LocationClient mLocationClient = null;
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 10;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 10;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		try 
		{
			mLocationRequest = LocationRequest.create();
			
			mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// Set the interval ceiling to one minute
			mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
			
			mLocationClient = new LocationClient(getApplicationContext(), this, this);
			
			mLocationClient.connect();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void onStart(Intent intent, int startId) {
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		return Service.START_STICKY;
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);		
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onLocationChanged(Location loc) 
	{
		lat = loc.getLatitude();
		lng = loc.getLongitude();
		if(lat!=0.0 && lng!=0.0)
		{
			if(Ltid != lat && lgid != lng)
			{
				Ltid = lat;
				lgid = lng;
				if(NetworkActivity.networkActivity!=null)
				{
					NetworkActivity.networkActivity.setLatLng("" + lat + ", " + lng + " > "+ new Date());
				}
			}
		}
	}
}
