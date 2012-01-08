package cyclerouteplanner.client;

import com.google.gwt.core.client.EntryPoint;

public class MainPage implements EntryPoint {

	RouteManager routeMan = new RouteManager();
	MapRenderer mapRenderer = new MapRenderer();
	UiRenderer uiRenderer = new UiRenderer();
	
	@Override public void onModuleLoad() {
		routeMan.addRouteUpdatedListener(mapRenderer);
		routeMan.addRouteUpdatedListener(uiRenderer);
		mapRenderer.onModuleLoad(routeMan);
		uiRenderer.onModuleLoad();
	}
}
