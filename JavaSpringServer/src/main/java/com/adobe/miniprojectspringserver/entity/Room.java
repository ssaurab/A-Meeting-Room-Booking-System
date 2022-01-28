


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;


@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "capacity", nullable = false)
    private int capacity;
    @Column(name = "status", nullable = false)
    private String status;
    @Lob
    @Column(name = "image")
    private byte[] image;
    @Column(name = "description", length = 2048)
    private String description;
    @Column(name = "price_per_day", nullable = false)
    private float pricePerDay;
    @Column(name = "price_per_hour", nullable = false)
    private float pricePerHour;
    @Column(name = "price_per_half_day", nullable = false)
    private float pricePerHalfDay;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "room_room_layout_relations",
            joinColumns = {
                    @JoinColumn(name = "room_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "room_layout_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<RoomLayout> layouts;
    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private Set<Booking> bookings;

    public Room(int id, String name, int capacity, String status, byte[] image, String description, float pricePerDay, float pricePerHour) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = status;
        this.image = image;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    public Room() {
        //  empty constructor required
    }

    public Set<RoomLayout> getLayouts() {
        return layouts;
    }

    public void setLayouts(Set<RoomLayout> layouts) {
        this.layouts = layouts;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return new String(image);
    }

    public void setImage(String image) {
        this.image = image.getBytes();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(float pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public float getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public float getPricePerHalfDay() {
        return pricePerHalfDay;
    }

    public void setPricePerHalfDay(float pricePerHalfDay) {
        this.pricePerHalfDay = pricePerHalfDay;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bookings == null) ? 0 : bookings.hashCode());
        result = prime * result + capacity;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + id;
        result = prime * result + Arrays.hashCode(image);
        result = prime * result + ((layouts == null) ? 0 : layouts.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + Float.floatToIntBits(pricePerDay);
        result = prime * result + Float.floatToIntBits(pricePerHalfDay);
        result = prime * result + Float.floatToIntBits(pricePerHour);
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        Room other = (Room) obj;
        if (bookings == null) {
            if (other.bookings != null)
                return false;
        } else if (!bookings.equals(other.bookings))
            return false;
        if (capacity != other.capacity)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (!Arrays.equals(image, other.image))
            return false;
        if (layouts == null) {
            if (other.layouts != null)
                return false;
        } else if (!layouts.equals(other.layouts))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Float.floatToIntBits(pricePerDay) != Float.floatToIntBits(other.pricePerDay))
            return false;
        if (Float.floatToIntBits(pricePerHalfDay) != Float.floatToIntBits(other.pricePerHalfDay))
            return false;
        if (Float.floatToIntBits(pricePerHour) != Float.floatToIntBits(other.pricePerHour))
            return false;
        if (status == null) {
            return other.status == null;
        } else return status.equals(other.status);
    }
}
