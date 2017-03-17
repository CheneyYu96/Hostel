package edu.nju.hostel.service;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Manager;
import edu.nju.hostel.entity.Room;
import edu.nju.hostel.utility.ResultInfo;
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
public interface ManagerService {
    Manager verifyManager(Integer id, String password);

    Manager findManager(Integer managerId);

    ResultInfo modifyPassword(Integer managerId, String originPassword, String password);

    List<ApproveVO> getApprove();

    Room getRoomInApprove(int id);

    Hotel getHotelInfoInApprove(int id);

    List<Room> getHotelRoomInApprove(int id);

    ResultInfo approveItem(Integer approveId);

    List<PayVO> getPay();

    ResultInfo payItem(int payId);

    List<PayWithMember> getPayWithMember(int hotelId);

    List<HotelStatistic> getHotelStatistic(LocalDate begin, LocalDate end);

    List<LiveIn> getAllMemberBookLine(StatisticType method, LocalDate begin, LocalDate end);

    RoomTypePie getAllMemberBookPie(StatisticType method, LocalDate begin, LocalDate end);

    List<LiveIn> getAllMemberInLine(StatisticType method, LocalDate begin, LocalDate end);

    RoomTypePie getAllMemberInPie(StatisticType method, LocalDate begin, LocalDate end);

    List<HostelFinance> getHostelFinance(StatisticType method, LocalDate begin, LocalDate end);

}
