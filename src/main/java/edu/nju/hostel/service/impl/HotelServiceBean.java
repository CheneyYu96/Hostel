package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.HotelRepository;
import edu.nju.hostel.dao.RoomRepository;
import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Room;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.HotelStatus;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.RoomType;
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
public class HotelServiceBean implements HotelService{

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public HotelServiceBean(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
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
    public ResultInfo raisePlan(int hotelId, RoomType type, LocalDate beginDate, LocalDate endDate, int discount) {
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

}
