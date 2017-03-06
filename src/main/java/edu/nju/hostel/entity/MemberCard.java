package edu.nju.hostel.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
@Entity
@Table(name = "member_card")
public class MemberCard {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne(mappedBy="card")
    private Member member;

    @Column(name = "bank_card")
    private String bankCard;

    @Column
    private int balance;

    @Column
    private int credit;

    @Column(name = "activated")
    private Boolean isActivated;

    @Column(name = "activate_date")
    private LocalDate activateDate;

    @Column(name = "consume")
    private int consumeAmount;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public LocalDate getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(LocalDate activateDate) {
        this.activateDate = activateDate;
    }

    public int getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(int consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
}
