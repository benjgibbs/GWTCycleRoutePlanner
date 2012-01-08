package cyclerouteplanner.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.directions.HasDirectionsStep;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.PolylineOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import cyclerouteplanner.client.Events.ClearRouteEvent;
import cyclerouteplanner.client.Events.ClearRouteListener;
import cyclerouteplanner.client.Events.RemoveLastPointEvent;
import cyclerouteplanner.client.Events.RemoveLastPointListener;
import cyclerouteplanner.client.Events.RouteUpdatedEvent;
import cyclerouteplanner.client.Events.RouteUpdatedListener;

public class MapRenderer {
	
	private MapWidget mapWidget;
	private List<Polyline> drawnRoute = new ArrayList<Polyline>();
	
	public void onModuleLoad(MouseEventCallback clickHandler){
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
		Event.addListener(mapWidget.getMap(), "click",clickHandler);

		fillTheScreen(Window.getClientWidth(), Window.getClientHeight());
		RootPanel.get("map_canvas").add(mapWidget);
	}
	
	private void fillTheScreen(int screenWidth, int screenHeight) {
		mapWidget.setPixelSize(screenWidth - 20, screenHeight - 20);
	}
	
	private void drawRoutePart(List<HasDirectionsStep> newSteps) {
		List<HasLatLng> path = new ArrayList<HasLatLng>();
		for (HasDirectionsStep step : newSteps) {
			for (HasLatLng pathPoint : step.getPath()) {
				path.add(pathPoint);
			}
		}
		Polyline pl = new Polyline();
		PolylineOptions options = new PolylineOptions();
		options.setStrokeColor("red");
		options.setStrokeWeight(10);
		options.setStrokeOpacity(0.6);
		options.setPath(path);
		pl.setOptions(options);

		pl.setMap(mapWidget.getMap());

		drawnRoute.add(pl);
	}

	public RouteUpdatedListener getRouteUpdatedListener() {
		return new RouteUpdatedListener() {
			@Override public void onEvent(RouteUpdatedEvent event) {
				List<List<HasDirectionsStep>> routeInStages = event.getRouteInStages();
				drawRoutePart(routeInStages.get(routeInStages.size() -1));
			}
		};
	}

	ClearRouteListener getClearRouteListener(){
		return new ClearRouteListener() {
			@Override public void onEvent(ClearRouteEvent event) {
				
			}
		};
	}
	
	RemoveLastPointListener getRemoveLastPointListener(){
		return new RemoveLastPointListener() {
			@Override public void onEvent(RemoveLastPointEvent event) {
				
			}
		};
	}
	
	
}
