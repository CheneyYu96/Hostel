package edu.nju.hostel.controller;

import com.sun.org.apache.regexp.internal.RE;
import edu.nju.hostel.entity.*;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.*;
import edu.nju.hostel.vo.InRecordWithName;
import edu.nju.hostel.vo.OutRecordWithInfo;
import edu.nju.hostel.vo.RoomInPlan;
import edu.nju.hostel.vo.RoomPrize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


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
    private final MemberService memberService;
    private final String HOTEL = "hotel/";

    @Autowired
    public HotelController(HotelService hotelService, MemberService memberService) {
        this.hotelService = hotelService;
        this.memberService = memberService;
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("name","客栈");
        return "login";
    }

    @RequestMapping("/home")
    public String home(HttpSession session, String id, String password, Model model){
        Hotel hotel;

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

    @RequestMapping("/plan")
    public String plan(HttpSession session, Model model){
        if(session.getAttribute("hotelId")==null){
            return login(model);
        }
        return HOTEL+"plan";
    }

    @RequestMapping("/stay")
    public String stay(HttpSession session, Model model){
        if(session.getAttribute("hotelId")==null){
            return login(model);
        }
        return HOTEL+"stay";
    }

    @RequestMapping("/statistic")
    public String statistic(HttpSession session, Model model){
        if(session.getAttribute("hotelId")==null){
            return login(model);
        }
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

    @RequestMapping("/getPlan")
    @ResponseBody
    public List<Plan> getPlan(@SessionAttribute int hotelId){
        return hotelService.getPlan(hotelId);
    }

    @RequestMapping("/addPlan")
    @ResponseBody
    public ResultInfo addPlan(@SessionAttribute int hotelId, String name, String des, RoomType type, String begin, String end, int discount){
        return hotelService.raisePlan(hotelId,name,des,type, DateUtil.parse(begin),DateUtil.parse(end),discount);
    }

    @RequestMapping("/delPlan")
    @ResponseBody
    public ResultInfo delPlan(int planId){
        return hotelService.delPlan(planId);
    }


    @RequestMapping(value = "/getRelateRoom")
    @ResponseBody
    public List<RoomInPlan> getRelateRoom(@SessionAttribute int hotelId, RoomType type, int discount){
        return hotelService.getRelateRooms(hotelId,type,discount);
    }

    @RequestMapping(value = "/getInRecord")
    @ResponseBody
    public List<InRecordWithName> getInRecord(@SessionAttribute int hotelId){
        return hotelService.getInRecord(hotelId);
    }

    @RequestMapping(value = "/addInRecord")
    @ResponseBody
    public ResultInfo addInRecord(@SessionAttribute int hotelId, Map<String, String> nameMap,String roomNumber, RoomType type, String begin, String end, int pay, boolean payByCard,String cardId){
        int card = FormatHelper.String2Id(cardId);
        ResultInfo resultInfo = new ResultInfo(true);
        if(card>0){
            resultInfo = memberService.payByCard(card,pay);
        }
        if(!resultInfo.isSuccess()){
            return resultInfo;
        }
        return hotelService.addInRecord(guestInfoMap2List(nameMap),hotelId,roomNumber,type,DateUtil.parse(begin),DateUtil.parse(end),pay,payByCard,0,card);
    }

    @RequestMapping(value = "/addRecordByOrder")
    @ResponseBody
    public ResultInfo addRecordByOrder(@SessionAttribute int hotelId, Map<String, String> nameMap, int orderId){

        return hotelService.addRecordByOrder(guestInfoMap2List(nameMap),hotelId,orderId);
    }

    @RequestMapping(value = "/getOutRecord")
    @ResponseBody
    public List<OutRecordWithInfo> getOutRecord(@SessionAttribute int hotelId){
        return hotelService.getOutRecord(hotelId);
    }

    @RequestMapping(value = "/addOutRecord")
    @ResponseBody
    public ResultInfo addOutRecord(@SessionAttribute int hotelId, int inRecordId, String date){
        return hotelService.addOutRecord(hotelId,inRecordId,DateUtil.parse(date));
    }

    @RequestMapping(value = "/getPrize")
    @ResponseBody
    public RoomPrize getPrize(@SessionAttribute int hotelId, String roomNumber, String begin, String end, String cardId){

        RoomPrize roomPrize = hotelService.getRoomPrizeInPlan(hotelId,roomNumber,DateUtil.parse(begin),DateUtil.parse(end));

        roomPrize.nowPrize = roomPrize.originPrize*roomPrize.planDiscount/100;

        if(cardId!=null && cardId.length()==7) {
            MemberCard card = memberService.findCard(FormatHelper.String2Id(cardId));
            if (card == null) {
                roomPrize.errorInfo = "卡号不存在";
            } else {
                roomPrize.memberDiscount = MemberLevel.getDiscount(card.getConsumeAmount());
                roomPrize.nowPrize = roomPrize.originPrize * roomPrize.memberDiscount * roomPrize.planDiscount/ 10000;
            }
        }
        return roomPrize;
    }

    /**
     *  if the value of key is 1, the key is member id,
     *  else the key is name
     * @param map
     * @return
     */
    private List<InRecordName> guestInfoMap2List(Map<String, String> map){
        return map
                .keySet()
                .stream()
                .map( o ->
                {
                    if("1".equals(map.get(o))){
                        int memberId = Integer.parseInt(o);
                        return new InRecordName(memberService.findMember(memberId).getName(),memberId);
                    }
                    return new InRecordName(o,0);
                })
                .collect(Collectors.toList());
    }

}
