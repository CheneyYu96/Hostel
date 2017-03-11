package edu.nju.hostel.entity;

import edu.nju.hostel.utility.OrderStatus;
import edu.nju.hostel.utility.RoomType;

import javax.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author yuminchen
 * @date 2017/3/3
 * @version V1.0
 */
@Entity
@Table(name = "my_order")
public class Order {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "mem_id")
    private int memberId;

    @Column(name = "hot_id")
    private int hotelId;

    @Column(name = "room_number")
    private String roomNumber;

    private RoomType type;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private LocalDate begin;

    @Column
    private LocalDate end;

    @Column
    private int pay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }
}
