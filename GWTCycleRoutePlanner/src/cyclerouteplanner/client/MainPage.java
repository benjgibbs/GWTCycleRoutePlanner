package cyclerouteplanner.client;

import com.google.gwt.core.client.EntryPoint;

public class MainPage implements EntryPoint {

	RouteManager routeMan = new RouteManager();
	MapRenderer mapRenderer = new MapRenderer();
	UiRenderer uiRenderer = new UiRenderer();
	
	@Override public void onModuleLoad() {
		routeMan.addRouteUpdatedListener(mapRenderer.getRouteUpdatedListener());
		routeMan.addRouteUpdatedListener(uiRenderer.getRouteUpdatedListener());
		
		uiRenderer.SubscribeToClearEvent(routeMan.getClearRouteListener());
		uiRenderer.SubscribeToRemoveLastPointEvent(routeMan.getRemoveLastPointListener());
		
		uiRenderer.SubscribeToClearEvent(mapRenderer.getClearRouteListener());
		uiRenderer.SubscribeToRemoveLastPointEvent(mapRenderer.getRemoveLastPointListener());
		
		mapRenderer.onModuleLoad(routeMan);
		uiRenderer.onModuleLoad();
	}
}
