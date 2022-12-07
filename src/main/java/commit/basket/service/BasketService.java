package commit.basket.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BasketService {

	List<Map<String, Object>> myBasketList(Map<String, Object> map, HttpServletRequest request) throws Exception;

	void updateBasket(Map<String, Object> map, HttpServletRequest request) throws Exception;

	void deleteBasket(Map<String, Object> map,HttpServletRequest request) throws Exception;

	void clearBasket(Map<String, Object> map, HttpServletRequest request)throws Exception;

	void insertBasket(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)throws Exception;


}
