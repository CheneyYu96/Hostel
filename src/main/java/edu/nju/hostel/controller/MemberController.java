package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;
    private final String MEMBER = "member/";

    private Member member = new Member();
    private MemberCard card;

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
        model.addAttribute("member",member);
        return MEMBER+"register";
    }

    @RequestMapping(value = "/register")
    public String register(Model model){

        card = memberService.register(member.getName(),member.getPassword(),member.getPhoneNumber());
        model.addAttribute("id",card.getId());
        return MEMBER+"activate";
    }

    @RequestMapping(value = "/activate")
    public String activate(String bank, int money, Model model){
        ResultInfo resultInfo = memberService.activate(card,bank,money);
        model.addAttribute("result",resultInfo);
        return MEMBER+"result";
    }

}
