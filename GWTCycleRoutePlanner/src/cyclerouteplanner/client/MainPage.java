package cyclerouteplanner.client;

import com.google.gwt.core.client.EntryPoint;

public class MainPage implements EntryPoint {

	RouteManager routeMan = new RouteManager();
	MapRenderer mapRenderer = new MapRenderer();
	UiRenderer uiRenderer = new UiRenderer();
	
	@Override public void onModuleLoad() {
		routeMan.addRouteUpdatedListener(mapRenderer.getRouteUpdatedListener());
		routeMan.addRouteUpdatedListener(uiRenderer.getRouteUpdatedListener());
		
		uiRenderer.addClearRouteListener(routeMan.getClearRouteListener());
		uiRenderer.addRemoveLastPointListener(routeMan.getRemoveLastPointListener());
		
		uiRenderer.addClearRouteListener(mapRenderer.getClearRouteListener());
		uiRenderer.addRemoveLastPointListener(mapRenderer.getRemoveLastPointListener());
		
		mapRenderer.onModuleLoad(routeMan);
		uiRenderer.onModuleLoad();
	}
}
