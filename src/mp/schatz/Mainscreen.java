package mp.schatz;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Mainscreen extends MapActivity implements LocationListener
{
	
	SensorManager sensorManager;
    Sensor accelerometer;
    TextView text;
    TextView gesamtText;
    TextView horizontalText;
    TextView vertikalText;
    TextView winkelText;
    EditText kontrastText;
    EditText helligkeitText;
    Button kontrastPlus;
    Button kontrastMinus;
    Button helligkeitPlus;
    Button helligkeitMinus;
    double helligkeit=0;
    double kontrast=90;
    Intent serviceIntent;
    long lastsignal;
    App app;
    Location lastLocation=new Location("GPS");
    int typ=1;
    List<android.hardware.Sensor> sensorList;
    MapView mapView;
    List<Overlay> mapOverlays;
    Punktekarte karte;
    int anzahl;
    GeomagneticField geoMagneticField;
    boolean inruhe=false;
    double[] gravitationalField={0,0,0};
    double[] gravitationalUnity={0,0,0};
    double gravitationalStrength=0;
    double[] magneticField={0,0,0};
    double[] magneticFieldCorrected={0,0,0};
    double magneticStrength;
    double averageMagneticStrength;
    double magneticVertical;
    double averageMagneticVertical;
    double magneticHorizontal;
    double averageMagneticHorizontal;
    double winkel;
    double averageWinkel;

    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        app=(App)getApplication();
		if(app.serviceGestartet==false)
		{
			Log.d("Service","starten");
		serviceIntent = new Intent(this, MessService.class);
		startService(serviceIntent);
		app.serviceGestartet=true;
		}
        geoMagneticField= new GeomagneticField(49, (float) 8.7, 130, System.currentTimeMillis());
       
    	Drawable zielIcon = this.getResources().getDrawable(R.drawable.ic_launcher);
        karte = new Punktekarte(zielIcon,this,app);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.displayZoomControls(true);
		mapOverlays = mapView.getOverlays();
	    mapOverlays.add(karte);
        MyLocationOverlay myLocation=new MyLocationOverlay(getApplicationContext(), mapView);
        myLocation.enableMyLocation();
        //mapOverlays.add(myLocation);
  
        
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
       //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
 
       Button button=(Button) findViewById(R.id.button);
       button.setOnClickListener(
    	         new View.OnClickListener() {
    	             public void onClick(View v) {
    	           averageMagneticHorizontal=0;
    	           averageMagneticVertical=0;
    	           averageMagneticStrength=0;
    	           averageWinkel=0;
    	           anzahl=0;
    	             }
    	         });
       
       helligkeitPlus=(Button) findViewById(R.id.HelligkeitPlus);
       helligkeitPlus.setOnClickListener(
    	         new View.OnClickListener() {
    	             public void onClick(View v) {
    	           helligkeit+=5;
    	           helligkeitText.setText(String.valueOf(helligkeit));
    	           karte.zeichnen(typ,helligkeit,kontrast,mapView);
    	             }
    	         });
       
       helligkeitMinus=(Button) findViewById(R.id.HelligkeitMinus);
       helligkeitMinus.setOnClickListener(
    	         new View.OnClickListener() {
    	             public void onClick(View v) {
    	           helligkeit-=5;
    	           helligkeitText.setText(String.valueOf(helligkeit));
    	           karte.zeichnen(typ,helligkeit,kontrast,mapView);
    	             }
    	         });
       
       kontrastPlus=(Button) findViewById(R.id.KontrastPlus);
       kontrastPlus.setOnClickListener(
    	         new View.OnClickListener() {
    	             public void onClick(View v) {
    	           kontrast+=5;
    	           kontrastText.setText(String.valueOf(kontrast));
    	           karte.zeichnen(typ,helligkeit,kontrast,mapView);
    	             }
    	         });
       
       kontrastMinus=(Button) findViewById(R.id.KontrastMinus);
       kontrastMinus.setOnClickListener(
    	         new View.OnClickListener() {
    	             public void onClick(View v) {
    	           kontrast-=5;
    	           kontrastText.setText(String.valueOf(kontrast));
    	           karte.zeichnen(typ,helligkeit,kontrast,mapView);
    	             }
    	         });
       kontrastText=(EditText) findViewById(R.id.Kontrast);
       helligkeitText=(EditText) findViewById(R.id.Helligkeit);
       kontrastText.addTextChangedListener(new TextWatcher(){
		public void afterTextChanged(Editable arg0) {
			if(arg0.toString()!=null)
			{
				Log.d("etwa","hier");
				kontrast=Double.valueOf(arg0.toString());
			karte.zeichnen(typ,helligkeit,kontrast,mapView);}
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}});
       
       helligkeitText.addTextChangedListener(new TextWatcher(){

   		public void afterTextChanged(Editable arg0) {
   			if(arg0.toString()!=null)
   			{
   				helligkeit=Double.parseDouble(arg0.toString());
   			karte.zeichnen(typ,helligkeit,kontrast,mapView);}
   		}

   		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
   				int arg3) {
   			// TODO Auto-generated method stub
   			
   		}

   		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
   				int arg3) {
   			// TODO Auto-generated method stub
   			
   		}});
       
       
    	
        gesamtText = (TextView) findViewById(R.id.Gesamt); //Textout mit entsprechendem Widget verbinden
        vertikalText = (TextView) findViewById(R.id.Vertikal); //Textout mit entsprechendem Widget verbinden
        horizontalText = (TextView) findViewById(R.id.Horizontal); //Textout mit entsprechendem Widget verbinden
        winkelText = (TextView) findViewById(R.id.Winkel); //Textout mit entsprechendem Widget verbinden
        
        
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    	sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
    	for(Sensor n:sensorList)
    	{
    		Log.d("Sensortypen",String.valueOf(n.getType()));
    		Log.d("Sensortypen",String.valueOf(n.getPower()));
    		if((n.getType()==1)||(n.getType()==2))
    		{

    		Log.d("Sensor",String.valueOf(n.getType()));
    		sensorManager.registerListener(orientationListener,n,SensorManager.SENSOR_DELAY_FASTEST);
    		}
    	}
    	
    	
    	
    }
    
	






private SensorEventListener orientationListener = new SensorEventListener() 
{
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		if(sensorEvent.sensor.getType()==1)
		{
			gravitationalField[0]=(double)sensorEvent.values[0];
			gravitationalField[1]=(double)sensorEvent.values[1];
			gravitationalField[2]=(double)sensorEvent.values[2];
			gravitationalStrength=Math.sqrt(Math.pow(gravitationalField[0], 2)+ Math.pow(gravitationalField[1], 2)+ Math.pow(gravitationalField[2], 2));
			if(Math.abs(gravitationalStrength-SensorManager.GRAVITY_EARTH)<0.2)
			{ 
				inruhe=true;
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
	

	//Averagen
	averageMagneticStrength=(magneticStrength+averageMagneticStrength*anzahl)/(anzahl+1);
	averageMagneticVertical=(magneticVertical+averageMagneticVertical*anzahl)/(anzahl+1);
	averageMagneticHorizontal=(magneticHorizontal+averageMagneticHorizontal*anzahl)/(anzahl+1);
	averageWinkel=(winkel+averageWinkel*anzahl)/(anzahl+1);
	anzahl++;
	
	gesamtText.setText(String.format("%.2f µT", averageMagneticStrength));
	vertikalText.setText(String.format("%.2f µT", averageMagneticVertical));
	horizontalText.setText(String.format("%.2f µT", averageMagneticHorizontal));
	winkelText.setText(String.format("%.2f °", averageWinkel));
	}
	}




	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
    	
    };
    


@Override
public boolean onMenuItemSelected(int featureId, MenuItem item) {
	// TODO Auto-generated method stub
	return super.onMenuItemSelected(featureId, item);
}


@Override
public boolean onCreateOptionsMenu(Menu menu)
{
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu,menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item)
{
	switch(item.getItemId())
	{
	case R.id.Gesamtfeld:
		typ=1;
		karte.zeichnen(1,helligkeit,kontrast,mapView);
		
		break;
	case R.id.vertikaleKomponente:
		typ=2;
		karte.zeichnen(2,helligkeit,kontrast,mapView);
		
		break;
		
	case R.id.horizontaleKomponente:
		typ=2;
		karte.zeichnen(3,helligkeit,kontrast,mapView);
		
		break;
		
	case R.id.Winkel:
		typ=2;
		karte.zeichnen(4,helligkeit,kontrast,mapView);
		
		break;

	}
	return true;
}


public void onLocationChanged(Location location) {
	//Log.d("Location",String.valueOf(location.getLatitude()));
	//karte.addLocation(location, 2);
	lastLocation.set(location);
	geoMagneticField=new GeomagneticField((float)location.getLatitude(), (float)location.getLongitude(), (float)location.getAltitude(), System.currentTimeMillis());
	// TODO Auto-generated method stub
	
}


public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}


public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}


public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}


@Override
protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
}
    

   

}
