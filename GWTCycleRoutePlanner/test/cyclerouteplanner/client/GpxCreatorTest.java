package cyclerouteplanner.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.maps.gwt.client.LatLng;

public class GpxCreatorTest extends GWTTestCase {

	@Test
	public void testThatWeCanCreateARouteWithOnePoint(){
		
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(LatLng.create(1.0, 1.0));
		
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
			"<gpx xmlns=\"http://www.topografix.com/GPX/1/1\">\n"+
			"    <rte>\n"+
			"        <rtept lon=\"1\" lat=\"1\"/>\n"+
			"    </rte>\n"+
			"</gpx>\n";

		assertThat(GpxCreator.getRouteString("Test",points), equalTo(expected));
	}

	@Override public String getModuleName() {
		return "cyclerouteplanner.GWTCycleRoutePlanner";
	}
	
}
