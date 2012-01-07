package cyclerouteplanner.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;
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
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class MainPage implements EntryPoint {
	private MapWidget mapWidget;
	private DirectionsService dirSvc = new DirectionsService();

	private List<HasLatLng> clicks = new ArrayList<HasLatLng>();
	private List<List<HasDirectionsStep>> route = new ArrayList<List<HasDirectionsStep>>();
	private List<Polyline> drawnRoute = new ArrayList<Polyline>();
	private double distance = 0.0;
	
	
	private final static DirectionsStatus Status = new DirectionsStatus();
	private final static DirectionsTravelMode TravelMode = new DirectionsTravelMode();
	private final static DirectionsUnitSystem UnitSystem = new DirectionsUnitSystem();
	

	@Override public void onModuleLoad() {

		LatLng home = new LatLng(51.0747504771771, -1.3252487182617188);
		final MapOptions options = new MapOptions();
		options.setZoom(14);
		options.setCenter(home);
		options.setMapTypeId(new MapTypeId().getRoadmap());
		options.setDraggable(true);
		options.setNavigationControl(true);
		options.setMapTypeControl(true);
		mapWidget = new MapWidget(options);

		Window.addResizeHandler(new ResizeHandler() {
			@Override public void onResize(ResizeEvent event) {
				fillTheScreen(event.getWidth(), event.getHeight());
			}
		});

		GWT.log("Adding click handler.");
		Event.addListener(mapWidget.getMap(), "click",
				new MouseEventCallback() {
					@Override public void callback(HasMouseEvent event) {
						GWT.log("Click.");
						clickHandler(event);
					}
				});

		fillTheScreen(Window.getClientWidth(), Window.getClientHeight());
		RootPanel.get("map_canvas").add(mapWidget);
	}

	private void fillTheScreen(int screenWidth, int screenHeight) {
		mapWidget.setPixelSize(screenWidth - 20, screenHeight - 20);
	}

	private void clickHandler(HasMouseEvent event) {
		
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
			@Override public void callback(HasDirectionsResult response,
					String status) {
				GWT.log("RouteUpdated");
				routeUpdated(response, status);
			}
		});
	}

	private void routeUpdated(HasDirectionsResult response, String status) {
		if (Status.Ok().equals(status)) {
			HasDirectionsRoute newRoute = response.getRoutes().get(0);
			List<HasDirectionsLeg> newLegs = newRoute.getLegs();

			List<HasDirectionsStep> newSteps = new ArrayList<HasDirectionsStep>();
			for(HasDirectionsLeg leg : newLegs){
				distance += leg.getDistance().getValue();
				for(HasDirectionsStep newStep : leg.getSteps()){
					newSteps.add(newStep);
				}
			}
			route.add(newSteps);
			drawRoute(newSteps);

		} else {
			GWT.log("Failed to get route. Status: " + status);
		}
	}

	private void drawRoute(List<HasDirectionsStep> newSteps) {
		List<HasLatLng> path = new ArrayList<HasLatLng>(); 
		for(HasDirectionsStep step : newSteps){
			for(HasLatLng pathPoint: step.getPath()){
				path.add(pathPoint);
			}
		}
		Polyline pl = new Polyline();
		pl.setMap(mapWidget.getMap());
		pl.setPath(path);
		drawnRoute.add(pl);
	}

	private native void removeGwtObjectId(JavaScriptObject jso) /*-{
		delete jso['__gwt_ObjectId'];
	}-*/;

}
