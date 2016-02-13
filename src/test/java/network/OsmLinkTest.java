package network;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Rasmus
 *
 */
public class OsmLinkTest 
{
	// Test object.
	private OsmLink link;
	
	// Variables for the link.
	private final int linkId = 1;
	private final String key = "highway";
	private final String value = "motorway";
	
	// Variables for the nodes.
	private OsmNode[] nodes;
	private final int[] nodeIds = {1,2};
	private final double[] nodeLats = {58.546269, 58.544477};
	private final double[] nodeLons = {15.042370, 15.044800};

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		// Creating two OSM nodes.
		OsmNode[] nodes = {new OsmNode(this.nodeIds[0], 
									   this.nodeLons[0], 
									   this.nodeLats[0], 
									   null),
						   new OsmNode(this.nodeIds[1], 
								   	   this.nodeLons[1], 
								   	   this.nodeLats[1], 
								   	   null)};
		this.nodes = nodes;
		// Creating attribute map.
		HashMap<String,String> attributes = new HashMap<String, String>();
		attributes.put(this.key, this.value);
		
		// Creating test object.
		this.link = new OsmLink(this.linkId, this.nodes, attributes);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		// Removing test object.
		this.link = null;
		
		// Removing nodes.
		this.nodes = null;
	}

	/**
	 * Test method for {@link network.OsmLink#OsmLink(java.lang.Integer, network.OsmNode[], java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkNoId() 
	{
		this.link = new OsmLink(null, this.nodes, null);
	}
	
	/**
	 * Test method for {@link network.OsmLink#OsmLink(java.lang.Integer, network.OsmNode[], java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkNoNodes() 
	{
		this.link = new OsmLink(this.linkId, null, null);
	}
	
	/**
	 * Test method for {@link network.OsmLink#OsmLink(java.lang.Integer, network.OsmNode[], java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkTooFewNodes() 
	{
		OsmNode[] nodes = {this.nodes[0]}; 
		this.link = new OsmLink(this.linkId, nodes, null);
	}
	
	/**
	 * Test method for {@link network.OsmLink#OsmLink(java.lang.Integer, network.OsmNode[], java.util.HashMap)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testOsmLinkNullNodes() 
	{
		OsmNode[] nodes = {this.nodes[0],null}; 
		this.link = new OsmLink(this.linkId, nodes, null);
	}

	/**
	 * Test method for {@link network.OsmLink#getId()}.
	 */
	@Test
	public final void testGetId() 
	{
		// Asserting id.
		assertEquals("Testing getId() ",
					  this.linkId, 
					  this.link.getId().intValue());
	}

	/**
	 * Test method for {@link network.OsmLink#getGeometry()}.
	 */
	@Test
	public final void testGetGeometry() 
	{
		Coordinate[] coordinates = this.link.getGeometry().getCoordinates();
		
		// Assert number of coordinates.
		assertEquals("Testing getGeometry() (number of coordinates) ", 
					 this.nodeIds.length,
					 coordinates.length);
		
		for(int i = 0; i < coordinates.length ; i++)
		{
			// Asserting longitude.
			assertEquals("Testing getGeometry() (lon) ",
						  this.nodeLons[i], 
						  coordinates[i].x, 
						  0.001);
			
			// Asserting latitude.
			assertEquals("Testing getGeometry() (lat) ",
						  this.nodeLats[i], 
						  coordinates[i].y, 
						  0.001);
		}
		
	}

	/**
	 * Test method for {@link network.OsmLink#getAttributes(java.lang.String)}.
	 */
	@Test
	public final void testGetAttributes() 
	{
		// Asserting working key/value pair.
		assertEquals("Testing getAttributes() (highway)  ",
					  this.value, 
					  this.link.getAttributes(this.key));
				
		// Asserting empty key/value pair.
		assertNull("Testing getAttributes() (Null key)  ",
		 		    this.link.getAttributes("Not existing key"));
		
		this.link = new OsmLink(this.linkId, this.nodes, null);
		
		// Asserting null attribute.
		assertNull("Testing getAttributes() (Null attribute object)  ",
	 		    this.link.getAttributes("Not existing key"));
		
	}

	/**
	 * Test method for {@link network.OsmLink#getNodes()}.
	 */
	@Test
	public final void testGetNodes() 
	{
		// Assert number of coordinates.
		assertEquals("Testing getGeometry() (number of coordinates) ", 
					  this.nodeIds.length,
					  link.getNodes().length);
		
		// Assert nodes.
		for(int i = 0; i < link.getNodes().length ; i++)
		{
			assertEquals("Testing getNodes() (object) ", 
					      this.nodes[i],
					      this.link.getNodes()[i]);
		}
		
	
	}
}
