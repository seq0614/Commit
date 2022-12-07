package commit.my.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("myDAO")
public class MyDAO extends AbstractDAO {

	public void updateMyInfo(Map<String, Object> map) {
		update("my.updateMyInfo", map);

	}

	public void deleteMyInfo(Map<String, Object> map) {
		update("my.deleteMyInfo", map);
		
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> myOrderList(Map<String, Object> map) {
		
		return selectList("my.selectMyOrderList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> myQnaList(Map<String, Object> map) {
		
		return selectList("my.selectMyQnaList", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> myQnaDetail(Map<String, Object> map) {
		
		return (Map<String, Object>) selectOne("my.selectMyQnaDetail", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> myCouponList(Map<String, Object> map) {
		
		return selectList("my.selectMyCouponList", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectFromOrder(Map<String, Object> map) {
	
		return selectList("my.selectFromOrder", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectFromOrderInfo(Map<String, Object> map) {
		
		return (Map<String, Object>) selectOne("my.selectFromOrderInfo", map);
	}

	

}