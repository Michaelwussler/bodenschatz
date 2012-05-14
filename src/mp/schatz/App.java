package mp.schatz;

import java.util.List;

import android.app.Application;
import android.content.Intent;

public class App extends Application 
{
	List<Messpunkt> offlineAufnahmen;
	Magnetkarte magnetkarte=new Magnetkarte();
	boolean serviceGestartet=false;
	Intent serviceIntent;
	@Override
	public void onCreate() {
		serviceIntent = new Intent(this, MessService.class);
		super.onCreate();
	}
	
}
