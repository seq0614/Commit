package commit.coupon.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("couponDAO")
public class CouponDAO extends AbstractDAO{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCouponList() {
		
		return selectList("coupon.selectCouponList");
	}

	public void downloadCoupon(Map<String, Object> map) {
		insert("coupon.downloadCoupon", map);
		
	}

	public void addCoupon(Map<String, Object> map) {
		insert("coupon.insertCoupon", map);
		
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectCouponOne(Map<String, Object> map) {
		
		return (Map<String, Object>) selectOne("coupon.selectCouponDetail", map);
	}

	public void updateCoupon(Map<String, Object> map) {
		update("coupon.updateCoupon", map);
		
	}

	public void deleteCoupon(Map<String, Object> map) {
		delete("coupon.deleteCoupon", map);
		
	}

	public int checkCoupon(Map<String, Object> map) {
		
		return (int) selectOne("coupon.checkCoupon", map);
	}

	

}
