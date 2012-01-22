package Firstdroid.Tutorial.Gps;

import android.telephony.SmsManager;

public class SMSSender 
{
	public String strMessage;
	public String strPhoneNumber;
	public SMSSender()
	{}
	public SMSSender(String p_strMessage,String p_strPhoneNumber)
	{
		strMessage=p_strMessage;
		strPhoneNumber=p_strPhoneNumber;
	}
	public String getMessage()
	{
		return strMessage;
	}
	public String getPhoneNumber()
	{
		return strPhoneNumber;
	}
	//Send string
	public void sendSingleString(String destination,String s)
	{
		try
		{
	          sendSmsMessage(destination,s);
		}
		catch(Exception e )
		{}
	}
	//Construct SAS format from inputs and send.
	public void sendSMS(String destination,String latitude,String longitude, String provider ,String body)
	{
		try
		{
	          sendSmsMessage(destination, "SAS1" + 
	        		  						"\n" + latitude + 
	        		  						"\n" + longitude + 
	        		  						"\n" + provider + 
	        		  						"\n" + body);
		}
		catch(Exception e )
		{}
	}
	  
    private void sendSmsMessage(String address,String message) throws Exception
	{
		SmsManager smsMgr = SmsManager.getDefault();
		smsMgr.sendTextMessage(address, null, message, null, null);
	}
    
    

}
