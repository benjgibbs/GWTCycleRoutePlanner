package cyclerouteplanner.client;

import java.util.List;

import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class GpxCreator
{
		
	public static String getRouteString(List<HasLatLng> points){
		Document doc = XMLParser.createDocument();
		Element gpx = doc.createElement("gpx");
		doc.appendChild(gpx);
		Element rte = doc.createElement("rte");
		gpx.appendChild(rte);
		for(HasLatLng p : points){
			Element rtept = doc.createElement("rtept");
			rtept.setAttribute("lat", Double.toString(p.getLatitude()));
			rtept.setAttribute("lon", Double.toString(p.getLongitude()));
			rte.appendChild(rtept);
		}
		
		return doc.toString();
	}
}