package mp.schatz;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MessService extends Service implements LocationListener
{
	private List<Sensor> sensorList;
	private SensorManager sensorManager;
	private GeomagneticField geoMagneticField= new GeomagneticField(49, (float) 8.7, 130, System.currentTimeMillis());
	private Location lastLocation=new Location("GPS");
    double[] gravitationalField={0,0,0};
    double[] gravitationalUnity={0,0,0};
    double gravitationalStrength=0;
    double[] magneticField={0,0,0};
    double[] magneticFieldCorrected={0,0,0};
    boolean inruhe;
	protected double magneticStrength;
	protected double magneticVertical;
	protected double magneticHorizontal;
	protected double winkel;
	long positionUpdate;
	LocationManager locationManager;

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    	sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
    	geoMagneticField= new GeomagneticField(49, (float) 8.7, 130, System.currentTimeMillis());

    	for(Sensor n:sensorList)
    	{
    		if((n.getType()==1)||(n.getType()==2))
    		{
    		sensorManager.registerListener(sensorListener,n,SensorManager.SENSOR_DELAY_FASTEST);
    		}
    	}
		
	}


	
	private SensorEventListener sensorListener = new SensorEventListener() 
	{
		public void onSensorChanged(SensorEvent sensorEvent)
		{

			if(sensorEvent.sensor.getType()==1)
			{
				gravitationalField[0]=(double)sensorEvent.values[0];
				gravitationalField[1]=(double)sensorEvent.values[1];
				gravitationalField[2]=(double)sensorEvent.values[2];
				gravitationalStrength=Math.sqrt(Math.pow(gravitationalField[0], 2)+ Math.pow(gravitationalField[1], 2)+ Math.pow(gravitationalField[2], 2));
				if((Math.abs(gravitationalStrength-SensorManager.GRAVITY_EARTH)<0.2)&&(System.currentTimeMillis()-positionUpdate<1000)&&(lastLocation.getAccuracy()<20))
				{ 
					inruhe=true;
					Log.d("Messung","inRuhe");
					gravitationalUnity[0]=gravitationalField[0]/gravitationalStrength;
					gravitationalUnity[1]=gravitationalField[1]/gravitationalStrength;
					gravitationalUnity[2]=gravitationalField[2]/gravitationalStrength;
				}
				else
				{inruhe=false;}
				
				

		}



		if((sensorEvent.sensor.getType()==2)&&(inruhe))
		{

			// 1 Magnetfeld einlesen
		magneticField[0]=sensorEvent.values[0];
		magneticField[1]=sensorEvent.values[1];
		magneticField[2]=sensorEvent.values[2];
			// 3 Feldvektor korrigieren
		magneticFieldCorrected[0]=magneticField[0]+geoMagneticField.getZ()/1000*gravitationalUnity[0];
		magneticFieldCorrected[1]=magneticField[1]+geoMagneticField.getZ()/1000*gravitationalUnity[1];
		magneticFieldCorrected[2]=magneticField[2]+geoMagneticField.getZ()/1000*gravitationalUnity[2];
		
			// 4 Gesamtstärke berechnen
		magneticStrength=Math.sqrt(Math.pow(magneticFieldCorrected[0], 2)+Math.pow(magneticFieldCorrected[1], 2)+Math.pow(magneticFieldCorrected[2], 2));
		
			// 5 Vertikale Feldstärke
		magneticVertical=gravitationalUnity[0]*magneticFieldCorrected[0]+gravitationalUnity[1]*magneticFieldCorrected[1]+gravitationalUnity[2]*magneticFieldCorrected[2];
		
			// 6 Horizontale Gesamtfeldstärke
		magneticHorizontal=Math.sqrt(Math.pow((magneticFieldCorrected[0]-(magneticVertical*gravitationalUnity[0])),2)+Math.pow((magneticFieldCorrected[1]-(magneticVertical*gravitationalUnity[1])),2)+Math.pow((magneticFieldCorrected[2]-(magneticVertical*gravitationalUnity[2])),2));
			// 7 Winkel
		winkel=Math.acos(magneticVertical/magneticStrength)*360/2/Math.PI;
			
		//Toast.makeText(getApplicationContext(), "Messung" , Toast.LENGTH_SHORT).show();
		//((App)getApplication()).offlineAufnahmen.add(new Messpunkt(lastLocation,magneticVertical, magneticHorizontal));
		((App)(getApplication())).magnetkarte.setPoint(lastLocation, magneticVertical, magneticHorizontal);
		}
		
		}




		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	    	
	    };
		
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onLocationChanged(Location location) 
	{
		lastLocation.set(location);
		geoMagneticField=new GeomagneticField((float)location.getLatitude(), (float)location.getLongitude(), (float)location.getAltitude(), System.currentTimeMillis());
		positionUpdate=System.currentTimeMillis();
	}
		

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(this);
		sensorManager.unregisterListener(sensorListener);
		super.onDestroy();
	}

}
	