package cyclerouteplanner.client;

import java.math.BigDecimal;

import cyclerouteplanner.client.gpx.ObjectFactory;
import cyclerouteplanner.client.gpx.WptType;

class LatLng
{
	LatLng(double lat, double lng)
	{
		this.lat = lat;
		this.lng = lng;
	}
	
	double lat;
	double lng;
	WptType toWpt(ObjectFactory factory)
	{
		WptType type =  factory.createWptType();
		type.setLat(new BigDecimal(lat));
		type.setLon(new BigDecimal(lng));
		return type;
	}
}