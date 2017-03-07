package edu.nju.hostel.service.impl;

import edu.nju.hostel.dao.HotelRepository;
import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public HotelServiceBean(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
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

        return hotelRepository.save(hotel);
    }

    @Override
    public ResultInfo modifyInfo(Hotel hotel) {
        return null;
    }

    @Override
    public Hotel verifyHotel(String id, String password) {
        Hotel hotel = hotelRepository.findOne(FormatHelper.String2Id(id));
        if(password.equals(hotel.getPassword())){
            return hotel;
        }
        return null;
    }

    @Override
    public ResultInfo raisePlan(int roomId, LocalDate beginDate, LocalDate endDate, int discount) {
        return null;
    }
}
