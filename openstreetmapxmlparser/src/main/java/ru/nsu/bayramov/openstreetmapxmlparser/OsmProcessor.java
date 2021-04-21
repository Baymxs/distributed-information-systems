package ru.nsu.bayramov.openstreetmapxmlparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * @author Bayramov Nizhad
 */
public class OsmProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OsmProcessor.class);
    private static final String NODE_TAG_NAME = "node";
    private static final String TAG_TAG_NAME = "tag";
    private static final QName USER_ATTR_NAME = new QName("user");
    private static final QName KEY_ATTR_NAME = new QName("k");

    public static OsmProcessingResult execute(InputStream inputStream) {
        logger.info("OSM processing: started");
        XMLInputFactory input = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = null;
        OsmProcessingResult osmProcessingResult = new OsmProcessingResult();
        try {
            xmlEventReader = input.createXMLEventReader(inputStream);
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.getEventType() == START_ELEMENT) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals(NODE_TAG_NAME)) {
                        Attribute userAttribute = startElement.getAttributeByName(USER_ATTR_NAME);
                        osmProcessingResult.addEditedNodeForUser(userAttribute.getValue());
                        processNodeKeys(xmlEventReader, osmProcessingResult);
                    }
                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML processing error", e);
        } finally {
            try {
                xmlEventReader.close();
            } catch (XMLStreamException e) {
                logger.error("XML processing error", e);
            }
        }
        logger.info("OSM processing: complete");
        return osmProcessingResult;
    }

    private static void processNodeKeys(XMLEventReader xmlEventReader, OsmProcessingResult osmProcessingResult) throws XMLStreamException {
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if (xmlEvent.getEventType() == END_ELEMENT) {
                EndElement endElement = xmlEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals(NODE_TAG_NAME)) {
                    return;
                }
            }
            if (xmlEvent.getEventType() == START_ELEMENT) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals(TAG_TAG_NAME)) {
                    Attribute keyAttribute = startElement.getAttributeByName(KEY_ATTR_NAME);
                    osmProcessingResult.addContainingNodeForKey(keyAttribute.getValue());
                }
            }
        }
    }

    public static class OsmProcessingResult {
        private final Map<String, Integer> userToEditedNodesCount = new HashMap<>();
        private final Map<String, Integer> keyToMarkedNodesCount = new HashMap<>();

        public void addEditedNodeForUser(String user) {
            userToEditedNodesCount.put(user, userToEditedNodesCount.getOrDefault(user, 0) + 1);
        }

        public void addContainingNodeForKey(String key) {
            keyToMarkedNodesCount.put(key, keyToMarkedNodesCount.getOrDefault(key, 0) + 1);
        }

        public Map<String, Integer> getUserToEditedNodesCount() {
            return userToEditedNodesCount;
        }

        public Map<String, Integer> getKeyToMarkedNodesCount() {
            return keyToMarkedNodesCount;
        }
    }
}
