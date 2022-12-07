package commit.main.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import commit.main.service.MainService;

@Controller
public class MainController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "mainService")
	private MainService mainService;

	@GetMapping(value = "/main")
	public ModelAndView main() throws Exception {

		Map<String, Object> bestPCList = mainService.getBestPC();
		ModelAndView mv = new ModelAndView();
		mv.addObject("gamingPC", bestPCList.get("gamingPC"));
		mv.addObject("officePC", bestPCList.get("officePC"));
		mv.setViewName("/main/main");

		return mv;
	}
}
