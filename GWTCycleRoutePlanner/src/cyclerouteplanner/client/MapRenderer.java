// Cycle Root Planner
// Copyright (C) 2012  Benjamin Gibbs
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>

package cyclerouteplanner.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.DirectionsStep;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ClickHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MVCArray;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;

import cyclerouteplanner.client.Events.ClearRouteEvent;
import cyclerouteplanner.client.Events.ClearRouteListener;
import cyclerouteplanner.client.Events.RemoveLastPointEvent;
import cyclerouteplanner.client.Events.RemoveLastPointListener;
import cyclerouteplanner.client.Events.RouteUpdatedEvent;
import cyclerouteplanner.client.Events.RouteUpdatedListener;

public class MapRenderer {

	private GoogleMap mapWidget;
	private LinkedList<Polyline> drawnRoute = new LinkedList<Polyline>();
	private Marker startMarker;
	private RootPanel mapCanvas;

	public void onModuleLoad(ClickHandler clickHandler) {
		LatLng home = LatLng.create(51.0747504771771, -1.3252487182617188);
		
		final MapOptions options = MapOptions.create();
		options.setZoom(14);
		options.setCenter(home);
		options.setMapTypeId(MapTypeId.ROADMAP);
		options.setDraggable(true);
		//options.setNavigationControl(true);
		options.setMapTypeControl(true);
		
		
		mapCanvas = RootPanel.get("map_canvas");
		mapCanvas.addStyleName("Map");
		
		mapWidget = GoogleMap.create(mapCanvas.getElement(),options);

		Window.addResizeHandler(new ResizeHandler() {
			@Override public void onResize(ResizeEvent event) {
				fillTheScreen(event.getWidth(), event.getHeight());
			}
		});

		GWT.log("Adding click handler.");
		mapWidget.addClickListener(clickHandler);
		fillTheScreen(Window.getClientWidth(), Window.getClientHeight());
	}

	private void fillTheScreen(int screenWidth, int screenHeight) {
		mapCanvas.setPixelSize(screenWidth, screenHeight);
	}

	private void drawRoutePart(List<DirectionsStep> newSteps) {
		MVCArray<LatLng> path = MVCArray.create();
		for (DirectionsStep step : newSteps) {
			JsArray<LatLng> path2 = step.getPath(); 
			for (int i = 0; i < path2.length(); i++) {
				path.push(path2.get(i));
			}
		}
		
		PolylineOptions options = PolylineOptions.create();
		options.setStrokeColor("red");
		options.setStrokeWeight(10);
		options.setStrokeOpacity(0.6);
		options.setPath(path);
		Polyline pl = Polyline.create(options);
		pl.setMap(mapWidget);
		drawnRoute.addLast(pl);
	}

	public RouteUpdatedListener getRouteUpdatedListener() {
		return new RouteUpdatedListener() {
			@Override public void onEvent(RouteUpdatedEvent event) {
				setStartMarker(event.getStart());
				LinkedList<List<DirectionsStep>> routeInStages = event.getRouteInStages();
				if (updateIsAnAddition(routeInStages))
					drawRoutePart(routeInStages.getLast());
			}
		};
	}

	private void setStartMarker(LatLng startPos) {
		if (startMarker == null) {
			if (startPos != null) {
				MarkerOptions options = MarkerOptions.create();
				options.setPosition(startPos);
				options.setTitle("Start");
				options.setMap(mapWidget);
				options.setVisible(true);
				startMarker = Marker.create(options);
			}
		} else {
			if (startPos == null) {
				startMarker.setMap((GoogleMap)null);
				startMarker = null;
			}
		}
	}

	private boolean updateIsAnAddition(LinkedList<List<DirectionsStep>> routeInStages) {
		return routeInStages.size() > drawnRoute.size();
	}

	ClearRouteListener getClearRouteListener() {
		return new ClearRouteListener() {
			@Override public void onEvent(ClearRouteEvent event) {
				while (!drawnRoute.isEmpty()) {
					drawnRoute.removeLast().setMap(null);
				}
				startMarker.setMap((GoogleMap)null);
			}
		};
	}

	RemoveLastPointListener getRemoveLastPointListener() {
		return new RemoveLastPointListener() {
			@Override public void onEvent(RemoveLastPointEvent event) {
				if (drawnRoute.size() > 0) {
					Polyline lastLine = drawnRoute.removeLast();
					lastLine.setMap(null);
				} else {
				}
			}
		};
	}
}
