package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.*;
import edu.nju.hostel.entity.*;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.HotelStatus;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.RoomType;
import edu.nju.hostel.vo.InRecordWithName;
import edu.nju.hostel.vo.OutRecordWithInfo;
import edu.nju.hostel.vo.RoomInPlan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    public HotelServiceBean(HotelRepository hotelRepository, RoomRepository roomRepository, PlanRepository planRepository, InRecordRepository inRecordRepository, OutRecordRepository outRecordRepository, RecordNameRepository recordNameRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.planRepository = planRepository;
        this.inRecordRepository = inRecordRepository;
        this.outRecordRepository = outRecordRepository;
        this.recordNameRepository = recordNameRepository;
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
    public ResultInfo addInRecord(List<InRecordName> nameList, int hotel, String roomNumber, RoomType type, LocalDate begin, LocalDate end, int pay, boolean payByCard, int orderId) {
        InRecord inRecord = inRecordRepository.save(new InRecord(hotel,roomNumber,type,begin,end,pay,payByCard,orderId));

        if(inRecord!=null) {
            nameList.forEach(inRecordName ->
                    {
                        inRecordName.setInRecordId(inRecord.getId());
                        recordNameRepository.save(inRecordName);
                    }
            );
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
        OutRecord record = outRecordRepository.save(new OutRecord(hotelId,inRecordId, date));
        if(record!=null){
            return new ResultInfo(true);
        }
        return new ResultInfo(false);
    }

}
