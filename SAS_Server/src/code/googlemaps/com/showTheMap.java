package code.googlemaps.com;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.android.maps.MapView.LayoutParams;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class showTheMap extends MapActivity
{
	MapView mapView; 
    MapController mc;
    GeoPoint p;
    String longitude, latitude;
     
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pushpin_red_70_33);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            return true;
        }
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);
 
        mapView = (MapView) findViewById(R.id.map_view);
//        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls(); 
 
//        zoomLayout.addView(zoomView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
 
        mc = mapView.getController();
        
        longitude = SAS_Server.longitude_xml.getText().toString();
    	latitude = SAS_Server.latitude_xml.getText().toString();
    	Toast.makeText(this,"Loading the point:\nLatitude= " + latitude + "\nLongitude= " + longitude, Toast.LENGTH_SHORT).show();
        
                
//        Toast.makeText(this,"NULL" , Toast.LENGTH_SHORT).show();
//        String coordinates[] = {"31.821273", "35.244784"}; HOME 
//        String coordinates[] = {"31.769633", "35.193355"}; //Collegue
        
        String coordinates[] = {latitude,longitude};
        
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
 
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
 
        mc.animateTo(p);
        mc.setZoom(17);
        
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
 
        mapView.invalidate();
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}
