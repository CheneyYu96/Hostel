package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.FormatHelper;
import edu.nju.hostel.utility.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

/**
 *
 * @author yuminchen
 * @date 2017/3/2
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;
    private final String MEMBER = "member/";


    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("name","会员");
        return "login";
    }

    @RequestMapping("/re")
    public String toRegister(Model model){
        model.addAttribute("name","会员");
        return "register";
    }

    @RequestMapping(value = "/register")
    public String register(HttpSession session, Model model, String username, String password, String phone){
        MemberCard card = memberService.register(username,password,phone);
        model.addAttribute("id", FormatHelper.Id2String(card.getId()));
        model.addAttribute("first", true);
        model.addAttribute("result", new ResultInfo(false));

        session.setAttribute("id",card.getId());
        return MEMBER+"activate";
    }

    @RequestMapping(value = "/activate")
    public String activate(@SessionAttribute int id, String bank, Integer money, Model model){
        ResultInfo resultInfo = memberService.activate(id,bank,money);
        if(resultInfo.isSuccess()){
            return MEMBER + "home";
        }
        else {
            model.addAttribute("first", false);
            model.addAttribute("id", FormatHelper.Id2String(id));
            model.addAttribute("result", resultInfo);

            return MEMBER + "activate";
        }
    }

    @RequestMapping(value = "/home")
    public String home(String username, String password){
        Member member = memberService.verifyMember(username,password);
        if(member!=null) {
            return MEMBER + "home";
        }
        return MEMBER + "login";
    }

}
