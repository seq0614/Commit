package commit.notice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import commit.etc.etc.CommitMap;
import commit.notice.service.NoticeService;

@Controller
@RequestMapping(value="/notice")
public class NoticeController {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="noticeService")
	private NoticeService noticeService;
	
	@RequestMapping(value="/list")
	public ModelAndView noticeList(CommitMap commitMap) throws Exception{
		ModelAndView mv = new ModelAndView("/notice/list");
		return mv;
	}
	
	@ResponseBody
	@PostMapping("/list/data")
	public List<Map<String, Object>> noticeListData(@RequestBody Map<String, Object> map) throws Exception{
		List<Map<String, Object>> list = noticeService.selectNoticeList(map);
		return list;
	}
	
	@RequestMapping(value="/detail")
	public ModelAndView noticeDetail(CommitMap commitMap, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("/notice/detail");
		Map<String,Object> map = noticeService.selectNoticeDetail(commitMap.getMap(),request);
		
		if(map.isEmpty()) {
			mv.addObject("msg", "삭제된 게시글입니다.");
			mv.addObject("path", "/notice/list");
			mv.setViewName("/alert/alert");
			return mv;
		}else {
			mv.addObject("detail", map.get("detail"));
			mv.addObject("list", map.get("list"));
			mv.addObject("comments", map.get("comments"));
			return mv;
		}
		
	}
	
	@ResponseBody
	@PostMapping("/comment/data/{NOTICE_IDX}/{PAGE}")
	public List<Map<String,Object>> commnetData(@PathVariable String NOTICE_IDX,@PathVariable int PAGE) throws Exception{
		Map<String, Object> map = new HashMap<>();
		
		map.put("NOTICE_IDX", NOTICE_IDX);
		map.put("currentPage", PAGE);
		
		return noticeService.selectCommentData(map);
	}
	
	
	
	@PostMapping("/comment/write")
	public ModelAndView writeComment(CommitMap commitMap, HttpServletRequest request) throws Exception{
		noticeService.insertComment(commitMap.getMap(),request);
		ModelAndView mv = new ModelAndView("redirect:/notice/detail");
		mv.addObject("NOTICE_IDX", commitMap.get("NOTICE_IDX"));
		return mv;
	}
	
	
	@PostMapping("/comment/delete")
	public ModelAndView deleteComment(CommitMap commitMap,HttpServletRequest request) throws Exception{
		noticeService.deleteComment(commitMap.getMap());
		ModelAndView mv = new ModelAndView("redirect:/notice/detail");
		mv.addObject("NOTICE_IDX", commitMap.get("NOTICE_IDX"));
		return mv;
	}
	
	
	@RequestMapping(value = "/admin/write", method=RequestMethod.GET)
	public ModelAndView writeForm(CommitMap commitMap) throws Exception{
		ModelAndView mv = new ModelAndView("/notice/writeForm");
		return mv;
	}
	
	
	
	@RequestMapping(value = "/admin/write", method=RequestMethod.POST)
	public ModelAndView writeNotice(CommitMap commitMap, HttpServletRequest request) throws Exception{
		noticeService.insertNotice(commitMap.getMap(), request);
		ModelAndView mv = new ModelAndView("redirect:/notice/list");
		return mv;
	}
	

	
	@RequestMapping(value="/admin/update", method=RequestMethod.GET)
	public ModelAndView updateNoticeForm(CommitMap commitMap, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("/notice/updateForm");
		Map<String, Object> map = noticeService.selectNoticeDetail(commitMap.getMap(),request);
		mv.addObject("detail", map.get("detail"));
		mv.addObject("list", map.get("list"));
		return mv;
	}
	
	@RequestMapping(value="/admin/update", method=RequestMethod.POST)
	public ModelAndView updateNotice(CommitMap commitMap, HttpServletRequest request) throws Exception{
		noticeService.updateNotice(commitMap.getMap(), request);
		ModelAndView mv = new ModelAndView("redirect:/notice/detail");
		mv.addObject("NOTICE_IDX", commitMap.get("NOTICE_IDX"));
		System.out.println(commitMap.get("NOTICE_IDX"));
		return mv;
	}
	
	@RequestMapping(value="/admin/delete")
	public ModelAndView deleteNotice(@RequestBody Map<String, Object> map) throws Exception{
		noticeService.deleteNotice(map);
		ModelAndView mv = new ModelAndView("redirect:/notice/list");
		return mv;
	}
	
	@RequestMapping(value="/downloadFile")
	public void downloadFile(CommitMap commitMap, HttpServletResponse response, HttpServletRequest request) throws Exception{
		noticeService.selectFileInfo(commitMap.getMap(),response,request);
	}
}
