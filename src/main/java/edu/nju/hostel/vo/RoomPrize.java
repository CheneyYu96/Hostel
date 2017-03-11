package edu.nju.hostel.vo;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
public class RoomPrize {
    public String roomNumber;
    public int originPrize;
    public int planDiscount;
    public int memberDiscount;
    public int nowPrize;
    public String errorInfo;

    public RoomPrize(String roomNumber, int originPrize) {
        this.roomNumber = roomNumber;
        this.originPrize = originPrize;
        this.planDiscount = 1;
        this.memberDiscount = 1;
    }

    public RoomPrize(String errorInfo){
        this.errorInfo = errorInfo;
    }
}
