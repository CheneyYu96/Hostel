package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.BankCardRepository;
import edu.nju.hostel.dao.MemberCardRepository;
import edu.nju.hostel.dao.MemberRepository;
import edu.nju.hostel.entity.BankCard;
import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.entity.Order;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
@Service
public class MemberServiceBean implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCardRepository memberCardRepository;
    private final BankCardRepository bankCardRepository;

    @Autowired
    public MemberServiceBean(MemberRepository memberRepository, MemberCardRepository memberCardRepository, BankCardRepository bankCardRepository) {
        this.memberRepository = memberRepository;
        this.memberCardRepository = memberCardRepository;
        this.bankCardRepository = bankCardRepository;
    }

    @Override
    public Member verifyMember(String name, String password) {

        List<Member> memberList = memberRepository.findByName(name);

        if(memberList!=null && memberList.size()>0){
            Member member = memberList.get(0);
            if(password.equals(member.getPassword())){
                return member;
            }
        }

        return null;
    }

    @Override
    public ResultInfo modifyInfo(Member member) {
        Member member1 = memberRepository.save(member);
        if(member1.getId()==member.getId()){
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

    @Transactional
    @Override
    public MemberCard register(String name, String password, String phoneNumber) {

        Member member = new Member();
        member.setName(name);
        member.setPassword(password);
        member.setPhoneNumber(phoneNumber);

        MemberCard card = new MemberCard();
        member.setCard(card);
        memberRepository.save(member);
        return memberCardRepository.save(card);
    }

    @Override
    @Transactional
    public ResultInfo activate(MemberCard card, String bankId, int money) {

        ResultInfo resultInfo = new ResultInfo(false);
        if(money < 1000){
            resultInfo.setInfo("金额小于1000");
        }
        else {

            List<BankCard> bankCardList = bankCardRepository.findByCardId(bankId);
            if(bankCardList==null||bankCardList.size()==0){
                resultInfo.setInfo("银行卡号不存在");
            }
            else {
                card.setBalance(money);
                card.setActivated(true);
                card.setBankCard(bankId);
                card.setActivateDate(LocalDate.now());
                card.setConsumeAmount(0);
                card.setCredit(0);

                memberCardRepository.save(card);
                resultInfo.setResult(true);
            }
        }
        return resultInfo;
    }

    @Override
    public ResultInfo payFee(MemberCard card, String bankId, int money) {

        ResultInfo resultInfo = new ResultInfo(true);

        List<BankCard> bankCardList = bankCardRepository.findByCardId(bankId);
        if(bankCardList==null||bankCardList.size()==0){
            resultInfo.setResult(false);
            resultInfo.setInfo("银行卡号不存在");
        }

        int origin = card.getBalance();
        card.setBalance(money+origin);

        if(!isInQualification(card).isSuccess() && card.getBalance() >= 1000){
            card.setActivated(true);
        }

        return resultInfo;
    }


    @Override
    public ResultInfo isInQualification(MemberCard card) {
        ResultInfo resultInfo = new ResultInfo(false);

        if(card.getBalance()>=1000){
            resultInfo.setResult(true);
        }
        else {
            if(card.getActivateDate().isBefore(LocalDate.now().minusYears(1))){
                resultInfo.setInfo("有效期已过，账户余额小于1000.请充值激活");
            }
            else {
                resultInfo.setResult(true);
            }
        }
        return resultInfo;
    }

    @Override
    public ResultInfo stopQualification(String memberId) {
        return null;
    }

    @Override
    public ResultInfo translateCredit(String cardId, int credit) {
        return null;
    }

    @Override
    public ResultInfo checkRoom(String memberId, String roomId, LocalDate beginDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Order bookRoom(String memberId, String roomId, LocalDate beginDate, LocalDate endDate) {
        return null;
    }

    @Override
    public ResultInfo cancelRoom(String orderId) {
        return null;
    }
}
