package edu.nju.hostel.entity;

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
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @Column
    private String des;

    @Column(name="hot_id")
    private int hotelId;

    @Column(name = "begin_date")
    private LocalDate beginDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column
    private int discount;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Plan() {
    }

    public Plan(String name, String des, int hotelId, LocalDate beginDate, LocalDate endDate, RoomType type, int discount) {
        this.name = name;
        this.des = des;
        this.hotelId = hotelId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.type = type;
        this.discount = discount;
    }
}
