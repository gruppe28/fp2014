package fp2014;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import GUI.AlarmPanel;
import GUI.MainFrame;
import database.DBHandler;

public class Watcher {

	private ArrayList<Alarm> alarms;
	private User user;
	private Timer t;

	public Watcher(final MainFrame parent, final User user){
		
		this.user = user;
		
		// Load alarms
		loadAlarms();
	
		t = new Timer();

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
		        			new AlarmPanel(parent, user, a);
		        		}
		        	}
		        	
		        }
		    },
		    0,
		    20000); // Repeat each 60 seconds
		
	}
	
	public void loadAlarms(){
		alarms = DBHandler.getAlarms(user);
	}
	
	public void stop(){
		t.cancel();
	}
	
	private String getTime(){
		return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
	}
	
	private String getDate(){
		return new SimpleDateFormat("d.M.y").format(Calendar.getInstance().getTime());
	}
	
}
