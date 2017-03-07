package edu.nju.hostel.service;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.utility.ResultInfo;

import java.time.LocalDate;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface HotelService {

    Hotel register(String name, String password, String address);

    ResultInfo modifyInfo(Hotel hotel);

    Hotel verifyHotel(String id, String password);

    ResultInfo raisePlan(int roomId, LocalDate beginDate, LocalDate endDate, int discount);
}
