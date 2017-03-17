package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.*;
import edu.nju.hostel.entity.*;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.*;
import edu.nju.hostel.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final OrderRepository orderRepository;
    private final HotelRepository hotelRepository;
    private final RoomRecordRepository roomRecordRepository;
    private final RoomRepository roomRepository;
    private final PlanRepository planRepository;
    private final InRecordRepository inRecordRepository;
    private final PayItemRepository payItemRepository;

    @Autowired
    public MemberServiceBean(MemberRepository memberRepository, MemberCardRepository memberCardRepository, BankCardRepository bankCardRepository, OrderRepository orderRepository, HotelRepository hotelRepository, RoomRecordRepository roomRecordRepository, RoomRepository roomRepository, PlanRepository planRepository, InRecordRepository inRecordRepository, PayItemRepository payItemRepository) {
        this.memberRepository = memberRepository;
        this.memberCardRepository = memberCardRepository;
        this.bankCardRepository = bankCardRepository;
        this.orderRepository = orderRepository;
        this.hotelRepository = hotelRepository;
        this.roomRecordRepository = roomRecordRepository;
        this.roomRepository = roomRepository;
        this.planRepository = planRepository;
        this.inRecordRepository = inRecordRepository;
        this.payItemRepository = payItemRepository;
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
    public Member findMember(int memberId) {
        return memberRepository.findOne(memberId);
    }

    @Override
    public MemberCard findCard(int cardId) {
        return memberCardRepository.findOne(cardId);
    }

    @Override
    public ResultInfo editInfo(int id, String name, String phone, String bankCard) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        member.setPhoneNumber(phone);
        member.getCard().setBankCard(bankCard);
        memberRepository.save(member);
        return new ResultInfo(true);
    }

    @Override
    public ResultInfo modifyPassword(int id, String originPass, String newPass) {
        Member member1 = memberRepository.findOne(id);
        if(originPass.equals(member1.getPassword())){
            member1.setPassword(newPass);
            memberRepository.save(member1);
            return new ResultInfo(true);
        }
        return new ResultInfo(false,"密码不正确");
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
    public ResultInfo activate(int cardId, String bankId, int money) {

        ResultInfo resultInfo = new ResultInfo(false);

        List<BankCard> bankCardList = bankCardRepository.findByCardId(bankId);
        if(bankCardList==null||bankCardList.size()==0){
            resultInfo.setInfo("银行卡号不存在");
        }
        else {
            MemberCard card = memberCardRepository.findOne(cardId);

            if(money + card.getBalance() < 1000){
                resultInfo.setInfo("金额小于1000");
                card.setActivated(false);
            }
            else {
                resultInfo.setResult(true);
                card.setActivated(true);
            }
            card.setBalance(money + card.getBalance());
            card.setBankCard(bankId);
            card.setActivateDate(LocalDate.now());
            card.setConsumeAmount(0);
            card.setCredit(0);
            card.setStatus(MemberStatus.已激活);

            memberCardRepository.save(card);
        }
        return resultInfo;
    }

    @Override
    public BalanceAndCredit payFee(int cardId, String bankId, int money) {

        ResultInfo resultInfo = new ResultInfo(true);
        List<BankCard> bankCardList = bankCardRepository.findByCardId(bankId);
        if(bankCardList==null||bankCardList.size()==0){
            resultInfo.setResult(false);
            resultInfo.setInfo("银行卡号不存在");
        }

        MemberCard card;
        if(!isInQualification(cardId).isSuccess()){
             card = memberCardRepository.findOne(cardId);
            if(card.getStatus()==MemberStatus.暂停&&(card.getBalance()+money)>=1000){
                card.setStatus(MemberStatus.已激活);
                card.setActivated(true);

            }
            card.setBalance(card.getBalance()+money);

        }
        else {
            card = memberCardRepository.findOne(cardId);
            card.setBalance(card.getBalance()+money);
        }
        memberCardRepository.save(card);

        BalanceAndCredit result = new BalanceAndCredit(resultInfo);
        result.balance = card.getBalance();
        return result;
    }

    @Override
    public ResultInfo isInQualification(int cardId) {
        MemberCard card = memberCardRepository.findOne(cardId);
        ResultInfo resultInfo = new ResultInfo(false);

        if(card.getBalance()>=1000){
            resultInfo.setResult(true);
        }
        else {
            if(card.getActivateDate().isBefore(LocalDate.now().minusYears(1))){
                if(card.getActivateDate().isBefore(LocalDate.now().minusYears(2))){
                    card.setStatus(MemberStatus.停止);
                    resultInfo.setInfo("会员记录已停止");
                }
                else {
                    card.setStatus(MemberStatus.暂停);
                    resultInfo.setInfo("有效期已过，账户余额小于1000.请充值激活");
                }
                memberCardRepository.save(card);
            }
            else {
                resultInfo.setResult(true);
            }
        }
        return resultInfo;
    }

    @Override
    public ResultInfo stopQualification(int memberId) {
        MemberCard card = memberRepository.findOne(memberId).getCard();
        card.setStatus(MemberStatus.停止);
        memberCardRepository.save(card);
        return new ResultInfo(true);
    }

    @Override
    public BalanceAndCredit translateCredit(int cardId, int credit) {
        if(credit%10!=0){
            return new BalanceAndCredit(new ResultInfo(false,"兑换积分为整十数"));
        }
        MemberCard card = memberCardRepository.findOne(cardId);
        if(card.getCredit()<credit){
            return new BalanceAndCredit(new ResultInfo(false,"积分不足"));
        }
        card.setCredit(card.getCredit()-credit);
        card.setBalance(card.getBalance()+credit/10);
        BalanceAndCredit result =  new BalanceAndCredit(new ResultInfo(true));
        result.balance = card.getBalance();
        result.credit = card.getCredit();
        memberCardRepository.save(card);
        return result;
    }

    @Override
    public List<OrderVO> getOrder(int cardId) {
        return orderRepository
                .findByMemberId(cardId)
                .stream()
                .map( order ->
                        {
                            OrderVO orderVO = new OrderVO();
                            orderVO.hotelName = hotelRepository.findOne(order.getHotelId()).getName();
                            BeanUtils.copyProperties(order,orderVO);
                            return orderVO;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public OrderVO makeOrder(int cardId, int hotelId, String roomNumber, RoomType type, LocalDate beginDate, LocalDate endDate, int pay) {

        Room room = roomRepository.findByHotelAndNumber(hotelId,roomNumber);

        Order order = new Order();
        order.setBegin(beginDate);
        order.setEnd(endDate);
        order.setHotelId(hotelId);
        order.setType(type);
        order.setRoomNumber(room.getRoomNumber());
        order.setMemberId(cardId);
        order.setPay(pay);
        order.setStatus(OrderStatus.预订);

        ResultInfo resultInfo = payByCard(cardId,pay);
        if(!resultInfo.isSuccess()){
            return new OrderVO(resultInfo.getInfo());
        }
        Order result = orderRepository.save(order);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(result,orderVO);
        orderVO.hotelName = hotelRepository.findOne(hotelId).getName();

        RoomRecord roomRecord = new RoomRecord(result.getId(),0,room.getId(),beginDate,endDate);
        roomRecordRepository.save(roomRecord);

        PayItem payItem = new PayItem();
        payItem.setHasPay(false);
        payItem.setOrderId(result.getId());
        payItem.setInRecordId(0);
        payItem.setHotelId(hotelId);

        payItemRepository.save(payItem);

        return orderVO;

    }

    @Override
    public ResultInfo cancelOrder(int orderId) {
        orderRepository.delete(orderId);
        RoomRecord roomRecord = roomRecordRepository.findByOrderId(orderId);
        roomRecordRepository.delete(roomRecord);
        return new ResultInfo(true);
    }

    @Override
    public ResultInfo payByCard(int cardId, int pay) {
        MemberCard card = memberCardRepository.findOne(cardId);
        if(card!=null){
            if(card.getBalance()<pay){
                return new ResultInfo(false,"余额不足");
            }
            card.setBalance(card.getBalance()-pay);
            card.setConsumeAmount(card.getConsumeAmount()+pay);
            card.setCredit(card.getCredit()+pay);
            return new ResultInfo(true);
        }
        else {
            return new ResultInfo(false,"卡号不存在");
        }
    }

    /**
     * todo :filter hotel which is not passed permission
     * @return
     */
    @Override
    public List<HotelVO> getHotel() {
        return hotelRepository
                .findAll()
                .stream()
                .map( hotel ->
                        {
                            HotelVO hotelVO = new HotelVO();
                            BeanUtils.copyProperties(hotel,hotelVO);
                            List<Integer> planIndex = planRepository
                                    .findByHotelId(hotel.getId())
                                    .stream()
                                    .map( o -> o.getId())
                                    .collect(Collectors.toList());
                            hotelVO.plan = planIndex;
                            return hotelVO;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public RoomPrize countPay(int cardId, int hotelId, RoomType type, LocalDate beginDate, LocalDate endDate) {
        int dayLength = DateUtil.endMinusBegin(beginDate,endDate);
        if(dayLength<0){
            return new RoomPrize("日期间隔小于1天");
        }
        List<Room> roomList = roomRepository
                .findByHotelAndType(hotelId,type)
                .stream()
                .filter( room ->
                        {
                            List<RoomRecord> roomRecordList = roomRecordRepository
                                    .findByRoomId(room.getId())
                                    .stream()
                                    .filter( roomRecord -> DateUtil.isTimeConflict(roomRecord.getBegin(),roomRecord.getEnd(),beginDate,endDate))
                                    .collect(Collectors.toList());
                            if(roomRecordList!=null&&roomRecordList.size()>0){
                                return false;
                            }
                            return true;
                        }
                )
                .collect(Collectors.toList());

        if(roomList==null||roomList.size()==0){
            return new RoomPrize("该时间段的此类房间已满");
        }
        Room room = roomList.get(0);
        RoomPrize roomPrize = new RoomPrize(room.getRoomNumber(),room.getPrize()*dayLength,room.getType());
        List<Plan> planList = planRepository
                .findByHotelId(hotelId)
                .stream()
                .filter( plan1 -> plan1.getType()==room.getType() )
                .filter( plan1 -> plan1.getBeginDate().isBefore(beginDate)&&plan1.getEndDate().isAfter(endDate))
                .collect(Collectors.toList());
        if(planList!=null&&planList.size()>0){
            roomPrize.planDiscount = planList.get(0).getDiscount();
        }
        MemberCard card = findCard(cardId);
        roomPrize.memberDiscount = MemberLevel.getDiscount(card.getConsumeAmount());
        roomPrize.nowPrize = roomPrize.originPrize * roomPrize.memberDiscount * roomPrize.planDiscount/ 10000;

        return roomPrize;
    }

    @Override
    public List<LiveIn> getBookLine(int cardId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = orderRepository
                .findByMemberId(cardId)
                .stream()
                .map( order -> new Translator(order.getPay(),order.getBegin()))
                .collect(Collectors.toList());

        switch (method){
            case 周统计: return Calculator.getLineW(translators,begin,end);
            case 月统计: return Calculator.getLineM(translators,begin,end);
            default: return null;
        }
    }

    @Override
    public RoomTypePie getBookPie(int cardId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Order> orderList = orderRepository
                .findByMemberId(cardId)
                .stream()
                .filter( order -> order.getBegin().isBefore(end)&&order.getBegin().isAfter(begin))
                .collect(Collectors.toList());
        return Calculator.getBookPie(orderList);
    }

    @Override
    public List<LiveIn> getInLine(int cardId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = inRecordRepository
                .findByCardId(cardId)
                .stream()
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());

        switch (method){
            case 周统计: return Calculator.getLineW(translators,begin,end);
            case 月统计: return Calculator.getLineM(translators,begin,end);
            default: return null;
        }
    }

    @Override
    public RoomTypePie getInPie(int cardId, StatisticType method, LocalDate begin, LocalDate end) {
        List<InRecord> inRecords = inRecordRepository
                .findByCardId(cardId)
                .stream()
                .filter( inRecord -> inRecord.getBegin().isAfter(begin)&&inRecord.getBegin().isBefore(end))
                .collect(Collectors.toList());
        return Calculator.getInPie(inRecords);
    }

    @Override
    public List<Translator> getFinance(int cardId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> orderTranslators = orderRepository
                .findByMemberId(cardId)
                .stream()
                .map( order -> new Translator(order.getPay(),order.getBegin()))
                .collect(Collectors.toList());
        List<Translator> inRecordTranslators = inRecordRepository
                .findByCardId(cardId)
                .stream()
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());

        orderTranslators = Stream
                .of(orderTranslators,inRecordTranslators)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Translator> translators = new ArrayList<>();

        switch (method){
            case 周统计:
                for(;begin.isBefore(end);begin = begin.plusWeeks(1)){
                    LocalDate finalBegin = begin;
                    Translator translator = orderTranslators
                            .stream()
                            .filter(translator1 -> DateUtil.inNextWeek(finalBegin, translator1.begin))
                            .reduce(new Translator(0,finalBegin), (t1, t2) ->
                            {
                                t2.pay = t2.pay + t1.pay;
                                return t2;
                            });
                    translators.add(translator);
                }
                break;

            case 月统计:
                for(;begin.isBefore(end);begin = begin.plusMonths(1)){
                    LocalDate finalBegin = begin;
                    Translator translator = orderTranslators
                            .stream()
                            .filter(translator1 -> DateUtil.inNextMonth(finalBegin, translator1.begin))
                            .reduce(new Translator(0,finalBegin), (t1, t2) ->
                            {
                                t2.pay = t2.pay + t1.pay;
                                return t2;
                            });
                    translators.add(translator);
                }
                break;
        }

        return translators;    }
}
