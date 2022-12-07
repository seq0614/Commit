package commit.basket.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.admin.dao.AdminDAO;
import commit.basket.dao.BasketDAO;
import commit.etc.helper.SessionHelper;
import commit.my.dao.MyDAO;

@Service("basketService")
public class BasketServiceImpl implements BasketService {
	Logger log = Logger.getLogger(this.getClass());

	@Resource(name="sessionHelper")
	private SessionHelper sessionHelper;
	
	@Resource(name = "basketDAO")
	private BasketDAO basketDAO;

	@Resource(name = "adminDAO")
	private AdminDAO adminDAO;
	
	@Resource(name="myDAO")
	private MyDAO myDAO;
	
	@Override
	public List<Map<String, Object>> myBasketList(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);
		
		return basketDAO.myBasketList(map);
	}
	
	@Override
	public void updateBasket(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);
		basketDAO.updateBasket(map);

	}

	@Override
	public void deleteBasket(Map<String, Object> map,HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);
		basketDAO.deleteBasket(map);
	}


	@Override
	public void clearBasket(Map<String, Object> map, HttpServletRequest request) throws Exception {
		map = sessionHelper.make(map, request);
		basketDAO.clearBasket(map);
	}

	

	@Override
	public void insertBasket(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		map = sessionHelper.make(map, request);
		
		int check = basketDAO.checkBasket(map);
		
		if(check != 0) {
			basketDAO.updateAmount(map);
		}else {
			basketDAO.insertBasket(map);			
		}
		
	}

}
