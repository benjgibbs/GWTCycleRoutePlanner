package cyclerouteplanner.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import cyclerouteplanner.client.gpx.GpxType;

public class GpxCreatorTest {

	
	
	@Test
	public void testThatWeCanCreateARouteWithOnePoint() throws JAXBException {
		
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(new LatLng(1.0, 1.0));
		
		GpxCreator creator = new GpxCreator(points);
		GpxType gpx = creator.getRoute();
				
		
		assertThat(gpx.getRte().size(), equalTo(1));
		
		
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
			"<gpx xmlns=\"http://www.topografix.com/GPX/1/1\">\n"+
			"    <rte>\n"+
			"        <rtept lon=\"1\" lat=\"1\"/>\n"+
			"    </rte>\n"+
			"</gpx>\n";
		assertThat(creator.getRouteString(), equalTo(expected));
	}
	
	


	
}
