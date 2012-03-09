package cyclerouteplanner;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import cyclerouteplanner.client.RouteManager;

public class GpxTests extends GWTTestCase {

	@Test public void checkThatBasisGpxCanBeCreated() {
		RouteManager rm = new RouteManager();
		String route = rm.createGpxRoute();
		
	}

	@Override public String getModuleName() {
		return "';
	}

}
