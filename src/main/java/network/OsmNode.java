package network;

import java.util.HashMap;

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
	private final Integer id;
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
	public OsmNode(Integer id, Double lon, Double lat, HashMap<String, String> attributes)
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
	public Integer getId() 
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

}
