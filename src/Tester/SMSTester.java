package Tester;
import java.util.Calendar;

import junit.framework.TestCase;
import Firstdroid.Tutorial.Gps.SMSSender;
import code.googlemaps.com.EmergencyDataBase;
import code.googlemaps.com.EmergencyDataBase.EmergencyNode;

public class SMSTester extends TestCase { 
	
	//Validation of data
	public void testMessageIsNotEmpty()
	{
		SMSSender sms = new SMSSender("Message","");
		assertEquals(sms.getMessage().isEmpty(),false);
	}
	public void testNumberPhoneIsNotEmptyAndInCorrectFormat()
	{
		SMSSender sms = new SMSSender("","0526850388");
		assertEquals(sms.getPhoneNumber().isEmpty(),false);
		assertEquals(sms.strPhoneNumber.matches("\\d{10}"),true);
	}
	
	//Validation of DB operation
	public void testAddNode()
	{
		EmergencyDataBase db=new EmergencyDataBase();		
		int intCount=db.getSize();
		db.addNode("a",Long.toString( Calendar.getInstance().getTimeInMillis()), "a");
		int intNewCount=db.getSize();
		assertEquals(intCount+1==intNewCount,true);
	}
	
    public void testGetNode()
    {	
    EmergencyDataBase db=new EmergencyDataBase();
    db.addNode("Get object text",Long.toString( Calendar.getInstance().getTimeInMillis()), "a");
    EmergencyNode objReturnsNode=db.getNode(1);
	assertEquals(objReturnsNode.text.isEmpty(),false);
	String strNewString="Set new text";
	objReturnsNode.text=strNewString;
	assertEquals(db.getNode(1).text==strNewString,false);
    }
    
   public void testGetRealNode()
    {	
	EmergencyDataBase db=new EmergencyDataBase();
	db.addNode("Get object text",Long.toString( Calendar.getInstance().getTimeInMillis()), "a");
	EmergencyNode objReturnsNode=db.getRealNodeById(1);
	assertEquals(objReturnsNode.text==null,false);
	String strNewString="Set new text";
	objReturnsNode.text=strNewString;
	String strOfNodeInList=db.getRealNodeById(1).text;
	assertEquals(strOfNodeInList==strNewString,true);
    }
} 

