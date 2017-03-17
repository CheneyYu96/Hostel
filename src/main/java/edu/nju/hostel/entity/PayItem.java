package edu.nju.hostel.entity;

import javax.persistence.*;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/16
 */
@Entity
@Table(name = "pay")
public class PayItem {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "in_record_id")
    private int inRecordId;

    @Column(name = "payed")
    private Boolean hasPay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean getHasPay() {
        return hasPay;
    }

    public void setHasPay(Boolean hasPay) {
        this.hasPay = hasPay;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}
