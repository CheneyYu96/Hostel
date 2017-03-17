package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.*;
import edu.nju.hostel.entity.*;
import edu.nju.hostel.service.ManagerService;
import edu.nju.hostel.utility.*;
import edu.nju.hostel.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
@Service
public class ManagerServiceBean implements ManagerService{

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ManagerRepository managerRepository;
    private final ApproveItemRepository approveItemRepository;
    private final PayItemRepository payItemRepository;
    private final InRecordRepository inRecordRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RechargeItemRepository rechargeItemRepository;

    @Autowired
    public ManagerServiceBean(HotelRepository hotelRepository,
                              RoomRepository roomRepository,
                              ManagerRepository managerRepository,
                              ApproveItemRepository approveItemRepository,
                              PayItemRepository payItemRepository,
                              InRecordRepository inRecordRepository,
                              OrderRepository orderRepository,
                              MemberRepository memberRepository, RechargeItemRepository rechargeItemRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.managerRepository = managerRepository;
        this.approveItemRepository = approveItemRepository;
        this.payItemRepository = payItemRepository;
        this.inRecordRepository = inRecordRepository;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.rechargeItemRepository = rechargeItemRepository;
    }

    @Override
    public Manager verifyManager(Integer id, String password) {
        Manager manager = managerRepository.findOne(id);
        if(manager!=null&&manager.getPassword().equals(password)){
            return manager;
        }
        return null;
    }

    @Override
    public Manager findManager(Integer managerId) {
        return managerRepository.findOne(managerId);
    }

    @Override
    public ResultInfo modifyPassword(Integer managerId, String originPassword, String password) {
        Manager manager = managerRepository.findOne(managerId);
        if(originPassword.equals(manager.getPassword())){
            manager.setPassword(password);
            managerRepository.save(manager);
            return new ResultInfo(true);
        }
        return new ResultInfo(false,"密码不正确");
    }

    @Override
    public List<ApproveVO> getApprove() {
        return approveItemRepository
                .findAll()
                .stream()
                .filter(approveItem -> !approveItem.getHasApproved())
                .map( approveItem ->
                        {
                            ApproveVO approveVO = new ApproveVO();
                            approveVO.id = approveItem.getId();
                            int hotelId = approveItem.getHotelId();
                            int roomId = approveItem.getRoomId();
                            if(hotelId>0&&roomId>0){
                                approveVO.type = ApproveType.开店申请;
                                approveVO.hotelName = hotelRepository
                                        .findOne(hotelId)
                                        .getName();
                            }
                            else if(hotelId>0&&roomId<=0){
                                approveVO.type = ApproveType.信息修改;
                                approveVO.hotelName = hotelRepository
                                        .findOne(hotelId)
                                        .getName();
                            }
                            else {
                                approveVO.type = ApproveType.房间修改;
                                approveVO.hotelName = hotelRepository
                                        .findOne(roomRepository
                                                .findOne(roomId)
                                                .getHotelId())
                                        .getName();
                            }
                            return approveVO;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public Room getRoomInApprove(int id) {
        return roomRepository
                .findOne(approveItemRepository
                        .findOne(id)
                        .getRoomId());
    }

    @Override
    public Hotel getHotelInfoInApprove(int id) {
        return hotelRepository
                .findOne(approveItemRepository
                        .findOne(id)
                        .getHotelId());
    }

    @Override
    public List<Room> getHotelRoomInApprove(int hotelId) {
        return roomRepository
                .findByHotelId(hotelId)
                .stream()
                .filter( room -> room.getStatus()== HotelStatus.待审批)
                .collect(Collectors.toList());
    }


    @Override
    public ResultInfo approveItem(Integer approveId) {

        ApproveItem approveItem = approveItemRepository.findOne(approveId);
        approveItem.setHasApproved(true);

        if(approveItem.getRoomId()>0){
            Room room = roomRepository.findOne(approveItem.getRoomId());
            room.setStatus(HotelStatus.已批准);
            roomRepository.save(room);
        }
        approveItemRepository.save(approveItem);
        return new ResultInfo(true);
    }

    @Override
    public List<PayVO> getPay() {
        return hotelRepository
                .findAll()
                .stream()
                .filter( hotel ->
                        {
                            List<PayItem> payItems = payItemRepository
                                    .findByHotelId(hotel.getId())
                                    .stream()
                                    .filter( payItem -> !payItem.getHasPay())
                                    .collect(Collectors.toList());
                            return payItems != null && payItems.size() > 0;
                        }
                )
                .map( hotel ->
                        {
                            PayVO payVO = new PayVO();
                            payVO.hotelId = FormatHelper.Id2String(hotel.getId());
                            payVO.name = hotel.getName();

                            payVO.payIndex = payItemRepository
                                    .findByHotelId(hotel.getId())
                                    .stream()
                                    .filter( payItem -> !payItem.getHasPay())
                                    .map(PayItem::getId)
                                    .collect(Collectors.toList());
                            return payVO;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public ResultInfo payItem(int payId) {
        PayItem payItem = payItemRepository.findOne(payId);
        payItem.setHasPay(true);

        payItemRepository.save(payItem);
        return new ResultInfo(true);
    }

    @Override
    public List<PayWithMember> getPayWithMember(int hotelId) {
        return payItemRepository
                .findByHotelId(hotelId)
                .stream()
                .filter( payItem -> !payItem.getHasPay())
                .map( payItem ->
                        {
                            PayWithMember payWithMember = new PayWithMember(payItem.getId());
                            if(payItem.getInRecordId()>0){

                                payWithMember.isOrder = false;
                                InRecord inRecord = inRecordRepository.findOne(payItem.getInRecordId());
                                if(inRecord.getCardId()>0){
                                    payWithMember.memberId = FormatHelper.Id2String(inRecord.getCardId());
                                    payWithMember.name = memberRepository.findOne(inRecord.getCardId()).getName();
                                }
                                else {
                                    return new PayWithMember(0);
                                }
                                payWithMember.pay = inRecord.getPay();
                            }
                            else {
                                payWithMember.isOrder = true;
                                Order order = orderRepository.findOne(payItem.getOrderId());
                                payWithMember.memberId = FormatHelper.Id2String(order.getMemberId());
                                payWithMember.name = memberRepository.findOne(order.getMemberId()).getName();
                                payWithMember.pay = order.getPay();

                            }
                            return payWithMember;
                        }
                )
                .filter(payWithMember -> payWithMember.id>0)
                .collect(Collectors.toList());

    }

    @Override
    public List<HotelStatistic> getHotelStatistic(LocalDate begin, LocalDate end) {
        return hotelRepository
                .findAll()
                .stream()
                .map( hotel ->
                        {
                            HotelStatistic hotelStatistic = new HotelStatistic();
                            hotelStatistic.hotelId = FormatHelper.Id2String(hotel.getId());
                            hotelStatistic.hotelName = hotel.getName();

                            List<Integer> payList = inRecordRepository
                                    .findByHotelId(hotel.getId())
                                    .stream()
                                    .filter( inRecord -> inRecord.getBegin().isAfter(begin)&&inRecord.getEnd().isBefore(end))
                                    .map(InRecord::getPay)
                                    .collect(Collectors.toList());

                            hotelStatistic.number = payList.size();
                            hotelStatistic.money = payList
                                    .stream()
                                    .reduce(0,Integer::sum);

                            return hotelStatistic;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<LiveIn> getAllMemberBookLine(StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = orderRepository
                .findAll()
                .stream()
                .map( order -> new Translator(order.getPay(),order.getBegin()))
                .collect(Collectors.toList());

        return Calculator.getLineByMethod(method,begin,end,translators);
    }

    @Override
    public RoomTypePie getAllMemberBookPie(StatisticType method, LocalDate begin, LocalDate end) {
        List<Order> orderList = orderRepository
                .findAll()
                .stream()
                .filter( order -> DateUtil.isBetween(begin,end,order.getBegin()))
                .collect(Collectors.toList());
        return Calculator.getBookPie(orderList);
    }

    @Override
    public List<LiveIn> getAllMemberInLine(StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = inRecordRepository
                .findAll()
                .stream()
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());
        return Calculator.getLineByMethod(method,begin,end,translators);
    }

    @Override
    public RoomTypePie getAllMemberInPie(StatisticType method, LocalDate begin, LocalDate end) {
        List<InRecord> inRecords = inRecordRepository
                .findAll()
                .stream()
                .filter( inRecord -> DateUtil.isBetween(begin,end,inRecord.getBegin()))
                .collect(Collectors.toList());
        return Calculator.getInPie(inRecords);
    }

    @Override
    public List<HostelFinance> getHostelFinance(StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> orderTranslators = orderRepository
               .findAll()
               .stream()
               .map( order -> new Translator(order.getPay(),order.getBegin()))
               .collect(Collectors.toList());

        List<Translator> inRecordTranslators = inRecordRepository
                .findAll()
                .stream()
                .filter( inRecord -> inRecord.getOrderId()<=0&&inRecord.getCardId()>0) //avoid count repeatedly
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());

        orderTranslators = Stream
                .of(orderTranslators,inRecordTranslators)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<PayTranslator> payTranslators = payItemRepository
                .findAll()
                .stream()
                .filter( PayItem::getHasPay)
                .map( payItem ->
                        {
                            if(payItem.getInRecordId()>0){
                                InRecord inRecord = inRecordRepository.findOne(payItem.getInRecordId());
                                return new PayTranslator(payItem.getId(),inRecord.getPay(),inRecord.getBegin());
                            }
                            else {
                                Order order = orderRepository.findOne(payItem.getOrderId());
                                return new PayTranslator(payItem.getId(),order.getPay(),order.getBegin());
                            }
                        }
                )
                .collect(Collectors.toList());

        List<HostelFinance> financeList = new ArrayList<>();

        switch (method){
            case 日统计:
                for(;begin.isBefore(end);begin = begin.plusDays(1)){
                    financeList.add(calculateHostelFinance(begin,orderTranslators,payTranslators,LocalDate::isEqual));
                }
                break;

            case 周统计:
                for(;begin.isBefore(end);begin = begin.plusWeeks(1)){
                    financeList.add(calculateHostelFinance(begin,orderTranslators,payTranslators,DateUtil::inNextWeek));
                }
                break;

            case 月统计:
                for(;begin.isBefore(end);begin = begin.plusMonths(1)){
                    financeList.add(calculateHostelFinance(begin,orderTranslators,payTranslators,DateUtil::inNextMonth));
                }
                break;
        }
        return financeList;
    }

    private HostelFinance calculateHostelFinance(LocalDate finalBegin, List<Translator> orderTranslators,List<PayTranslator> payTranslators,JudgeInDate judgeInDate){
        List<RechargeItem> rechargeItems = rechargeItemRepository
                .findAll()
                .stream()
                .filter(rechargeItem -> judgeInDate.isInDate(finalBegin, rechargeItem.getDate()))
                .collect(Collectors.toList());

        HostelFinance hostelFinance = new HostelFinance(finalBegin);
        hostelFinance.rechargeNumber = rechargeItems.size();
        hostelFinance.rechargeAmount = rechargeItems
                .stream()
                .map(RechargeItem::getAmount)
                .reduce(0, Integer::sum);

        hostelFinance.consume = orderTranslators
                .stream()
                .filter(translator -> judgeInDate.isInDate(finalBegin,translator.begin))
                .map(Translator::getPay)
                .reduce(0, Integer::sum);

        hostelFinance.pay = payTranslators
                .stream()
                .filter( payTranslator -> judgeInDate.isInDate(finalBegin,payTranslator.date))
                .map(PayTranslator::getMoney)
                .reduce(0, Integer::sum);

        return hostelFinance;
    }

    interface JudgeInDate{
        boolean isInDate(LocalDate begin, LocalDate testDate);
    }

}
