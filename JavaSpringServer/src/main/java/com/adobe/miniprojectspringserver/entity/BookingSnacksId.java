

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class BookingSnacksId implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Booking booking;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Snack snack;


    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Snack getSnacks() {
        return snack;
    }

    public void setSnacks(Snack snack) {
        this.snack = snack;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingSnacksId)) return false;

        BookingSnacksId that = (BookingSnacksId) o;

        if (getBooking() != null ? !getBooking().equals(that.getBooking()) : that.getBooking() != null) return false;
        return getSnacks() != null ? getSnacks().equals(that.getSnacks()) : that.getSnacks() == null;
    }

    @Override
    public int hashCode() {
        int result = getBooking() != null ? getBooking().hashCode() : 0;
        result = 31 * result + (getSnacks() != null ? getSnacks().hashCode() : 0);
        return result;
    }
}
