package ggalantsev;

import ggalantsev.entity.Offers;
import org.apache.log4j.Logger;
import ggalantsev.util.Parser;
import ggalantsev.util.SerializeXML;

import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String pattern = null;
        // if no args - use console to input search pattern
        if (args.length == 0) {
            System.out.println("Insert search pattern");
            pattern = sc.nextLine();
            if (pattern == null || pattern.equals("")) {
                System.out.println("Parser closing...");
                return;
            }
        } else {
            // use argument as search pattern
            pattern = args[0];
        }

        // for time-using logging
        long timeMillis = System.currentTimeMillis();
        log.info("Parser started...");

        Set<String> absLinks = Parser.getUrlSetFromSearchByPattern(pattern);

        log.info("All search urls (" + absLinks.size() + ") collected in "
                + (System.currentTimeMillis() - timeMillis) + " ms.");

        Offers offers = new Offers();
        for (String url : absLinks) {
            Offers temp = null;
            try {
                temp = Parser.getOffers(url);
            } catch (Exception e) {
                log.error("Error reading page: " + url);
                log.error(e);
                e.printStackTrace();
            }
            offers.addOffers(temp);
        }

        // for console xml output
//        SerializeXML.generateXMLtoConsole(offers);

        // write xml to file
        SerializeXML.generateXMLtoFile(offers, "offers.xml");

        // Show POJOs on console
//        System.out.println(offers.toString());

        log.info("Parsing finished in " + (System.currentTimeMillis() - timeMillis) + " ms.");
        log.info("Amount of triggered HTTP request: " + Parser.getHttpRequestsCount() + ".");
        log.info("Amount of extracted products: " + offers.getList().size() + ".");
        log.info("Memory Footprint: "
                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024)
                + " kilobytes.");
        System.out.println("Press enter to exit");
        sc.nextLine();
        log.info("----------------- Exiting -----------------");
    }
}
