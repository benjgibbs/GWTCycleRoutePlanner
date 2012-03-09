package cyclerouteplanner.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GpxWindow extends PopupPanel {
	public GpxWindow(String gpxRoute){
		
		VerticalPanel vp = new VerticalPanel();
		
		TextArea ta = new TextArea();
		ta.setWidth("400px");
		ta.setHeight("200px");
		ta.setText(gpxRoute);
		vp.add(ta);
		
		Button close = new Button("Close");
		close.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				GpxWindow.this.hide();
			}
		});
		vp.add(close);
		
		setWidget(vp);
		setTitle("Gpx Points");
	}
}
