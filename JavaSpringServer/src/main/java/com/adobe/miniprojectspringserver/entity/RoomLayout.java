

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;


@Entity
@Table(name = "room_layout")
public class RoomLayout {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", unique = true, nullable = false)
    private String title;
    @Lob
    @Column(name = "image")
    private byte[] image;

    @JsonIgnore
    @OneToMany(mappedBy = "roomLayout")
    private Set<Booking> bookings;

    @JsonIgnore
    @ManyToMany(mappedBy = "layouts")
    private Set<Room> rooms;

    public RoomLayout() {
    }

    public RoomLayout(int id, String title, byte[] image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
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

    public String getImage() {
        return new String(image);
    }

    public void setImage(String image) {
        this.image = image.getBytes();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((image == null) ? 0 : Arrays.toString(image).hashCode());
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
        RoomLayout other = (RoomLayout) obj;
        if (bookings == null) {
            if (other.bookings != null)
                return false;
        } else if (!bookings.equals(other.bookings))
            return false;
        if (id != other.id)
            return false;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!Arrays.equals(image, other.image))
            return false;
        if (rooms == null) {
            if (other.rooms != null)
                return false;
        } else if (!rooms.equals(other.rooms))
            return false;
        if (title == null) {
            return other.title == null;
        } else return title.equals(other.title);
    }

}
