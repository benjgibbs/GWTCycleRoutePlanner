package cyclerouteplanner.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
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

public class UiRenderer implements RouteUpdatedListener{

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
	
	public void SubscribeToClearEvent(ClearRouteListener listener){
		clearRouteEventor.addListener(listener);
	}
	
	public void SubscribeToRemoveLastPointEvent(RemoveLastPointListener listener){
		removeLastPointEventor.addListener(listener);
	}

	@Override public void onEvent(RouteUpdatedEvent event) {
		routeUpdated(event.getDistance());
	}
}
