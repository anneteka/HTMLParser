package aboutyou;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

@XmlRootElement(name="offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Offers {
    Offers(){
        offers = new ArrayList<Offer>();
    }
    @XmlElement(name="offer")
    private ArrayList<Offer> offers;

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }
}
