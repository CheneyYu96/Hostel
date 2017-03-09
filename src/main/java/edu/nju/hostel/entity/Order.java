package edu.nju.hostel.entity;

import edu.nju.hostel.utility.OrderStatus;

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

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private LocalDate date;

    @Column
    private int money;

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
