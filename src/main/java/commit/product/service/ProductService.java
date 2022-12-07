package commit.product.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ProductService {

	List<Map<String, Object>> getProList(Map<String, Object> map)throws Exception;
	
	Map<String, Object> selectProDetail(Map<String, Object> map) throws Exception;
	
	//Map<String, Object> getProInfo(Map<String, Object> map)throws Exception;
	
	//List<Map<String, Object>> getProInfo(Map<String, Object> map)throws Exception;

	void insertOrder(Map<String, Object> map, HttpServletRequest request)throws Exception;

	int cancelOrder(Map<String, Object> map, HttpServletRequest request);

	List<Map<String, Object>> getProInfo(String[] PRO_IDX, String[] AMOUNT);


}
