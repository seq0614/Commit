package commit.basket.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.basket.service.BasketService;
import commit.etc.etc.CommitMap;
import commit.member.service.MemberService;

@Controller
@RequestMapping(value = "/basket")
public class BasketController {
	
	Logger log = Logger.getLogger(this.getClass());

	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	
	@Resource(name = "basketService")
	private BasketService basketService;
	
	
	@RequestMapping(value = "/main")
	public ModelAndView basketList(CommitMap commitMap, HttpServletRequest request) throws Exception {
		List<Map<String, Object>> list = basketService.myBasketList(commitMap.getMap(),request);
		ModelAndView mv = new ModelAndView("/basket/main");
		mv.addObject("list",list);
		return mv;
	}
	
	
	//장바구니에 담은 상품 개수 수정
	@RequestMapping(value = "/update")
	@ResponseBody
	public void updateBasket(@RequestBody Map<String,Object> commitMap,HttpServletRequest request) throws Exception{
		
		basketService.updateBasket(commitMap, request);
		
		
	}
	
	//장바구니 선택 삭제
	@RequestMapping(value = "/delete")
	@ResponseBody
	public void deleteBasket(@RequestBody Map<String,Object> commitMap,HttpServletRequest request) throws Exception{
		
		basketService.deleteBasket(commitMap, request);
		
	}
	
	//장바구니 전체 비우기
	@RequestMapping(value = "/clear")
	@ResponseBody
	public void clearBasket(CommitMap commitMap, HttpServletRequest request) throws Exception{
		
		basketService.clearBasket(commitMap.getMap(), request);
		
	}
	
	//장바구니 추가
	@ResponseBody
	@RequestMapping(value="/add")//상품 번호와 수량을 받아와서 session에 있는 아이디에 장바구니 목록 추가
	public void addBasket(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		basketService.insertBasket(map,request,response);
		
	}
	
	
}
