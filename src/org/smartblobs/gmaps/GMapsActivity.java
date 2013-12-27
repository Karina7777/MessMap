package org.smartblobs.gmaps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class GMapsActivity extends Activity {
	private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
 //   	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
 //   	mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Log.d("qqqq", "oooo");
        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
        	private ArrayList<Point> extractPoints(String s) throws JSONException{
        		ArrayList<Point> ps = new ArrayList<Point>();
        		JSONArray arr = new JSONArray(s);
        		for (int i = 0; i < arr.length(); i++) {
       			   JSONObject o = arr.getJSONObject(i);
       			   ps.add(new Point(o.getDouble("altitude"), o.getDouble("logitude")));
    			}        		
        		return ps;
        	}
        	
        	private ArrayList<Point> getPoints(){
        		ArrayList<Point> ps = new ArrayList<Point> ();
        		try { ps = extractPoints(httpGetString());
        		} catch (Exception e) {
        			Log.e("json http", "exception");
        		}
        		return ps;
        	}
        	
        	private String httpGetString(){
            	StringBuilder total = new StringBuilder();
            	try {
            		
            		InputStream content = null;
            		HttpClient httpclient = new DefaultHttpClient();
            		HttpResponse response = httpclient.execute(new HttpGet("http://messmap777.appspot.com/"));
            	    content = response.getEntity().getContent();
             	    BufferedReader r = new BufferedReader(new InputStreamReader(content));
            	    
            	    String line;
            	    while ((line = r.readLine()) != null) {
            	        total.append(line);
            	    }
            	    
            	} catch (Exception e) {
           	  		Log.d("[GET REQUEST]", e.toString());
            	}
           	    return total.toString();
            }
        	@Override
            public void run() {
        		final ArrayList<Point> ps = getPoints();
            	handler.post(new Runnable() {
                    @Override
                    public void run() {
                    	Iterator<Point> i = ps.iterator();
                    	while(i.hasNext()){
                    		Point p = i.next();
                    		Double x = p.getLatitude();
                    		Double y = p.getLongitude();
                    		Log.d("Main thread", "Latitude: " + x.toString() + "," + "Longitude: " + y.toString());
                    		mMap.addMarker(new MarkerOptions()
                    		.position(new LatLng(p.getLatitude(), p.getLongitude()))
                            .title("Hello world"));

                    	}
                    
                    	// Do whatever you want
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 3000); // 1000 = 1 second.
    
    }
}
