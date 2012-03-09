package cyclerouteplanner.client;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import cyclerouteplanner.client.gpx.GpxType;
import cyclerouteplanner.client.gpx.ObjectFactory;
import cyclerouteplanner.client.gpx.RteType;
import cyclerouteplanner.client.gpx.WptType;

public class GpxCreator
{
	final GpxType gpx; 
	final ObjectFactory factory = new ObjectFactory();
	
	public GpxCreator(List<LatLng> points){
		gpx = factory.createGpxType();
		RteType rte = factory.createRteType();
		List<WptType> rtept = rte.getRtept();
		for(LatLng p : points){
			rtept.add(p.toWpt(factory));
		}
		gpx.getRte().add(rte);
	}
	
	public GpxType getRoute() {
		return gpx;
	}
	
	public String getRouteString(){
		try {
			JAXBElement<GpxType> createdGpx = factory.createGpx(gpx);
			JAXBContext jc = JAXBContext.newInstance("cyclerouteplanner.client.gpx");
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			marshaller.marshal(createdGpx, out);
			return out.toString();
		} catch (JAXBException e) {
			return "Error: " + e;
		}
	}
}