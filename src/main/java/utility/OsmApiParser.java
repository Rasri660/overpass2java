/**
 * 
 */
package utility;

import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import network.OsmLink;
import network.OsmNode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;

/**
 * @author Rasmus
 *
 */
public class OsmApiParser 
{	
	public static void parse(JSONObject unparsed,
							 HashMap<BigInteger, OsmNode> nodeMap,
							 HashMap<BigInteger, OsmLink> linkMap,
							 HashSet<OsmNode> intersectionNodes) 
							 throws ParseException, 
							 		InvalidObjectException
	{
		
		String dateOfTakeout = (String) ((JSONObject)unparsed.get("osm3s")).get("timestamp_osm_base");
		System.out.println("Date of creation: " + dateOfTakeout);
		
		JSONArray osmElements = (JSONArray)unparsed.get("elements");
		
		System.out.println("Number of elements: " + osmElements.size());
		
		HashSet<OsmNode> usedNodes = new HashSet<OsmNode>();
		
		ArrayDeque<JSONObject> unparsedOsmLinks = new ArrayDeque<JSONObject>();
		
		for (int i = 0 ; i < osmElements.size() ; i++)
		{
			JSONObject osmElement = (JSONObject)osmElements.get(i);
			
			// Reading type of OSM element.
			String type = (String) osmElement.get("type");
			
			if(type.equalsIgnoreCase("node"))
			{
				paseNode(osmElement, nodeMap);
				
			}
			else if(type.equalsIgnoreCase("way"))
			{
				unparsedOsmLinks.add(osmElement);
			}
			else
			{
				System.out.println("Element " + i + "has unknow type " + type + ".");
			}
		}
		
		while(!unparsedOsmLinks.isEmpty())
		{
			
			parseLink(unparsedOsmLinks.poll(),
					  linkMap, 
					  nodeMap,
					  usedNodes, 
					  intersectionNodes);			
			
		}
		
		System.out.println("Number of loaded nodes: " + nodeMap.size());
		System.out.println("Number of loaded links: " + linkMap.size());
		System.out.println("Number of detected intersections: " 
						  + intersectionNodes.size());
		System.out.println("Done!");
		
	}
	
	
	private static void parseLink(JSONObject osmElement,
								  HashMap<BigInteger, OsmLink> linkMap,
								  HashMap<BigInteger, OsmNode> nodeMap,
								  HashSet<OsmNode> usedNodes,
								  HashSet<OsmNode> intersectionNodes) 
								  throws InvalidObjectException 
	{
		// Reading element id.
		BigInteger id = new BigInteger((osmElement.get("id")).toString());
		
		// Creating a HashMap to store OSM element tags.
		HashMap<String, String> attributes = parseTags((JSONObject) osmElement.get("tags"));
		
		JSONArray nodeIds = (JSONArray) osmElement.get("nodes");
		
		// Loading nodes.
		ArrayList<OsmNode> nodes = new ArrayList<OsmNode>();
		for(int i = 0 ; i < nodeIds.size() ; i++)
		{
			BigInteger nodeId = new BigInteger((nodeIds.get(i)).toString());
			OsmNode node = nodeMap.get(nodeId);
			nodes.add(node);
			if(usedNodes.contains(node))
			{
				intersectionNodes.add(node);
			}
			else
			{
				usedNodes.add(node);
			}
		}
		
		OsmLink link = linkMap.get(id);
		
		if(link == null)
		{
			link = new OsmLink(id, nodes.toArray(new OsmNode[0]), attributes);
			
			linkMap.put(id, link);
		}
		else
		{
			throw new InvalidObjectException("Link " + id + " allready exists.");
		}
		
	}


	private static void paseNode(JSONObject osmElement,
								 HashMap<BigInteger , OsmNode> nodeMap) 
								 throws ParseException 
	{
		// Reading element id.
		BigInteger  id = new BigInteger((osmElement.get("id")).toString());
		
		// Reading coordinates.
		Double lon = (Double) osmElement.get("lon");
		Double lat = (Double) osmElement.get("lat");
		
		// Creating a HashMap to store OSM element tags.
		HashMap<String, String> attributes = parseTags((JSONObject) osmElement.get("tags"));
		
		OsmNode node = nodeMap.get(id);
		
		if(node == null)
		{
			node = new OsmNode(id, lon, lat, attributes);
			
			nodeMap.put(id, node);
		}
		else
		{
			if(lon != node.getGeometry().getCoordinate().x)
			{
				throw new ParseException("Miss match in longitude for node "
										+ id + " (" 
										+ node.getGeometry().getCoordinate().x
										+ " vs " + lon +")",0);
			}
			else if(lat != node.getGeometry().getCoordinate().y)
			{
				throw new ParseException("Miss match in latitude for node "
						+ id + " (" 
						+ node.getGeometry().getCoordinate().y
						+ " vs " + lat +")",0);
			}
			
			for(Entry<String, String> attribute : attributes.entrySet())
			{
				node.setAttributes(attribute.getKey(), 
								   attribute.getValue());
			}
		}		
		
	}


	private static HashMap<String, String> parseTags(JSONObject osmTags) 
	{
		// Creating attribute HashMap.
		HashMap<String, String> attributes = new HashMap<String, String>();
		
		if(osmTags != null)
		{
			@SuppressWarnings("unchecked")
			Set<String> tagKeys = osmTags.keySet();
			 
			// Adding the tags to the attribute HashMap.
			for(String tagKey : tagKeys)
			{
				 String tagValue = (String) osmTags.get(tagKey);
				 
				 attributes.put(tagKey, tagValue);	
			}
		}
		 
		return attributes;
	}


	public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException
	{
		
		
		JSONParser parser = new JSONParser();
		
		
		String jsonFilePath = OsmApiParser.class.getClassLoader().getResource("stockholm.opapi").getFile();
		FileReader jsonfile = new FileReader(jsonFilePath);
		
		Object obj = parser.parse(jsonfile);
		
		HashMap<BigInteger, OsmNode> nodeMap = new HashMap<BigInteger, OsmNode>();
		HashMap<BigInteger, OsmLink> linkMap = new HashMap<BigInteger, OsmLink>();
		HashSet<OsmNode> intersectionNodes = new HashSet<OsmNode>();
		
		parse((JSONObject) obj, nodeMap, linkMap, intersectionNodes);
		
		HashSet<String> attributeList = new HashSet<String>();
		for(OsmLink link : linkMap.values())
		{
			for(String key : link.getAttributeKeys())
			{
				attributeList.add(key);
			}
		}
		
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		
		writer.print("link_id");
		
		for(String key : attributeList)
		{
			writer.print(";"+key);
		}
		
		writer.println(";geom");
		for(OsmLink link : linkMap.values())
		{
			writer.print(link.getId().toString());
			for(String key : attributeList)
			{
				writer.print(";"+link.getAttributes(key));
			}
			writer.println(";" + link.getGeometry().toString());
		}
		
		writer.close();
		
		writer = new PrintWriter("output_nodes.txt", "UTF-8");
		
		attributeList = new HashSet<String>();
		for(OsmNode node : intersectionNodes)
		{
			for(String key : node.getAttributeKeys())
			{
				attributeList.add(key);
			}
		}
		
		writer.print("node_id");
		
		for(String key : attributeList)
		{
			writer.print(";"+key);
		}
		
		writer.println(";geom");
		for(OsmNode node : intersectionNodes)
		{
			writer.print(node.getId().toString());
			for(String key : attributeList)
			{
				writer.print(";"+node.getAttributes(key));
			}
			writer.println(";" + node.getGeometry().toString());
		}
		
		writer.close();
	}
}
