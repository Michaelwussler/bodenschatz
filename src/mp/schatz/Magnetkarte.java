package mp.schatz;

import java.util.ArrayList;

import android.location.Location;

public class Magnetkarte 
{
	ArrayList<Kartenpunkt> liste= new ArrayList<Kartenpunkt>();

	public Magnetkarte() 
	{
		
	}
	
	public void setPoint(Location location, double magneticVertical, double magneticHorizontal)
	{
		int latitude=((int)(location.getLatitude()*10000));
		int longitude=((int)(location.getLongitude()*10000));
		boolean gefunden=false;
		for(Kartenpunkt n:liste)
		{
			if((n.latitude==latitude)&&(n.longitude==longitude))
			{
				gefunden=true;
				n.update(magneticVertical, magneticHorizontal);
			}
		}
		if(gefunden==false)
		{
			liste.add(new Kartenpunkt(latitude,longitude,magneticVertical,magneticHorizontal));
		}
	}

}


