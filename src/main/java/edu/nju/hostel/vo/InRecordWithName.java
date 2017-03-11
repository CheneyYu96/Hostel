package edu.nju.hostel.vo;

import edu.nju.hostel.utility.RoomType;

import java.time.LocalDate;
import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
public class InRecordWithName {

    public int id;
    public List<String> nameList;
    public String roomNumber;
    public RoomType type;
    public LocalDate begin;
    public LocalDate end;
    public int pay;
    private boolean payByCard;

    public InRecordWithName() {
    }

    public InRecordWithName(List<String> nameList, int id, String roomNumber, RoomType type, LocalDate begin, LocalDate end, int pay, boolean payByCard) {
        this.nameList = nameList;
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.begin = begin;
        this.end = end;
        this.pay = pay;
        this.payByCard = payByCard;
    }
}
