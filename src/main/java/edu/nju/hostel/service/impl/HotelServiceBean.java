package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.*;
import edu.nju.hostel.entity.*;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.utility.*;
import edu.nju.hostel.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class HotelServiceBean implements HotelService{

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final PlanRepository planRepository;
    private final InRecordRepository inRecordRepository;
    private final OutRecordRepository outRecordRepository;
    private final RecordNameRepository recordNameRepository;
    private final OrderRepository orderRepository;
    private final RoomRecordRepository roomRecordRepository;
    private final ApproveItemRepository approveItemRepository;
    private final PayItemRepository payItemRepository;

    @Autowired
    public HotelServiceBean(
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            PlanRepository planRepository,
            InRecordRepository inRecordRepository,
            OutRecordRepository outRecordRepository,
            RecordNameRepository recordNameRepository,
            OrderRepository orderRepository,
            RoomRecordRepository roomRecordRepository,
            ApproveItemRepository approveItemRepository,
            PayItemRepository payItemRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.planRepository = planRepository;
        this.inRecordRepository = inRecordRepository;
        this.outRecordRepository = outRecordRepository;
        this.recordNameRepository = recordNameRepository;
        this.orderRepository = orderRepository;
        this.roomRecordRepository = roomRecordRepository;
        this.approveItemRepository = approveItemRepository;
        this.payItemRepository = payItemRepository;
    }

    @Override
    public Hotel register(String name, String password, String address) {
        if(name == null||password == null){
            return null;
        }
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setPassword(password);
        hotel.setAddress(address);
        hotel.setStatus(HotelStatus.未申请);
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getInfo(int hotelId) {
        return hotelRepository.findOne(hotelId);
    }

    @Override
    public ResultInfo modifyInfo(Hotel hotel) {
        Hotel hotel1 = hotelRepository.findOne(hotel.getId());
        hotel1.setName(hotel.getName());
        hotel1.setAddress(hotel.getAddress());
        hotel1.setStatus(HotelStatus.待审批);

        Hotel result = hotelRepository.save(hotel1);
        if(result!=null){
            ApproveItem approveItem = new ApproveItem(result.getId(),0,false);
            approveItemRepository.save(approveItem);
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

    @Override
    public Hotel verifyHotel(String id, String password) {
        Hotel hotel = hotelRepository.findOne(FormatHelper.String2Id(id));

        if(hotel!=null&&password.equals(hotel.getPassword())){
            if(hotel.getStatus()==HotelStatus.待审批) {
                List<ApproveItem> approveItems = approveItemRepository
                        .findByHotelId(hotel.getId())
                        .stream()
                        .filter(approveItem -> !approveItem.getHasApproved())
                        .collect(Collectors.toList());

                if (approveItems == null || approveItems.size() == 0) {
                    hotel.setStatus(HotelStatus.已批准);
                }
            }
            Hotel result = hotelRepository.save(hotel);
            return result;
        }
        return null;
    }

    @Override
    public ResultInfo modifyRoom(Room room) {

        Room room1 = roomRepository.findOne(room.getId());
        room1.setPrize(room.getPrize());
        room1.setRoomNumber(room.getRoomNumber());
        room1.setType(room.getType());
        room1.setStatus(HotelStatus.待审批);

        Room result = roomRepository.save(room1);
        if(result!=null){
            ApproveItem approveItem = new ApproveItem(0,result.getId(),false);
            approveItemRepository.save(approveItem);
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

    @Override
    @Transactional
    public ResultInfo addRoom(int hotelId, RoomType type, String roomNumber, int prize) {

        Room room = new Room();
        ResultInfo resultInfo = new ResultInfo(true);
        Hotel hotel = hotelRepository.findOne(hotelId);
        if(roomRepository.findByHotelAndNumber(hotelId,roomNumber)!=null){
            resultInfo.setResult(false);
            resultInfo.setInfo("房间号已存在");
            return resultInfo;
        }

        room.setHotelId(hotelId);
        room.setPrize(prize);
        room.setRoomNumber(roomNumber);
        room.setType(type);
        room.setStatus(HotelStatus.待审批);
        Room room1 = roomRepository.save(room);
        ApproveItem approveItem = new ApproveItem(0,room1.getId(),false);

        if(hotel.getStatus()==HotelStatus.未申请){
            approveItem.setHotelId(hotel.getId());
        }
        approveItemRepository.save(approveItem);
        hotel.setStatus(HotelStatus.待审批);
        hotelRepository.save(hotel);

        return resultInfo;
    }

    @Override
    public ResultInfo delRoom(int roomId) {
       roomRepository.delete(roomId);
       return new ResultInfo(true);
    }

    @Override
    public List<Room> getRooms(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Override
    public ResultInfo raisePlan(int hotelId, String name, String des, RoomType type, LocalDate beginDate, LocalDate endDate, int discount) {
        Plan plan = new Plan(name,des,hotelId,beginDate,endDate,type,discount);
        Plan result = planRepository.save(plan);
        if(result!=null){
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

    @Override
    public List<RoomInPlan> getRelateRooms(int hotelId, RoomType type, int discount) {
        List<Room> roomList = getRooms(hotelId);
        return roomList
                .stream()
                .filter(room ->
                        type==room.getType()
                )
                .map(room ->
                        new RoomInPlan(room.getRoomNumber(),type,room.getPrize(),discount+"%",room.getPrize()*discount/100)
                )
                .collect(Collectors.toList());

    }

    @Override
    public RoomPrize getRoomPrizeInPlan(int hotelId, String roomNumber, LocalDate begin, LocalDate end) {
        Room room = roomRepository.findByHotelAndNumber(hotelId,roomNumber);

        if(room==null){
            return new RoomPrize("房间号不存在");
        }
        int dayLength = DateUtil.endMinusBegin(begin,end);
        if(dayLength<0){
            return new RoomPrize("日期间隔小于1天");
        }

        List<RoomRecord> roomRecords = roomRecordRepository
                .findByRoomId(room.getId())
                .stream()
                .filter( roomRecord -> DateUtil.isTimeConflict(roomRecord.getBegin(),roomRecord.getEnd(),begin,end))
                .collect(Collectors.toList());
        if(roomRecords!=null&&roomRecords.size()>0){
            return new RoomPrize("该时间段此房间有客");
        }

        RoomPrize roomPrize = new RoomPrize(roomNumber,room.getPrize()*dayLength,room.getType());
        List<Plan> planList = getPlan(hotelId)
                .stream()
                .filter( plan1 -> plan1.getType()==room.getType() )
                .filter( plan1 -> plan1.getBeginDate().isBefore(begin)&&plan1.getEndDate().isAfter(end))
                .collect(Collectors.toList());

        if(planList!=null&&planList.size()>0){
            roomPrize.planDiscount = planList.get(0).getDiscount();
        }
        return roomPrize;
    }

    @Override
    public List<Plan> getPlan(int hotelId) {
        return planRepository.findByHotelId(hotelId);
    }

    @Override
    public ResultInfo delPlan(int planId) {
        planRepository.delete(planId);
        return new ResultInfo(true);
    }

    @Override
    public List<InRecordWithName> getInRecord(int hotelId) {

        return inRecordRepository
                .findByHotelId(hotelId)
                .stream()
                .map( inRecord ->
                        {
                            List<String> nameList = recordNameRepository
                                    .findByInRecordId(inRecord.getId())
                                    .stream()
                                    .map(o -> o.getName())
                                    .collect(Collectors.toList());
                            InRecordWithName inRecordWithName = new InRecordWithName();
                            BeanUtils.copyProperties(inRecord,inRecordWithName);
                            inRecordWithName.nameList = nameList;

                            return inRecordWithName;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public ResultInfo addInRecord(List<InRecordName> nameList, int hotel, String roomNumber, RoomType type, LocalDate begin, LocalDate end, int pay, boolean payByCard, int orderId,int cardId) {

        InRecord inRecord = inRecordRepository.save(new InRecord(hotel,roomNumber,type,begin,end,pay,payByCard,orderId,cardId));

        if(inRecord!=null) {
            nameList.forEach(inRecordName ->
                    {
                        inRecordName.setInRecordId(inRecord.getId());
                        recordNameRepository.save(inRecordName);
                    }
            );
            if(orderId==0) {
                Room room = roomRepository.findByHotelAndNumber(hotel, roomNumber);
                RoomRecord roomRecord = new RoomRecord(0,inRecord.getId(),room.getId(), begin, end);
                roomRecordRepository.save(roomRecord);
            }
            else {
                Order order = orderRepository.findOne(orderId);
                order.setStatus(OrderStatus.入住);
                orderRepository.save(order);
            }
            return new ResultInfo(true);
        }

        return new ResultInfo(false);
    }

    @Override
    public List<OutRecordWithInfo> getOutRecord(int hotelId) {
        return outRecordRepository
                .findByHotelId(hotelId)
                .stream()
                .map( outRecord ->
                        {
                            InRecord inRecord = inRecordRepository.findOne(outRecord.getInRecordId());
                            OutRecordWithInfo info = new OutRecordWithInfo();
                            BeanUtils.copyProperties(outRecord,info);
                            BeanUtils.copyProperties(inRecord,info,"id");
                            return info;
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public ResultInfo addOutRecord(int hotelId, int inRecordId, LocalDate date) {

        InRecord inRecord = inRecordRepository.findOne(inRecordId);
        if(inRecord==null){
            return new ResultInfo(false,"入住单号不存在");
        }

        OutRecord record = outRecordRepository.save(new OutRecord(hotelId,inRecordId, date));
        if(record!=null){

            if(inRecord.getOrderId()>0){
                Order order = orderRepository.findOne(inRecord.getOrderId());
                order.setStatus(OrderStatus.完成);
                orderRepository.save(order);
            }
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

    @Override
    public ResultInfo addRecordByOrder(List<InRecordName> nameList, int hotelId, int orderId) {

        Order order = orderRepository.findOne(orderId);
        if(order==null){
            return new ResultInfo(false,"该订单号不存在");
        }
        if(hotelId!=order.getHotelId()){
            return new ResultInfo(false,"订单不在本客栈预定");
        }
        return addInRecord(
                nameList,
                order.getHotelId(),
                order.getRoomNumber(),
                order.getType(),
                order.getBegin(),
                order.getEnd(),
                order.getPay(),
                true,
                orderId,
                0
        );
    }


    @Override
    public List<LiveIn> getBookLine(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = orderRepository
                .findByHotelId(hotelId)
                .stream()
                .map( order -> new Translator(order.getPay(),order.getBegin()))
                .collect(Collectors.toList());

        switch (method){
            case 日统计: return Calculator.getLineD(translators,begin,end);
            case 周统计: return Calculator.getLineW(translators,begin,end);
            case 月统计: return Calculator.getLineM(translators,begin,end);
        }
        return null;
    }


    @Override
    public RoomTypePie getBookPie(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Order> orderList = orderRepository
                .findByHotelId(hotelId)
                .stream()
                .filter( order -> order.getBegin().isAfter(begin)&&order.getBegin().isBefore(end))
                .collect(Collectors.toList());

        return Calculator.getBookPie(orderList);

    }


    @Override
    public List<LiveIn> getInLine(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = inRecordRepository
                .findByHotelId(hotelId)
                .stream()
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());

        switch (method){
            case 日统计: return Calculator.getLineD(translators,begin,end);
            case 周统计: return Calculator.getLineW(translators,begin,end);
            case 月统计: return Calculator.getLineM(translators,begin,end);
        }
        return null;
    }

    @Override
    public RoomTypePie getInPie(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<InRecord> inRecords = inRecordRepository
                .findByHotelId(hotelId)
                .stream()
                .filter( inRecord -> inRecord.getBegin().isAfter(begin)&&inRecord.getBegin().isBefore(end))
                .collect(Collectors.toList());
        return Calculator.getInPie(inRecords);
    }

    @Override
    public List<Translator> getFinance(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> orderTranslators = orderRepository
                .findByHotelId(hotelId)
                .stream()
                .map( order -> new Translator(order.getPay(),order.getBegin()))
                .collect(Collectors.toList());
        List<Translator> inRecordTranslators = inRecordRepository
                .findByHotelId(hotelId)
                .stream()
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());

        orderTranslators = Stream
                .of(orderTranslators,inRecordTranslators)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Translator> translators = new ArrayList<>();

        switch (method){
            case 日统计:
                for(;begin.isBefore(end);begin = begin.plusDays(1)){
                    LocalDate finalBegin = begin;
                    Translator translator = orderTranslators
                            .stream()
                            .filter(translator1 -> translator1.begin.isEqual(finalBegin))
                            .reduce(new Translator(0,finalBegin), (t1, t2) ->
                            {
                                t2.pay = t2.pay + t1.pay;
                                return t2;
                            });
                    translators.add(translator);
                }
                break;

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

        return translators;
    }

    @Override
    public ResultInfo modifyPassword(Integer hotelId, String originPassword, String password) {
        Hotel hotel = hotelRepository.findOne(hotelId);
        if(originPassword.equals(hotel.getPassword())){
            hotel.setPassword(password);
            hotelRepository.save(hotel);
            return new ResultInfo(true);
        }
        return new ResultInfo(false,"密码不正确");
    }


}
