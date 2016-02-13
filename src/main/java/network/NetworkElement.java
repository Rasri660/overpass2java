package network;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This interface is used for all classes in the network.
 * 
 * @author Rasmus Ringdahl
 *
 */
public interface NetworkElement 
{

	/**
	 * This method gets the id of the network element.
	 * 
	 * @return id of the element.
	 */
	public Integer getId();
	
	/**
	 * This method gets the geometry of the network element.
	 * It can be a Point or a LineString.
	 * 
	 * @return geometry of the element.
	 */
	public Geometry getGeometry();
	
}
