package commit.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.main.dao.MainDAO;

@Service("mainService")
public class MainServiceImpl implements MainService {

	
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="mainDAO")
	private MainDAO mainDAO;

	@Override
	public Map<String, Object> getBestPC() throws Exception {
	
		
		List<Map<String,Object>> gamingPC =  mainDAO.getGamingPC();
		List<Map<String,Object>> officePC = mainDAO.getOfficePC();
		
		Map<String,Object> bestPCList = new HashMap<String, Object>();
		
		bestPCList.put("gamingPC", gamingPC);
		bestPCList.put("officePC", officePC);
		
		
		
		
		return bestPCList;
	}
	
}
