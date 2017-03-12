package edu.nju.hostel.vo;

import edu.nju.hostel.utility.RoomType;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
public class RoomPrize {
    public String roomNumber;
    public RoomType type;
    public int originPrize;
    public int planDiscount;
    public int memberDiscount;
    public int nowPrize;
    public String errorInfo;

    public RoomPrize(String roomNumber, int originPrize, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.originPrize = originPrize;
        this.planDiscount = 100;
        this.memberDiscount = 100;
    }

    public RoomPrize(String errorInfo){
        this.errorInfo = errorInfo;
    }
}
