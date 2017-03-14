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
}
