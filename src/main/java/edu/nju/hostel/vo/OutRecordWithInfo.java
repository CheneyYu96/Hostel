package edu.nju.hostel.vo;

import edu.nju.hostel.utility.RoomType;

import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
public class OutRecordWithInfo {
    public int id;
    public String roomNumber;
    public RoomType type;
    public LocalDate date;

    public OutRecordWithInfo() {
    }

    public OutRecordWithInfo(int id, String roomNumber, RoomType type, LocalDate date) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
