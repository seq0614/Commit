package commit.member.controller;

import commit.etc.etc.CommitMap;
import commit.etc.helper.Alert;
import commit.etc.helper.SessionHelper;
import commit.member.service.MemberService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping(value = "/member")
public class MemberController {

    Logger log = Logger.getLogger(this.getClass());
    @Resource(name = "sessionHelper")
    private SessionHelper sessionHelper;

    @Resource(name = "memberService")
    private MemberService memberService;

    @Resource(name = "alert")
    private Alert alert;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입폼
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public ModelAndView joinForm(CommitMap commitMap, HttpServletRequest request) throws Exception {

        ModelAndView mv = new ModelAndView();
        String checkRequest = memberService.loginCheck(commitMap.getMap(), request);

        if (checkRequest.equals("correct-request")) {
            mv.setViewName("/member/joinForm");
            return mv;
        }
        // 잘못된 요청이라면
        else {
            mv.addObject("msg", "이미 가입된 회원입니다.");
            mv.addObject("path", "/main");
            mv.setViewName("/alert/alert");
            return mv;
        }

    }

    // 회원가입 처리
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String joinSuccess(CommitMap commitMap, HttpServletRequest request) throws Exception {

        memberService.join(commitMap.getMap(), request, bCryptPasswordEncoder);
        String path = "redirect:/member/login";
        if(commitMap.get("OAUTH") !=null){
            path = "redirect:/";
        }

        return path;

    }

    @RequestMapping(value = "/confirm/id")
    @ResponseBody
    public int confirmId(@RequestBody Map<String, Object> commitMap) throws Exception {

        // 중복된 아이디가 없으면 0 있다면 1
        int checkId = memberService.confirmId(commitMap);

        return checkId;

    }

    @RequestMapping(value = "/confirm/email")
    @ResponseBody
    public int confirmEmail(@RequestBody Map<String, Object> commitMap) throws Exception {

        // 중복된 이메일이 없으면 0 있다면 1
        int checkEmail = memberService.confirmEmail(commitMap);

        return checkEmail;
    }


    @RequestMapping(value = "/findID", method = RequestMethod.GET)
    public String findIdForm() throws Exception {
        return "/member/findID";
    }


    @RequestMapping(value = "/findID", method = RequestMethod.POST)
    public ModelAndView findId(CommitMap commitMap) throws Exception {
        ModelAndView mv = new ModelAndView();
        int checkId = memberService.checkID(commitMap.getMap());

        if (checkId != 0) {
            String findID = memberService.findID(commitMap.getMap());
            mv.addObject("MEM_ID", findID);
            mv.setViewName("/member/successFindID");
        } else {
            mv.addObject("msg", "해당 정보를 가진 회원이 없습니다.");
            mv.addObject("path", "/member/findID");
            mv.setViewName("/alert/alert");
        }
        return mv;
    }


    @RequestMapping(value = "/findPW", method = RequestMethod.GET)
    public String findPWForm() throws Exception {
        return "/member/findPW";
    }


    @RequestMapping(value = "/findPW", method = RequestMethod.POST)
    public ModelAndView findPW(CommitMap commitMap) throws Exception {
        ModelAndView mv = new ModelAndView();
        int checkPW = memberService.checkPW(commitMap.getMap());

        if (checkPW != 0) {
            memberService.sendEmail(commitMap.getMap(), bCryptPasswordEncoder);
            mv.addObject("msg", "임시 비밀번호가 이메일로 전송되었습니다.");
            mv.setViewName("/alert/windowClose");//팝업창 닫음
        } else {
            mv.addObject("msg", "해당 정보를 가진 회원이 없습니다.");
            mv.addObject("path", "/member/findPW");
            mv.setViewName("/alert/alert");
        }
        return mv;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginForm(CommitMap commitMap, HttpServletRequest request) throws Exception {

        ModelAndView mv = new ModelAndView();
        String checkRequest = memberService.loginCheck(commitMap.getMap(), request);

        if (checkRequest.equals("correct-request")) {
            mv.setViewName("/member/loginForm");
            return mv;
        } else {
            mv.addObject("msg", "로그인 상태입니다.");
            mv.addObject("path", "/main");
            mv.setViewName("/alert/alert");
            return mv;
        }

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(CommitMap commitMap, HttpServletRequest request) throws Exception {

        // my서비스에서 checkMember(회원정보 수정할때) 같이 사용하려고 request도 넣어줌
        Map<String, Object> loginMap = memberService.checkMember(commitMap.getMap(), request);
        HttpSession session = request.getSession();

        ModelAndView mv = new ModelAndView();

        if (loginMap == null) {
            mv.addObject("msg", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            mv.addObject("path", "/member/login");
            mv.setViewName("/alert/alert");
            return mv;
        }

        if (loginMap.get("OAUTH") != null){
            mv.addObject("msg", "간편로그인을 이용해주세요");
            mv.addObject("path", "/member/login");
            mv.setViewName("/alert/alert");
            return mv;
        }

        String inputPassword = commitMap.get("MEM_PW").toString();
        String savePassword = loginMap.get("MEM_PW").toString();

        boolean checkPassword = bCryptPasswordEncoder.matches(inputPassword, savePassword);

        if (checkPassword) {
            session.setAttribute("MEM_ID", loginMap.get("MEM_ID"));
            session.setAttribute("MEM_NAME", loginMap.get("MEM_NAME"));//마이페이지에서 -님 이름 뽑아내기 위함.
            if (loginMap.get("ADMIN").equals("Y")) {//관리자라면
                session.setAttribute("admin", loginMap.get("ADMIN"));
                mv.setViewName("redirect:/admin/main");
            } else {// 일반 회원인 경우
                mv.setViewName("redirect:/main");
            }
        } else {// 비밀번호 불일치
            mv.addObject("msg", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            mv.addObject("path", "/member/login");
            mv.setViewName("/alert/alert");
        }
        return mv;

    }

    @RequestMapping(value = "/logout")
    public void logout(CommitMap commitMap, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Map<String, Object> map = sessionHelper.make(commitMap.getMap(), request);

        if (map.get("MEM_ID") != null) {
            sessionHelper.remove(request);
            alert.make(response, "로그아웃 되었습니다.", "/main");
        } else {// 로그인이 안되어있는데 로그아웃을 시도하는 경우
            alert.make(response, "로그인이 필요합니다.", "/member/login");
        }

    }

    @GetMapping("/auth/kakao/callback")
    public ModelAndView kakaoCallback(String code, HttpServletRequest request) throws Exception {

        ModelAndView mv = new ModelAndView();
        Map<String, Object> kakaoMap = memberService.kakaoCallback(code, request, bCryptPasswordEncoder);
        int result = Integer.parseInt(kakaoMap.get("result").toString());
        if (result == 0) {
            mv.addObject("kakaoMap", kakaoMap);
            mv.setViewName("/member/joinForm");
        }else {
            mv.setViewName("redirect:/");
        }

        return mv;
    }
}
