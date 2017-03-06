package edu.nju.hostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author yuminchen
 * @date 2017/3/2
 * @version V1.0
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(){
        return "home";
    }
}
