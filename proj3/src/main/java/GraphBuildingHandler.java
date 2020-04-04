import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 * pathfinding, under some constraints.
 * See OSM documentation on
 * <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 * <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 * <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 * and the java
 * <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 * <p>
 * You may find the CSCourseGraphDB and CSCourseGraphDBHandler examples useful.
 * <p>
 * The idea here is that some external library is going to walk through the XML
 * file, and your override method tells Java what to do every time it gets to the next
 * element in the file. This is a very common but strange-when-you-first-see it pattern.
 * It is similar to the Visitor pattern we discussed for graphs.
 *
 * @author Alan Yao, Maurice Lee
 */
public class GraphBuildingHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(
            Arrays.asList("motorway", "trunk", "primary", "secondary", "tertiary",
                    "unclassified", "residential", "living_street", "motorway_link",
                    "trunk_link", "primary_link", "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;
    private GraphDB.Node curNode;
    private GraphDB.Way curWay;
    private List<Long> nodesInCurWay = new ArrayList<>();

    /**
     * Create a new GraphBuildingHandler.
     *
     * @param g The graph to populate with the XML data.
     */
    public GraphBuildingHandler(GraphDB g) {
        this.g = g;
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */
        if (qName.equals("node")) {
            /* We encountered a new <node...> tag. */
            activeState = "node";
            long id = Long.parseLong(attributes.getValue("id"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            double lat = Double.parseDouble(attributes.getValue("lat"));
            GraphDB.Node n = new GraphDB.Node(id, lon, lat);
            g.addNode(n);
            curNode = n;
        } else if (qName.equals("way")) {
            /* We encountered a new <way...> tag. */
            activeState = "way";
            long id = Long.parseLong(attributes.getValue("id"));
            GraphDB.Way w = new GraphDB.Way(id);
            g.addWay(w);
            curWay = w;
        } else if (activeState.equals("way") && qName.equals("nd")) {
            /* While looking at a way, we found a <nd...> tag. */
            long nodeId = Long.parseLong(attributes.getValue("ref"));
            nodesInCurWay.add(nodeId);
            if (!g.getNode(nodeId).wayIds.contains(curWay.id)) {
                g.getNode(nodeId).wayIds.add(curWay.id);
            }
        } else if (activeState.equals("way") && qName.equals("tag")) {
            /* While looking at a way, we found a <tag...> tag. */
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("maxspeed")) {
                curWay.maxSpeed = v;
            } else if (k.equals("highway")) {
                curWay.highway = v;
                if (ALLOWED_HIGHWAY_TYPES.contains(v)) {
                    for (int i = 0, j = 1; j < nodesInCurWay.size(); i++, j++) {
                        Long lastNode = nodesInCurWay.get(i);
                        Long node = nodesInCurWay.get(j);
                        g.addAdj(lastNode, node);
                        g.addAdj(node, lastNode);
                    }
                    curWay.locations = nodesInCurWay;
                }
            } else if (k.equals("name")) {
                curWay.name = v;
            }
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue(
                "k").equals("name")) {
            /* While looking at a node, we found a <tag...> with k="name". */
            String name = attributes.getValue("v");
            String cleanName = GraphDB.cleanString(name);
            g.addCleanNameToTrie(cleanName, name);
            GraphDB.NameNode nameNode = new GraphDB.NameNode(curNode.id,
                                        curNode.lon, curNode.lat, name);
            g.addNameNode(nameNode);
            g.addLocation(name, curNode.id);
            g.addLocation(cleanName, curNode.id);
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            /* We are done looking at a way. (We finished looking at the nodes, speeds, etc...)*/
            nodesInCurWay = new ArrayList<>();
        }
    }
}
