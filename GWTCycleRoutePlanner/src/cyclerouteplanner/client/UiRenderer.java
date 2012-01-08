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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import cyclerouteplanner.client.Events.ClearRouteEvent;
import cyclerouteplanner.client.Events.ClearRouteListener;
import cyclerouteplanner.client.Events.EventGenerator;
import cyclerouteplanner.client.Events.RemoveLastPointEvent;
import cyclerouteplanner.client.Events.RemoveLastPointListener;
import cyclerouteplanner.client.Events.RouteUpdatedEvent;
import cyclerouteplanner.client.Events.RouteUpdatedListener;

public class UiRenderer {

	private DisclosurePanel uiPanel;
	private VerticalPanel vPanel;
	private Label distanceField;
	private Button undoLastPoint;
	private Button clearRoute;
	
	private final static NumberFormat DistanceFormatter = NumberFormat.getFormat("#,##0.##");
	
	private EventGenerator<ClearRouteEvent> clearRouteEventor = new EventGenerator<ClearRouteEvent>();
	private EventGenerator<RemoveLastPointEvent> removeLastPointEventor = new EventGenerator<RemoveLastPointEvent>();
	
	public void onModuleLoad(){
		
		vPanel = new VerticalPanel();
		vPanel.setStyleName("Panel");
		
		distanceField = new Label();
		distanceField.setText("0 m");
		distanceField.addStyleName("PanelText");
		vPanel.add(distanceField);

		undoLastPoint = new Button("Undo Last Point");
		undoLastPoint.addStyleName("PanelButton");
		undoLastPoint.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				removeLastPointEventor.onEvent(new RemoveLastPointEvent());
			}
		});
		vPanel.add(undoLastPoint);
		
		clearRoute = new Button("Clear Route");
		clearRoute.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				clearRouteEventor.onEvent(new ClearRouteEvent());
			}
		});
		clearRoute.addStyleName("PanelButton");
		vPanel.add(clearRoute);
		
		uiPanel = new DisclosurePanel("Controls");
		uiPanel.add(vPanel);
		uiPanel.setOpen(true);
		
		RootPanel uiDiv = RootPanel.get("ui");
		uiDiv.setStyleName("UI");
		uiDiv.add(uiPanel);
		
	}
	
	private void routeUpdated(double distanceM){
		String t = "Distance: " + DistanceFormatter.format(distanceM / 1000.0) + " km";
		distanceField.setText(t);
	}
	
	public void addClearRouteListener(ClearRouteListener listener){
		clearRouteEventor.addListener(listener);
	}
	
	public void addRemoveLastPointListener(RemoveLastPointListener listener){
		removeLastPointEventor.addListener(listener);
	}

	public RouteUpdatedListener getRouteUpdatedListener() {
		return new RouteUpdatedListener() {
			@Override public void onEvent(RouteUpdatedEvent event) {
				routeUpdated(event.getDistance());
			}
		};
	}
}
