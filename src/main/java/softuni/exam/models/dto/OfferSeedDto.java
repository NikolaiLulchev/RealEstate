package softuni.exam.models.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {

    @Positive
    @XmlElement
    private double price;

    @XmlElement(name = "agent")
    @NotNull
    private OfferSeedAgentDto agent;

    @XmlElement
    @NotNull
    private OfferSeedApartDto apartment;

    @XmlElement
    private String publishedOn;

    public OfferSeedDto() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OfferSeedAgentDto getAgent() {
        return agent;
    }

    public void setAgent(OfferSeedAgentDto agent) {
        this.agent = agent;
    }

    public OfferSeedApartDto getApartment() {
        return apartment;
    }

    public void setApartment(OfferSeedApartDto apartment) {
        this.apartment = apartment;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }
}
