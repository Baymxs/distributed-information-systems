package ru.nsu.bayramov.openstreetmapxmlparserwithmarshalling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshalling.generated.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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

    public static OsmProcessingResult execute(InputStream inputStream) {
        logger.info("OSM processing: started");
        XMLInputFactory input = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = null;
        OsmProcessingResult osmProcessingResult = new OsmProcessingResult();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            xmlEventReader = input.createXMLEventReader(inputStream);
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.peek();
                if (xmlEvent.getEventType() == START_ELEMENT) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals(NODE_TAG_NAME)) {
                        processNode(xmlEventReader, jaxbContext, osmProcessingResult);
                        continue;
                    }
                }
                xmlEventReader.nextEvent();
            }
        } catch (XMLStreamException e) {
            logger.error("XML processing error", e);
        } catch (JAXBException e) {
            logger.error("JAXB exception", e);
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

    private static void processNode(XMLEventReader xmlEventReader, JAXBContext jaxbContext, OsmProcessingResult osmProcessingResult) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Node node = (Node) unmarshaller.unmarshal(xmlEventReader);
        osmProcessingResult.addEditedNodeForUser(node.getUser());
        node.getTag().parallelStream().forEach(tag -> osmProcessingResult.addContainingNodeForKey(tag.getK()));
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
