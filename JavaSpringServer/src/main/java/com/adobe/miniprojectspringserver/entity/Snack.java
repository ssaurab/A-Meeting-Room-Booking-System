

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "snacks")
public class Snack {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", unique = true)
    private String title;
    @Column(name = "price", nullable = false)
    private Float price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.snack")
    @JsonIgnore
    private List<BookingSnacks> bookings;


    public Snack(int id, String title, Float price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public Snack() {

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<BookingSnacks> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingSnacks> bookingSnacks) {
        this.bookings = bookingSnacks;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bookings == null) ? 0 : bookings.hashCode());
        result = prime * result + id;
        result = prime * result + ((price == null) ? 0 : price.hashCode());
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
        Snack other = (Snack) obj;
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
        if (title == null) {
            return other.title == null;
        } else return title.equals(other.title);
    }

}
