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
    private Boolean payByCard;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
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
}
