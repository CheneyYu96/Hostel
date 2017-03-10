package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Room;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 *
 * @author yuminchen
 * @date 2017/3/2
 * @version V1.0
 */
@Controller
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService hotelService;
    private final String HOTEL = "hotel/";

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("name","客栈");
        return "login";
    }

    @RequestMapping("/home")
    public String home(HttpSession session, String id, String password, Model model){
        Hotel hotel = null;

        if(session.getAttribute("hotelId")==null){
            if(id == null || password == null){
                return login(model);
            }
            hotel = hotelService.verifyHotel(id,password);
            if(hotel==null){
                model.addAttribute("success", false);
                return login(model);
            }
            session.setAttribute("hotelId",hotel.getId());
        }
        else {
            hotel = hotelService.getInfo((Integer)session.getAttribute("hotelId"));
        }

        model.addAttribute("hotel", hotel);
        model.addAttribute("hotelId", FormatHelper.Id2String(hotel.getId()));
        return HOTEL+"home";
    }

    @RequestMapping("/re")
    public String toRegister(Model model){
        model.addAttribute("name","客栈");
        return "register";
    }

    @RequestMapping("/register")
    public String register(HttpSession session, Model model, String hotelname, String password, String address){
        if(hotelname == null|| password == null || address==null){
            return login(model);
        }
        Hotel hotel = hotelService.register(hotelname,password,address);
        session.setAttribute("hotelId",hotel.getId());
        model.addAttribute("hotelId", FormatHelper.Id2String(hotel.getId()));
        model.addAttribute("hotel", hotel);
        return HOTEL + "home";
    }

    @RequestMapping("/stay")
    public String stay(Model model){
        return HOTEL+"stay";
    }

    @RequestMapping("/statistic")
    public String statistic(Model model){
        return HOTEL+"statistic";
    }


    @RequestMapping("/addRoom")
    @ResponseBody
    public ResultInfo addRoom(@SessionAttribute int hotelId, String type, String roomNumber, int prize){
        return hotelService.addRoom(hotelId, RoomType.valueOf(type), roomNumber, prize);
    }

    @RequestMapping("/modifyRoom")
    @ResponseBody
    public ResultInfo modifyRoom(int roomId, RoomType type, String roomNumber, int prize){
        return hotelService.modifyRoom(new Room(roomId,type,roomNumber,prize));
    }

    @RequestMapping("/delRoom")
    @ResponseBody
    public ResultInfo delRoom(int roomId){
        return hotelService.delRoom(roomId);
    }

    @RequestMapping("/getRooms")
    @ResponseBody
    public List<Room> getRooms(@SessionAttribute int hotelId){
        return hotelService.getRooms(hotelId);
    }

    @RequestMapping("/editInfo")
    @ResponseBody
    public ResultInfo editInfo(@SessionAttribute int hotelId, String name, String address){
        return hotelService.modifyInfo(new Hotel(hotelId, name,address));
    }


}
