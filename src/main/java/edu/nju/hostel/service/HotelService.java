package edu.nju.hostel.service;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Room;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.RoomType;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface HotelService {

    Hotel register(String name, String password, String address);

    ResultInfo modifyInfo(Hotel hotel);

    Hotel verifyHotel(String id, String password);

    ResultInfo raisePlan(int hotelId, RoomType type, LocalDate beginDate, LocalDate endDate, int discount);

    ResultInfo modifyRoom(Room room);

    ResultInfo addRoom(int hotelId, RoomType type, String roomNumber, int prize);

    ResultInfo delRoom(int roomId);

    List<Room> getRooms(int hotelId);

}
