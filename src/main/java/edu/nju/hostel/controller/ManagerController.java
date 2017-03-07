package edu.nju.hostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("name","经理");
        return "login";
    }

    @RequestMapping("/home")
    public String home(Model model){
        return MANAGER + "home";
    }
}
