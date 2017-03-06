package edu.nju.hostel.service;

import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.entity.Order;
import edu.nju.hostel.utility.ResultInfo;

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

    ResultInfo modifyInfo(Member member);

    MemberCard register(String name, String password, String phoneNumber);

    ResultInfo activate(MemberCard card, String bankId, int money);

    ResultInfo payFee(MemberCard card, String bankId, int money);

    ResultInfo isInQualification(MemberCard card);

    ResultInfo stopQualification(String memberId);

    ResultInfo translateCredit(String cardId, int credit);

    /**
     * check room booking condition
     * and the balance in member card is enough
     * @param memberId
     * @param roomId
     * @param beginDate
     * @param endDate
     * @return
     */
    ResultInfo checkRoom(String memberId, String roomId, LocalDate beginDate, LocalDate endDate);

    Order bookRoom(String memberId, String roomId, LocalDate beginDate, LocalDate endDate);

    ResultInfo cancelRoom(String orderId);




}
