package softuni.exam.models.entity;

import com.google.gson.annotations.Expose;
import softuni.exam.models.enums.ApartmentType;

import javax.persistence.*;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {

    @Expose
    @Column(name = "apartment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApartmentType apartmentType;

    private double area;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;

    public Apartment() {
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
