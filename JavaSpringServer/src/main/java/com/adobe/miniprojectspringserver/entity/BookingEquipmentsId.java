


import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class BookingEquipmentsId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Booking booking;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Equipment equipment;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Equipment getEquipments() {
        return equipment;
    }

    public void setEquipments(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingEquipmentsId)) return false;
        BookingEquipmentsId that = (BookingEquipmentsId) o;
        if (getBooking() != null ? !getBooking().equals(that.getBooking()) : that.getBooking() != null) return false;
        return getEquipments() != null ? getEquipments().equals(that.getEquipments()) : that.getEquipments() == null;
    }

    @Override
    public int hashCode() {
        int result = getBooking() != null ? getBooking().hashCode() : 0;
        result = 31 * result + (getEquipments() != null ? getEquipments().hashCode() : 0);
        return result;
    }
}
