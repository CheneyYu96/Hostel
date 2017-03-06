package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Hotel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;


/**
 *
 * @author yuminchen
 * @date 2017/3/2
 * @version V1.0
 */
@Controller
@RequestMapping("/hotel")
public class HotelController {

    private final String HOTEL = "hotel/";
    Hotel hotel = new Hotel();

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("name","客栈");
        return "login";
    }

    @RequestMapping("/info")
    public String showInfo(){

        return HOTEL+"hotel";
    }

}
