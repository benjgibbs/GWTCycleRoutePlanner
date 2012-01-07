package cyclerouteplanner;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Test;

import com.google.gwt.maps.client.directions.DirectionsTravelMode;

public class PlaceHolderTest {

	@Test public void whatDoesTravelModeReturn() {
		DirectionsTravelMode mode = new DirectionsTravelMode();
		String walking = mode.Walking();
		String driving = mode.Driving();
		assertThat(walking, equalTo("WALKING"));
		assertThat(driving, equalTo("DRIVING"));
		
	}
}

