package ggalantsev.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

@Data
@XmlRootElement(name = "offer")
@XmlType(propOrder={"name","brand","color", "size","price","initialPrice","description","articleID","shippingCosts"})
public class Offer {

    private String name;
    private String brand;
    private String color;
    private String size;
    private BigDecimal price;
    private BigDecimal initialPrice;
    private String description;
    private String articleID;
    private BigDecimal shippingCosts = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP); // free shipping for all goods

    @Override
    public String toString() {
        return "\nOffer{" + "\n" +
                "Name : \"" + name + "\"\n" +
                "Brand : \"" + brand + "\"\n" +
                "Color : \"" + color + "\"\n" +
                "Size : \"" + size + "\"\n" +
                "Price : \"" + price + "\"\n" +
                "InitialPrice : \"" + initialPrice + "\"\n" +
                "Description : \"" + description + "\"\n" +
                "ArticleID : \"" + articleID + "\"\n" +
                "ShippingCosts : \"" + shippingCosts + "\"\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Offer offer = (Offer) o;

        return getArticleID().equals(offer.getArticleID());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getArticleID().hashCode();
        return result;
    }
}
