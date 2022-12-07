package commit.product.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;
@Repository("productDAO")
public class ProductDAO extends AbstractDAO{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProList(Map<String, Object> map) {
	
		return selectList("product.selectProList", map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectProDetail(Map<String, Object> map) {
		return (Map<String, Object>)selectOne("product.selectProDetail", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectProImage(Map<String, Object> map) {
		return (List<Map<String, Object>>)selectList("product.seleteProImage", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectProReview(Map<String, Object> map) {
		return (List<Map<String, Object>>)selectList("product.selectProReview", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectProQna(Map<String, Object> map) {
		return (List<Map<String, Object>>)selectList("product.selectProQna", map);
	}
	

	@SuppressWarnings("unchecked")
	public Map<String, Object> getProInfo(Map<String, Object> map) {
		
		return (Map<String, Object>) selectOne("product.selectProDetail", map);
	}

	public void insertProOrder(Map<String, Object> map) {
		insert("product.insertProOrder", map);
		
	}

	public void insertOrderInfo(Map<String, Object> map) {
		insert("product.insertOrderInfo", map);
		
	}

	public void updateProStock(Map<String, Object> map) {
		update("product.updateProStock", map);
		
	}

	public void updateMemberCoupon(Map<String, Object> map) {
		update("product.updateMemberCoupon", map);
		
	}

	public void cancelOrder(Map<String, Object> map) {
		update("product.cancelOrder", map);
	}

	
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkOrderState(Map<String, Object> map) {
		return (Map<String, Object>) selectOne("product.selectOrderState", map);
	}



}
