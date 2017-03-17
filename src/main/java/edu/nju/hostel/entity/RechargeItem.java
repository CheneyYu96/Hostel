package edu.nju.hostel.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/17
 */
@Entity
@Table(name = "recharge")
public class RechargeItem {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "card_id")
    private int cardId;

    private int amount;

    private LocalDate date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
