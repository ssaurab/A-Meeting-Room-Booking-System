


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regDate")
    private Date regDate;

    @Column(name = "date")
    private String date;

    @Column(name = "fromHour", nullable = false)
    private int fromHour;

    @Column(name = "toHour", nullable = false)
    private int toHour;

    @Column(name = "attendees", nullable = false)
    private int attendees;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "room_price", nullable = false)
    private float roomPrice;


    @Column(name = "equipment_price", nullable = false)
    private float equipmentPrice;

    @Column(name = "food_and_drink_price", nullable = false)
    private float foodAndDrinkPrice;

    @Column(name = "sub_total", nullable = false)
    private float subTotal;

    @Column(name = "tax", nullable = false)
    private float tax;

    @Column(name = "total", nullable = false)
    private float total;

    @Column(name = "deposit", nullable = false)
    private float deposit;

    @ManyToOne
    @JoinColumn(name = "room_layout_id", referencedColumnName = "id")
    private RoomLayout roomLayout;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingEquipments> equipments;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingSnacks> snacks;

    public Booking() {

    }

    public Booking(int id, String date, int fromHour, int toHour, int attendees, String status, String paymentMethod, float roomPrice,
                   float equipmentPrice, float foodAndDrinkPrice, float subTotal, float tax, float total, float deposit,
                   RoomLayout roomLayout, Room room, Client client, List<BookingEquipments> equipments,
                   List<BookingSnacks> snacks) {
        super();
        this.id = id;
        this.date = date;
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.attendees = attendees;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.roomPrice = roomPrice;
        this.equipmentPrice = equipmentPrice;
        this.foodAndDrinkPrice = foodAndDrinkPrice;
        this.subTotal = subTotal;
        this.tax = tax;
        this.total = total;
        this.deposit = deposit;
        this.roomLayout = roomLayout;
        this.room = room;
        this.client = client;
        this.equipments = equipments;
        this.snacks = snacks;
    }

    public List<BookingSnacks> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<BookingSnacks> snacks) {
        this.snacks = snacks;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public List<BookingEquipments> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<BookingEquipments> bookingEquipments) {
        this.equipments = bookingEquipments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public float getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(float roomPrice) {
        this.roomPrice = roomPrice;
    }

    public float getEquipmentPrice() {
        return equipmentPrice;
    }

    public void setEquipmentPrice(float equipmentPrice) {
        this.equipmentPrice = equipmentPrice;
    }

    public float getFoodAndDrinkPrice() {
        return foodAndDrinkPrice;
    }

    public void setFoodAndDrinkPrice(float foodAndDrinkPrice) {
        this.foodAndDrinkPrice = foodAndDrinkPrice;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public RoomLayout getRoomLayout() {
        return roomLayout;
    }

    public void setRoomLayout(RoomLayout roomLayout) {
        this.roomLayout = roomLayout;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + attendees;
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + Float.floatToIntBits(deposit);
        result = prime * result + Float.floatToIntBits(equipmentPrice);
        result = prime * result + Float.floatToIntBits(foodAndDrinkPrice);
        result = prime * result + id;
        result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
        result = prime * result + Float.floatToIntBits(roomPrice);
        result = prime * result + ((snacks == null) ? 0 : snacks.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + Float.floatToIntBits(subTotal);
        result = prime * result + Float.floatToIntBits(tax);
        result = prime * result + Float.floatToIntBits(total);
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
        Booking other = (Booking) obj;
        if (attendees != other.attendees)
            return false;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (Float.floatToIntBits(deposit) != Float.floatToIntBits(other.deposit))
            return false;
        if (Float.floatToIntBits(equipmentPrice) != Float.floatToIntBits(other.equipmentPrice))
            return false;
        if (equipments == null) {
            if (other.equipments != null)
                return false;
        } else if (!equipments.equals(other.equipments))
            return false;
        if (Float.floatToIntBits(foodAndDrinkPrice) != Float.floatToIntBits(other.foodAndDrinkPrice))
            return false;
        if (id != other.id)
            return false;
        if (paymentMethod == null) {
            if (other.paymentMethod != null)
                return false;
        } else if (!paymentMethod.equals(other.paymentMethod))
            return false;
        if (room == null) {
            if (other.room != null)
                return false;
        } else if (!room.equals(other.room))
            return false;
        if (roomLayout == null) {
            if (other.roomLayout != null)
                return false;
        } else if (!roomLayout.equals(other.roomLayout))
            return false;
        if (Float.floatToIntBits(roomPrice) != Float.floatToIntBits(other.roomPrice))
            return false;
        if (snacks == null) {
            if (other.snacks != null)
                return false;
        } else if (!snacks.equals(other.snacks))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (Float.floatToIntBits(subTotal) != Float.floatToIntBits(other.subTotal))
            return false;
        if (Float.floatToIntBits(tax) != Float.floatToIntBits(other.tax))
            return false;
        return Float.floatToIntBits(total) == Float.floatToIntBits(other.total);
    }


    @Override
    public String toString() {
        return "Booking [id=" + id + ", date=" + date + ", attendees=" + attendees + ", status=" + status + ", total="
                + total + ", deposit=" + deposit + ", roomLayout=" + roomLayout.getTitle() + ", room=" + room.getName() + ", client="
                + client.getName() + "]";
    }


}
