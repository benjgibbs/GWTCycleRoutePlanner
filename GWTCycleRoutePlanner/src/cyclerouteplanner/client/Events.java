package cyclerouteplanner.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.directions.HasDirectionsStep;

public class Events {
	public interface EventListener<EventT> {
		public void onEvent(EventT event);
	}
	
	public final static class RouteUpdatedEvent{
		private final List<List<HasDirectionsStep>> routeInStages;
		private final double distance;
		public RouteUpdatedEvent(List<List<HasDirectionsStep>> routeInStages, double distance){
			this.distance = distance;
			this.routeInStages = routeInStages;
		}
		public List<List<HasDirectionsStep>> getRouteInStages() {
			return routeInStages;
		}
		public double getDistance() {
			return distance;
		}
	}

	public interface RouteUpdatedListener extends EventListener<RouteUpdatedEvent>{ }
	
	public static class RemoveLastPointEvent{ }
	public interface RemoveLastPointListener extends EventListener<RemoveLastPointEvent>{ }

	public static class ClearRouteEvent{ }
	public interface ClearRouteListener extends EventListener<ClearRouteEvent>{ }
	
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