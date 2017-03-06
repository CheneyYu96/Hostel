package edu.nju.hostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author yuminchen
 * @date 2017/2/20
 * @version V1.0
 */
@Controller
public class ThymeController {
    @RequestMapping("/hello")
    public String hello(@RequestParam(value="name", required=false, defaultValue="HostelWorld") String name, Model model){
        model.addAttribute("hello",name);
        return "hello";
    }
}
