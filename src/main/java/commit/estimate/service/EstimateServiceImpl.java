package commit.estimate.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.estimate.dao.EstimateDAO;

@Service("estimateService")
public class EstimateServiceImpl implements EstimateService {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "estimateDAO")
	private EstimateDAO estimateDAO;

	// 견적을 위한 parts 리스트
	@Override
	public List<Map<String, Object>> selectPsList(Map<String, Object> map) throws Exception {
		return estimateDAO.selectPsList(map);
	}

}