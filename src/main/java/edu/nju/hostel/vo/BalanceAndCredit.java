package edu.nju.hostel.vo;

import edu.nju.hostel.utility.ResultInfo;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/14
 */
public class BalanceAndCredit {
    public int balance;
    public int credit;
    public ResultInfo info;

    public BalanceAndCredit(ResultInfo info) {
        this.info = info;
    }
}
