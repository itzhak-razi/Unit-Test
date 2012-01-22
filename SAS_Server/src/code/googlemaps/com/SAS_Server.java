package code.googlemaps.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class SAS_Server extends Activity//extends MapActivity 
{
	public static EditText  longitude_xml, latitude_xml, phone_xml;
	
	public static String latitude, longitude, phone;
	
	public static EmergencyDataBase      dataBase;
	
	public static String clientNumber;
	
	
	
	//public static EditText longitude;
	//public static EditText latitude;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

    	dataBase  = new EmergencyDataBase();


    	longitude_xml  =  (EditText) findViewById(R.id.longitudeValue);
    	latitude_xml  = (EditText) findViewById(R.id.latitudeValue);
    	phone_xml = (EditText) findViewById(R.id.phonevalue);

    	longitude_xml.setText("none");
    	latitude_xml.setText("none");
    	phone_xml.setText("none");
    	latitude   = "none";
    	longitude = "none";
    	phone = "none";
    }
    
    public void onResume()
    {
 	  super.onResume();
 	   
 	  longitude_xml.setText(longitude);
 	  latitude_xml.setText(latitude);
 	  phone_xml.setText(phone);
    }
    
    public void mapButtonHandler(View view) 
    {
//    	longitude_xml = (EditText)findViewById(R.id.longitudeValue);
//        latitude_xml = (EditText)findViewById(R.id.latitudeValue);
//    	
//        if (longitude_xml.getText() == null || 
//    		latitude_xml.getText() == null || 
//    		longitude_xml.getText().toString() == "none" || 
//    		latitude_xml.getText().toString() == "none")
//    		Toast.makeText(this, longitude_xml.getText(), Toast.LENGTH_SHORT).show();
//    	
//    	else
//    	{    		
	    	Intent intentExercise = new Intent(view.getContext(), showTheMap.class);
	        startActivityForResult(intentExercise, 0);
//    	}
        
    } 
    
    public void smsButtonHandler(View view) 
    {
    	Intent intentExercise = new Intent(view.getContext(), ServerPart.class);
    	startActivity(intentExercise);
    } 
    
    public void saveButtonHandler(View view) 
    {
    	longitude_xml = (EditText)findViewById(R.id.longitudeValue);
        latitude_xml = (EditText)findViewById(R.id.latitudeValue);
        phone_xml = (EditText)findViewById(R.id.phonevalue);
    	        
    	Toast.makeText(this,"Saved the points..." , Toast.LENGTH_SHORT).show();
    } 
    
    
    
    
    
        
    
    
}
