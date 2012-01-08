package cyclerouteplanner.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.directions.DirectionsCallback;
import com.google.gwt.maps.client.directions.DirectionsRequest;
import com.google.gwt.maps.client.directions.DirectionsService;
import com.google.gwt.maps.client.directions.DirectionsStatus;
import com.google.gwt.maps.client.directions.DirectionsTravelMode;
import com.google.gwt.maps.client.directions.DirectionsUnitSystem;
import com.google.gwt.maps.client.directions.HasDirectionsLeg;
import com.google.gwt.maps.client.directions.HasDirectionsResult;
import com.google.gwt.maps.client.directions.HasDirectionsRoute;
import com.google.gwt.maps.client.directions.HasDirectionsStep;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;

public class RouteManager extends MouseEventCallback {

	private List<HasLatLng> clicks = new ArrayList<HasLatLng>();
	private List<List<HasDirectionsStep>> route = new ArrayList<List<HasDirectionsStep>>();
	
	private double distance = 0.0;

	private DirectionsService dirSvc = new DirectionsService();
	
	private final static DirectionsStatus Status = new DirectionsStatus();
	private final static DirectionsTravelMode TravelMode = new DirectionsTravelMode();
	private final static DirectionsUnitSystem UnitSystem = new DirectionsUnitSystem();

	//TODO: Create a route change event and and interface sensitive to this instead of
	// 		coupling like this.
	private MapRenderer mapRenderer;
	private UiRenderer uiRenderer;
	
	public void onModuleLoad(MapRenderer mapRenderer, UiRenderer uiRenderer){
		this.mapRenderer = mapRenderer;
		this.uiRenderer = uiRenderer;
	}
	
	
	@Override public void callback(HasMouseEvent event) {
		GWT.log("Mouse clicked: " + event);
		HasLatLng latLng = event.getLatLng();
		clicks.add(latLng);

		if (clicks.size() < 2)
			return;

		HasLatLng start = clicks.get(clicks.size() - 2);
		HasLatLng end = clicks.get(clicks.size() - 1);

		DirectionsRequest request = new DirectionsRequest();
		request.setOriginLatLng(start);
		request.setDestinationLatLng(end);
		request.setTravelMode(TravelMode.Driving());
		request.setUnitSystem(UnitSystem.Metric());
		request.setProvideRouteAlternatives(false);

		if (!GWT.isScript()) {
			removeGwtObjectId(request.getJso());
		}

		GWT.log("Requesting route");
		dirSvc.route(request, new DirectionsCallback() {
			@Override public void callback(HasDirectionsResult response, String status) {
				GWT.log("RouteUpdated");
				routeUpdated(response, status);
			}
		});
	}
	
	private void routeUpdated(HasDirectionsResult response, String status){
		if (Status.Ok().equals(status)) {
			HasDirectionsRoute newRoute = response.getRoutes().get(0);
			List<HasDirectionsLeg> newLegs = newRoute.getLegs();

			List<HasDirectionsStep> newSteps = new ArrayList<HasDirectionsStep>();
			for (HasDirectionsLeg leg : newLegs) {
				distance += leg.getDistance().getValue();
				for (HasDirectionsStep newStep : leg.getSteps()) {
					newSteps.add(newStep);
				}
			}
			route.add(newSteps);
			uiRenderer.routeUpdated(distance);
			mapRenderer.drawRoutePart(newSteps);
		} else {
			GWT.log("Failed to get route. Status: " + status);
		}
	}
	

	// Workaround
	private native void removeGwtObjectId(JavaScriptObject jso) /*-{
		delete jso['__gwt_ObjectId'];
	}-*/;

}
