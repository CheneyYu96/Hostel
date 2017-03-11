package edu.nju.hostel.entity;

import edu.nju.hostel.utility.RoomType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
@Entity
@Table(name = "in_record")
public class InRecord {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "room_number")
    private String roomNumber;

    private RoomType type;

    private LocalDate begin;

    private LocalDate end;

    private int pay;

    @Column(name = "is_card")
    private Boolean payByCard;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "card_id")
    private int cardId;

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

    public Boolean getPayByCard() {
        return payByCard;
    }

    public void setPayByCard(Boolean payByCard) {
        this.payByCard = payByCard;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public InRecord() {
    }

    public InRecord(int hotelId, String roomNumber, RoomType type, LocalDate begin, LocalDate end, int pay, Boolean payByCard, int orderId, int cardId) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.begin = begin;
        this.end = end;
        this.pay = pay;
        this.payByCard = payByCard;
        this.orderId = orderId;
        this.cardId = cardId;
    }
}
