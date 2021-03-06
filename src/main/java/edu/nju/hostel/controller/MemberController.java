package edu.nju.hostel.controller;

import edu.nju.hostel.entity.Hotel;
import edu.nju.hostel.entity.Member;
import edu.nju.hostel.entity.MemberCard;
import edu.nju.hostel.service.MemberService;
import edu.nju.hostel.utility.*;
import edu.nju.hostel.vo.*;
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
    public String login(HttpSession session, Model model){
         if(session.getAttribute("cardId")!=null) {
             session.removeAttribute("cardId");
         }
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
            Member member = memberService.findMember(cardId);
            model.addAttribute("member", member);
            model.addAttribute("card", member.getCard());
            model.addAttribute("page", "home");
            model.addAttribute("level", MemberLevel.getLevel(member.getCard().getConsumeAmount()));
            model.addAttribute("discount", MemberLevel.getDiscount(member.getCard().getConsumeAmount()));
            model.addAttribute("cardId", FormatHelper.Id2String(cardId));
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
                return login(session,model);
            }

            member = memberService.verifyMember(username,password);
            if(member == null){
                model.addAttribute("success", false);
                return login(session,model);
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
        return login(session,model);
    }

    @RequestMapping(value = "/book")
    public String book(HttpSession session, Model model){
        if(session.getAttribute("cardId")==null){
            return login(session,model);
        }
        model.addAttribute("page","book");
        return MEMBER + "book";
    }


    @RequestMapping(value = "/statistic")
    public String statistic(HttpSession session, Model model){
        if(session.getAttribute("cardId")==null){
            return login(session,model);
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

    @RequestMapping(value = "/getHotel")
    @ResponseBody
    public List<HotelVO> getHotel(){
        return memberService.getHotel();
    }

    @RequestMapping(value = "/makeOrder")
    @ResponseBody
    public OrderVO makeOrder(@SessionAttribute int cardId, int hotelId, String roomNumber, RoomType type, String begin, String end, int pay){
        return memberService.makeOrder(cardId,hotelId,roomNumber,type, DateUtil.parse(begin),DateUtil.parse(end),pay);
    }

    @RequestMapping(value = "/cancelOrder")
    @ResponseBody
    public ResultInfo cancelOrder(int orderId){
        return memberService.cancelOrder(orderId);
    }

    @RequestMapping(value = "/countPay")
    @ResponseBody
    public RoomPrize countPay(@SessionAttribute int cardId, int hotelId, RoomType type, String begin, String end){
        return memberService.countPay(cardId,hotelId,type,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping(value = "/getBookLine")
    @ResponseBody
    public List<LiveIn> getBookLine(@SessionAttribute int cardId, StatisticType method, String begin, String end){
        return memberService.getBookLine(cardId,method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping(value = "/getBookPie")
    @ResponseBody
    public RoomTypePie getBookPie(@SessionAttribute int cardId, StatisticType method,  String begin, String end){
        return memberService.getBookPie(cardId,method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping(value = "/getInLine")
    @ResponseBody
    public List<LiveIn> getInLine(@SessionAttribute int cardId, StatisticType method,  String begin, String end){
        return memberService.getInLine(cardId,method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping(value = "/getInPie")
    @ResponseBody
    public RoomTypePie getInPie(@SessionAttribute int cardId, StatisticType method,  String begin, String end){
        return memberService.getInPie(cardId,method,DateUtil.parse(begin),DateUtil.parse(end));
    }

    @RequestMapping(value = "/getCost")
    @ResponseBody
    public List<Translator> getCost(@SessionAttribute int cardId, StatisticType method,  String begin, String end){
        return memberService.getFinance(cardId,method,DateUtil.parse(begin),DateUtil.parse(end));
    }

}
