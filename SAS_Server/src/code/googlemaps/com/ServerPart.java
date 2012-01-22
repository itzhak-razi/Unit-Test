package code.googlemaps.com;


import java.util.ArrayList;


import code.googlemaps.com.EmergencyDataBase.EmergencyNode;
import code.googlemaps.com.R;
import code.googlemaps.com.SmsReceiver;
import code.googlemaps.com.R.id;
import code.googlemaps.com.R.layout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ServerPart extends Activity implements OnClickListener, OnItemClickListener
{
	//Our database
	EmergencyDataBase dataBase;
	
	//List of emergencies
	ArrayList<String> smsList = new ArrayList<String>();
	
	Context context;
	boolean inArchive = false;
	

	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setTheme( android.R.style.Theme_Light );
        setContentView(R.layout.update_database);
        
        this.dataBase = SAS_Server.dataBase;
       
        context   = getApplicationContext();
        
        //IZIK register popup menu for SMS List
        //
        registerForContextMenu(findViewById(R.id.SMSList));
        
        this.findViewById( R.id.UpdateList ).setOnClickListener(this);
        this.findViewById( R.id.ArchiveList ).setOnClickListener(this);
    	
		
        /**
         * You can also register your intent filter here.
         * And here is example how to do this.
         *
         * IntentFilter filter = new IntentFilter( "android.provider.Telephony.SMS_RECEIVED" );
         * filter.setPriority( IntentFilter.SYSTEM_HIGH_PRIORITY );
         * registerReceiver( new SmsReceiver(), filter );
        **/     
        //Update data base when start.
        findViewById(R.id.UpdateList).performClick();

    }
	
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo)
	{
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    
	    if(inArchive)
	    	inflater.inflate(R.layout.pop_up_archive, menu);
	    else
	        inflater.inflate(R.layout.pop_up, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	            .getMenuInfo();
	    
	    String[]           splitted; 
	    String             aux,id;
	    int                nodeId;
	    EmergencyNode      currNode;
	 
	    //get selected item from list
	    aux      = smsList.get((int) info.id);
	    splitted = aux.split("\n"); 
	    id       = splitted[0];
	    id       = id.substring(11);
	    nodeId   = Integer.parseInt(id);
	    currNode = dataBase.getNodeById(nodeId);
	    
	    splitted = currNode.text.split("\n");
	    
        //Take care on active node.
	    if(currNode.status.equals("active"))
	    	switch (item.getItemId())
	    	{
	    	case R.id.Remove:
	    		dataBase.getRealNodeById(nodeId).status = "archive";
	    		findViewById(R.id.UpdateList).performClick();
	    		Toast.makeText( context ,"Removed to archive" ,Toast.LENGTH_SHORT ).show();
	    		
	    		break;


	    	case R.id.Fetch:
	    		 SAS_Server.latitude      = splitted[1];
	             SAS_Server.longitude     = splitted[2];
	             SAS_Server.phone = currNode.source;
	    		Toast.makeText( context ,"Fetched" + "\n" + "Emergency ID : " + currNode.nodeId ,Toast.LENGTH_SHORT ).show();
	    		break;
	    	}
	    //Take care on archive node
	    if(currNode.status.equals("archive"))
	    	switch (item.getItemId())
	    	{
	    	case R.id.Return:
	    		dataBase.getRealNodeById(nodeId).status = "active";
	    		findViewById(R.id.ArchiveList).performClick();
	    		Toast.makeText( context ,"Returned" ,Toast.LENGTH_SHORT ).show();
	    		break;


	    	case R.id.FetchArchive:
	    		 SAS_Server.latitude    = splitted[1];
	             SAS_Server.longitude   = splitted[2];
	             SAS_Server.phone = currNode.source;
	    		Toast.makeText( context ,"FETCHED Archive" + "\n" + "ID : " + currNode.nodeId ,Toast.LENGTH_SHORT ).show();
	    		break;
	    	}
	    return false;
	}

    //Click on single event 
	public void onItemClick( AdapterView<?> parent, View view, int pos, long id ) 
	{
		String altit,longit,sender,data = null,status,time;
		String[] splitted;
		int nodeId;
		
		String item = ((TextView)view).getText().toString();
		splitted    = item.split("\n");
		
		data        = splitted[0].substring(11);
		nodeId      = Integer.parseInt(data);
		

		try 
		{
			
		    splitted = dataBase.getNodeById(nodeId ).text.split("\n"); 
			
		    status = dataBase.getNode(nodeId).status;
		    time   = dataBase.getNode(nodeId).timeArrived;
			sender = dataBase.getNode(nodeId).source;
			
			altit  = splitted[1];
			longit = splitted[2];
	
			data = "";
			for ( int i = 3; i < splitted.length; ++i )
			{
			    data += splitted[i];
			}
		    
			//Long.parseLong(altit);
			//Long.parseLong(longit);
            //			sender
			//instead of this toast , location on map can be shown.
			Toast.makeText( this,"Sender : " + sender  + "\n"+
					             "Time   : " + time    + "\n"+
					             "Latitude : " + altit + "\n" +
					             "Longitude: " + longit + "\n" +
					             "Text   : " + data            , Toast.LENGTH_SHORT ).show();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
    
    
	//Click on button Update database.Builds DataBase from inbox.
	//Click on button Update database.Builds DataBase from inbox.
		public void onClick( View v ) 
		{
			ContentResolver contentResolver = getContentResolver();
			Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ),null,null, null, null);

			int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );
			int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );
			int indexDate = cursor.getColumnIndex( SmsReceiver.DATE);
			EmergencyNode auxNode;
			
			String aux = "";
			
			if ( indexBody < 0 || !cursor.moveToFirst() ) return;
			
			smsList.clear();
			
			//Fill the database...
			//Check each sms for required format.
			if( v.getId() == R.id.UpdateList )
			{
				inArchive = false;
				do
				{

					aux = cursor.getString( indexBody );
					if(aux.substring(0, 4).equals("SAS1"))
					{
						this.dataBase.addNode(aux, cursor.getString( indexDate ),cursor.getString( indexAddr ) );
						//smsList.add( str );
					}
				}
				while( cursor.moveToNext() );


				//Fill the GUI. GUI works with database, not with inbox.
				for( int i = 1  ; i <= this.dataBase.getSize() ; i++)
				{
					auxNode = dataBase.getNodeById(i);
					if(!auxNode.status.equals("archive"))
						smsList.add("Emergency #"+ auxNode.nodeId     + "\n"+
								"Sender:"    + auxNode.source     + "\n"+
								"Status :"   + auxNode.status     + "\n"+ 
								"Arrived: " +  auxNode.timeArrived+ "\n"+
						"_________________________________");
				}
			}
			//Fill the Archive
			if( v.getId() == R.id.ArchiveList)
			{
				inArchive = true;
				
				
				//Fill the GUI. GUI works with database, not with inbox.
				for( int i = 1  ; i <= this.dataBase.getSize() ; i++)
				{
					auxNode = dataBase.getNodeById(i);
					if(auxNode.status.equals("archive"))
						smsList.add("Emergency #"+ auxNode.nodeId     + "\n"+
								"Sender:"    + auxNode.source     + "\n"+
								"Status :"   + auxNode.status     + "\n"+ 
								"Arrived: " +  auxNode.timeArrived+ "\n"+
						"_________________________________");
				}
			}
			
		

			
			ListView smsListView = (ListView) findViewById( R.id.SMSList );
			smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
			smsListView.setOnItemClickListener( this );
		}
}