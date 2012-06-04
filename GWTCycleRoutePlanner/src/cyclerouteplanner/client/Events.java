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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.maps.gwt.client.DirectionsStep;
import com.google.maps.gwt.client.LatLng;


public class Events {
	public interface EventListener<EventT> {
		public void onEvent(EventT event);
	}
	
	public final static class RouteUpdatedEvent{
		private final LinkedList<List<DirectionsStep>> routeInStages;
		private final double distance;
		private final LatLng start;
		public RouteUpdatedEvent(LatLng start, LinkedList<List<DirectionsStep>> routeInStages, double distance){
			this.distance = distance;
			this.routeInStages = routeInStages;
			this.start = start;
		}
		public LinkedList<List<DirectionsStep>> getRouteInStages() {
			return routeInStages;
		}
		public double getDistance() {
			return distance;
		}
		public LatLng getStart() {
			return start;
		}
	}

	public interface RouteUpdatedListener extends EventListener<RouteUpdatedEvent>{ }
	
	public static class RemoveLastPointEvent{ }
	public interface RemoveLastPointListener extends EventListener<RemoveLastPointEvent> {
		public void onEvent(RemoveLastPointEvent event);
	}

	public static class ClearRouteEvent{ }
	public interface ClearRouteListener extends EventListener<ClearRouteEvent> { 
		public void onEvent(ClearRouteEvent event);
	}
	
	public static class EventGenerator<EventT>{
		private final List<EventListener<EventT>> listeners = new ArrayList<EventListener<EventT>>();
		public void addListener(EventListener<EventT> listener){
			listeners.add(listener);
		}
		public void removeListener(EventListener<EventT> listener){
			listeners.remove(listener);
		}
		public void onEvent(EventT event){
			for(EventListener<EventT> listener : listeners){
				listener.onEvent(event);
			}
		}
	}
	
}
