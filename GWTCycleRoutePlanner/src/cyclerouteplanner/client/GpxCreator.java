package cyclerouteplanner.client;

import java.util.List;

import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class GpxCreator
{
	public static String getRouteString(String name, List<HasLatLng> points){
		Document doc = XMLParser.createDocument();
		Element gpx = doc.createElement("gpx");
		doc.appendChild(gpx);
		Element rte = doc.createElement("rte");
		rte.setAttribute("Name", name);
		gpx.appendChild(rte);
		for(HasLatLng p : points){
			Element rtept = doc.createElement("rtept");
			rtept.setAttribute("lat", Double.toString(p.getLatitude()));
			rtept.setAttribute("lon", Double.toString(p.getLongitude()));
			rte.appendChild(rtept);
		}
		return format(doc);
	}
	
	private static void writeTo(Node n, int indent, StringBuilder builder){
		for(int i = 0; i < indent; ++i){
			builder.append(" ");
		}
		if(n.getChildNodes().getLength() == 0){
			builder.append(n.toString()).append("\n");	
		} else {
			builder.append("<").append(n.getNodeName());
			NamedNodeMap attributes = n.getAttributes();
			for(int a = 0; a < attributes.getLength(); ++a){
				Node attr = attributes.item(a);
				builder.append(" ").append(attr.getNodeName())
					.append("=\"").append(attr.getNodeValue())
					.append("\"");
			}
			builder.append(">\n");
			NodeList list = n.getChildNodes();
			for(int i = 0; i < list.getLength(); ++i){
				writeTo(list.item(i), indent + 4, builder);
			}
			for(int i = 0; i < indent; ++i){
				builder.append(" ");
			}	
			builder.append("</").append(n.getNodeName()).append(">\n");
		}
	}
	
	private static String format(Document doc){
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>").append("\n");
		writeTo(doc.getFirstChild(), 0, builder);		
		return builder.toString();
	}
}