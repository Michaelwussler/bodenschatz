package mp.schatz;

import android.location.Location;


public class Kartenpunkt
{
	int anzahl=0;
	int latitude=0;
	int longitude=0;
	double magneticVertical;
	double magneticHorizontal;
	Location location;
	public Kartenpunkt(int latitude2, int longitude2, double magneticVerticalarg, double magneticHorizontalarg)
	{
		latitude=latitude2;
		longitude=longitude2;
		magneticVertical=magneticVerticalarg;
		magneticHorizontal=magneticHorizontalarg;
		location=new Location("GPS");
		location.setLatitude(latitude/10000);
		location.setLongitude(longitude/10000);
		anzahl=1;
	}
	public void update(double magneticVertical2, double magneticHorizontal2) {
		magneticVertical=(magneticVertical*anzahl+magneticVertical)/(anzahl+1);
		magneticHorizontal=(magneticHorizontal*anzahl+magneticHorizontal)/(anzahl+1);
		anzahl++;
	}

}


