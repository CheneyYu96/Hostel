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
    private int id;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="hot_id")
    private Hotel hotel;

    @Column(name = "begin_date")
    private LocalDate beginDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column
    private int discount;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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
}
