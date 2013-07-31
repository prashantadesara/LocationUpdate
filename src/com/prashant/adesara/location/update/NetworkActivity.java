package com.prashant.adesara.location.update;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Start service from this activity and display current lat/lng.
 * @author Prashant Adesara 
 **/

@SuppressLint("HandlerLeak")
public class NetworkActivity extends Activity 
{
	public static NetworkActivity networkActivity;
	private static TextView lat_lng;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try 
        {
        	networkActivity = this;
        	lat_lng = (TextView)findViewById(R.id.lat_lng);
        	
        	String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        	System.out.println("provider________: "+provider);
        	
        	if(!provider.contains("gps")) //Auto enable gps option
        	{
        		final Intent poke = new Intent();
        		poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
        		poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        		poke.setData(Uri.parse("3"));
        		sendBroadcast(poke);
        	}
        	
        	if(!provider.contains("network"))
        	{
        		startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
        	}
        	else if(provider.contains("network"))
        	{
        		startTrackingService();
        	}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
    }
    public void setLatLng(final String latLng)
    {
    	this.latLng = latLng;
    	myHandler.sendEmptyMessage(0);
    }
    
    private String latLng="";
    
    Handler myHandler = new Handler()
    {
    	public void handleMessage(Message msg) 
    	{
    		lat_lng.setText(""+latLng);	
    	};
    };
    private void startTrackingService()
    {
    	startService(new Intent(getApplicationContext(), ServiceDemo.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    	if(!provider.contains("network"))
    	{
    		Toast.makeText(getApplicationContext(), "Please select wifi & mobile location to get lat/lng", Toast.LENGTH_LONG).show();
    		startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
    	}
    	else if(provider.contains("network"))
    	{
    		startTrackingService();
    	}
    	
    }
}