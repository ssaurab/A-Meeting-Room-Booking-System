


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "booking_equipments_relations")
@AssociationOverrides({
        @AssociationOverride(name = "pk.booking",
                joinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id")),
        @AssociationOverride(name = "pk.equipment",
                joinColumns = @JoinColumn(name = "equipment_id", referencedColumnName = "id"))})
public class BookingEquipments implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @EmbeddedId
    private BookingEquipmentsId pk = new BookingEquipmentsId();

    @Column(name = "count")
    private int count;

    public BookingEquipmentsId getPk() {
        return pk;
    }

    public void setPk(BookingEquipmentsId pk) {
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
    public Equipment getEquipments() {
        return pk.getEquipments();
    }

    public void setEquipments(Equipment equipment) {
        pk.setEquipments(equipment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingEquipments)) return false;

        BookingEquipments that = (BookingEquipments) o;

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
