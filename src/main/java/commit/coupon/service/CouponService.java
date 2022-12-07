package commit.coupon.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CouponService {

	List<Map<String, Object>> getCouponList()throws Exception;

	String downloadCoupon(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)throws Exception;

	void addCoupon(Map<String, Object> map)throws Exception;

	Map<String, Object> selectCouponOne(Map<String, Object> map)throws Exception;

	void updateCoupon(Map<String, Object> map)throws Exception;

	void deleteCoupon(Map<String, Object> map)throws Exception;

	





}
