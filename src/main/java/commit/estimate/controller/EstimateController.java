package commit.estimate.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import commit.estimate.service.EstimateService;
import commit.etc.etc.CommitMap;

@Controller
@RequestMapping(value = "/estimate")
public class EstimateController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "estimateService")
	private EstimateService estimateService;

	@RequestMapping(value = "/main")
	public String main(CommitMap commitMap) throws Exception {
		return "/estimate/main";
	}

	@RequestMapping(value = "/data")
	@ResponseBody
	public List<Map<String, Object>> data(CommitMap commitMap) throws Exception {

		List<Map<String, Object>> list = estimateService.selectPsList(commitMap.getMap());
		return list;
	}

}
