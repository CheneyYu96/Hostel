package edu.nju.hostel.vo;


import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/17
 */
public class HostelFinance {
    public int rechargeNumber;  // member recharge times together
    public int rechargeAmount;  // member recharge money together

    public int consume;         // member consume money together, containing order and in record
    public int pay;             // the amount that hostel has payed money to hotel together

    public LocalDate date;

    public HostelFinance(LocalDate date) {
        this.date = date;
    }
}
