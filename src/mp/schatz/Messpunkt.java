package mp.schatz;

import android.location.Location;

public class Messpunkt 
{
	private double latitude;
	private double longitude;
	private double altitude;
	private double magneticHorizontal;
	private double magneticVertical;
	
	public Messpunkt(Location location, double vertical, double horizontal) 
	{
		latitude=location.getLatitude();
		longitude=location.getLongitude();
		altitude=location.getAltitude();
		magneticVertical=vertical;
		magneticHorizontal=horizontal;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public double getMagneticHorizontal() {
		return magneticHorizontal;
	}

	public double getMagneticVertical() {
		return magneticVertical;
	}
	
	



}
