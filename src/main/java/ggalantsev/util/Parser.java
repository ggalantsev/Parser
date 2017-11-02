package ggalantsev.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ggalantsev.entity.Offer;
import ggalantsev.entity.Offers;
import ggalantsev.entity.Variant;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Parser {

    private static final Logger log = Logger.getLogger(Parser.class);

    private static int httpRequestsCount = 0;

    public static Set<String> getUrlSetFromSearchByPattern(String pattern) {
        return getUrlSetFromSearch("https://www.aboutyou.de/suche?term=%22" + pattern + "%22");
    }

    public static Set<String> getUrlSetFromSearch(String searchUrl) {
        Document search = Parser.getDocumentFromUrl(searchUrl);

        // For local testing
//        Document search = Parser.getDocumentFromFile(new File("search2.html"));

        Set<String> offersURLs = new HashSet<String>();

        Elements href = search.body().getElementsByClass("product-image-list").first()
                .getElementsByAttribute("href");

        for (Element element : href) {
            String url = element.attr("href");
            if (url != null && url.length() > 0) {
                if (url.indexOf("/p/") == 0) {
                    offersURLs.add("https://www.aboutyou.de" + url);
                } else {
                    offersURLs.addAll(getUrlSetFromSearch("https://www.aboutyou.de" + url));
                }
            }
        }
        log.trace("Collected " + offersURLs.size() + " urls from " + searchUrl);
        return offersURLs;
    }

    public static Offers getOffers(File file) {
        return getOffersFromDocument(
                getDocumentFromFile(file));
    }

    public static Offers getOffers(String url) {
        return getOffersFromDocument(
                getDocumentFromUrl(url));
    }

    private static Offers getOffersFromDocument(Document doc) {
        long milis = System.currentTimeMillis();
        String json = doc.html();
        json = json.split(Pattern.quote("window.__INITIAL_STATE__="))[1];
        json = json.substring(0, json.indexOf(";</script>"));

        log.trace("JSON parsed in: " + (System.currentTimeMillis() - milis) + " ms.");

        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(json);
        JsonObject product = object
                .get("adpPage").getAsJsonObject()
                .get("product").getAsJsonObject();

        // Initialize offer fields
        String name = product
                .get("data").getAsJsonObject()
                .get("name").getAsString();
        String brand = product
                .get("brand").getAsJsonObject()
                .get("name").getAsString();
        String description = Parser.cleanHTML(doc
                .getElementsByClass("wrapper_1w5lv0w")
                .html());
        String articleID = product
                .get("productInfo").getAsJsonObject()
                .get("articleNumber").getAsString();
        // No sense to iterate thought colors. Another color it is another product card in search.
        String color = product
                .get("styles").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("color").getAsString();

        // Creating map of variants with prices
        JsonArray variantsArray = product.get("variants").getAsJsonArray();
        Map<Long, Variant> variants = new HashMap<Long, Variant>();
        for (JsonElement variant : variantsArray) {
            Variant v = new Variant();
            v.setId(variant.getAsJsonObject()
                    .get("id").getAsLong());
            v.setPrice(new BigDecimal(
                    variant.getAsJsonObject()
                            .get("price").getAsJsonObject()
                            .get("current").getAsBigInteger())
                    .divide(new BigDecimal(200))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setInitialPrice(new BigDecimal(
                    variant.getAsJsonObject()
                            .get("price").getAsJsonObject()
                            .get("old").getAsBigInteger())
                    .divide(new BigDecimal(200))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
            variants.put(v.getId(), v);
        }
        // Updating sizes in variants
        JsonArray sizesArray = product.get("sizes").getAsJsonArray();
        for (JsonElement size : sizesArray) {
            Variant v = variants.get(size.getAsJsonObject()
                    .get("variantId").getAsLong());
            v.setSize(size.getAsJsonObject()
                    .get("shopSize").getAsString());
        }

        log.trace("Data parsed: " + (System.currentTimeMillis() - milis) + " ms.");
        //Creating list of offer variables
        Offers offers = new Offers();
        for (Variant v : variants.values()) {
            Offer offer = new Offer();
            offer.setName(name);
            offer.setBrand(brand);
            offer.setColor(color);
            offer.setSize(v.getSize());
            offer.setPrice(v.getPrice());
            offer.setInitialPrice(v.getInitialPrice());
            offer.setDescription(description);
            offer.setArticleID(articleID);

            offers.addOffer(offer);
        }

        log.trace("POJO created: " + (System.currentTimeMillis() - milis) + " ms.");

        return offers;
    }

    private static Document getDocumentFromUrl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Chrome/61.0.3163.100")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get();
            httpRequestsCount++;
            log.info("Initialized: [" + doc.title() + "] " + url);
            return doc;
        } catch (IOException e) {
            log.error("Can't download: [" + url + ']');
            log.error(e.toString());
            return null;
        }
    }

    private static Document getDocumentFromFile(File filename) {
        try {
            Document doc = Jsoup.parse(filename, "UTF-8");
            log.info("Initialized: [" + doc.title() + ']');
            return doc;
        } catch (IOException e) {
            log.error("Can't read: [" + filename + ']');
            log.error(e.toString());
            return null;
        }
    }

    private static String cleanHTML(String html) {
        return html
                .replaceAll("[<](/)?div[^>]*[>]", "")
                .replaceAll(" class?=\"[^>]*[\"]", "")
                .replaceAll(" data-reactid?=\"[^>]*[\"]", "")
                .replaceAll("<!--.*?-->", "")
                .replaceAll(" +", " ")
                .replaceAll("\n ", "\n")
                .replaceAll("\n+", "");
    }

    public static int getHttpRequestsCount(){
        return httpRequestsCount;
    }

}
