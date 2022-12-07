package commit.coupon.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.coupon.service.CouponService;
import commit.etc.etc.CommitMap;
import commit.member.service.MemberService;

@Controller
@RequestMapping(value = "/coupon")
public class CouponController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "couponService")
	private CouponService couponService;

	@Resource(name = "memberService")
	private MemberService memberService;

	//회원 비회원 admin 모두 이용 가능
	@RequestMapping(value = "/list")
	public ModelAndView couponList(CommitMap commitMap) throws Exception {

		ModelAndView mv = new ModelAndView();
		List<Map<String, Object>> couponList = couponService.getCouponList();

		mv.addObject("couponList", couponList);
		mv.setViewName("/coupon/member/list");
		return mv;
	}
	
	//admin쿠폰 목록 + 수정 삭제 기능 있음
	@RequestMapping(value = "/admin/list")
	public ModelAndView adminCoupon(CommitMap commitMap) throws Exception {

		ModelAndView mv = new ModelAndView();
		List<Map<String, Object>> couponList = couponService.getCouponList();

		mv.addObject("couponList", couponList);
		mv.setViewName("/coupon/admin/list");
		return mv;
	}

	
	/*
	@RequestMapping(value = "/download")
	public void couponDownload(CommitMap commitMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		couponService.downloadCoupon(commitMap.getMap(), request, response);

		

		// 다운로드 한 후 나의 목록을 다시 보여줌

	}*/
	
	//ajax 사용 -> 쿠폰다운
	@RequestMapping(value = "/download", produces = "application/text; charset=utf-8")
	@ResponseBody
	public String couponDownload(@RequestBody Map<String,Object> commitMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String message = couponService.downloadCoupon(commitMap, request, response);

		return message;

	}

	@RequestMapping(value = "/admin/add", method = RequestMethod.GET)
	public String couponAddForm() {
		return "/coupon/admin/addForm";
	}

	@RequestMapping(value = "/admin/add", method = RequestMethod.POST)
	@ResponseBody
	public void couponAdd(@RequestBody Map<String,Object> commitMap) throws Exception {

		couponService.addCoupon(commitMap);
		
	}

	
	@RequestMapping(value = "/admin/update", method = RequestMethod.GET)
	public ModelAndView couponUpdateForm(CommitMap commitMap) throws Exception {

		Map<String, Object> couponOne = couponService.selectCouponOne(commitMap.getMap());
		ModelAndView mv = new ModelAndView();
		mv.addObject("couponOne", couponOne);
		mv.setViewName("/coupon/admin/updateForm");
		return mv;
	}

	@RequestMapping(value = "/admin/update", method = RequestMethod.POST)
	@ResponseBody
	public void couponUpdate(@RequestBody Map<String,Object> commitMap) throws Exception {

		couponService.updateCoupon(commitMap);
		
	}

	@RequestMapping(value = "/admin/delete") // 마이바티스에 값 형식으로 들어가는거라 어떤 타입으로 받아오든 딱히 상관은 없음.
	@ResponseBody
	public void couponDelete(@RequestBody Map<String,Object> commitMap) throws Exception {

		couponService.deleteCoupon(commitMap);
		
	}

}
