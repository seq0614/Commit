package commit.coupon.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import commit.coupon.dao.CouponDAO;
import commit.etc.helper.SessionHelper;

@Service("couponService")
public class CouponServiceImpl implements CouponService {
	
	Logger log = Logger.getLogger(this.getClass());


	
	@Resource(name="sessionHelper")
	private SessionHelper sessionHelper;
	
	
	@Resource(name="couponDAO")
	private CouponDAO couponDAO;
	
	

	@Override
	public List<Map<String, Object>> getCouponList() throws Exception {
		
		return couponDAO.getCouponList();
	
	}

	@Override
	public String downloadCoupon(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		map = sessionHelper.make(map, request);
		
		//이미 다운받고 사용한 쿠폰도 다시 다운 못받게 할거임
		int check = couponDAO.checkCoupon(map);
		if(check != 0) {
			
			String message = "이미 다운로드 받은 쿠폰입니다.";
			return message;
		}
		
		couponDAO.downloadCoupon(map);
		String message = "쿠폰 발급 완료!";
		return message;
	
	}

	@Override
	public void addCoupon(Map<String, Object> map) throws Exception {
		couponDAO.addCoupon(map);
		
	}

	@Override
	public Map<String, Object> selectCouponOne(Map<String, Object> map) throws Exception {
		
		return couponDAO.selectCouponOne(map);
	}

	@Override
	public void updateCoupon(Map<String, Object> map) throws Exception {
		
		couponDAO.updateCoupon(map);
	}

	@Override
	public void deleteCoupon(Map<String, Object> map) throws Exception {
		couponDAO.deleteCoupon(map);
		
	}

}
