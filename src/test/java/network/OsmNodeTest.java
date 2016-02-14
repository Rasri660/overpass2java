package network;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for OsmNode.
 * 
 * @author Rasmus Ringdahl
 *
 */
public class OsmNodeTest 
{
	// Test object.
	private OsmNode node;
	
	// Variables for the node.
	private final BigInteger id = new BigInteger("1");
	private final double lon = 14.775023;
	private final double lat = 58.295001;
	private final String key = "highway";
	private final String value = "traffic_signals";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		
		// Creating attribute map.
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put(this.key, this.value);
		
		// Creating test object.
		this.node = new OsmNode(this.id, this.lon, this.lat, attributes);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		// Removing test object.
		this.node = null;
	}
	
	/**
	 * Test method for {@link network.OsmNode#OsmLink()(java.lang.Integer, java.lang.Double,java.lang.Double, java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkNoId() 
	{
		this.node = new OsmNode(null, this.lon, this.lat, null);
	}
	
	/**
	 * Test method for {@link network.OsmNode#OsmLink()(java.lang.Integer, java.lang.Double,java.lang.Double, java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkNoLon() 
	{
		this.node = new OsmNode(this.id, null, this.lat, null);
	}
	
	/**
	 * Test method for {@link network.OsmNode#OsmLink()(java.lang.Integer, java.lang.Double,java.lang.Double, java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkNoLat() 
	{
		this.node = new OsmNode(this.id, this.lon, null, null);
	}

	/**
	 * Test method for {@link network.OsmNode#getId()}.
	 */
	@Test
	public final void testGetId() 
	{
		// Asserting id.
		assertEquals("Testing getId() ",
					  this.id, 
					  this.node.getId());
	}

	/**
	 * Test method for {@link network.OsmNode#getGeometry()}.
	 */
	@Test
	public final void testGetGeometry() 
	{
		// Asserting longitude.
		assertEquals("Testing getGeometry() (lon) ",
					  this.lon, 
					  this.node.getGeometry().getCoordinate().x, 
					  0.001);
		
		// Asserting latitude.
		assertEquals("Testing getGeometry() (lat) ",
					  this.lat, 
					  this.node.getGeometry().getCoordinate().y, 
					  0.001);
	}

	/**
	 * Test method for {@link network.OsmNode#getAttributes(java.lang.String)}.
	 */
	@Test
	public final void testGetAttributes() 
	{
		// Asserting working key/value pair.
		assertEquals("Testing getAttributes() (highway)  ",
					  this.value, 
					  this.node.getAttributes(this.key));
		
		// Asserting empty key/value pair.
		assertNull("Testing getAttributes() (Null key)  ",
				  this.node.getAttributes("Not existing key"));
		
		// Creating test object.
		this.node = new OsmNode(this.id, this.lon, this.lat, null);
		
		// Asserting empty key/value pair.
		assertNull("Testing getAttributes() (Null attribute object)  ",
				    this.node.getAttributes("Not existing key"));
	}

}
