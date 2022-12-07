package commit.qna.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.etc.etc.CommitMap;
import commit.qna.service.QnaService;

@Controller
@RequestMapping(value="/qna")
public class QnaController {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="qnaService")
	private QnaService qnaService;
	
	@RequestMapping(value="/write", method = RequestMethod.GET)
	public ModelAndView qnaWriteForm(CommitMap commitMap) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("PRO_IDX", commitMap.get("PRO_IDX"));
		mv.addObject("ROOT_IDX", commitMap.get("ROOT_IDX"));
		mv.setViewName("/qna/writeForm");
		return mv;
	}
	
	

	@RequestMapping(value="/write", method = RequestMethod.POST)
	public ModelAndView qnaWrite(CommitMap commitMap, HttpServletRequest request) throws Exception {
		
		qnaService.insertQna(commitMap.getMap(), request);
		ModelAndView mv = new ModelAndView();	
		
		mv.addObject("msg", "게시글이 등록되었습니다.");
		mv.setViewName("/alert/windowClose");
		return mv; 
		
		
	}
	

	
	@RequestMapping(value="/update", method = RequestMethod.POST)
	@ResponseBody
	public void qnaUpdate(@RequestBody Map<String,Object> commitMap) throws Exception {
		
		qnaService.updateQna(commitMap);
		
	}
	
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public void qnaDelete(@RequestBody Map<String,Object> commitMap,HttpServletRequest request) throws Exception {
		
		qnaService.deleteQna(commitMap, request);
	
	}
	
}
