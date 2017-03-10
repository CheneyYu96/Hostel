package edu.nju.hostel.vo;

import edu.nju.hostel.utility.RoomType;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/10
 */
public class RoomInPlan {
    public String roomNumber;

    public RoomType type;

    public int originPrize;

    public String discount;

    public int nowPrize;

    public RoomInPlan(String roomNumber, RoomType type, int originPrize, String discount, int nowPrize) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.originPrize = originPrize;
        this.discount = discount;
        this.nowPrize = nowPrize;
    }
}
