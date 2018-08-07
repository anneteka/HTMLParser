package aboutyou;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Offer implements Cloneable {
    private String
            name,
            brand,
            color,
            price,
            initialPrice,
            description,
            size,
            articleID,
            shippingCosts;

    public Offer() {
    }

    Offer(Offer offer) {
        name = offer.name;
        brand = offer.brand;
        color = offer.color;
        price = offer.price;
        initialPrice = offer.initialPrice;
        description = offer.description;
        articleID = offer.articleID;
        shippingCosts = offer.shippingCosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(String initialPrice) {
        this.initialPrice = initialPrice;
    }

    public String getArticleID() {
        return articleID;
    }


    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(String shippingCosts) {
        this.shippingCosts = shippingCosts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(size, offer.size) &&
                Objects.equals(articleID, offer.articleID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, articleID);
    }
}
