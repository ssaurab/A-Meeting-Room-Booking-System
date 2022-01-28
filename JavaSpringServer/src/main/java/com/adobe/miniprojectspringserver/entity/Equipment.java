

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", unique = true)
    private String title;
    @Column(name = "bookmultipleunit", nullable = false)
    private boolean bookMultipleUnit;
    @Column(name = "pricetype", nullable = false)
    private String priceType;
    @Column(name = "price", nullable = false)
    private Float price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.equipment")
    @JsonIgnore
    private List<BookingEquipments> bookings;

    public Equipment(int id, String title, boolean bookMultipleUnit, String priceType, Float price) {
        this.id = id;
        this.title = title;
        this.bookMultipleUnit = bookMultipleUnit;
        this.priceType = priceType;
        this.price = price;
    }

    public Equipment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBookMultipleUnit() {
        return bookMultipleUnit;
    }

    public void setBookMultipleUnit(boolean bookMultipleUnit) {
        this.bookMultipleUnit = bookMultipleUnit;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<BookingEquipments> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingEquipments> bookingEquipments) {
        this.bookings = bookingEquipments;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bookMultipleUnit ? 1231 : 1237);
        result = prime * result + ((bookings == null) ? 0 : bookings.hashCode());
        result = prime * result + id;
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((priceType == null) ? 0 : priceType.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Equipment other = (Equipment) obj;
        if (bookMultipleUnit != other.bookMultipleUnit)
            return false;
        if (bookings == null) {
            if (other.bookings != null)
                return false;
        } else if (!bookings.equals(other.bookings))
            return false;
        if (id != other.id)
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (priceType == null) {
            if (other.priceType != null)
                return false;
        } else if (!priceType.equals(other.priceType))
            return false;
        if (title == null) {
            return other.title == null;
        } else return title.equals(other.title);
    }
}
