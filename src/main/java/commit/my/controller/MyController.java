package commit.my.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.etc.etc.CommitMap;
import commit.member.service.MemberService;
import commit.my.service.MyService;

@Controller
@RequestMapping(value = "/mypage")
public class MyController {

    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "myService")
    private MyService myService;

    @Resource(name = "memberService")
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/main")
    public String main() {
        return "/mypage/main";
    }


    @RequestMapping(value = "/myInfo")
    public ModelAndView myInfo(CommitMap commitMap, HttpServletRequest request, HttpServletResponse response) throws Exception {


        Map<String, Object> checkInfo = memberService.checkMember(commitMap.getMap(), request);
        ModelAndView mv = new ModelAndView();

        String inputPassword = commitMap.get("MEM_PW").toString();
        String savePassword = checkInfo.get("MEM_PW").toString();

        boolean checkPassword = bCryptPasswordEncoder.matches(inputPassword, savePassword);

        if (checkPassword) {
            mv.addObject("myInfo", checkInfo);
            mv.setViewName("/mypage/modifyInfo");
        } else {
            mv.addObject("msg", "비밀번호가 일치하지 않습니다.");
            mv.addObject("num", -1);
            mv.setViewName("/alert/historyBack");
        }
        return mv;


    }

    @RequestMapping(value = "/myInfo/update")
    public String updateMyInfo(CommitMap commitMap, HttpServletRequest request, Model model) throws Exception {

        myService.updateMyInfo(commitMap.getMap(), request, bCryptPasswordEncoder);


        model.addAttribute("msg", "회원정보 수정이 완료되었습니다.");
        model.addAttribute("path", "/mypage/main");
        //정보수정하고 마이페이지 메인으로
        return "/alert/alert";
    }

    @RequestMapping(value = "/myInfo/del")
    @ResponseBody
    public void deleteMyInfo(HttpServletRequest request) throws Exception {

        CommitMap commitMap = new CommitMap();
        myService.deleteMyInfo(commitMap.getMap(), request);

    }

    @RequestMapping(value = "/myOrder")
    public ModelAndView myOrder(CommitMap commitMap, HttpServletRequest request) throws Exception {

        List<Map<String, Object>> orderList = myService.myOrderList(commitMap.getMap(), request);
        ModelAndView mv = new ModelAndView();
        mv.addObject("orderList", orderList);
        mv.setViewName("/mypage/myOrderList");
        return mv;

    }


    @RequestMapping(value = "/myOrderDatail")
    public ModelAndView myOrderDetail(CommitMap commitMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = myService.orderDetail(commitMap.getMap(), request, response);

        ModelAndView mv = new ModelAndView();
        mv.addObject("order", map.get("order"));
        mv.addObject("orderInfo", map.get("orderInfo"));
        mv.setViewName("/mypage/myOrderDetail");
        return mv;


    }


    @RequestMapping(value = "/myQna")
    public ModelAndView myQna(CommitMap commitMap, HttpServletRequest request) throws Exception {

        List<Map<String, Object>> qnaList = myService.myQnaList(commitMap.getMap(), request);
        ModelAndView mv = new ModelAndView();
        mv.addObject("qnaList", qnaList);
        mv.setViewName("/mypage/myQna");
        return mv;
    }

    //qna detail에서 사진 출력
    @RequestMapping(value = "/myQnaDetail")
    public ModelAndView myQnaDetail(CommitMap commitMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> qnaDetail = myService.myQnaDetail(commitMap.getMap(), request, response);
        ModelAndView mv = new ModelAndView();
        mv.addObject("qnaDetail", qnaDetail);
        mv.setViewName("/mypage/myQnaDetail");
        return mv;

    }


    @RequestMapping(value = "/myCoupon")
    public ModelAndView myCoupon(CommitMap commitMap, HttpServletRequest request) throws Exception {


        List<Map<String, Object>> couponList = myService.myCouponList(commitMap.getMap(), request);
        ModelAndView mv = new ModelAndView();
        mv.addObject("couponList", couponList);
        mv.setViewName("/mypage/myCoupon");
        return mv;
    }

}