package edu.nju.hostel.entity;

import edu.nju.hostel.utility.HotelStatus;

import javax.persistence.*;

/**
 *
 * @author yuminchen
 * @date 2017/3/3
 * @version V1.0
 */
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue
    private int id;

    @Column(length = 32)
    private String name;

    @Column(length = 32)
    private String password;

    @Column(length = 32)
    private String address;

    @Column
    @Enumerated(EnumType.STRING)
    private HotelStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HotelStatus getStatus() {
        return status;
    }

    public void setStatus(HotelStatus status) {
        this.status = status;
    }
}


