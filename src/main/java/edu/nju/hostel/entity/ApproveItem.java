package edu.nju.hostel.entity;

import edu.nju.hostel.utility.HotelStatus;

import javax.persistence.*;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/16
 */
@Entity
@Table(name = "approve")
public class ApproveItem {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "approved")
    private Boolean hasApproved;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Boolean getHasApproved() {
        return hasApproved;
    }

    public void setHasApproved(Boolean hasApproved) {
        this.hasApproved = hasApproved;
    }
}
