package cyclerouteplanner.client;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class UiRenderer {

	private DisclosurePanel uiPanel;
	private TextArea distanceField;
	
	private final static NumberFormat DistanceFormatter = NumberFormat.getFormat("#,##0.##");
	
	public void onModuleLoad(){
		uiPanel = new DisclosurePanel("Controls");
		uiPanel.setPixelSize(200, 400);

		distanceField = new TextArea();
		distanceField.setText("0 m");

		uiPanel.add(distanceField);

		RootPanel uiDiv = RootPanel.get("ui");
		uiDiv.setStyleName("UI");
		uiDiv.add(uiPanel);
	}
	
	public void routeUpdated(double distanceM){
		String t = "Distance: " + DistanceFormatter.format(distanceM / 1000.0) + " km";
		distanceField.setText(t);
	}
	
}
