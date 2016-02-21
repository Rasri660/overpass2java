package network;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * This class contains an OSM node with its data.
 * 
 * @author Rasmus Ringdahl
 *
 */
public class OsmNode implements NetworkElement 
{
	private final BigInteger  id;
	private final HashMap<String,String> attributes;
	private final Point coordinate;

	/**
	 * This is the constructor for an OSM node.
	 * 
	 * @param id			- OSM node id.
	 * @param lon			- Longitude of the node.
	 * @param lat			- Latitude of the node.
	 * @param attributes	- HashMap with all the OSM tags of the node.
	 */
	public OsmNode(BigInteger id, Double lon, Double lat, HashMap<String, String> attributes)
	{
		// Checking for valid id.
		if (id == null)
		{
			throw new IllegalArgumentException("The link id cannot be null");
		}
		
		// Checking for valid longitude.
		if (lon == null)
		{
			throw new IllegalArgumentException("The longitude cannot be null");
		}
		
		// Checking for valid latitude.
		if (lat == null)
		{
			throw new IllegalArgumentException("The latitude cannot be null");
		}
				
		this.id = id;
		this.attributes = attributes;
		
		GeometryFactory factory = new GeometryFactory();
		
		this.coordinate = factory.createPoint(new Coordinate(lon, lat));
		
	}

	/**
	 * This method gets the OSM node id.
	 * 
	 * @return The OSM node id.
	 */
	public BigInteger getId() 
	{
		
		return this.id;
	}

	/**
	 * This method gets the Point geometry of the OSM node.
	 * 
	 * @return geometry (Point)
	 */
	public Geometry getGeometry() 
	{
		
		return this.coordinate;
	}


	/**
	 * This method gets an attribute of the OSM node.
	 * The method returns null if the attribute do not exist.
	 * @param key - The attribute key.
	 * @return The attribute value.
	 */
	public String getAttributes(String key) 
	{
		// Returns null if the attribute object is null.
		if(this.attributes == null)
		{
			return null;
		}
		// Returns the value from the attribute.
		else
		{
			return this.attributes.get(key);
		}
	}
	
	/**
	 * This method gets a keyset with all attribute keys of the OSM Node.
	 * The method returns null if the attribute keyset does not exist.
	 * @param key - The attribute key.
	 * @return The key set
	 */
	public Set<String> getAttributeKeys()
	{
		// Returns null if the attribute object is null.
		if(this.attributes == null)
		{
			return null;
		}
		// Returns the value from the attribute.
		else
		{
			return this.attributes.keySet();
		}
	}
	
	/**
	 * This method sets the attribute of the OSM node.
	 * @param key 	- The attribute key.
	 * @param value	- The attribute value.
	 */
	public void setAttributes(String key, String value)
	{
		this.attributes.put(key, value);
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("OSM Node\n");
		builder.append(String.format("Node id: %s\n", this.id.toString()));
		builder.append(String.format("Longitude: %.6f\nLatitude: %.6f\n",
									  this.coordinate.getCoordinate().x,
									  this.coordinate.getCoordinate().y));
		if(this.attributes == null)
		{
			builder.append("No attributes.");
		}
		else
		{
			builder.append("Attributes:");
			
			for(Entry<String, String> entry : this.attributes.entrySet())
			{
				builder.append(String.format("\n %s: %s", 
							   entry.getKey(),
							   entry.getValue()));
			}
		}	
		
		return builder.toString();
	}
}
