package commit.product.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.etc.etc.CommitMap;
import commit.member.service.MemberService;
import commit.my.service.MyService;
import commit.product.service.ProductService;

@Controller
@RequestMapping(value = "/pro")
public class ProductController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "memberService")
	private MemberService memberService;

	@Resource(name = "myService")
	private MyService myService;


	// 페이지 접속후 컴퓨터 or 부품을 선택해서 페이지만 들어옴 -> PRO_GROUP만 선택
	@RequestMapping(value = "/list")	
	public ModelAndView list(CommitMap commitMap) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		if (commitMap.get("PRO_GROUP").equals("PC")) {
			mv.setViewName("/pro/pcList");
		} else {
			mv.setViewName("/pro/psList");
		}

		return mv;

	}
	
	@PostMapping(value = "/list/data")	
	@ResponseBody
	public List<Map<String, Object>> listData(@RequestBody Map<String, Object> map) throws Exception {
		List<Map<String, Object>> proList = productService.getProList(map);
		return proList;
	}

	@GetMapping(value = "/detail")
	public ModelAndView detail(@RequestParam String PRO_IDX) throws Exception {
		ModelAndView mv = new ModelAndView("/pro/detail");
		mv.addObject("PRO_IDX", PRO_IDX);
		return mv;
	}
	

	@PostMapping(value = "/detail")
	@ResponseBody
	public Map<String, Object> detailData(@RequestBody Map<String, Object> commitMap) throws Exception {
		Map<String, Object> map = productService.selectProDetail(commitMap);
		return map;
	}

	//주문폼
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public ModelAndView orderForm(
								@RequestParam("PRO_IDX")String[] PRO_IDX,
								@RequestParam("AMOUNT")String[] AMOUNT,
								CommitMap commmitMap, 
								HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		List<Map<String, Object>> proInfoList = productService.getProInfo(PRO_IDX, AMOUNT);
		
		if(proInfoList.isEmpty()) {
			
			mv.addObject("msg", "해당 상품의 재고가 부족합니다.");
			mv.addObject("num", -1);
			mv.setViewName("/alert/historyBack");
			return mv;
		}
		Map<String,Object> myInfo = memberService.checkMember(commmitMap.getMap(), request);
		
		
		List<Map<String,Object>> couponList = myService.myCouponList(commmitMap.getMap(),request);
		
		
		mv.addObject("proInfoList",proInfoList);
		mv.addObject("myInfo",myInfo);
		mv.addObject("couponList",couponList);
		mv.setViewName("/pro/order/form");
		return mv;
		
	}
	
	//실제 주문
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	@ResponseBody
	public void order(@RequestBody Map<String,Object> commitMap, HttpServletRequest request) throws Exception {

		
		productService.insertOrder(commitMap, request);

	}

	@RequestMapping(value = "/order/cancel")
	@ResponseBody
	public int cancel(@RequestBody Map<String,Object> commitMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int check = productService.cancelOrder(commitMap, request);
		//1이면 취소 성공 -1이면 취소불가
		return check;
	}

}