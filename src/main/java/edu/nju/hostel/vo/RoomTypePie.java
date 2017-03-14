package edu.nju.hostel.vo;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/13
 */
public class RoomTypePie {
    public int singleRoom;
    public int doubleRoom;
    public int bigRoom;
    public int wholeRoom;

    public RoomTypePie() {
        this.singleRoom = 0;
        this.doubleRoom = 0;
        this.bigRoom = 0;
        this.wholeRoom = 0;
    }

    public int getDoubleRoom() {
        return doubleRoom;
    }

    public void setDoubleRoom(int doubleRoom) {
        this.doubleRoom = doubleRoom;
    }

    public int getBigRoom() {
        return bigRoom;
    }

    public void setBigRoom(int bigRoom) {
        this.bigRoom = bigRoom;
    }

    public int getWholeRoom() {
        return wholeRoom;
    }

    public void setWholeRoom(int wholeRoom) {
        this.wholeRoom = wholeRoom;
    }

    public int getSingleRoom() {

        return singleRoom;
    }

    public void setSingleRoom(int singleRoom) {
        this.singleRoom = singleRoom;
    }
}
