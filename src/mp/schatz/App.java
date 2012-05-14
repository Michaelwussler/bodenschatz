package mp.schatz;

import java.util.List;

import android.app.Application;

public class App extends Application 
{
	List<Messpunkt> offlineAufnahmen;
	Magnetkarte magnetkarte=new Magnetkarte();
	boolean serviceGestartet=false;
}
