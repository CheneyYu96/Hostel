package edu.nju.hostel.service;

import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.entity.Order;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.vo.BalanceAndCredit;

import java.time.LocalDate;

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

    ResultInfo payFee(int cardId, String bankId, int money);

    ResultInfo isInQualification(MemberCard card);

    ResultInfo stopQualification(String memberId);

    BalanceAndCredit translateCredit(int cardId, int credit);

    /**
     * check room booking condition
     * and the balance in member card is enough
     * @param memberId
     * @param roomId
     * @param beginDate
     * @param endDate
     * @return
     */
    ResultInfo checkRoom(int memberId, int roomId, LocalDate beginDate, LocalDate endDate);

    Order bookRoom(int memberId, int roomId, LocalDate beginDate, LocalDate endDate);

    ResultInfo cancelRoom(int orderId);

    ResultInfo payByCard(int cardId, int pay);


}
