package ggalantsev.entity;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name="offers")
public class Offers {

    Logger log = Logger.getLogger(Offers.class);

    @XmlElement(name="offer")
    private List<Offer> offers = new LinkedList<Offer>();

    public void addOffer(Offer offer){
        offers.add(offer);
        log.trace("Added offer \"" + offer.getName() + "\".");
    }

    public void addOffers(Offers offers){
        if (offers != null){
            this.offers.addAll(offers.getList());
            log.trace("Added offer List.");
        }
    }

    public List<Offer> getList(){
        return this.offers;
    }

    @Override
    public String toString() {
        return "Offers: \n" +
                offers +
                '\n';
    }
}
