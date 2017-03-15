package edu.nju.hostel.service;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.entity.Order;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.RoomType;
import edu.nju.hostel.vo.BalanceAndCredit;
import edu.nju.hostel.vo.HotelVO;
import edu.nju.hostel.vo.OrderVO;
import edu.nju.hostel.vo.RoomPrize;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface MemberService {

    /**
     *
     * @param name
     * @param password
     * @return
     */
    Member verifyMember(String name, String password);

    Member findMember(int memberId);

    MemberCard findCard(int cardId);

    ResultInfo editInfo(int id, String name, String phone, String bankCard);

    ResultInfo modifyPassword(int id, String originPass, String newPass);

    MemberCard register(String name, String password, String phoneNumber);

    ResultInfo activate(int cardId, String bankId, int money);

    BalanceAndCredit payFee(int cardId, String bankId, int money);

    ResultInfo isInQualification(int cardId);

    ResultInfo stopQualification(int memberId);

    BalanceAndCredit translateCredit(int cardId, int credit);

    List<OrderVO> getOrder(int cardId);

    OrderVO makeOrder(int cardId, int hotelId, String roomNumber, RoomType type, LocalDate beginDate, LocalDate endDate, int pay);

    ResultInfo cancelOrder(int orderId);

    ResultInfo payByCard(int cardId, int pay);

    List<HotelVO> getHotel();

    RoomPrize countPay(int cardId, int hotelId, RoomType type, LocalDate begin, LocalDate end);
}
