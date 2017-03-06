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
    private int id;

    @ManyToOne
    @JoinColumn(name = "mem_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "hot_id")
    private Hotel hotel;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private LocalDate date;

    @Column
    private int money;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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
