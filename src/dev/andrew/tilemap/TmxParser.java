package dev.andrew.tilemap;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dev.andrew.loader.ImageLoader;
import dev.andrew.physics.Polygon;
import dev.andrew.physics.Vec2;

public class TmxParser {
	
	public Map createTilemap(String fileName) {
		Map map = new Map();
		
		Document doc = parse(fileName);
		
		Element e = doc.getDocumentElement();
		
		Node mapNode = doc.getFirstChild();
		NamedNodeMap mapNodeMap = mapNode.getAttributes();
		int mapWidth = Integer.parseInt(mapNodeMap.getNamedItem("width").getNodeValue());
		int mapHeight = Integer.parseInt(mapNodeMap.getNamedItem("height").getNodeValue());
		int tileWidth = 0;
		int tileHeight = 0;
		
		NodeList list = doc.getElementsByTagName("tileset");
		for(int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			
			NamedNodeMap nodeMap = n.getAttributes();
			int firstgid = Integer.parseInt(nodeMap.getNamedItem("firstgid").getNodeValue());
			String tsxSource = nodeMap.getNamedItem("source").getNodeValue();
			
			Document tsxDoc = parse(tsxSource);
			Element tsxElement = tsxDoc.getDocumentElement();
			NamedNodeMap tsxNodeMap = tsxElement.getAttributes();
			String name = tsxNodeMap.getNamedItem("name").getNodeValue();
			tileWidth = Integer.parseInt(tsxNodeMap.getNamedItem("tilewidth").getNodeValue());
			tileHeight = Integer.parseInt(tsxNodeMap.getNamedItem("tileheight").getNodeValue());
			
			Node imageNode = tsxDoc.getElementsByTagName("image").item(0);
			NamedNodeMap imageNodeMap = imageNode.getAttributes();
			String tilesetSource = imageNodeMap.getNamedItem("source").getNodeValue();
			int imageWidth = Integer.parseInt(imageNodeMap.getNamedItem("width").getNodeValue());
			int imageHeight = Integer.parseInt(imageNodeMap.getNamedItem("height").getNodeValue());
		
			map.tilesets.add(new Tileset(firstgid, name, tileWidth, tileHeight, imageWidth, imageHeight, ImageLoader.load("/" + tilesetSource)));
		}
		
		NodeList layerNodeList = doc.getElementsByTagName("layer");
		for(int i = 0; i < layerNodeList.getLength(); i++) {
			Node n = layerNodeList.item(i);
			
			NamedNodeMap layerNodeMap = n.getAttributes();
			int id = Integer.parseInt(layerNodeMap.getNamedItem("id").getNodeValue());
			String name = layerNodeMap.getNamedItem("name").getNodeValue();
			int rows = Integer.parseInt(layerNodeMap.getNamedItem("height").getNodeValue());
			int cols = Integer.parseInt(layerNodeMap.getNamedItem("width").getNodeValue());
			
			String text = n.getTextContent().replaceAll("\n", "");
			text = text.replaceAll(" ", "");
			String[] data = text.split(",");
			
			map.layers.add(new Layer(id, name, rows, cols, data));
		}
		
		NodeList objLayerNodeList = doc.getElementsByTagName("objectgroup");
		for(int i = 0; i < objLayerNodeList.getLength(); i++) {
			Node n = objLayerNodeList.item(i);
			
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap objLayerAttribs = n.getAttributes();
				int objLayerId = Integer.parseInt(objLayerAttribs.getNamedItem("id").getNodeValue());
				String objLayerName = objLayerAttribs.getNamedItem("name").getNodeValue();
				
				ObjectLayer newObjLayer = new ObjectLayer(objLayerId, objLayerName);
				map.objectLayers.add(newObjLayer);
				
				NodeList objNodeList = n.getChildNodes();
				for(int j = 0; j < objNodeList.getLength(); j++) {
					Node objNode = objNodeList.item(j);
					
					if(objNode.getNodeType() == Node.ELEMENT_NODE) {
						if(objLayerName.equals("Colliders")) {
							NodeList nl = objNode.getChildNodes();
							
							NamedNodeMap objAttribs = objNode.getAttributes();
							int objId = Integer.parseInt(objAttribs.getNamedItem("id").getNodeValue());
							int objX = Integer.parseInt(objAttribs.getNamedItem("x").getNodeValue());
							int objY = Integer.parseInt(objAttribs.getNamedItem("y").getNodeValue());
							Vec2[] vertices = null;
							
							for(int k = 0; k < nl.getLength(); k++) {
								Node shapeNode = nl.item(k);
								
								if(shapeNode.getNodeType() == Node.ELEMENT_NODE) {
									NamedNodeMap shapeAttribs = shapeNode.getAttributes();
									String points = shapeAttribs.getNamedItem("points").getNodeValue();
									String[] tokens = points.split(" ");
									vertices = new Vec2[tokens.length];
									
									for(int t = 0; t < tokens.length; t++) {
										String[] coords = tokens[t].split(",");
										vertices[t] = new Vec2(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]));
									}
								}
							}
							
							MapObject mo = new MapObject();
							mo.id = objId;
							mo.polygon = new Polygon(vertices);
							mo.name = "";
							mo.x = objX;
							mo.y = objY;
							mo.type = MapObject.Type.COLLIDER;
							
							newObjLayer.mapObjects.add(mo);
						}else if(objLayerName.equals("Entities")) {
							NodeList nl = objNode.getChildNodes();
							
							NamedNodeMap objAttribs = objNode.getAttributes();
							int objId = Integer.parseInt(objAttribs.getNamedItem("id").getNodeValue());
							String objName = objAttribs.getNamedItem("name").getNodeValue();
							int objX = Integer.parseInt(objAttribs.getNamedItem("x").getNodeValue());
							int objY = Integer.parseInt(objAttribs.getNamedItem("y").getNodeValue());
							
							MapObject mo = new MapObject();
							mo.id = objId;
							mo.name = objName;
							mo.x = objX;
							mo.y = objY;
							mo.type = MapObject.Type.ENTITY;
							
							newObjLayer.mapObjects.add(mo);
						}
					}
				}
			}
		}
		
		map.worldWidth = mapWidth * tileWidth;
		map.worldHeight = mapHeight * tileHeight;
		
		return map;
	}
	
	public Document parse(String fileName) {
		try {
			InputStream in = TmxParser.class.getResourceAsStream(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(in);
			doc.getDocumentElement().normalize();
			
			in.close();
			
			return doc;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
