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

    @Autowired
    public HotelServiceBean(HotelRepository hotelRepository, RoomRepository roomRepository, PlanRepository planRepository, InRecordRepository inRecordRepository, OutRecordRepository outRecordRepository, RecordNameRepository recordNameRepository, OrderRepository orderRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.planRepository = planRepository;
        this.inRecordRepository = inRecordRepository;
        this.outRecordRepository = outRecordRepository;
        this.recordNameRepository = recordNameRepository;
        this.orderRepository = orderRepository;
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
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

    @Override
    public Hotel verifyHotel(String id, String password) {
        Hotel hotel = hotelRepository.findOne(FormatHelper.String2Id(id));
        if(hotel!=null&&password.equals(hotel.getPassword())){
            return hotel;
        }
        return null;
    }

    @Override
    public ResultInfo modifyRoom(Room room) {

        Room room1 = roomRepository.findOne(room.getId());
        room1.setPrize(room.getPrize());
        room1.setRoomNumber(room.getRoomNumber());
        room1.setType(room.getType());

        Room result = roomRepository.save(room1);
        if(result!=null){
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
        room.setAvailable(true);
        room.setPrize(prize);
        room.setRoomNumber(roomNumber);
        room.setType(type);
        room.setStatus(HotelStatus.待审批);
        hotel.setStatus(HotelStatus.待审批);

        hotelRepository.save(hotel);
        roomRepository.save(room);
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
            Room room = roomRepository.findByHotelAndNumber(hotel,roomNumber);
            room.setAvailable(false);
            roomRepository.save(room);
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

            Room room = roomRepository.findByHotelAndNumber(inRecord.getHotelId(),inRecord.getRoomNumber());
            room.setAvailable(true);
            roomRepository.save(room);

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
                false,
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
            case 日统计: return getLineD(translators,begin,end);
            case 周统计: return getLineW(translators,begin,end);
            case 月统计: return getLineM(translators,begin,end);
        }
        return null;
    }

    private List<LiveIn> getLineW(List<Translator> translators, LocalDate begin, LocalDate end ){
        List<LiveIn> liveInList = new ArrayList<>();
        for(;begin.isBefore(end);begin = begin.plusWeeks(1)){
            LocalDate finalBegin = begin;
            LiveIn reduce = translators
                    .stream()
                    .filter(translator -> DateUtil.inNextWeek(finalBegin, translator.begin))
                    .map(translator -> new LiveIn(1, translator.pay, finalBegin))
                    .reduce(new LiveIn(0,0,finalBegin), (in1, in2) ->
                    {
                        in2.amount = in1.amount + in2.amount;
                        in2.money = in1.money + in2.money;
                        return in2;
                    });
            liveInList.add(reduce);
        }
        return liveInList;
    }

    private List<LiveIn> getLineD(List<Translator> translators, LocalDate begin, LocalDate end ){
        List<LiveIn> liveInList = new ArrayList<>();
        for(;begin.isBefore(end);begin = begin.plusDays(1)){
            LocalDate finalBegin = begin;
            LiveIn reduce = translators
                    .stream()
                    .filter(translator -> translator.begin.isEqual(finalBegin))
                    .map(translator -> new LiveIn(1, translator.pay, finalBegin))
                    .reduce(new LiveIn(0,0,finalBegin), (in1, in2) ->
                    {
                        in2.amount = in1.amount + in2.amount;
                        in2.money = in1.money + in2.money;
                        return in2;
                    });
            liveInList.add(reduce);
        }
        return liveInList;
    }

    private List<LiveIn> getLineM(List<Translator> translators, LocalDate begin, LocalDate end ){
        List<LiveIn> liveInList = new ArrayList<>();
        for(;begin.isBefore(end);begin = begin.plusMonths(1)){
            LocalDate finalBegin = begin;
            LiveIn reduce = translators
                    .stream()
                    .filter(translator -> DateUtil.inNextMonth(finalBegin, translator.begin))
                    .map(translator -> new LiveIn(1,translator.pay, finalBegin))
                    .reduce(new LiveIn(0,0,finalBegin), (in1, in2) ->
                    {
                        in2.amount = in1.amount + in2.amount;
                        in2.money = in1.money + in2.money;
                        return in2;
                    });
            liveInList.add(reduce);
        }
        return liveInList;
    }

    @Override
    public RoomTypePie getBookPie(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Order> orderList = orderRepository.findByHotelId(hotelId);
        RoomTypePie pie = new RoomTypePie();

        for(Order order : orderList){
            switch (order.getType()){
                case 单床房:pie.singleRoom++; break;
                case 双床房:pie.doubleRoom++; break;
                case 大床房:pie.bigRoom++; break;
                case 套间:pie.wholeRoom++; break;
            }
        }
        return pie;
    }


    @Override
    public List<LiveIn> getInLine(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<Translator> translators = inRecordRepository
                .findByHotelId(hotelId)
                .stream()
                .map( inRecord -> new Translator(inRecord.getPay(),inRecord.getBegin()))
                .collect(Collectors.toList());

        switch (method){
            case 日统计: return getLineD(translators,begin,end);
            case 周统计: return getLineW(translators,begin,end);
            case 月统计: return getLineM(translators,begin,end);
        }
        return null;
    }

    @Override
    public RoomTypePie getInPie(int hotelId, StatisticType method, LocalDate begin, LocalDate end) {
        List<InRecord> inRecords = inRecordRepository.findByHotelId(hotelId);
        RoomTypePie pie = new RoomTypePie();

        for(InRecord inRecord : inRecords){
            switch (inRecord.getType()){
                case 单床房:pie.singleRoom++; break;
                case 双床房:pie.doubleRoom++; break;
                case 大床房:pie.bigRoom++; break;
                case 套间:pie.wholeRoom++; break;
            }
        }
        return pie;
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


}
