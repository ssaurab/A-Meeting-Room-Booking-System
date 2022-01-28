

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "booking_snacks_relations")
@AssociationOverrides({
        @AssociationOverride(name = "pk.booking",
                joinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id")),
        @AssociationOverride(name = "pk.snack",
                joinColumns = @JoinColumn(name = "snack_id", referencedColumnName = "id"))})
public class BookingSnacks implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @EmbeddedId
    private BookingSnacksId pk = new BookingSnacksId();

    @Column(name = "count")
    private int count;

    public BookingSnacksId getPk() {
        return pk;
    }

    public void setPk(BookingSnacksId pk) {
        this.pk = pk;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JsonIgnore
    @Transient
    public Booking getBooking() {
        return pk.getBooking();
    }

    public void setBooking(Booking booking) {
        pk.setBooking(booking);
    }

    @Transient
    public Snack getSnacks() {
        return pk.getSnacks();
    }

    public void setSnacks(Snack equipments) {
        pk.setSnacks(equipments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingSnacks)) return false;

        BookingSnacks that = (BookingSnacks) o;

        if (getCount() != that.getCount()) return false;
        return getPk() != null ? getPk().equals(that.getPk()) : that.getPk() == null;
    }

    @Override
    public int hashCode() {
        int result = getPk() != null ? getPk().hashCode() : 0;
        result = 31 * result + getCount();
        return result;
    }
}
