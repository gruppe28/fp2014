package fp2014;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import database.DBHandler;
import GUI.AlarmGUI;
import GUI.KalenderView;

public class Watcher {

	private ArrayList<Alarm> alarms;
	private Ansatt user;

	public Watcher(final KalenderView parent, final Ansatt user){
		
		this.user = user;
		
		// Load alarms
		loadAlarms();
	
		Timer t = new Timer();

		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		        	// Update announcement counter
		        	parent.updateAnnounchementCounter();
		        	
		        	//Trigger alarms
		        	String time = getTime();
		        	String date = getDate();

		        	for(Alarm a : alarms){
		        		if(a.getTime().equals(time) && a.getDate().equals(date)) { 
		        			new AlarmGUI(parent, user, a);
		        		}
		        	}
		        	
		        }
		    },
		    0,
		    60000); // Repeat each 60 seconds
		
	}
	
	public void loadAlarms(){
		alarms = DBHandler.getAlarms(user);
	}
	
	private String getTime(){
		return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
	}
	
	private String getDate(){
		return new SimpleDateFormat("d.M.y").format(Calendar.getInstance().getTime());
	}
}