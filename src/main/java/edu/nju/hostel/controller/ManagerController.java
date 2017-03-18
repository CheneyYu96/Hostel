package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Manager;
import edu.nju.hostel.entity.Room;
import edu.nju.hostel.service.HotelService;
import edu.nju.hostel.service.ManagerService;
import edu.nju.hostel.utility.DateUtil;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.ResultInfo;
import edu.nju.hostel.utility.StatisticType;
import edu.nju.hostel.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author yuminchen
 * @date 2017/3/2
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/manager")
public class ManagerController {
    private final String MANAGER = "manager/";

    private final ManagerService managerService;
    private final HotelService hotelService;

    @Autowired
    public ManagerController(ManagerService managerService, HotelService hotelService) {
        this.managerService = managerService;
        this.hotelService = hotelService;
    }

    @RequestMapping("/login")
    public String login(HttpSession session, Model model){
        if(session.getAttribute("managerId")!=null) {
            session.removeAttribute("managerId");
        }
        model.addAttribute("name","经理");
        return "login";
    }

    @RequestMapping(value = "/home")
    public String home(HttpSession session, String id, String password, Model model){
        Manager manager;
        if(session.getAttribute("managerId")==null){
            if(id==null || password==null){
                return login(session,model);
            }

            manager = managerService.verifyManager(FormatHelper.String2Id(id),password);
            if(manager == null){
                model.addAttribute("success", false);
                return login(session,model);
            }
            session.setAttribute("managerId",manager.getId());
        }
        else {
            manager = managerService.findManager((Integer)session.getAttribute("managerId"));
        }

        model.addAttribute("manager", manager);
        model.addAttribute("page", "home");
        model.addAttribute("managerId", FormatHelper.Id2String(manager.getId()));
        return MANAGER+"home";
    }

    @RequestMapping(value = "/changepwd")
    public String changePassword(HttpSession session,  Model model, String originPassword, String password){
        ResultInfo info = managerService.modifyPassword((Integer) session.getAttribute("managerId"),originPassword,password);
        session.removeAttribute("hotelId");
        model.addAttribute("result", info);
        return login(session,model);
    }

    @RequestMapping(value = "/approve")
    public String approve(HttpSession session,  Model model){
        if(session.getAttribute("managerId")==null){
            return login(session,model);
        }
        model.addAttribute("page","approve");
        return MANAGER + "approve";
    }

    @RequestMapping(value = "/statistic")
    public String statistic(HttpSession session,  Model model){
        if(session.getAttribute("managerId")==null){
            return login(session,model);
        }
        model.addAttribute("page","statistic");
        return MANAGER + "statistic";
    }

    @RequestMapping("/getApprove")
    @ResponseBody
    public List<ApproveVO> getApprove(){
        return managerService.getApprove();
    }

    @RequestMapping("/getRoomInApprove")
    @ResponseBody
    public Room getRoomInApprove(int approveId){
        return managerService.getRoomInApprove(approveId);
    }

    @RequestMapping("/getHotelInfoInApprove")
    @ResponseBody
    public Hotel getHotelInfoInApprove(int approveId){
        return managerService.getHotelInfoInApprove(approveId);
    }

    @RequestMapping("/getHotelRoomInApprove")
    @ResponseBody
    public List<Room> getHotelRoomInApprove(int hotelId){
        return managerService.getHotelRoomInApprove(hotelId);
    }

    @RequestMapping("/approveItem")
    @ResponseBody
    public ResultInfo approveItem(int approveId){
        return managerService.approveItem(approveId);
    }

    @RequestMapping("/getPay")
    @ResponseBody
    public List<PayVO> getPay(){
        return managerService.getPay();
    }

    @RequestMapping("/payItem")
    @ResponseBody
    public ResultInfo payItem(int payId){
        return managerService.payItem(payId);
    }

    @RequestMapping("/getPayWithMember")
    @ResponseBody
    public List<PayWithMember> getPayWithMember(String hotelId){
        return managerService.getPayWithMember(FormatHelper.String2Id(hotelId));
    }

    @RequestMapping("/getHotelStatistic")
    @ResponseBody
    public List<HotelStatistic> getHotelStatistic(String begin, String end){
        return managerService.getHotelStatistic(DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getAllMemberBookLine")
    @ResponseBody
    public List<LiveIn> getAllMemberBookLine(StatisticType method, String begin, String end){
        return managerService.getAllMemberBookLine(method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getAllMemberBookPie")
    @ResponseBody
    public RoomTypePie getAllMemberBookPie(StatisticType method, String begin, String end){
        return managerService.getAllMemberBookPie(method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getAllMemberInLine")
    @ResponseBody
    public List<LiveIn> getAllMemberInLine(StatisticType method, String begin, String end){
        return managerService.getAllMemberInLine(method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getAllMemberInPie")
    @ResponseBody
    public RoomTypePie getAllMemberInPie(StatisticType method, String begin, String end){
        return managerService.getAllMemberInPie(method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getHostelFinance")
    @ResponseBody
    public List<HostelFinance> getHostelFinance(StatisticType method, String begin, String end){
        return managerService.getHostelFinance(method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getHotelInLine")
    @ResponseBody
    public List<LiveIn> getHotelInLine(String hotelId, StatisticType method, String begin, String end){
        return hotelService.getInLine(FormatHelper.String2Id(hotelId),method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping("/getHotelInPie")
    @ResponseBody
    public RoomTypePie getHotelInPie(String hotelId, StatisticType method, String begin, String end){
        return hotelService.getInPie(FormatHelper.String2Id(hotelId),method,DateUtil.parse(begin),DateUtil.parse(end));
    }



}
