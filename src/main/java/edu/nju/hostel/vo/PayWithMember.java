package edu.nju.hostel.vo;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/17
 */
public class PayWithMember {
    public int id;
    public String memberId;
    public String name;
    public boolean isOrder;
    public int pay;

    public PayWithMember(int id) {
        this.id = id;
    }


}
