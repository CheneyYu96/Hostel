package edu.nju.hostel.service;

import edu.nju.hostel.entity.*;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.RoomType;
import edu.nju.hostel.utility.StatisticType;
import edu.nju.hostel.vo.*;

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

    Hotel getInfo(int hotelId);

    ResultInfo modifyInfo(Hotel hotel);

    Hotel verifyHotel(String id, String password);

    ResultInfo modifyRoom(Room room);

    ResultInfo addRoom(int hotelId, RoomType type, String roomNumber, int prize);

    ResultInfo delRoom(int roomId);

    List<Room> getRooms(int hotelId);

    ResultInfo raisePlan(int hotelId, String name, String des, RoomType type, LocalDate beginDate, LocalDate endDate, int discount);

    List<RoomInPlan> getRelateRooms(int hotelId, RoomType type, int discount);

    RoomPrize getRoomPrizeInPlan(int hotelId, String roomNumber, LocalDate begin, LocalDate end);

    List<Plan> getPlan(int hotelId);

    ResultInfo delPlan(int planId);

    List<InRecordWithName> getInRecord(int hotelId);

    ResultInfo addInRecord(List<InRecordName> nameList, int hotel, String roomNumber, RoomType type, LocalDate begin, LocalDate end, int pay, boolean payByCard, int orderId,int cardId);

    List<OutRecordWithInfo> getOutRecord(int hotelId);

    ResultInfo addOutRecord(int hotelId, int inRecordId, LocalDate date);

    ResultInfo addRecordByOrder(List<InRecordName> nameList,int hotelId, int orderId);

    List<LiveIn> getBookLine(int hotelId, StatisticType method, LocalDate begin, LocalDate end);

    RoomTypePie getBookPie(int hotelId, StatisticType method, LocalDate begin, LocalDate end);

    List<LiveIn> getInLine(int hotelId, StatisticType method, LocalDate begin, LocalDate end);

    RoomTypePie getInPie(int hotelId, StatisticType method, LocalDate begin, LocalDate end);

    List<Translator> getFinance(int hotelId, StatisticType method, LocalDate begin, LocalDate end);

    ResultInfo modifyPassword(Integer hotelId, String originPassword, String password);
}
