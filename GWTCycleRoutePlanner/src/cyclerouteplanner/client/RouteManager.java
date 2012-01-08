package cyclerouteplanner.client;

import java.util.ArrayList;
import java.util.LinkedList;
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

import cyclerouteplanner.client.Events.ClearRouteEvent;
import cyclerouteplanner.client.Events.ClearRouteListener;
import cyclerouteplanner.client.Events.EventGenerator;
import cyclerouteplanner.client.Events.RemoveLastPointEvent;
import cyclerouteplanner.client.Events.RemoveLastPointListener;
import cyclerouteplanner.client.Events.RouteUpdatedEvent;
import cyclerouteplanner.client.Events.RouteUpdatedListener;

public class RouteManager extends MouseEventCallback  {

	private LinkedList<HasLatLng> clicks = new LinkedList<HasLatLng>();
	private LinkedList<List<HasDirectionsStep>> route = new LinkedList<List<HasDirectionsStep>>();
	private EventGenerator<RouteUpdatedEvent> routeUpdatedEventor = new EventGenerator<RouteUpdatedEvent>(); 
	private double distance = 0.0;

	private DirectionsService dirSvc = new DirectionsService();
	
	private final static DirectionsStatus Status = new DirectionsStatus();
	private final static DirectionsTravelMode TravelMode = new DirectionsTravelMode();
	private final static DirectionsUnitSystem UnitSystem = new DirectionsUnitSystem();

	public void addRouteUpdatedListener(RouteUpdatedListener listener){
		routeUpdatedEventor.addListener(listener);
	}
	
	@Override public void callback(HasMouseEvent event) {
		GWT.log("Mouse clicked: " + event);
		HasLatLng latLng = event.getLatLng();
		clicks.add(latLng);

		if (clicks.size() < 2)
			return;

		HasLatLng start = clicks.get(clicks.size() - 2);
		HasLatLng end = clicks.getLast();

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
				routeUpdated(response, status);
			}
		});
	}
	
	private void routeUpdated(HasDirectionsResult response, String status){
		GWT.log("RouteUpdated: " + response);
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
			routeUpdatedEventor.onEvent(new RouteUpdatedEvent(route, distance));
		} else {
			GWT.log("Failed to get route. Status: " + status);
		}
	}
	

	// Workaround
	private native void removeGwtObjectId(JavaScriptObject jso) /*-{
		delete jso['__gwt_ObjectId'];
	}-*/;
	
	ClearRouteListener getClearRouteListener(){
		return new ClearRouteListener() {
			@Override public void onEvent(ClearRouteEvent event) {
				
			}
		};
	}
	
	RemoveLastPointListener getRemoveLastPointListener(){
		return new RemoveLastPointListener() {
			@Override public void onEvent(RemoveLastPointEvent event) {
				if(clicks.size() == 0)
					return;
				
				clicks.removeLast();
				
				if(route.size() == 0)
					return;
				
				List<HasDirectionsStep> lastLeg =route.removeLast();
				for(HasDirectionsStep step : lastLeg){
					distance -= step.getDistance().getValue();
				}
				
				routeUpdatedEventor.onEvent(new RouteUpdatedEvent(route,distance));
			}
		};
	}

}
