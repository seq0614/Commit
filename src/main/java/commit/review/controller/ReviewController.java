package commit.review.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.etc.etc.CommitMap;
import commit.review.service.ReviewService;

@Controller
@RequestMapping(value="/review")
public class ReviewController {

	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="reviewSerive")
	private ReviewService reviewService;
	
	
	@RequestMapping(value="/myReview")
	public ModelAndView myReview(CommitMap commitMap, HttpServletRequest request) throws Exception {
		
		List<Map<String,Object>> reviewList = reviewService.selectMyReview(commitMap.getMap(),request);
		ModelAndView mv = new ModelAndView(); 
		mv.addObject("reviewList", reviewList);
		mv.setViewName("/review/myReview");
		return mv;
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public ModelAndView ReviewForm(CommitMap commitMap, HttpServletRequest request) throws Exception {
		
		//리뷰 작성이 가능한지 체크
		String path = reviewService.checkValid(commitMap.getMap(), request);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("ORDER_IDX", commitMap.get("ORDER_IDX"));
		mv.addObject("PRO_IDX", commitMap.get("PRO_IDX"));
		mv.setViewName(path);
		return mv;
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public ModelAndView writeReview(CommitMap commitMap, HttpServletRequest request, Model model) throws Exception {
		reviewService.insertReview(commitMap.getMap(), request);
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", "리뷰 작성이 완료되었습니다.");
		mv.addObject("num", -2);
		mv.setViewName("/alert/historyBack");
		
		return mv;
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public String reviewDelete(@RequestBody Map<String, Object> commitMap) throws Exception {
		reviewService.deleteReview(commitMap);
		return "ok";
	}
	
	
}
