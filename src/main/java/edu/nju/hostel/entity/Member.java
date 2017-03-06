package edu.nju.hostel.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 *
 * @author yuminchen
 * @date 2017/3/3
 * @version V1.0
 */
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @Column
    private String password;

    @Column(length = 11,name = "phone")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "card")
    private MemberCard card;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MemberCard getCard() {
        return card;
    }

    public void setCard(MemberCard card) {
        this.card = card;
    }

}
