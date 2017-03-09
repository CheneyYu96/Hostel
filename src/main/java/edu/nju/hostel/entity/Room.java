package edu.nju.hostel.entity;

import edu.nju.hostel.utility.HotelStatus;
import edu.nju.hostel.utility.RoomType;

import javax.persistence.*;

/**
 *
 * @author yuminchen
 * @date 2017/3/3
 * @version V1.0
 */
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue
    private int id;

    @Column(name="hot_id")
    private int hotelId;

    @Column
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column(name = "number")
    private String roomNumber;

    @Column(name = "available")
    private Boolean isAvailable;

    @Column
    @Enumerated(EnumType.STRING)
    private HotelStatus status;

    @Column
    private int prize;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HotelStatus getStatus() {
        return status;
    }

    public void setStatus(HotelStatus status) {
        this.status = status;
    }

    public Room() {
    }

    public Room(int id, RoomType type, String roomNumber, int prize) {
        this.id = id;
        this.type = type;
        this.roomNumber = roomNumber;
        this.prize = prize;
    }
}
