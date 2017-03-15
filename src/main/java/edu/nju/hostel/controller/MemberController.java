package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.*;
import edu.nju.hostel.vo.BalanceAndCredit;
import edu.nju.hostel.vo.OrderVO;
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

        session.setAttribute("cardId",card.getId());
        return MEMBER+"activate";
    }

    @RequestMapping(value = "/activate")
    public String activate(@SessionAttribute int cardId, String bank, Integer money, Model model){
        ResultInfo resultInfo = memberService.activate(cardId,bank,money);
        if(resultInfo.isSuccess()){
            return MEMBER + "home";
        }
        else {
            model.addAttribute("first", false);
            model.addAttribute("id", FormatHelper.Id2String(cardId));
            model.addAttribute("result", resultInfo);

            return MEMBER + "activate";
        }
    }

    @RequestMapping(value = "/home")
    public String home(HttpSession session, String username, String password, Model model){
        Member member;
        if(session.getAttribute("cardId")==null){
            if(username==null || password==null){
                return login(model);
            }

            member = memberService.verifyMember(username,password);
            if(member == null){
                model.addAttribute("success", false);
                return login(model);
            }
            session.setAttribute("cardId",member.getId());
        }
        else {
            member = memberService.findMember((Integer)session.getAttribute("cardId"));
        }

        ResultInfo resultInfo = memberService.isInQualification(member.getCard().getId());
        if(!resultInfo.isSuccess()){
            model.addAttribute("tip",resultInfo.getInfo());
        }

        model.addAttribute("member", member);
        model.addAttribute("card", member.getCard());
        model.addAttribute("page", "home");
        model.addAttribute("level", MemberLevel.getLevel(member.getCard().getConsumeAmount()));
        model.addAttribute("discount", MemberLevel.getDiscount(member.getCard().getConsumeAmount()));
        model.addAttribute("cardId", FormatHelper.Id2String(member.getId()));
        return MEMBER + "home";
    }

    @RequestMapping(value = "/changepwd")
    public String changePassword(HttpSession session,  Model model, String originPassword, String password){
        ResultInfo info = memberService.modifyPassword((Integer) session.getAttribute("cardId"),originPassword,password);
        session.removeAttribute("cardId");
        model.addAttribute("result", info);
        return login(model);
    }

    @RequestMapping(value = "/book")
    public String book(HttpSession session, Model model){
        if(session.getAttribute("cardId")==null){
            return login(model);
        }
        model.addAttribute("page","book");
        return MEMBER + "book";
    }


    @RequestMapping(value = "/statistic")
    public String statistic(HttpSession session, Model model){
        if(session.getAttribute("cardId")==null){
            return login(model);
        }
        model.addAttribute("page","statistic");
        return MEMBER + "statistic";
    }

    @RequestMapping(value = "/editInfo")
    @ResponseBody
    public ResultInfo editInfo(@SessionAttribute int cardId, String name, String phone, String bankCard){
        return memberService.editInfo(cardId,name,phone,bankCard);
    }

    @RequestMapping(value = "/translateCredit")
    @ResponseBody
    public BalanceAndCredit translateCredit(@SessionAttribute int cardId, int credit){
        return memberService.translateCredit(cardId,credit);
    }

    @RequestMapping(value = "/stopQlf")
    @ResponseBody
    public ResultInfo stopQlf(@SessionAttribute int cardId){
        return memberService.stopQualification(cardId);
    }

    @RequestMapping(value = "/payFee")
    @ResponseBody
    public BalanceAndCredit payFee(@SessionAttribute int cardId, String bank, int money){
        return memberService.payFee(cardId,bank,money);
    }

    @RequestMapping(value = "/getOrder")
    @ResponseBody
    public List<OrderVO> getOrder(@SessionAttribute int cardId){
        return memberService.getOrder(cardId);
    }

    @RequestMapping(value = "/makeOrder")
    @ResponseBody
    public OrderVO makeOrder(@SessionAttribute int cardId, int hotelId, RoomType type, String begin, String end, int pay){
        return memberService.makeOrder(cardId,hotelId,type, DateUtil.parse(begin),DateUtil.parse(end),pay);
    }

    @RequestMapping(value = "/cancelOrder")
    @ResponseBody
    public ResultInfo cancelOrder(int orderId){
        return memberService.cancelOrder(orderId);
    }

}
