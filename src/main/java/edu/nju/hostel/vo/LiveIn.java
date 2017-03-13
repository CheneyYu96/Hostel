package edu.nju.hostel.vo;

import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/12
 */
public class LiveIn {
    public int amount;
    public int money;
    public LocalDate date;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
