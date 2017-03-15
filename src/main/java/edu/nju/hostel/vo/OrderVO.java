package edu.nju.hostel.vo;

import edu.nju.hostel.utility.OrderStatus;
import edu.nju.hostel.utility.RoomType;

import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/15
 */
public class OrderVO {

    public int id;
    public String hotelName;
    public String roomNumber;
    public RoomType type;
    public OrderStatus status;
    public LocalDate begin;
    public LocalDate end;
    public int pay;
    public String info;

    public OrderVO(String info) {
        this.info = info;
    }

    public OrderVO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
