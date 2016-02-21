package network;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * This class contains an OSM link with its data.
 * 
 * @author Rasmus Ringdahl
 *
 */
public class OsmLink implements NetworkElement 
{
	private BigInteger id;
	private HashMap<String,String> attributes;
	private OsmNode[] nodes;

	/**
	 * This is the constructor for an OSM link.
	 * 
	 * @param id			- OSM link id.
	 * @param nodes			- Array with the nodes that build up the OSM link.
	 * @param attributes	- HashMap with all the OSM tags of the link.
	 */
	public OsmLink(BigInteger id, OsmNode[] nodes, HashMap<String, String> attributes)
	{
		// Checking for valid id.
		if (id == null)
		{
			throw new IllegalArgumentException("The link id cannot be null");
		}
		
		// Checking for valid array with nodes
		if(nodes == null)
		{
			throw new IllegalArgumentException("The array with OsmNodes cannot be null");
		}
		else if(nodes.length < 2)
		{
			throw new IllegalArgumentException("The array with OsmNodes must have at least 2 nodes.");
		}
		else
		{
			for(int i = 0; i < nodes.length ; i++)
			{
				if(nodes[i] == null)
				{
					throw new IllegalArgumentException("The array with OsmNodes cannot have null elements");
				}
			}
		}
		
		
		this.id = id;
		this.nodes = nodes;
		
		this.attributes = attributes;
		
	}
	
	/**
	 * This method gets the OSM link id.
	 * 
	 * @return The OSM link id.
	 */
	public BigInteger getId() 
	{
		
		return this.id;
	}

	/**
	 * This method gets the LineString geometry of the OSM link.
	 * 
	 * @return geometry (LineString)
	 */
	public Geometry getGeometry() 
	{
		GeometryFactory factory = new GeometryFactory();
		Coordinate[] coordinates = new Coordinate[nodes.length];
		
		for(int i = 0 ; i < this.nodes.length ; i++)
		{
			coordinates[i] = nodes[i].getGeometry().getCoordinate();
		}
		
		
		return factory.createLineString(coordinates);
	}
	

	/**
	 * This method gets an attribute of the OSM link.
	 * The method returns null if the attribute do not exist.
	 * @param key - The attribute key.
	 * @return The attribute value
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
	 * This method gets a keyset with all attribute keys of the OSM link.
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
	 * This method gets an array with all the OSM nodes that build up the 
	 * OSM link.
	 * 
	 * @return Array with OsmNode
	 */
	public OsmNode[] getNodes()
	{
		return this.nodes;
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("OSM Link\n");
		builder.append(String.format("Link id: %s\n", this.id.toString()));
		builder.append("Nodes:\n{");
		for(int i = 0; i < this.nodes.length ; i++)
		{
			builder.append(String.format("\n%s",this.nodes[i].toString()));
		}
		builder.append("\n}\n");
		
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
