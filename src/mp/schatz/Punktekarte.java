package mp.schatz;

import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class Punktekarte extends ItemizedOverlay 
{	public Activity activity;	
	private App app;
	public ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	public Punktekarte(Drawable defaultMarker, Activity activity, App apparg) 
	{
		super(boundCenterBottom(defaultMarker));
		this.activity=activity;
		app=apparg;
		addPunkt(0,0);
		//populate();

	}


	
	public void zeichnen(int typ,double helligkeit, double kontrast, MapView mapView)
	{
		this.mOverlays.clear();
		Log.d("Zeichnen", String.valueOf((double)(100.00/mapView.getLatitudeSpan()*mapView.getWidth())));
		if(app.magnetkarte.liste.size()!=0)
		
			{for(Kartenpunkt n:app.magnetkarte.liste)
		{
			Log.d("Logs",";"+String.valueOf(n.latitude) + ";"+ String.valueOf(n.longitude) + ";"+ String.valueOf(n.magneticVertical) + ";"+ String.valueOf(n.magneticVertical));
			GeoPoint point = new GeoPoint((int)(n.latitude*100),(int)(n.longitude*100));
			OverlayItem overlayitem = new OverlayItem(point,String.valueOf(n.magneticVertical),String.valueOf(n.magneticHorizontal));
			Drawable drawable= activity.getResources().getDrawable(R.drawable.rot);
			double resolution = (double)(100.00/mapView.getLatitudeSpan()*mapView.getWidth());
			//Log.d("Resolution",String.valueOf(resolution));
			drawable.setBounds((int)(-resolution), (int)(-resolution), 0, 0);
			if(typ==1)
			{drawable.setAlpha(alphaBestimmen(Math.sqrt(Math.pow(n.magneticVertical,2)+Math.pow(n.magneticHorizontal,2)),helligkeit,kontrast));}
			if(typ==2)
			{drawable.setAlpha(alphaBestimmen(n.magneticVertical, helligkeit, kontrast));}
			if(typ==3)
			{drawable.setAlpha(alphaBestimmen(n.magneticHorizontal, helligkeit, kontrast));}
			if(typ==4)
			{drawable.setAlpha(alphaBestimmen(Math.acos(n.magneticVertical/Math.sqrt(Math.pow(n.magneticVertical,2)+Math.pow(n.magneticHorizontal,2))),helligkeit,kontrast));}
			//drawable.setBounds(-100, -100, 100, 100);
			//drawable.setAlpha(255);
			overlayitem.setMarker(drawable);
			mOverlays.add(overlayitem);


		}
			

			}
		else{Log.d("Zeichnen","noch kein Wert");}
		populate();	
		Toast.makeText(app, "gezeichnet", Toast.LENGTH_SHORT).show();
	}
	
	public Punktekarte(Drawable defaultMarker, Activity activity, Location location, int umkreis, int typ)
	{
		super(boundCenterBottom(defaultMarker));
		this.activity=activity;
	}
	
	
	
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	
	
	public void addPunkt(double latitude,double longitude)
	{
		GeoPoint point = new GeoPoint((int)(latitude*1000000),(int)(longitude*1000000));
		OverlayItem overlayitem = new OverlayItem(point, "", "");
		mOverlays.add(overlayitem);
		Log.d("Droid","zeichnen");
		populate();
	}
	
	

	
	public void addLocation(Location location, double value)
	{
		GeoPoint point = new GeoPoint((int)(location.getLatitude()*1000000),(int)(location.getLongitude()*1000000));
		OverlayItem overlayitem = new OverlayItem(point,"value","");
		Drawable drawable= activity.getResources().getDrawable(R.drawable.rot);
		drawable.setBounds(-5, -5, 5, 5);
		drawable.setAlpha((int) value);
		overlayitem.setMarker(drawable);
		mOverlays.add(overlayitem);
		populate();	
	}
	
	@Override 
    public void draw(Canvas canvas, MapView mapView, boolean shadow) 
    { 
        if(!shadow) 
        { 
            super.draw(canvas, mapView, false); 
        } 
    }
    
	
	@Override
	protected OverlayItem createItem(int i) 
	{
	  return mOverlays.get(i);
	}

	private int alphaBestimmen(double wert, double helligkeit, double kontrast)
	{

		if(helligkeit+kontrast<wert)
		{Log.d("Alpha",String.valueOf(255));
			return 250;}
		if(helligkeit-kontrast>wert)
		{Log.d("Alpha","10");
			return 10;}
		double temp=((wert-helligkeit)/kontrast*128.0+128.0);
		Log.d("Alpha",String.valueOf(temp));
		return (int)temp;
	
	}

	
	@Override
	protected boolean onTap(int index) {
		Log.d("touch","item"+String.valueOf(mOverlays.get(index).getPoint().getLatitudeE6()));
		//((Mainscreen)activity).dialogStrecke=mOverlays.get(index).getTitle()
		Toast.makeText(activity, mOverlays.get(index).getTitle()+"<->"+mOverlays.get(index).getSnippet() , Toast.LENGTH_SHORT).show();
		//activity.showDialog(1);
		return super.onTap(index);
	}



	

	
}
