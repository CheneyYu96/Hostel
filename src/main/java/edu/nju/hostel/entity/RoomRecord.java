package edu.nju.hostel.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/14
 */
@Entity
@Table(name = "room_record")
public class RoomRecord {
    @Id
    @GeneratedValue
    private int id;

    @Column(name="order_id")
    private int orderId;

    @Column(name="record_id")
    private int inRecordId;

    @Column(name="room_id")
    private int roomId;

    private LocalDate begin;

    private LocalDate end;

    public RoomRecord() {
    }

    public RoomRecord(int orderId, int inRecordId, int roomId, LocalDate begin, LocalDate end) {
        this.orderId = orderId;
        this.inRecordId = inRecordId;
        this.roomId = roomId;
        this.begin = begin;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getInRecordId() {
        return inRecordId;
    }

    public void setInRecordId(int inRecordId) {
        this.inRecordId = inRecordId;
    }
}
