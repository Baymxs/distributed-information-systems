package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.generated.Node;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao.DaoProvider;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.NodeEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.sql.SQLException;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * @author Bayramov Nizhad
 */
public class OsmProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OsmProcessor.class);
    private static final String NODE_TAG_NAME = "node";

    public static void execute(InputStream inputStream) {
        logger.info("OSM processing: started");
        XMLInputFactory input = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            xmlEventReader = input.createXMLEventReader(inputStream);
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.peek();
                if (xmlEvent.getEventType() == START_ELEMENT) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals(NODE_TAG_NAME)) {
                        processNode(xmlEventReader, jaxbContext);
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
    }

    private static void processNode(XMLEventReader xmlEventReader, JAXBContext jaxbContext) throws XMLStreamException, JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Node node = (Node) unmarshaller.unmarshal(xmlEventReader);
        try {
            DaoProvider.getInstance().getNodeDao().putNode(NodeEntity.createFromXml(node));
        } catch (SQLException e) {
            logger.error("SQL error", e);
            throw new RuntimeException(e);
        }

    }
}
