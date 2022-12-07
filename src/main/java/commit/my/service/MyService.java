package commit.my.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MyService {

	void updateMyInfo(Map<String, Object> map,HttpServletRequest request, BCryptPasswordEncoder bCryptPasswordEncoder)throws Exception;

	void deleteMyInfo(Map<String, Object> map,HttpServletRequest request)throws Exception;

	List<Map<String, Object>> myOrderList(Map<String, Object> map,HttpServletRequest request)throws Exception;
	
	Map<String, Object> orderDetail(Map<String, Object> map,HttpServletRequest request,HttpServletResponse response)throws Exception;
	
	List<Map<String, Object>> myQnaList(Map<String, Object> map,HttpServletRequest request)throws Exception;

	Map<String, Object> myQnaDetail(Map<String, Object> map,HttpServletRequest request, HttpServletResponse response)throws Exception;

	List<Map<String, Object>> myCouponList(Map<String, Object> map,HttpServletRequest request)throws Exception;

	

	
	

	

}
