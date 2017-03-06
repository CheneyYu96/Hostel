package edu.nju.hostel.entity;

import javax.persistence.*;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
@Entity
@Table(name = "bank_card")
public class BankCard {
    @Id
    @GeneratedValue
    private int id;

    @Column(length = 16,name = "card_id")
    private String cardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
