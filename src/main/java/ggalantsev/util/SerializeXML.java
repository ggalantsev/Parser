package ggalantsev.util;

import ggalantsev.entity.Offers;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class SerializeXML {

    private static final Logger log = Logger.getLogger(SerializeXML.class);

    public static void generateXMLtoConsole(Offers offers){
        try {
            log.trace("XML to console generating started.");
            JAXBContext jaxbContext = JAXBContext.newInstance(Offers.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // Read-friendly formatting
            marshaller.marshal(offers, System.out);
            log.trace("XML to console generating finished.");
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }
    }

    public static void generateXMLtoFile(Offers offers, String filename){
        try {
            log.trace("XML to file generating started.");
            JAXBContext jaxbContext = JAXBContext.newInstance(Offers.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            File file = new File(filename);
            marshaller.marshal(offers, file);
            log.trace("XML to file generating finished.");
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }
    }
}
