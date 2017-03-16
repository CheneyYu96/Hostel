package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Manager;
import edu.nju.hostel.service.ManagerService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

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

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
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



}
