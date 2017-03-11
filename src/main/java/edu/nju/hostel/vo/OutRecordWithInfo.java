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
}
