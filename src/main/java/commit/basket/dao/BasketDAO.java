package commit.basket.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("basketDAO")
public class BasketDAO extends AbstractDAO{
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> myBasketList(Map<String, Object> map) throws Exception{
		return (List<Map<String, Object>>)selectList("basket.selectMyBasket", map);
	}
	
	public void updateBasket(Map<String, Object> map) throws Exception{
		update("basket.updateBasket", map);
	}
	
	public void deleteBasket(Map<String, Object> map) throws Exception{
		delete("basket.deleteBasket", map);
	}
	
	public void clearBasket(Map<String, Object> map) {
		delete("basket.clearBasket", map);
	}
	
	public void insertBasket(Map<String, Object> map) {
		insert("basket.insertBasket", map);
		
	}
	
	public int checkBasket(Map<String, Object> map) {
		return (Integer)selectOne("basket.checkBasket",map);
	}
	
	public void updateAmount(Map<String, Object> map) {
		update("basket.updateAmount", map);
	}
	
}
