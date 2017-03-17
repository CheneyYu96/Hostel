package edu.nju.hostel.vo;

import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/14
 */
public class Translator {
    public int pay;
    public LocalDate begin;

    public Translator(int pay, LocalDate begin) {
        this.pay = pay;
        this.begin = begin;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public Translator() {
    }
}
