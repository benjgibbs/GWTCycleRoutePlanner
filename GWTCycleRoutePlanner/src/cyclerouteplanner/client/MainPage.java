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
